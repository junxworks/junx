/*
 ***************************************************************************************
 * 
 * @Title:  EventApplication.java   
 * @Package io.github.junxworks.junx.test.event   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-13 15:20:15   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.test.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.junxworks.junx.event.EventBus;
import io.github.junxworks.junx.event.EventChannel;
import io.github.junxworks.junx.event.impl.DisruptorEventChannel;
import io.github.junxworks.junx.event.impl.ExecutorEventChannel;
import io.github.junxworks.junx.event.impl.SimpleEventBus;
import io.github.junxworks.junx.event.impl.SimpleEventChannel;

@SpringBootApplication
public class EventApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventApplication.class, args);
	}

	@Configuration
	public static class Config {

		@Bean(initMethod = "start", destroyMethod = "stop")
		public EventBus eventBus() {
			SimpleEventBus bus = new SimpleEventBus();
			bus.setName("Simple Event Bus");
			return bus;
		}

		@Bean
		public EventChannel simpleChannel(EventBus bus) {
			SimpleChannelTestHandler handler = new SimpleChannelTestHandler();
			SimpleEventChannel simpleChannel = new SimpleEventChannel("simple-channel", Topics.TOPIC_SIMPLE);
			simpleChannel.setEventHandler(handler);
			bus.registerChannel(simpleChannel); //注册到bus
			return simpleChannel;
		}

		@Bean
		public EventChannel executorChannel(EventBus bus) {
			SimpleChannelTestHandler handler = new SimpleChannelTestHandler();
			ExecutorEventChannel executorChannel = new ExecutorEventChannel("executor-channel", Topics.TOPIC_EXECUTOR);
			executorChannel.setBufferSize(8192);
			executorChannel.setMinThreads(1);
			executorChannel.setMaxThreads(2);
			executorChannel.setEventHandler(handler);
			bus.registerChannel(executorChannel); //注册到bus
			return executorChannel;
		}

		@Bean
		public EventChannel disruptorChannel(EventBus bus) {
			SimpleChannelTestHandler handler = new SimpleChannelTestHandler();
			DisruptorEventChannel disruptorChannel = new DisruptorEventChannel("disruptor-channel", Topics.TOPIC_DISRUPTOR);
			disruptorChannel.setEventHandler(handler);
			bus.registerChannel(disruptorChannel); //注册到bus
			return disruptorChannel;
		}
	}
}
