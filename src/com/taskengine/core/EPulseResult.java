package com.taskengine.core;

/**
 * @author Ziegler
 * 心跳返回值
 */
public enum EPulseResult {
	/** 执行0条*/
	EXECUTE_ZERO,
	/** 执行了所有TASK*/
	EXECUTE_ALL,
	/** 执行了部分，仍然剩余Task未执行完*/
	EXECUTE_PART_OF_ALL,
	;
}
