package com.taskengine.core;

public interface IConsumeTask<T> {

	void apply(T t);
}
