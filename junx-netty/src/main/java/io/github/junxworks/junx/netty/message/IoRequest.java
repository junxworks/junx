/*
 ***************************************************************************************
 * 
 * @Title:  IoRequest.java   
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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * io请求
 *
 * @ClassName:  IoRequest
 * @author: Michael
 * @date:   2018-5-25 23:36:30
 * @since:  v1.0
 */
public class IoRequest implements IdentifiableMessage {

	/** 报文类型. */
	@JSONField(ordinal = 0)
	private byte type = MessageConstants.MESSAGE_TYPE_NORMAL;

	/** 请求唯一id. */
	@JSONField(ordinal = 1)
	private String id = UUID.randomUUID().toString();

	/** 是否需要服务器端应答，如果是false，则服务器端不会应答. */
	@JSONField(ordinal = 2)
	private boolean needResponse = true;

	/** 客户端ip，不会参与序列化和反序列化. */
	@JSONField(ordinal = 2)
	private String remoteIp;

	/** 客户端设置的请求超时时间，从服务器端接收到请求开始计算，默认-1不超时，单位毫秒. */
	@JSONField(ordinal = 3)
	private int requestTimeout = -1;

	/** 非必填. */
	@JSONField(serialize = false)
	private byte[] data; //data数据

	public boolean isNeedResponse() {
		return needResponse;
	}

	public void setNeedResponse(boolean needResponse) {
		this.needResponse = needResponse;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(int timeout) {
		this.requestTimeout = timeout;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && (obj instanceof IoRequest) && id.equals(((IoRequest) obj).id);
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
		return id;
	}

	public byte[] toBytes() throws IOException {
		try (MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();) {
			pack(packer);
			return packer.toByteArray();
		}
	}

	public void toStream(OutputStream out) throws IOException {
		try (MessagePacker packer = MessagePack.newDefaultPacker(out);) {
			pack(packer);
		}
	}

	protected void pack(MessagePacker packer) throws IOException {
		packer.packByte(type);
		packer.packString(id);
		packer.packBoolean(needResponse);
		packer.packInt(requestTimeout);
		if (data == null || data.length == 0) {
			packer.packBinaryHeader(0);
		} else {
			packer.packBinaryHeader(data.length);
			packer.writePayload(data);
		}
	}

	public IoRequest readFromBytes(byte[] data) throws IOException {
		try (MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(data);) {
			return unpack(unpacker);
		}
	}

	public IoRequest readFromStream(InputStream in) throws IOException {
		try (MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(in);) {
			return unpack(unpacker);
		}
	}

	protected IoRequest unpack(MessageUnpacker unpacker) throws IOException {
		setType(unpacker.unpackByte());
		setId(unpacker.unpackString());
		setNeedResponse(unpacker.unpackBoolean());
		setRequestTimeout(unpacker.unpackInt());
		int dataLength = unpacker.unpackBinaryHeader();
		if (dataLength > 0) {
			setData(unpacker.readPayload(dataLength));
		}
		return this;
	}
}
