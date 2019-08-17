package com.taskengine.test.room;

import com.taskengine.core.ITask;
import com.taskengine.core.ITaskUnit;

/**
 * @author Ziegler
 * 消息任务
 */
public class MessageTask implements ITask {

	static int n = 0;
	
	@Override
	public void run(ITaskUnit taskUnit) {
		++n;
		System.out.println("执行一个消息：" + n);
	}

}
