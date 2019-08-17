package com.taskengine.core;

/**
 * @author Ziegler
 * 任务执行器，支持器装载任务单元，执行任务单元上的任务
 */
public class TaskExecutor implements Runnable {

	boolean running = true;
	
	private final TaskEngine taskEngine;
	
	public TaskExecutor(TaskEngine taskUnitManager) {
		this.taskEngine = taskUnitManager;
	}
	
	@Override
	public void run() {
		
		while (running) {
			long now = System.currentTimeMillis();
			
			ITaskUnit taskUnit = taskEngine.pollTaskUnit();
			
			if (null == taskUnit) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				continue;
			}
			
			// 执行状态
			taskUnit.changeStatus(ETaskUnitStatus.RUNNING);
			
			// 执行
			EPulseResult ePulseResult = taskUnit.pulse(now);
			if (EPulseResult.EXECUTE_ZERO == ePulseResult
					|| EPulseResult.EXECUTE_ALL == ePulseResult) {
				// 所有数据已经执行完毕，添加到下一帧执行列表
				taskEngine.addTaskUnitToNextFrame(taskUnit);
			} else if (EPulseResult.EXECUTE_PART_OF_ALL == ePulseResult) {
				// 执行单元还有数据未执行完毕，将taskUnit压入当前帧执行列表，当前帧继续执行一次
				taskEngine.addTaskUnitToCurFrame(taskUnit);
			}
			
			// 执行后的room切换到Wait状态
			taskUnit.changeStatus(ETaskUnitStatus.WAITING);
		}
		
	}

	
}
