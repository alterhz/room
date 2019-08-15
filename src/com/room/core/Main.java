package com.room.core;

public class Main {

	
	public static void main(String[] args) {
		MsgRoomManager.getInstance().startMultiThread(10);
		
		boolean running = true;
		while (running) {
			long now = System.currentTimeMillis();
			
			MsgRoomManager.getInstance().pulse(now);
		}
		
	}
	
	
	
}
