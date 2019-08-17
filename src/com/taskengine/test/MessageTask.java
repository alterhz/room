package com.taskengine.test;

import com.taskengine.core.ITask;
import com.taskengine.core.ITaskUnit;

public class MessageTask implements ITask {

	static int n = 0;
	
	@Override
	public void run(ITaskUnit taskUnit) {
		++n;
		System.out.println("执行一个消息：" + n);
	}

}
