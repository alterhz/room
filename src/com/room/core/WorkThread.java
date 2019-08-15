package com.room.core;

public class WorkThread implements Runnable {

	boolean running = true;
	
	@Override
	public void run() {
		
		while (running) {
			long now = System.currentTimeMillis();
			
			IRoom<?> room = MsgRoomManager.getInstance().popThreadRoom();
			
			// 执行状态
			room.changeStatus(ERoomStatus.RUNNING);
			
			// 执行
			room.pulse(now);
			
			// 执行后的room切换到Wait状态
			room.changeStatus(ERoomStatus.WAITING);
		}
		
	}

	
}
