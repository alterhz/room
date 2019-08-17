package com.taskengine.core;

public enum FinalConfig {
	INSTANCE;
	
	/** 单词心跳消费的消息数量*/
	public final int ONE_PULSE_CONSUME_MESSAGE_COUNT = 100;
	
	/** 每帧的间隔时间33毫秒*/
	public final long ONE_FRAME_TIME_INTERVAL = 33;
}
