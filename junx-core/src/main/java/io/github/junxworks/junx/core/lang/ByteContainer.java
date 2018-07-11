/*
 ***************************************************************************************
 * 
 * @Title:  ByteContainer.java   
 * @Package io.github.junxworks.junx.core.lang   
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
package io.github.junxworks.junx.core.lang;

import java.io.UnsupportedEncodingException;

import io.github.junxworks.junx.core.util.StringUtils;

/**
 * byte容器，用于转换各种类型数据到byte容器，最后产出一个byte数组
 *
 * @ClassName:  ByteContrainer
 * @author: Michael
 * @date:   2017-9-6 15:32:40
 * @since:  v1.0
 */
public final class ByteContainer {

	/** 常量 min_buf_size. */
	private static final int min_buf_size = 256;

	/** buff. */
	private byte[] buffer = null;

	/** len. */
	private int _len = 0;

	/** pos. */
	private int _pos = 0;

	/** buff error. */
	private int buffer_error = 0;

	/**
	 * 构造一个新的 byte contrainer 对象.
	 */
	public ByteContainer() {
		applyCapacity(min_buf_size);
	}

	/**
	 * 构造一个新的 byte contrainer 对象.
	 *
	 * @param len the len
	 */
	public ByteContainer(int len) {
		applyCapacity(len);
	}

	/**
	 * 构造一个新的 byte contrainer 对象.
	 *
	 * @param buff the buff
	 */
	public ByteContainer(byte[] buff) {
		buffer = buff;
		_len = buff.length;
	}

	/**
	 * 构造一个新的 byte contrainer 对象.
	 *
	 * @param buff the buff
	 * @param len the len
	 */
	public ByteContainer(byte[] buff, int len) {
		buffer = buff;
		_len = len;
	}

	/**
	 * Array.
	 *
	 * @return the byte[]
	 */
	public byte[] getBuffer() {
		return buffer;
	}

	/**
	 * 将内部的数据转换成byte数组
	 *
	 * @return the byte[]
	 */
	public byte[] toBytes() {
		return toBytes(0);
	}

	/**
	 * To bytes.
	 *
	 * @param skip_len the skip len
	 * @return the byte[]
	 */
	public byte[] toBytes(int skip_len) {
		if (skip_len < 0)
			return null;

		int len = size() - skip_len;
		if (len <= 0)
			return null;

		byte[] ret = new byte[len];
		System.arraycopy(buffer, skip_len, ret, 0, len);

		return ret;

	}

	/**
	 * 是否可用.
	 *
	 * @return true, if successful
	 */
	public boolean available() {
		return this.buffer_error == 0;
	}

	/**
	 * 
	 *检查是否还有len这么长的字节可读
	 * @param len the len
	 * @return true, if successful
	 */
	public boolean rangeCheck(int len) {
		return available() && rangeCheck(len, _pos);
	}

	/**
	 * Can read.
	 *
	 * @param len the len
	 * @param pos the pos
	 * @return true, if successful
	 */
	public boolean rangeCheck(int len, int pos) {
		return pos + len <= buffer.length;
	}

	/**
	 * 检查是否还有len这么长的字节可读
	 *
	 * @param len the len
	 * @return true, if successful
	 */
	private boolean checkBuffSize(int len) {
		return checkBuffSize(len, _pos);
	}

	/**
	 * Check buff size.
	 *
	 * @param len the len
	 * @param pos the pos
	 * @return true, if successful
	 */
	private boolean checkBuffSize(int len, int pos) {
		if (pos + len <= buffer.length)
			return true;

		buffer_error = 1;

		return false;
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return _len;
	}

	/**
	 * Max size.
	 *
	 * @return the int
	 */
	public int max_size() {
		return buffer.length;
	}

	/**
	 * Ppos.
	 *
	 * @param pos the pos
	 */
	public void ppos(int pos) {
		_len = pos;
	}

