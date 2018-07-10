package com.webfluxwebsocket.game.config;

import com.webfluxwebsocket.game.model.Player;
import com.webfluxwebsocket.game.model.enumeration.PlayerState;
import com.webfluxwebsocket.game.service.GameEngineService;
import com.webfluxwebsocket.game.service.mapper.PlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
public class WebSocketConfig {

    @Autowired
    private GameEngineService gameEngineService;

    @Autowired
    private PlayerMapper playerMapper;

    @Value("/${game.ws-host}/")
    private String webSocketHostName;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public HandlerMapping getUrlHandlerMapping() {
        List<Player>                 players = gameEngineService.findByStates(PlayerState.START_ROUND, PlayerState.PLAY, PlayerState.STOP_ROUND);
        Map<String, WebSocketHandler> socketHandlers = new HashMap<>();

        for (Player player : players) {
            socketHandlers.put(webSocketHostName + player.getId(),
                    socketSession -> socketSession.send(Flux.<String>generate(sink -> {
                        switch (player.getState()) {
                            case START_ROUND:
                                sink.complete();
                                break;
                            case PLAY:
                                sink.next(playerMapper.toJson(player));
                                break;
                            case STOP_ROUND:
                                sink.next(playerMapper.toJson(player));
                                break;
                        } }).map( socketSession::textMessage )
                            .delayElements(Duration.ofSeconds(1))));
        }

        SimpleUrlHandlerMapping urlHandlerMapping = new SimpleUrlHandlerMapping();
        urlHandlerMapping.setUrlMap(socketHandlers);
        urlHandlerMapping.setOrder(1);
        return urlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter getWebSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
