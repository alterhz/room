package com.room.core;

public class ConsumeMessage implements IConsumeMessage<Message> {

	int count = 0;
	
	@Override
	public void apply(Message t) {
		++count;
		
		if (count % 1000 == 0) {
			System.out.println("消费消息：" + count);
		}
		
	}

}
