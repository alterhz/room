package com.room.core;

public class Main {

	
	public static void main(String[] args) {
		
		MsgRoomManager msgRoomManager = new MsgRoomManager();
		
		// 启动10个线程 
		msgRoomManager.startMultiThread(10);
		
		// 创建100个房间
		for (int i=0; i<100; ++i) {
			msgRoomManager.createRoom();
		}
		
		boolean running = true;
		while (running) {
			long now = System.currentTimeMillis();
			
			msgRoomManager.pulse(now);
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
