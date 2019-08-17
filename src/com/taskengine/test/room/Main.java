package com.taskengine.test.room;

import com.taskengine.core.ITask;
import com.taskengine.core.TaskEngine;

public class Main {

	
	public static void main(String[] args) {
		
//		TaskEngine taskEngine = TaskEngine.getDefault();
		TaskEngine.buildTaskEngine("serviceEngine");
		
		TaskEngine taskEngine = TaskEngine.getTaskEngine("serviceEngine");
		
		// 创建100个房间
		for (int i=0; i<100; ++i) {
			taskEngine.addTaskUnit(new RoomTaskUnit());
		}
		
		taskEngine.start(2);
		
		while (true) {
			
			mockGenerateTask(taskEngine);
			
			try {
				Thread.sleep(1000000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	/**
	 * 模拟生成Task
	 */
	public static void mockGenerateTask(TaskEngine taskEngine) {
		int messageCount = (int) (Math.random() * 100);
		for (int i=0; i<messageCount; ++i) {
			ITask task = new MessageTask();
			
			int taskUnitId = (int) (Math.random() * 100);
			// 根据逻辑将不同的消息分配到不同的房间
			taskEngine.postTask(taskUnitId, task);
		}
	}
	
}
