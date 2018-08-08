package com.webfluxwebsocket.game.web.rest;

import java.net.URISyntaxException;

import com.google.gson.JsonSyntaxException;
import com.webfluxwebsocket.game.domain.Player;
import com.webfluxwebsocket.game.domain.enumeration.PlayerState;
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
		return gameEngineService.save(player)
				.map(registrationPlayer -> new ResponseEntity<>(registrationPlayer, HttpStatus.CREATED))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/players")
	public Flux<Player> getPlayerAll() {
		return gameEngineService.findAll();
	}

	/**
	 * @see https://stackify.com/reactive-spring-5
	 * Reactive Web Servers With Spring 5  ???  Here’s a quick example of what that might look like
	 * **********************************
	 * По умолчанию Spring Boot WebFlux будет использовать Netty-сервер, который поддерживает все, что нам нужно.
	 *
	 * Написание серверной логики Spring WebFlux практически аналогично написанию типичной логики Spring MVC.
	 * Фактически мы можем использовать точно такие же аннотации для определения наших контроллеров, пока мы возвращаем типы Reactor из наших методов контроллера.
	 *
	 * Reactive WebSocket Clients  ???  We can write a simple client that will call the WebSocket Echo Service and log the messages as follows
	 * **************************
	 * Реактивное программирование с одним ответом - это может быть с помощью WebSockets - можем получить произвольное количество сообщений возвращающихся к нам которые нам нужно обрабатывать по мере их появления (поток).
	 * Реализация может быть выполнена используя интерфейс WebSocketClient, который предоставляет Spring WebFlux.
	 * Чтобы использовать его нам нужно знать какой конкретный тип мы используем - по умолчанию реализация ReactorNettyWebSocketClient доступна и готова к использованию.
	 * Мы можем написать простой клиент, который будет вызывать службу Echo WebSocket и записывать сообщения ( https://www.websocket.org/echo.html )
	 *
	 * @see https://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html  ???  23.2.3 Request and Response Body Conversion
	 */
	//TODO:  'Flux' - (схож с Mono) предоставляет возможность асинхронной работы со множеством объектов
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
	/**
	 * клиент веб-сокета 'WebSocket' ( https://www.websocket.org/echo.html ) прямо из браузера лезет на канал сокета ws://localhost/echo из веб-браузера
	 *
	 * а клиент 'EventSource' ( https://learn.javascript.ru/server-sent-events ) работает только по обычному HTTP-протоколу и умеет работать со стримами которые бросает ему сервер...
	 * поэтому на сервере есть свой клиент веб-сокета 'ReactorNettyWebSocketClient' ( https://stackify.com/reactive-spring-5 ) который слушает каналы веб-сокета ws://localhost/echo и перебрасывает данные на HTTP-протокол
	 *
	 * чтобы написать интеграционный тест для веб-сокета на REST-метод - достаточно будет проверить веб-сокет через клиент 'ReactorNettyWebSocketClient'
	 */

	@GetMapping("/game-round")
	public Flux<Player> getActualGameRound() {
		return gameEngineService.findByStates(PlayerState.PLAY);
	}
}