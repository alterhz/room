package com.room.core;

public class MsgRoomManager extends IRoomManager<Message> {

	private static MsgRoomManager instance = new MsgRoomManager();
	public static MsgRoomManager getInstance() {
		return instance;
	}
	@Override
	public Room create() {
		return new Room();
	}
	
	@Override
	public Message generateMessage() {
		return new Message();
	}
	
}
