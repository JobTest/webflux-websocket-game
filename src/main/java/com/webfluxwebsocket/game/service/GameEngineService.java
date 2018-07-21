package com.webfluxwebsocket.game.service;

import com.webfluxwebsocket.game.domain.Player;
import com.webfluxwebsocket.game.domain.enumeration.PlayerState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;

/**
 * Service Interface for GameEngine.
 */
public interface GameEngineService {

    /**
     * The random number generation algorithm.
     * @param from
     * @param to
     * @return
     */
    int getThrowOfNumber(int from, int to);

    /**
     * The allows get the generated number this moment.
     * @return
     */
    Integer getGeneratedNumber();

    /**
     * The allows get score a state for some number this moment.
     * @param number
     * @return
     */
    String checkWinning(Integer number);

    Flux<Player> findAll();

    Flux<Player> findByStates(PlayerState... states);

    Mono<Player> findById(String playerId);

    Mono<Player> save(Player player);

    /**
     * The allows you to connect new player to the round of the game and open broadcasting from results of the game.
     * @param playerId
     * @param player
     * @return
     * @throws URISyntaxException
     * @throws IllegalAccessException
     */
    Flux<String> getConnectToGameRound(String playerId, Player player) throws URISyntaxException, IllegalAccessException;
}
