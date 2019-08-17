package com.taskengine.test.room;

public class Main {

	
	public static void main(String[] args) {
		
		RoomTaskEngine mockTaskEngine = new RoomTaskEngine();
		
		// 创建100个房间
		for (int i=0; i<100; ++i) {
			mockTaskEngine.addTaskUnit(new RoomTaskUnit());
		}
		
		mockTaskEngine.start(2);
		
	}
	
}
