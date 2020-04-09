package com.demo.reactive.rabbitmq.producer.impl;


import com.demo.reactive.rabbitmq.producer.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.*;

import static reactor.core.publisher.Mono.just;


@Slf4j
@Component
public class ReactiveSender implements MessageSender {

    private final Sender sender = RabbitFlux.createSender();


    public void send(final Object message, final String queue) {
        final var resultFlux = sender.sendWithPublishConfirms(
                just(new OutboundMessage("", queue, (message.toString()).getBytes())));
        sender.declareQueue(QueueSpecification.queue(queue))
                .thenMany(resultFlux)
                .doOnError(e -> log.error("Send failed: ", e))
                .subscribe(ReactiveSender::accept);

    }

    private static void accept(OutboundMessageResult r) {
        if (r.isAck()) {
            log.info("Message '{}' sent successfully", new String(r.getOutboundMessage().getBody()));
        }
    }

}
