package com.webfluxwebsocket.game.service.impl;

import com.webfluxwebsocket.game.dao.PlayerRepository;
import com.webfluxwebsocket.game.model.Player;
import com.webfluxwebsocket.game.model.enumeration.PlayerState;
import com.webfluxwebsocket.game.service.GameEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private final PlayerRepository playerRepository;

    public GameEngineServiceImpl() {
        this.playerRepository = new PlayerRepository();
    }

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
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public List<Player> findByStates(PlayerState... states) {
        return playerRepository.findByStates(states);
    }

    @Override
    public Player save(Player player) {
        playerRepository.save(player);
        return playerRepository.findOne(player.getId());
    }

    @Override
    public Flux<String> getConnectToGameRound(String playerId, Player player) throws URISyntaxException, IllegalAccessException {
        boolean isPlayerStatus = isPlayerStatus(playerRepository.findOne(playerId));
        if (isPlayerStatus) {
            save(player);
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
    private void stopRound() {
        generatedNumber = getThrowOfNumber(from, to);

        logger.debug("Stop the Round with the Random of Number = {}", generatedNumber);

        for (Player player : findByStates(PlayerState.PLAY)) {
            player.setWin( checkWinning(player.getNumber()) );
            player.setState(PlayerState.STOP_ROUND);
        }
    }

    @Scheduled(cron = "${game.cron.start-round}")
    private void startRound() {
        logger.debug("Get Start new Round");

        for (Player player : findByStates(PlayerState.STOP_ROUND)) {
            player.setWin(null);
            player.setNumber(null);
            player.setState(PlayerState.START_ROUND);
        }
    }

    private boolean isPlayerStatus(Player player) {
        return player==null
                || player.getState()==null
                || ( !player.getState().equals(PlayerState.PLAY) && !player.getState().equals(PlayerState.STOP_ROUND) );
    }

    private boolean isPlayerWin(Integer number) {
        return number!=null
                && generatedNumber!=null
                && generatedNumber==number;
    }
}
