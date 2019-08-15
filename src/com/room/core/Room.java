package com.room.core;

public class Room implements IRoom<Message> {

	private static int globalId = 100000;
	private int id = 0;
	
	volatile ERoomStatus eRoomStatus = ERoomStatus.PREPARE;
	
	ISafeQueue<Message> messageQueue = null;
	
	IConsumerMessage<Message> consumerMessage = null;
	
	public Room() {
		this.id = (++globalId);
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public ERoomStatus getStatus() {
		return eRoomStatus;
	}
	
	@Override
	public void addMessage(Message t) {
		messageQueue.add(t);
	}

	@Override
	public void pulse(long now) {
		
		for (int i=0; i<FinalConfig.INSTANCE.ONE_PULSE_CONSUME_MESSAGE_COUNT; ++i) {
			Message message = messageQueue.pop();
			// 消费message
			consumerMessage.apply(message);
		}
		
	}

	@Override
	public void changeStatus(ERoomStatus e) {
		this.eRoomStatus = e;
	}

}
