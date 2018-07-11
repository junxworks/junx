/*
 ***************************************************************************************
 * 
 * @Title:  HostParse.java   
 * @Package io.github.junxworks.junx.cache.provider   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:38:51   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.cache.provider;

import java.util.ArrayList;

import com.aerospike.client.Host;

/**
 * 将多个ip、port配置装换为多个Host实例
 *
 * @ClassName:  HostParse
 * @author: 司福林
 * @date:   2017-8-14 14:24:35
 * @since:  v1.0
 */
public class HostParse {
	
	/** hosts. */
	private Host[] hosts;
	
	/** str. */
	private String str;
	
	/** offset. */
	private int offset;
	
	/** length. */
	private int length;
	
	/** 配置的间隔符号 */
	private char c;
	
	/**
	 * 将ip、port号转换为Host对象.
	 *
	 * @param str 多个ip、port组成的字符串
	 * @param defaultPort the default port
	 */
	public  Host[] converHosts(String str, int defaultPort) {
		this.str = str;
		this.length = str.length();
		this.offset = 0;
		this.c = ',';
		
		ArrayList<Host> list = new ArrayList<Host>();
		String hostname;
		String tlsname;
		int port;
		
		while (offset < length) {
			if (c != ',') {
				throw new RuntimeException();
			}
			hostname = parseHost();
			tlsname = null;
			port = defaultPort;
			
			if (offset < length && c == ':') {
				String s = parseString();
				
				if (s.length() > 0) {
					if (Character.isDigit(s.charAt(0))) {
						// Found port.
						port = Integer.parseInt(s);
					}
					else {
						// Found tls name.
						tlsname = s;
						
						// Parse port.
						s = parseString();
						
						if (s.length() > 0) {
							port = Integer.parseInt(s);
						}
					}
				}
			}				
			list.add(new Host(hostname, port));
		}
		return hosts = list.toArray(new Host[list.size()]);
	}
	
	/**
	 * Parses the host.
	 *
	 * @return the string
	 */
	private String parseHost() {
		c = str.charAt(offset);
		
		if (c == '[') {
			// IPv6 addresses are enclosed by brackets.
			int begin = ++offset;
			
			while (offset < length) {
				c = str.charAt(offset);
				
				if (c == ']') {
					String s = str.substring(begin, offset++);
					
					if (offset < length) {
						c = str.charAt(offset++);
					}
					return s;
				}
				offset++;
			}
			throw new RuntimeException("Unterminated bracket");
		}
		else {
			return parseString();
		}
	}
	
	/**
	 * Parses the string.
	 *
	 * @return the string
	 */
	private String parseString() {
		int begin = offset;
		
		while (offset < length) {
			c = str.charAt(offset);
			
			if (c == ':' || c == ',') {
				return str.substring(begin, offset++);
			}
			offset++;
		}
		return str.substring(begin, offset);
	}
}
