package com.room.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class IRoomManager {

	/** 房间的创建和销毁由此线程单独处理*/
	private Map<Integer, IRoom> allRoom = new HashMap<Integer, IRoom>();
	
	/** 安全队列*/
	private ISafeQueue<IRoom> safeRoom = new SafeQueue<IRoom>();
	
	/** 上一帧的时间*/
	private long prevFrameTime = 0L;
	
	/**
	 * 创建房间
	 * @return
	 */
	public void createRoom() {
		IRoom room = new Room();
		
		allRoom.put(room.getId(), room);
	}
	
	
	public abstract Message generateMessage();
	
	/**
	 * 模拟接收消息
	 */
	public void receiveMessage() {
		int messageCount = (int) (Math.random() * 100);
		for (int i=0; i<messageCount; ++i) {
			Message message = generateMessage();
			
			int roomId = (int) (Math.random() * 100);
			// 根据逻辑将不同的消息分配到不同的房间
			IRoom room = (IRoom)getRoom(roomId);
			if (null != room) {
				room.addMessage(message);
			}
		}
	}
	
	/**
	 * 启动多个线程
	 * @param count
	 */
	public void startMultiThread(int count) {
		for (int i=0; i<count; ++i) {
			Thread thread = new Thread(new WorkThread(this));
			thread.start();
		}
	}
	
	/**
	 * 心跳
	 * @param now
	 */
	public void pulse(long now) {
		// 服务器帧频率控制
		if (now - prevFrameTime >= FinalConfig.INSTANCE.ONE_FRAME_TIME_INTERVAL) {
			prevFrameTime = now;
			
			pulseFrame(now);
			
			receiveMessage();
		}
	}
	
	/**
	 * 帧心跳，每帧调用一次
	 * @param now
	 */
	public void pulseFrame(long now) {
		// 销毁处于ERoomStatus.DESTROY状态的的room
		Iterator<Map.Entry<Integer, IRoom>> iter = allRoom.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, IRoom> entry = iter.next();
			IRoom room = entry.getValue();
			if (room.getStatus() == ERoomStatus.DESTROY) {
				iter.remove();
			}
		}
		
		// 计算处于ERoomStatus.WAITING状态的Room
		List<IRoom> allWaitingRoom = new LinkedList<>();
		for (IRoom room : allRoom.values()) {
			if (room.getStatus() == ERoomStatus.WAITING
					|| room.getStatus() == ERoomStatus.PREPARE) {
				room.changeStatus(ERoomStatus.READY);
				allWaitingRoom.add(room);
			}
		}
		
		// 将所有readRoom追加到执行队列
		safeRoom.addAll(allWaitingRoom);
	}
	
	/**
	 * 返回一个线程可以执行Room
	 * @return
	 */
	public IRoom popThreadRoom() {
		return safeRoom.pop();
	}
	
	/**
	 * 通过id获得房间
	 * @param id
	 * @return
	 */
	private IRoom getRoom(int id) {
		IRoom room = allRoom.get(id);
		return room;
	}
	
}
