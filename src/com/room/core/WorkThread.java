package com.room.core;

public class WorkThread implements Runnable {

	boolean running = true;
	
	private final IRoomManager roomManager;
	
	public WorkThread(IRoomManager roomManager) {
		this.roomManager = roomManager;
	}
	
	@Override
	public void run() {
		
		while (running) {
			long now = System.currentTimeMillis();
			
			IRoom room = roomManager.popThreadRoom();
			
			if (null == room) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				continue;
			}
			
			// 执行状态
			room.changeStatus(ERoomStatus.RUNNING);
			
			// 执行
			room.pulse(now);
			
			// 执行后的room切换到Wait状态
			room.changeStatus(ERoomStatus.WAITING);
		}
		
	}

	
}
