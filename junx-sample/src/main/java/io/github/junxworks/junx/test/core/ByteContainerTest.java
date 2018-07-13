package io.github.junxworks.junx.test.core;

import java.io.IOException;

import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import io.github.junxworks.junx.core.lang.ByteContainer;
import io.github.junxworks.junx.core.util.ClockUtils;
import io.github.junxworks.junx.core.util.ClockUtils.Clock;

public class ByteContainerTest {
	public static String string = "1239649781892730asjdhfq#$%#^%$*&(&(*";

	public static byte[] bytes = string.getBytes();

	public static byte b = (byte) 1;

	public static boolean bo = true;

	public static double d = 1.11234d;

	public static float f = 2.123412f;

	public static int count = 1000000;

	public static long l = 1234L;

	public static short s = 123;

	@Test
	public void byteContainerVSMessagePack() throws Exception {
		byte[] bcData = bcWrite();
		System.out.println("bc len:" + bcData.length);
		Clock clock = ClockUtils.createClock();
		for (int i = 0; i < count; i++) {
			bcWrite();
		}
		System.out.println("bc write cost:" + clock.countMillisAndReset());
		byte[] msgData = msgWrite();
		System.out.println("msg len:" + msgData.length);
		for (int i = 0; i < count; i++) {
			msgWrite();
		}
		System.out.println("msg write cost:" + clock.countMillisAndReset());
		for (int i = 0; i < count; i++) {
			bcRead(bcData);
		}
		System.out.println("bc read cost:" + clock.countMillisAndReset());
		for (int i = 0; i < count; i++) {
			msgRead(msgData);
		}
		System.out.println("msg read cost:" + clock.countMillisAndReset());

	}

	public static byte[] bcWrite() {
		ByteContainer bc = new ByteContainer();
		bc.writeBoolean(bo);
		bc.writeByte(b);
		bc.writeBytes(bytes);
		bc.writeDouble(d);
		bc.writeFloat(f);
		bc.writeInt(count);
		bc.writeLong(l);
		bc.writeShort(s);
		bc.writeString(string);
		return bc.toBytes();
	}

	public static void bcRead(byte[] data) {
		ByteContainer bc = new ByteContainer(data);
		bc.readBoolean();
		bc.readByte();
		bc.readByteArray();
		bc.readDouble();
		bc.readFloat();
		bc.readInt();
		bc.readLong();
		bc.readShort();
		bc.readString();
	}

	public static byte[] msgWrite() throws IOException {
		try (MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();) {
			packer.packBoolean(bo);
			packer.packByte(b);
			packer.packBinaryHeader(bytes.length);
			packer.writePayload(bytes);
			packer.packDouble(d);
			packer.packFloat(f);
			packer.packInt(count);
			packer.packLong(l);
			packer.packShort(s);
			packer.packString(string);
			return packer.toByteArray();
		}
	}

	public static void msgRead(byte[] data) throws IOException {
		try (MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(data);) {
			unpacker.unpackBoolean();
			unpacker.unpackByte();
			unpacker.readPayload(unpacker.unpackBinaryHeader());
			unpacker.unpackDouble();
			unpacker.unpackFloat();
			unpacker.unpackInt();
			unpacker.unpackLong();
			unpacker.unpackShort();
			unpacker.unpackString();
		}
	}
}
