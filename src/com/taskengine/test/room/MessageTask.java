package com.taskengine.test.room;

import com.taskengine.core.ITask;
import com.taskengine.core.ITaskUnit;

/**
 * @author Ziegler
 * 消息任务
 */
public class MessageTask implements ITask {

	static int n = 0;
	
	@Override
	public void run(ITaskUnit taskUnit) {
		++n;
		System.out.println("执行一个消息：" + n);
		
		// 测试锁竞争
//		String name = "a";
//		synchronized (name) {
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			
//			System.out.print("dddd");
//		}
	}

}
