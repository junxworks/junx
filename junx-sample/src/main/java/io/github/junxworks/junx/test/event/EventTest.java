package io.github.junxworks.junx.test.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.junxworks.junx.event.EventBus;
import io.github.junxworks.junx.event.EventContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventApplication.class)
public class EventTest {
	@Autowired
	private EventBus bus;

	@Test
	public void eventTest() throws Exception {
		bus.publish(createEvent(Topics.TOPIC_SIMPLE));
		bus.publish(createEvent(Topics.TOPIC_DISRUPTOR));
		bus.publish(createEvent(Topics.TOPIC_EXECUTOR));
		Thread.sleep(1000);//防止进程停止
	}

	private EventContext createEvent(String topic) {
		EventContext event = new EventContext(topic);
		event.setData(Params.NAME, "michael");
		return event;
	}
}
