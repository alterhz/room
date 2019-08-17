package com.taskengine.test.room;

import com.taskengine.core.ITask;
import com.taskengine.core.ITaskEngine;
import com.taskengine.core.ITaskUnit;

public class RoomTaskEngine extends ITaskEngine {
	
	/**
	 * 模拟生成Task
	 */
	public void mockGenerateTask() {
		int messageCount = (int) (Math.random() * 100);
		for (int i=0; i<messageCount; ++i) {
			ITask task = new MessageTask();
			
			int taskUnitId = (int) (Math.random() * 100);
			// 根据逻辑将不同的消息分配到不同的房间
			ITaskUnit taskUnit = (ITaskUnit)getUnitTask(taskUnitId);
			if (null != taskUnit) {
				taskUnit.postTask(task);
			}
		}
	}

	@Override
	public void pulseFrame(long now) {
		super.pulseFrame(now);
		
		mockGenerateTask();
	}
	
}