	/**
	 * Gpos.
	 *
	 * @return the int
	 */
	public int gpos() {
		return _pos;
	}

	/**
	 * Gpos.
	 *
	 * @param pos the pos
	 */
	public void position(int pos) {
		_pos = pos;
	}

	/**
	 * Reserve.
	 *
	 * @param len the len
	 */
	public void resize(int len) {
		if (len <= buffer.length)
			return;

		byte[] t = new byte[len];
		System.arraycopy(buffer, 0, t, 0, _len);

		buffer = t;
	}

	/**
	 * 请求指定cap大小的空间，如果没有这么大的话，需要resize一下
	 *
	 * @param cap the cap
	 */
	private void applyCapacity(int size) {
		int cap = _len + size;
		if (buffer == null) {
			buffer = new byte[Math.max(cap, min_buf_size)];
			return;
		}

		if (cap < buffer.length)
			return;

		int n = Math.max(buffer.length, min_buf_size);

		while (n < cap)
			n += n >> 2;

		resize(n);
	}

	/**
	 * Save.
	 *
	 * @param b the b
	 * @return the byte contrainer
	 */
	public ByteContainer writeByte(byte b) {
		applyCapacity(1);
		buffer[_len++] = b;

		return this;
	}

	/**
	 * Save.
	 *
	 * @param i the i
	 * @return the byte contrainer
	 */
	public ByteContainer writeShort(short i) {
		applyCapacity(2);
		buffer[_len++] = (byte) ((i >>> 8) & 0xff);
		buffer[_len++] = (byte) (i & 0xff);

		return this;
	}

	/**
	 * Save.
	 *
	 * @param i the i
	 * @return the byte contrainer
	 */
	public ByteContainer writeInt(int i) {
		applyCapacity(4);
		buffer[_len++] = (byte) ((i >>> 24) & 0xff);
		buffer[_len++] = (byte) ((i >>> 16) & 0xff);
		buffer[_len++] = (byte) ((i >>> 8) & 0xff);
		buffer[_len++] = (byte) (i & 0xff);

		return this;
	}

	/**
	 * Save.
	 *
	 * @param i the i
	 * @return the byte contrainer
	 */
	public ByteContainer writeLong(long i) {
		writeInt((int) (i >> 32));
		return writeInt((int) (i & 0xFFFFFFFFL));
	}

	/**
	 * Save.
	 *
	 * @param i the i
	 * @return the byte contrainer
	 */
	public ByteContainer writeFloat(float i) {
		return writeInt(Float.floatToIntBits(i));
	}

	/**
	 * Save.
	 *
	 * @param i the i
	 * @return the byte contrainer
	 */
	public ByteContainer writeDouble(double i) {
		return writeLong(Double.doubleToLongBits(i));
	}

