package io.duna.core.eventbus;

import io.duna.core.concurrent.Future;
import io.duna.core.eventbus.event.Emitter;
import io.duna.core.eventbus.event.Subscriber;
import io.duna.core.eventbus.queue.MessageQueue;

import java.util.function.Consumer;

public interface EventBus2 {

    void setDefaultDeadLetterSink(Consumer<Message<?>> messageHandler);

    <T> Emitter<T> outbound(String name);

    <T> Subscriber<T> inbound();

    <T> Subscriber<T> inbound(String name);

    <T> MessageQueue<T> queue(String name);

    <T> void poll(String queueName, Subscriber<T> target);

    <T> void process(Message<T> incoming);

    <T> Future<Void> dispatch(Message<T> outgoing, boolean multicast, Consumer<Message<T>> deliveryErrorConsumer);

}
