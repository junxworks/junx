/*
 ***************************************************************************************
 * 
 * @Title:  IoResponse.java   
 * @Package io.github.junxworks.junx.netty.message   
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
package io.github.junxworks.junx.netty.message;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import io.github.junxworks.junx.core.lang.ByteContainer;

/**
 *
 * @ClassName:  IoResponse
 * @author: Michael
 * @date:   2018-5-25 23:30:16
 * @since:  v1.0
 */
public class IoResponse implements IdentifiableMessage {

	/** 应答类型，同request. */
	@JSONField(ordinal = 0)
	private byte type = MessageConstants.MESSAGE_TYPE_NORMAL;

	/** 请求id. */
	@JSONField(ordinal = 1)
	private String requestId;

	/** 状态码. */
	@JSONField(ordinal = 2)
	private short statusCode = MessageConstants.STATUS_CODE_SUCCESS;

	/** 应答服务器ip. */
	@JSONField(ordinal = 3)
	private String serverAddr;

	/** 应答数据. */
	@JSONField(serialize = false)
	private byte[] data;//响应数据

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public short getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(short statusCode) {
		this.statusCode = statusCode;
	}

	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	@Override
	@JSONField(serialize = false)
	public String getUUID() {
		// TODO Auto-generated method stub
		return requestId;
	}

	public boolean isOK() {
		return MessageConstants.STATUS_CODE_SUCCESS == statusCode;
	}

	public byte[] toBytes() throws IOException {
		ByteContainer byteContainer = new ByteContainer();
		pack(byteContainer);
		return byteContainer.toBytes();
	}

	protected void pack(ByteContainer byteContainer) throws IOException {
		byteContainer.writeByte(type);
		byteContainer.writeUTF(requestId);
		byteContainer.writeUTF(serverAddr);
		byteContainer.writeShort(statusCode);
		byteContainer.writeBytes(data);
	}

	public IoResponse readFromBytes(byte[] data) throws IOException {
		ByteContainer byteContainer = new ByteContainer(data);
		return unpack(byteContainer);
	}

	protected IoResponse unpack(ByteContainer byteContainer) throws IOException {
		setType(byteContainer.readByte());
		setRequestId(byteContainer.readUTF());
		setServerAddr(byteContainer.readUTF());
		setStatusCode(byteContainer.readShort());
		setData(byteContainer.readByteArray());
		return this;
	}
}