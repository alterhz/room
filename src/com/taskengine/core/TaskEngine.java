package com.taskengine.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class TaskEngine implements Runnable {

	/** 线程安全队列，房间的创建和销毁由此线程单独处理*/
	private static Map<String, TaskEngine> allTaskEngine = new ConcurrentHashMap<String, TaskEngine>();
	
	/** 名称*/
	private final String name;
	
	/** 线程安全队列，房间的创建和销毁由此线程单独处理*/
	private Map<Integer, ITaskUnit> allTaskUnit = new ConcurrentHashMap<Integer, ITaskUnit>();
	
	/** 当前帧正在执行的任务单元列表*/
	private ConcurrentLinkedQueue<ITaskUnit> curFrameTaskUnits = new ConcurrentLinkedQueue<ITaskUnit>();
	
	/** 下一帧执行的任务单元列表*/
	private ConcurrentLinkedQueue<ITaskUnit> nextFrameTaskUnits = new ConcurrentLinkedQueue<ITaskUnit>();
	
	/** 上一帧的时间*/
	private long prevFrameTime = 0L;
	
	/** 运行状态*/
	boolean running = false;
	
	public TaskEngine(String name) {
		this.name = name;
	}
	
	private static final TaskEngine defaultEngine = new TaskEngine("default");
	
	/**
	 * 默认TaskEngine
	 * @return
	 */
	public static TaskEngine getDefault() {
		return defaultEngine;
	}
	
	/**
	 * 创建一个TaskEngine
	 * @param name
	 * @return
	 */
	public static TaskEngine buildTaskEngine(String name) {
		synchronized (name) {
			TaskEngine newTaskEngine = new TaskEngine(name);
			
			allTaskEngine.putIfAbsent(name, newTaskEngine);
			
			return newTaskEngine;
		}
	}
	
	/**
	 * 获取TaskEngine
	 * @param name
	 * @return
	 */
	public static TaskEngine getTaskEngine(String name) {
		TaskEngine taskEngine = allTaskEngine.get(name);
		return taskEngine;
	}
	
	/**
	 * 启动TaskEngine
	 * @param taskEngine
	 */
	public void start(int taskExecutorThreadNum) {
		if (running) {
			throw new RuntimeException("ITaskEngine重复启动");
		}
		
		running = true;
		
		Thread thread = new Thread(this);
		thread.start();
		
		startMultiThread(taskExecutorThreadNum);
	}
	
	@Override
	public void run() {
		// 开始pulse循环
		while (running) {
			long now = System.currentTimeMillis();
			
			pulse(now);
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
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
	 * @param threadNum
	 */
	public void startMultiThread(int threadNum) {
		for (int i=0; i<threadNum; ++i) {
			Thread thread = new Thread(new TaskExecutor(this));
			thread.start();
		}
	}
	
	/**
	 * 投递Task
	 * @param taskUnitId
	 * @param task
	 */
	public boolean postTask(int taskUnitId, ITask task) {
		ITaskUnit taskUnit = getUnitTask(taskUnitId);
		if (null == taskUnit) {
			return false;
		}
		
		taskUnit.postTask(task);
		return true;
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

	public String getName() {
		return name;
	}
	
}
