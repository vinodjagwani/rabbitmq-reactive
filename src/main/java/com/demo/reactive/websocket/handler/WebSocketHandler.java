package com.demo.reactive.websocket.handler;

import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface WebSocketHandler extends org.springframework.web.reactive.socket.WebSocketHandler {

    Mono<Void> handle(final WebSocketSession webSocketSession);
}
