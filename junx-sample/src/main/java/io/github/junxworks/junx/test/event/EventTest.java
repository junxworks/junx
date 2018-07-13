/*
 ***************************************************************************************
 * 
 * @Title:  EventTest.java   
 * @Package io.github.junxworks.junx.test.event   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:16   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
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
