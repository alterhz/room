package com.taskengine.test.room;

public class Main {

	
	public static void main(String[] args) {
		
		RoomTaskEngine mockTaskEngine = new RoomTaskEngine();
		
		// 启动10个线程 
		mockTaskEngine.startMultiThread(10);
		
		// 创建100个房间
		for (int i=0; i<100; ++i) {
			mockTaskEngine.addTaskUnit(new RoomTaskUnit());
		}
		
		// 开始pulse循环
		boolean running = true;
		while (running) {
			long now = System.currentTimeMillis();
			
			mockTaskEngine.pulse(now);
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
