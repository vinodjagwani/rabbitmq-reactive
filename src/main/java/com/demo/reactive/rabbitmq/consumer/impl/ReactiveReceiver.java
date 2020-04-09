package com.demo.reactive.rabbitmq.consumer.impl;

import com.demo.reactive.rabbitmq.consumer.MessageReceiver;
import com.rabbitmq.client.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;


@Slf4j
@Component
public class ReactiveReceiver implements MessageReceiver {

    private final Sender sender = RabbitFlux.createSender();

    private final Receiver receiver = RabbitFlux.createReceiver();


    public Flux<Object> consume(final String queue) {
        return receiver.consumeAutoAck(queue)
                .delaySubscription(sender.declareQueue(QueueSpecification.queue(queue)))
                .map(Delivery::getBody);
    }

}
