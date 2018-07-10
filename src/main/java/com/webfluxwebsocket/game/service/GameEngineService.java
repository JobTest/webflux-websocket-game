package com.webfluxwebsocket.game.service;

import com.webfluxwebsocket.game.model.Player;
import com.webfluxwebsocket.game.model.enumeration.PlayerState;
import reactor.core.publisher.Flux;

import java.net.URISyntaxException;
import java.util.List;

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
    int getThrowOfNumber(final int from, final int to);

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

    List<Player> findAll();

    List<Player> findByStates(PlayerState... states);

    Player save(Player player);

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
