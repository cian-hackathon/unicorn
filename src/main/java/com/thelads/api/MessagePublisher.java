package com.thelads.api;

@FunctionalInterface
public interface MessagePublisher<T> {

    void publishMessage(T message)throws Exception;
}
