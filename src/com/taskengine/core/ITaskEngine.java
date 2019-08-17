package com.taskengine.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class ITaskEngine {

	/** 房间的创建和销毁由此线程单独处理*/
	private Map<Integer, ITaskUnit> allTaskUnit = new HashMap<Integer, ITaskUnit>();
	
	/** 当前帧正在执行的任务单元列表*/
	private ConcurrentLinkedQueue<ITaskUnit> curFrameTaskUnits = new ConcurrentLinkedQueue<ITaskUnit>();
	
	/** 下一帧执行的任务单元列表*/
	private ConcurrentLinkedQueue<ITaskUnit> nextFrameTaskUnits = new ConcurrentLinkedQueue<ITaskUnit>();
	
	/** 上一帧的时间*/
	private long prevFrameTime = 0L;
	
	/**
	 * 添加任务单元
	 * @return
	 */
	public ITaskUnit addTaskUnit(ITaskUnit taskUnit) {
		if (allTaskUnit.containsKey(taskUnit.getId())) {
			return null;
		}
		
		allTaskUnit.put(taskUnit.getId(), taskUnit);
		
		// 将新添加的执行单元添加到下一帧执行队列
		nextFrameTaskUnits.add(taskUnit);
		
		return taskUnit;
	}
	
	/**
	 * 移除任务单元
	 * @param taskUnit
	 */
	public void removeTaskUnit(ITaskUnit taskUnit) {
		if (ETaskUnitStatus.MARK_DESTROY == taskUnit.getStatus()) {
			return ;
		}
		
		taskUnit.changeStatus(ETaskUnitStatus.MARK_DESTROY);
	}
	
	/**
	 * 启动多个线程
	 * @param count
	 */
	public void startMultiThread(int count) {
		for (int i=0; i<count; ++i) {
			Thread thread = new Thread(new TaskExecutor(this));
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
		}
	}
	
	/**
	 * 帧心跳，每帧调用一次
	 * @param now
	 */
	public void pulseFrame(long now) {
		// 将下一帧的TaskUnit全部添加到当前帧
		int count = nextFrameTaskUnits.size();
		for (int i=0; i<count; ++i) {
			ITaskUnit taskUnit = nextFrameTaskUnits.poll();
			if (null == taskUnit) {
				break;
			}
			
			// 移除销毁状态的TaskUnit
			if (ETaskUnitStatus.DESTROY == taskUnit.getStatus()) {
				allTaskUnit.remove(taskUnit.getId());
				continue;
			}
			
			curFrameTaskUnits.add(taskUnit);
		}
		
		// TaskUnit分配算法1
//		// 计算处于ERoomStatus.WAITING状态的Room
//		List<ITaskUnit> allWaitingRoom = new LinkedList<>();
//		for (ITaskUnit room : allRoom.values()) {
//			if (room.getStatus() == ETaskUnitStatus.WAITING
//					|| room.getStatus() == ETaskUnitStatus.PREPARE) {
//				room.changeStatus(ETaskUnitStatus.READY);
//				allWaitingRoom.add(room);
//			}
//		}
//		
//		// 将所有readRoom追加到执行队列
//		executingTaskUnits.addAll(allWaitingRoom);
		
	}
	
	/**
	 * 返回一个线程可以执行Room
	 * @return
	 */
	public ITaskUnit pollTaskUnit() {
		return curFrameTaskUnits.poll();
	}
	
	/**
	 * 将TaskUnit添加到当前帧执行队列末尾
	 * @param taskUnit
	 */
	public void addTaskUnitToCurFrame(ITaskUnit taskUnit) {
		this.curFrameTaskUnits.add(taskUnit);
	}
	
	/**
	 * 将TaskUnit添加到下一帧
	 * @param taskUnit
	 */
	public void addTaskUnitToNextFrame(ITaskUnit taskUnit) {
		this.nextFrameTaskUnits.add(taskUnit);
	}
	
	/**
	 * 通过id获得TaskUnit
	 * @param id
	 * @return
	 */
	public ITaskUnit getUnitTask(int id) {
		ITaskUnit room = allTaskUnit.get(id);
		return room;
	}
	
}
