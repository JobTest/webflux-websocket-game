package com.webfluxwebsocket.game.service.impl;

import com.webfluxwebsocket.game.repository.PlayerRepository;
import com.webfluxwebsocket.game.domain.Player;
import com.webfluxwebsocket.game.domain.enumeration.PlayerState;
import com.webfluxwebsocket.game.service.GameEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Service Implementation for GameEngine.
 */
@Service
public class GameEngineServiceImpl implements GameEngineService {

    private static final Logger logger = LoggerFactory.getLogger(GameEngineServiceImpl.class);

    private final static int from = 0;
    private final static int to = 10;

    private static Integer generatedNumber;

    @Value("ws://${server.address}:${server.port}/${game.ws-host}")
    private String webSocketHostName;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Integer getGeneratedNumber() {
        return generatedNumber;
    }

    @Override
    public int getThrowOfNumber(final int from, final int to) {
        return from + (int) (Math.random() * to);
    }

    @Override
    public String checkWinning(Integer number) {
        return isPlayerWin(number)
                ? "You win"
                : "You lose";
    }

    @Override
    public Flux<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public Flux<Player> findByStates(PlayerState... states) {
        return playerRepository.findAll()
                .filter(gamePlayer -> Arrays.asList(states).contains(gamePlayer.getState()));
    }

    @Override
    public Mono<Player> findById(String playerId) {
        return playerRepository.findById(playerId);
    }

    @Override
    public Mono<Player> save(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Flux<String> getConnectToGameRound(String playerId, Player player) throws URISyntaxException, IllegalAccessException {
        Player monoPlayer = playerRepository.findById(playerId).block();
        boolean isPlayerStatus = isPlayerStatus(monoPlayer);
        if (isPlayerStatus) {
            save(player).subscribe();
        } else {
            throw new IllegalAccessException("This time a server is busy to process Your request");
        }

        EmitterProcessor<String> emitter = EmitterProcessor.create();
        WebSocketClient socketClient = new ReactorNettyWebSocketClient();
        String urlSocketChanel = webSocketHostName + "/" + playerId;

        Mono<Void> mono = socketClient.execute(new URI(urlSocketChanel),
                socketSession -> socketSession.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .subscribeWith(emitter)
                        .then());
        return emitter.doOnSubscribe(subscription -> mono.subscribe());
    }

    @Scheduled(cron = "${game.cron.stop-round}")
    public void stopRound() {
        generatedNumber = getThrowOfNumber(from, to);

        logger.debug("Stop the Round with the Random of Number = {}", generatedNumber);

        playerRepository.findAll( Example.of(new Player(null, null, null, PlayerState.PLAY)) )
                .flatMap(player -> {
                    player.setWin(checkWinning(player.getNumber()));
                    player.setState(PlayerState.STOP_ROUND);
                    return playerRepository.save(player); })
                .subscribe();
    }

    @Scheduled(cron = "${game.cron.start-round}")
    public void startRound() {
        logger.debug("Get Start new Round");

        playerRepository.findAll( Example.of(new Player(null, null, null, PlayerState.STOP_ROUND)) )
                .flatMap(player -> {
                    player.setWin(null);
                    player.setNumber(null);
                    player.setState(PlayerState.START_ROUND);
                    return playerRepository.save(player); })
                .subscribe();
    }

    private boolean isPlayerStatus(Player player) {
        return player==null
                || player.getState()==null
                || ( player.getState()!=PlayerState.PLAY && player.getState()!=PlayerState.STOP_ROUND );
    }

    private boolean isPlayerWin(Integer number) {
        return number.equals(null)
                && generatedNumber.equals(null)
                && generatedNumber.equals(number);
    }
}
