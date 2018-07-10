package com.webfluxwebsocket.game.web.rest;

import com.webfluxwebsocket.game.model.Player;
import com.webfluxwebsocket.game.model.enumeration.PlayerState;
import com.webfluxwebsocket.game.service.GameEngineService;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebFluxWebSocketPlayerIntegrationTest extends BaseTest {

    @Autowired
    private GameEngineService gameEngineService;

    private Map<String, Player> players = new HashMap<>();

    @PostConstruct
    public void setup() {
        players.put("test1", new Player("test1", null, null, PlayerState.CONNECT));
        players.put("test2", new Player("test2", null, null, PlayerState.START_ROUND));
        players.put("test3", new Player("test3", null, 1, PlayerState.PLAY));
        players.put("test4", new Player("test4", gameEngineService.checkWinning(1), 1, PlayerState.STOP_ROUND));
    }

    /**
     * checkRegistrationPlayer
     */
    @Test
    public void test1() {
        prepareRegistrationPlayer(players.get("test1"), players.get("test1"));
        prepareRegistrationPlayer(players.get("test2"), players.get("test2"));
        prepareRegistrationPlayer(players.get("test3"), players.get("test3"));
    }

    /**
     * checkGetPlayerAll
     */
    @Test
    public void test2() {
        List<Player> playerList = new ArrayList<>();
        playerList.add(players.get("test1"));
        playerList.add(players.get("test2"));
        playerList.add(players.get("test3"));

        prepareGetPlayerAll(3,
                new Player[]{players.get("test1"), players.get("test2"), players.get("test3")},
                playerList);
    }

    /**
     * checkGetActualGameRound
     */
    @Test
    public void test3() {
        List<Player> playerActual = new ArrayList<>();
        playerActual.add(players.get("test3"));

        prepareGetActualGameRound(1,
                new Player[]{players.get("test3")},
                playerActual);
    }
}