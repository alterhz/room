package com.room.core;

public interface IRoom<T> {

	/**
	 * 房间id
	 * @return
	 */
	int getId();
	
	/**
	 * 返回当前房间的状态
	 * @return
	 */
	ERoomStatus getStatus();
	
	/**
	 * [注意!!!]更改状态，状态的更改需要慎重，可能关系线程问题
	 * @param e
	 */
	void changeStatus(ERoomStatus e);
	
	/**
	 * 添加消息
	 * @param <T>
	 * @param t
	 */
	void addMessage(T t);
	
	
	/**
	 * 心跳
	 * @param now
	 */
	void pulse(long now);
	
}
