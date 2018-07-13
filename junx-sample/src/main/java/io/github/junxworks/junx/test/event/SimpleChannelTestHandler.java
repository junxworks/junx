package io.github.junxworks.junx.test.event;

import io.github.junxworks.junx.event.EventChannel;
import io.github.junxworks.junx.event.EventChannelHandler;
import io.github.junxworks.junx.event.EventContext;

public class SimpleChannelTestHandler implements EventChannelHandler {

	@Override
	public void handleEvent(EventContext event, EventChannel channel) throws Exception {
		String myName = event.getData(String.class, Params.NAME);
		System.out.println(Thread.currentThread().getName() + ":" + myName);
	}

}
