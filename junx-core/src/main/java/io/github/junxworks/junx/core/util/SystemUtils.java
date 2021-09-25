/*
 ***************************************************************************************
 * 
 * @Title:  SystemUtils.java   
 * @Package io.github.junxworks.junx.core.util   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:34:36   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.core.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class SystemUtils {

	/** 操作系统处理器数，看CPU有多少core，1个core多少线程数. */
	public static final int SYS_PROCESSORS = Runtime.getRuntime().availableProcessors();

	/** 操作系统名. */
	public static final String SYS_NAME = System.getProperty("os.name");

	public static final boolean LINUX_SYSTEM = "Linux".equalsIgnoreCase(SYS_NAME);

	public static String IP;

	public static String HOSTNAME;

	static {
		try {
			IP = getLocalIPAddress();
		} catch (Exception e) {
			IP = "unknown";
		}
		try {
			HOSTNAME = getHostName();
		} catch (UnknownHostException e) {
			HOSTNAME = "unknown";
		}
	}

	public static String getHostName() throws UnknownHostException {
		Inet4Address ip = (Inet4Address) Inet4Address.getLocalHost();
		return ip.getHostName();
	}

	private static final String IP4Regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

	public static String getLocalIPAddress() throws UnknownHostException, SocketException {
		// Before we connect somewhere, we cannot be sure about what we'd be bound to; however,
		// we only connect when the message where client ID is, is long constructed. Thus,
		// just use whichever IP address we can find.
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface current = interfaces.nextElement();
			if (!current.isUp() || current.isLoopback() || current.isVirtual())
				continue;
			String displayName = current.getDisplayName();
			if (StringUtils.notNull(displayName) && displayName.contains("Virtual Ethernet Adapter")) {
				continue;
			}
			Enumeration<InetAddress> addresses = current.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress addr = addresses.nextElement();
				if (addr.isLoopbackAddress())
					continue;
				String ipAddr = addr.getHostAddress();
				if (ipAddr.matches(IP4Regex)) {
					return ipAddr;
				} else {
					continue;
				}

			}
		}
		return null;
	}

	public static final void initializeServerIP() throws UnknownHostException, SocketException {
		String addr = getLocalIPAddress();//注册zookeeper，先初始化本地ip
		if (StringUtils.notNull(addr)) {//默认非空
			System.setProperty("server.ip", addr); //如果是空，则手动从启动参数中加入
		}
	}
}
