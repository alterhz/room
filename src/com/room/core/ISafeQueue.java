package com.room.core;

import java.util.Collection;

/**
 * @author Administrator
 *	此消息队列要求是线程安全队列
 * @param <T>
 */
public interface ISafeQueue<T> {

	/**
	 * 获得一个对象
	 * @return
	 */
	T pop();
	
	/**
	 * 添加一个对象
	 * @param t
	 */
	void add(T t);
	
	/**
	 * 添加多个对象
	 * @param c
	 */
	void addAll(Collection<T> c);
	
}
