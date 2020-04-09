package com.demo.reactive.rabbitmq.consumer;


import reactor.core.publisher.Flux;

@FunctionalInterface
public interface MessageReceiver {

    Flux<Object> consume(final String value);

}
