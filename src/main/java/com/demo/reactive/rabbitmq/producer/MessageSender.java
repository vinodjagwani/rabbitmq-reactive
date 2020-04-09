package com.demo.reactive.rabbitmq.producer;

@FunctionalInterface
public interface MessageSender {

    void send(final Object message, final String queue);
}
