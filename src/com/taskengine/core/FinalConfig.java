package com.taskengine.core;

public enum FinalConfig {
	INSTANCE;
	
	/** 单词心跳执行任务数量*/
	public final int ONE_PULSE_EXECUTE_TASK_COUNT = 1000;
	
	/** 单词心跳执行最长时间（毫秒）*/
	public final int ONE_PULSE_MAX_EXECUTE_TIME = 100;
	
	/** 每帧的间隔时间33毫秒*/
	public final long ONE_FRAME_TIME_INTERVAL = 33;
}
