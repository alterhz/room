package com.taskengine.core;

/**
 * @author Ziegler
 * 任务单元，所有的任务都会分配到任务单元上，支持异步添加任务
 */
public interface ITaskUnit {

	/**
	 * 房间id
	 * @return
	 */
	int getId();
	
	/**
	 * 返回当前房间的状态
	 * @return
	 */
	ETaskUnitStatus getStatus();
	
	/**
	 * [注意!!!]更改状态，状态的更改需要慎重，可能关系线程问题
	 * @param e
	 */
	void changeStatus(ETaskUnitStatus e);
	
	/**
	 * 投递任务Task
	 * @param <T>
	 * @param t
	 */
	void postTask(ITask t);
	
	
	/**
	 * 心跳
	 * @param now
	 */
	EPulseResult pulse(long now);
	
}
