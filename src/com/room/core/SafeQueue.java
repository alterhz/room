package com.room.core;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SafeQueue<T> implements ISafeQueue<T> {

private ConcurrentLinkedQueue<T> messageQueue = new ConcurrentLinkedQueue<T>();
	
	@Override
	public T pop() {
		return messageQueue.poll();
	}

	@Override
	public void add(T t) {
		messageQueue.offer(t);
	}

	@Override
	public void addAll(Collection<T> c) {
		messageQueue.addAll(c);
	}


}
