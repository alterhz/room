package com.taskengine.core;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskUnit implements ITaskUnit {

	private static int globalId = 0;
	private int id = 0;
	
	volatile ETaskUnitStatus eTaskUnitStatus = ETaskUnitStatus.PREPARE;
	
	ConcurrentLinkedQueue<ITask> tasks = new ConcurrentLinkedQueue<ITask>();
	
	public TaskUnit() {
		this.id = (++globalId);
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public ETaskUnitStatus getStatus() {
		return eTaskUnitStatus;
	}
	
	@Override
	public void postTask(ITask t) {
		tasks.add(t);
	}

	@Override
	public EPulseResult pulse(long now) {
		
		// 将当前执行器设置为销毁状态
		if (ETaskUnitStatus.MARK_DESTROY == eTaskUnitStatus) {
			changeStatus(ETaskUnitStatus.DESTROY);
		}
		
		long beforeTime = System.currentTimeMillis();
		
		for (int i=0; i<FinalConfig.INSTANCE.ONE_PULSE_EXECUTE_TASK_COUNT; ++i) {
			ITask task = tasks.poll();
			if (null == task) {
				if (i == 0) {
					return EPulseResult.EXECUTE_ZERO;
				}
				
				return EPulseResult.EXECUTE_ALL;
			}
			
			// 消费任务
			task.run(this);
			
			long nowTime = System.currentTimeMillis();
			if (nowTime - beforeTime > FinalConfig.INSTANCE.ONE_PULSE_EXECUTE_TASK_COUNT) {
				System.out.println("当前帧执行时间大于" + FinalConfig.INSTANCE.ONE_PULSE_EXECUTE_TASK_COUNT + "毫秒，提前结束执行器。");
				return EPulseResult.EXECUTE_OUT_OF_TIME;
			}
		}
		
		return EPulseResult.EXECUTE_PART_OF_ALL;
	}

	@Override
	public void changeStatus(ETaskUnitStatus e) {
		this.eTaskUnitStatus = e;
	}

}
