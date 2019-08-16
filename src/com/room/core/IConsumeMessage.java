package com.room.core;

public interface IConsumeMessage<T> {

	void apply(T t);
}
