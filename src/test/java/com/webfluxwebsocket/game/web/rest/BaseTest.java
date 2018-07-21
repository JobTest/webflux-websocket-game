package com.webfluxwebsocket.game.web.rest;

import com.webfluxwebsocket.game.config.WebSocketConfig;
import com.webfluxwebsocket.game.domain.Player;
import com.webfluxwebsocket.game.service.GameEngineService;
import com.webfluxwebsocket.game.service.mapper.PlayerMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest({
        GameEngineResource.class,
        WebSocketConfig.class,
        PlayerMapper.class,
        GameEngineService.class
})
@ActiveProfiles("test")
public abstract class BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    static final String URL_REGISTRATION_PLAYER = "/api/players";
    static final String URL_GET_PLAYER_ALL = "/api/players";
    static final String URL_GET_ACTUAL_GAME_ROUND = "/api/game-round";
    static final String URL_GET_CONNECT_TO_GAME_ROUND = "/api/game-round/{playerId}";

    void prepareRegistrationPlayer(Player actual, Player expected) {
        webTestClient.post().uri(URL_REGISTRATION_PLAYER)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(actual))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Player.class)
                .isEqualTo(expected);
    }

    void prepareGetPlayerAll(int expectedSize, Player[] expectedContains, List<Player> expectedEqual) {
        webTestClient.get().uri(URL_GET_PLAYER_ALL).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Player.class)
                .hasSize(expectedSize)
                .contains(expectedContains)
                .isEqualTo(expectedEqual);
    }

    void prepareGetActualGameRound(int expectedSize, Player[] expectedContains, List<Player> expectedEqual) {
        webTestClient.get().uri(URL_GET_ACTUAL_GAME_ROUND).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Player.class)
                .hasSize(expectedSize)
                .contains(expectedContains)
                .isEqualTo(expectedEqual);
    }

    void prepareGetConnectToGameRound(String playerId, int number) {
        String jsonPlayer = "\"id\":\"" + playerId + "\",\"state\":\"PLAY\",\"number\":\"" + number + "\"";
        webTestClient.get().uri(URL_GET_CONNECT_TO_GAME_ROUND + "?jsonPlayer=" + jsonPlayer, playerId)
                .exchange();
    }
}
