package com.room.core;

public class MsgRoomManager extends IRoomManager {
	
	@Override
	public Message generateMessage() {
		return new Message();
	}
	
}
