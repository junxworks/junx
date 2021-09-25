/*
 ***************************************************************************************
 * 
 * @Title:  ServerConfig.java   
 * @Package io.github.junxworks.junx.netty   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:52:42   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.netty;

import java.io.IOException;
import java.net.ServerSocket;

import io.github.junxworks.junx.core.util.SystemUtils;

/**
 * 服务器配置
 *
 * @ClassName:  ServerConfig
 * @author: Michael
 * @date:   2018-5-30 18:21:26
 * @since:  v1.0
 */
public class ServerConfig {

	public static final int DEFAULT_READ_IDLE = 0;

	public static final int DEFAULT_WRITE_IDLE = 0;

	public static final int DEFAULT_ALL_IDLE = 180;

	/** 是否开启Netty日志. */
	private boolean openNettyLog = false;

	private boolean tcpNoDelay = true;

	private boolean tcpKeepAlive = true;

	/** 服务器监听ip. */
	private String ip = "0.0.0.0";

	/** 服务器端口. */
	private int port = 0;

	/** 服务器worker线程数. */
	private int workerCount = SystemUtils.SYS_PROCESSORS * 2;//读超时

	/** 心跳检测读闲置周期，单位秒，默认0. */
	private int readIdle = DEFAULT_READ_IDLE;//读超时

	/** 心跳检测写闲置周期，单位秒，默认0. */
	private int writeIdle = DEFAULT_WRITE_IDLE;//写超时，目前不设置

	/** 心跳检测读写闲置周期，单位秒，默认180 */
	private int allIdle = DEFAULT_ALL_IDLE;//读写超时，单位秒

	/** 
	 * 对于ChannelOption.SO_BACKLOG的解释： 
	 * 服务器端TCP内核维护有两个队列，我们称之为A、B队列。客户端向服务器端connect时，会发送带有SYN标志的包（第一次握手），服务器端 
	 * 接收到客户端发送的SYN时，向客户端发送SYN ACK确认（第二次握手），此时TCP内核模块把客户端连接加入到A队列中，然后服务器接收到 
	 * 客户端发送的ACK时（第三次握手），TCP内核模块把客户端连接从A队列移动到B队列，连接完成，应用程序的accept会返回。也就是说accept 
	 * 从B队列中取出完成了三次握手的连接。 
	 * A队列和B队列的长度之和就是backlog。当A、B队列的长度之和大于ChannelOption.SO_BACKLOG时，新的连接将会被TCP内核拒绝。 
	 * 所以，如果backlog过小，可能会出现accept速度跟不上，A、B队列满了，导致新的客户端无法连接。要注意的是，backlog对程序支持的 
	 * 连接数并无影响，backlog影响的只是还没有被accept取出的连接 
	 */
	private int backlogSize = 1024;

	private int connTimeoutMillis = 3000;

	/** ChannelOption.SO_SNDBUF参数对应于套接字选项中的SO_SNDBUF，
	 * ChannelOption.SO_RCVBUF参数对应于套接字选项中的SO_RCVBUF这两个参数用于操作接收缓冲区和发送缓冲区的大小，
	 * 接收缓冲区用于保存网络协议站内收到的数据，直到应用程序读取成功，发送缓冲区用于保存发送数据，直到发送成功。
	 * socket参数，接收缓冲区大小，默认32K. */
	private int rcvBufSize = 1024 * 32;

	/** socket参数，发送缓冲区大小，默认32K. */
	private int sendBufSize = 1024 * 32;

	/** reuse address. */
	private boolean reuseAddress = false;

	/** so linger. */
	private int soLinger = -1;

	/** auto read. */
	private boolean autoRead = false;

	public int getSoLinger() {
		return soLinger;
	}

	public void setSoLinger(int soLinger) {
		this.soLinger = soLinger;
	}

	public boolean isAutoRead() {
		return autoRead;
	}

	public void setAutoRead(boolean autoRead) {
		this.autoRead = autoRead;
	}

	public boolean isTcpKeepAlive() {
		return tcpKeepAlive;
	}

	public void setTcpKeepAlive(boolean tcpKeepAlive) {
		this.tcpKeepAlive = tcpKeepAlive;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public int getRcvBufSize() {
		return rcvBufSize;
	}

	public void setRcvBufSize(int rcvBufSize) {
		this.rcvBufSize = rcvBufSize;
	}

	public int getSendBufSize() {
		return sendBufSize;
	}

	public void setSendBufSize(int sendBufSize) {
		this.sendBufSize = sendBufSize;
	}

	public int getConnTimeoutMillis() {
		return connTimeoutMillis;
	}

	public void setConnTimeoutMillis(int connTimeoutMillis) {
		this.connTimeoutMillis = connTimeoutMillis;
	}

	public boolean isOpenNettyLog() {
		return openNettyLog;
	}

	public void setOpenNettyLog(boolean openNettyLog) {
		this.openNettyLog = openNettyLog;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		if (port == 0) {
			try (ServerSocket s = new ServerSocket(0)) {
				port = s.getLocalPort(); //随机可用端口
			} catch (IOException e) {
			}
		}
		if (port == 0) {
			port = (int) (Math.random() * 60000) + 5534; //5534~65534
		}
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

	public int getReadIdle() {
		return readIdle;
	}

	public void setReadIdle(int readIdle) {
		this.readIdle = readIdle;
	}

	public int getWriteIdle() {
		return writeIdle;
	}

	public void setWriteIdle(int writeIdle) {
		this.writeIdle = writeIdle;
	}

	public int getAllIdle() {
		return allIdle;
	}

	public void setAllIdle(int allIdle) {
		this.allIdle = allIdle;
	}

	public int getBacklogSize() {
		return backlogSize;
	}

	public void setBacklogSize(int backlogSize) {
		this.backlogSize = backlogSize;
	}

	public boolean isReuseAddress() {
		return reuseAddress;
	}

	public void setReuseAddress(boolean reuseAddress) {
		this.reuseAddress = reuseAddress;
	}

}
