package com.taskengine.core;

public enum ETaskUnitStatus {
	/** 新创建的默认状态，未曾添加到线程执行队列*/
	PREPARE,
	/** 就绪状态：随时可以被线程执行*/
	READY,
	/** 运行态：处于线程执行中*/
	RUNNING,
	/** 等待状态：运行过的处于等待状态*/
	WAITING,
	/** 标记删除*/
	MARK_DESTROY,
	/** 待销毁状态：等待销毁*/
	DESTROY,
}
