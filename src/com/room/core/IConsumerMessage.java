package com.room.core;

public interface IConsumerMessage<T> {

	void apply(T t);
}