	/**
	 * Save.
	 *
	 * @param s the s
	 * @return the byte contrainer
	 */
	public ByteContainer writeUTF(String s) {
		if (StringUtils.isNull(s))
			return wirteLength(Integer.MAX_VALUE);

		try {
			writeBytes(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * Save 2.
	 *
	 * @param s the s
	 * @return the byte contrainer
	 */
	public ByteContainer writeString(String s) {
		if (StringUtils.isNull(s))
			return wirteLength(Integer.MAX_VALUE);

		return writeChars(s.toCharArray());
	}

	/**
	 * Save.
	 *
	 * @param s the s
	 * @return the byte contrainer
	 */
	public ByteContainer writeBytes(byte[] s) {
		if (s == null) {
			return wirteLength(Integer.MAX_VALUE);
		}

		wirteLength(s.length);
		applyCapacity(s.length);
		System.arraycopy(s, 0, buffer, _len, s.length);
		_len += s.length;

		return this;
	}

	/**
	 * Save.
	 *
	 * @param s the s
	 * @return the byte contrainer
	 */
	public ByteContainer writeChars(char[] s) {
		if (s == null)
			return wirteLength(Integer.MAX_VALUE);

		applyCapacity(s.length * 2 + 4);
		wirteLength(s.length);
		for (int i = 0; i < s.length; i++)
			writeShort((short) s[i]);

		return this;
	}

	/**
	 * 写入数据长度
	 *
	 * @param len the len
	 * @return the byte contrainer
	 */
	public ByteContainer wirteLength(int len) {
		while (len > 0x7F) {
			writeByte((byte) (len & 0x7F));
			len >>= 7;
		}

		writeByte((byte) (len | 0x80));

		return this;
	}

	/**
	 * C int.
	 *
	 * @param b the b
	 * @return the int
	 */
	public final int c_int(byte b) {
		return 0xff & b;
	}

	/**
	 * C int.
	 *
	 * @param b the b
	 * @return the int
	 */
	public final int c_int(short b) {
		return 0xffff & b;
	}

	/**
	 * C long.
	 *
	 * @param i the i
	 * @return the long
	 */
	public final long c_long(int i) {
		return 0xffffffffL & i;
	}

	/**
	 * Load byte.
	 *
	 * @return the byte
	 */
	public byte readByte() {
		if (!this.checkBuffSize(1))
			return -1;

		return buffer[_pos++];
	}

	/**
	 * Load short.
	 *
	 * @return the short
	 */
	public short readShort() {
		if (!this.checkBuffSize(2))
			return -1;

		int ret = c_int(buffer[_pos++]) << 8;
		return (short) (ret | c_int(buffer[_pos++]));
	}

	/**
	 * Load int.
	 *
	 * @return the int
	 */
	public int readInt() {
		int ret = c_int(readShort()) << 16;
		return (ret) | c_int(readShort());
	}

	/**
	 * Load long.
	 *
	 * @return the long
	 */
	public long readLong() {
		long ret = c_long(readInt()) << 32;
		return (ret) | c_long(readInt());
	}

	/**
	 * Load float.
	 *
	 * @return the float
	 */
	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}

	/**
	 * Load double.
	 *
	 * @return the double
	 */
	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}

	/**
	 * Load byte array.
	 *
	 * @return the byte[]
	 */
	public byte[] readByteArray() {
		int len = allocate();
		if (len == Integer.MAX_VALUE)
			return null;

		byte[] ret = new byte[len];

		if (!this.checkBuffSize(len))
			return null;

		System.arraycopy(buffer, _pos, ret, 0, len);
		_pos += len;

		return ret;
	}

	/**
	 * Load char array.
	 *
	 * @return the char[]
	 */
	public char[] readCharArray() {
		int len = allocate();
		if (len == Integer.MAX_VALUE)
			return null;

		char[] ret = new char[len];

		if (!this.checkBuffSize(len * 2))
			return null;

		for (int i = 0; i < len; i++)
			ret[i] = (char) readShort();

		return ret;
	}

	/**
	 * Load string.
	 *
	 * @return the string
	 */
	public String readUTF() {
		byte[] b = readByteArray();
		if (b == null)
			return null;

		try {
			return new String(b, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Load string 2.
	 *
	 * @return the string
	 */
	public String readString() {
		char[] b = readCharArray();
		if (b == null)
			return null;

		return String.valueOf(b);
	}

	/**
	 * 计算结尾
	 *
	 * @return the int
	 */
	public int allocate() {
		int ret = 0, c;
		for (int i = 0; i < 5; i++) {
			c = c_int(readByte());
			if ((c & 0x80) == 0x80) {
				return ret | ((c ^ 0x80) << i * 7);
			}

			ret |= c << (i * 7);
		}

		return 0;
	}

	/**
	 * Load bool.
	 *
	 * @return true, if successful
	 */
	public boolean readBoolean() {
		return readByte() != 0;
	}

	/**
	 * Save.
	 *
	 * @param b the b
	 * @return the byte contrainer
	 */
	public ByteContainer writeBoolean(boolean b) {
		return writeByte((byte) (b ? 1 : 0));
	}

}