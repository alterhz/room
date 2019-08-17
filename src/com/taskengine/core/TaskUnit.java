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
		
		for (int i=0; i<FinalConfig.INSTANCE.ONE_PULSE_CONSUME_MESSAGE_COUNT; ++i) {
			ITask task = tasks.poll();
			if (null == task) {
				if (i == 0) {
					return EPulseResult.EXECUTE_ZERO;
				}
				
				return EPulseResult.EXECUTE_ALL;
			}
			
			// 消费任务
			task.run(this);
		}
		
		return EPulseResult.EXECUTE_PART_OF_ALL;
	}

	@Override
	public void changeStatus(ETaskUnitStatus e) {
		this.eTaskUnitStatus = e;
	}

}
