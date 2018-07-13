/*
 ***************************************************************************************
 * 
 * @Title:  ServerApplication.java   
 * @Package io.github.junxworks.junx.test.netty.server   
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
package io.github.junxworks.junx.test.netty.server;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.junxworks.junx.core.lifecycle.Service;
import io.github.junxworks.junx.netty.NettyServer;
import io.github.junxworks.junx.netty.ServerConfig;
import io.github.junxworks.junx.netty.heartbeat.CommonServerHeartbeatHandlerFactory;
import io.github.junxworks.junx.netty.initializer.CommonChannelInitializer;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Configuration
	public static class Config {
		
		@Bean(name = "nettyServer", initMethod = "start", destroyMethod = "stop")
		public Service nettyServer( ServerEventHandler handler) throws IOException {
			ServerConfig config = new ServerConfig(); //服务器配置可以通过spring配置来实现
			config.setPort(8080);
			CommonChannelInitializer initializer = new CommonChannelInitializer(handler);
			initializer.setAllIdle(config.getAllIdle());
			initializer.setReadIdle(config.getReadIdle());
			initializer.setWriteIdle(config.getWriteIdle());
			initializer.setHeartbeatHandlerFactory(new CommonServerHeartbeatHandlerFactory()); //服务器端心跳检测
			NettyServer server = new NettyServer(config, initializer);
			server.setName("TestServer");
			return server;
		}
	}
}
