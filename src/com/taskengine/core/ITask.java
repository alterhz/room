package com.taskengine.core;


/**
 * @author Ziegler
 * 任务接口
 */
public interface ITask {
	/**
	 * 执行
	 */
	void run(ITaskUnit taskUnit);
}
