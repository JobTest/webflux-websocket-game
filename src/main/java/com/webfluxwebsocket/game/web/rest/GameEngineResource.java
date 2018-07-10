package com.webfluxwebsocket.game.web.rest;

import java.net.URISyntaxException;

import com.google.gson.JsonSyntaxException;
import com.webfluxwebsocket.game.model.Player;
import com.webfluxwebsocket.game.model.enumeration.PlayerState;
import com.webfluxwebsocket.game.service.GameEngineService;
import com.webfluxwebsocket.game.service.mapper.PlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for GameEngine.
 */
@RestController
@RequestMapping(value="/api")
public class GameEngineResource {

	private static final Logger logger = LoggerFactory.getLogger(GameEngineResource.class);

	@Autowired
	private GameEngineService gameEngineService;

	@Autowired
	private PlayerMapper playerMapper;

	@PostMapping("/players")
	public Mono<ResponseEntity<Player>> registrationPlayer(@RequestBody Player player) {
		return Mono.just(new ResponseEntity<>(gameEngineService.save(player), HttpStatus.CREATED));
	}

	@GetMapping("/players")
	public Flux<Player> getPlayerAll() {
		return Flux.fromIterable(gameEngineService.findAll());
	}

	@GetMapping(path = "/game-round/{playerId}")
	public ResponseEntity<Flux<String>> getConnectToGameRound(@PathVariable String playerId, @RequestParam String jsonPlayer) {
		logger.info("player ID = {}; json Player: {};", playerId, jsonPlayer);

		try {
			Player player = playerMapper.fromJson("{" + jsonPlayer + "}");
			Flux<String> connectToGameRound = gameEngineService.getConnectToGameRound(playerId, player);
			return new ResponseEntity<>(connectToGameRound, HttpStatus.OK);
		} catch (URISyntaxException | IllegalAccessException | JsonSyntaxException ex) {
			logger.error(ex.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/game-round")
	public Flux<Player> getActualGameRound() {
		return Flux.fromIterable(gameEngineService.findByStates(PlayerState.PLAY));
	}
}