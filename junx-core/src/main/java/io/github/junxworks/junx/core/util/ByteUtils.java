/*
 ***************************************************************************************
 * EP for web developers.Supported By Junxworks
 * @Title:  ByteUtils.java   
 * @Package io.github.junxworks.junx.core.util   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Administrator
 * @date:   2021-1-31 15:44:22   
 * @version V1.0 
 * @Copyright: 2021 Junxworks. All rights reserved. 
 * 注意：
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */

package io.github.junxworks.junx.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;

import io.github.junxworks.junx.core.exception.BaseRuntimeException;
import io.github.junxworks.junx.core.lang.ByteContainer;

// TODO: Auto-generated Javadoc
/**
 * Byte工具类.
 *
 * @author Michael
 * @since v1.0
 * @date Aug 7, 2015
 */
public class ByteUtils {

	/**
	 * 把byte数组转换成int
	 *
	 * @param b
	 *            the b
	 * @param start
	 *            the start
	 * @param len
	 *            the len
	 * @return the int
	 */
	public static int bytes2Int(byte[] b, int start, int len) {
		int sum = 0;
		int end = start + len;
		for (int i = start; i < end; i++) {
			int n = ((int) b[i]) & 0xff;
			n <<= (--len) * 8;
			sum = n + sum;
		}
		return sum;
	}

	/**
	 * Int转换成 byte数组.
	 *
	 * @param value
	 *            the value
	 * @param len
	 *            the len
	 * @return the byte[]
	 */
	public static byte[] int2Bytes(int value, int len) {
		byte[] b = new byte[len];
		for (int i = 0; i < len; i++) {
			b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
		}
		return b;
	}

	/**
	 * byte数组转换成String
	 *
	 * @param b
	 *            the b
	 * @param start
	 *            the start
	 * @param len
	 *            the len
	 * @return the string
	 */
	public static String bytes2String(byte[] b, int start, int len) {
		return new String(b, start, len);
	}

	/**
	 * String转换成 byte数组.
	 *
	 * @param str
	 *            the str
	 * @return the byte[]
	 */
	public static byte[] string2Bytes(String str) {
		return str.getBytes();
	}

	/**
	 * 将指定originalBytes原始数组中offset开始len长度的内容替换成replaceBytes的内容.
	 *
	 * @param originalBytes
	 *            the original bytes
	 * @param offset
	 *            the offset
	 * @param len
	 *            the len
	 * @param replaceBytes
	 *            the replace bytes
	 * @return the byte[]
	 */
	public static byte[] bytesReplace(byte[] originalBytes, int offset, int len, byte[] replaceBytes) {
		byte[] newBytes = new byte[originalBytes.length + (replaceBytes.length - len)];
		System.arraycopy(originalBytes, 0, newBytes, 0, offset);
		System.arraycopy(replaceBytes, 0, newBytes, offset, replaceBytes.length);
		System.arraycopy(originalBytes, offset + len, newBytes, offset + replaceBytes.length, originalBytes.length - offset - len);
		return newBytes;
	}

	/**
	 * 将输入流转换穿byte数组
	 *
	 * @param in
	 *            输入流
	 * @return 从输入流里面读取的byte数组
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] inputStreamToBytes(InputStream in) throws IOException {
		byte[] buffer = new byte[8 * 1024];
		int readBytes;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			while ((readBytes = in.read(buffer)) != -1) {
				out.write(buffer, 0, readBytes);
			}
			return out.toByteArray();
		} finally {
			if (out != null)
				out.close();
		}
	}

	/**
	 * 将一个集合中的对象，转换成string后，序列化成byte数组
	 *
	 * @param data the data
	 * @return the byte[]
	 * @throws IOException 
	 */
	public static byte[] list2bytes(List<Object> data) throws IOException {
		if (data == null || data.isEmpty()) {
			return null;
		}
		ByteContainer container = new ByteContainer();
		container.writeInt(data.size()); //list size
		for (int i = 0, len = data.size(); i < len; i++) {
			Object v = data.get(i);
			container.writeString(v == null ? null : v.toString());
		}
		return container.toBytes();
	}

	/**
	 * 将一个集合中的对象，转换成string后，序列化成byte数组
	 *
	 * @param data the data
	 * @return the byte[]
	 * @throws IOException 
	 */
	public static List<Object> bytes2List(byte[] data) throws IOException {
		List<Object> res = new ArrayList<Object>();
		if (data == null || data.length == 0) {
			return res;
		}
		ByteContainer container = new ByteContainer(data);
		int size = container.readInt(); //list size
		while (size-- > 0) {
			res.add(container.readString());
		}
		return res;
	}

	/**
	 * Object 2 json bytes.
	 *
	 * @param obj the obj
	 * @return the byte[]
	 */
	public static byte[] object2JsonBytes(Object obj) {
		if (obj == null) {
			return null;
		}
		return JSON.toJSONString(obj).getBytes();
	}

	/**
	 * Json bytes 2 object.
	 *
	 * @param <T> the generic type
	 * @param clazz the clazz
	 * @param data the data
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonBytes2Object(Class<T> type, byte[] data) {
		if (data == null) {
			return null;
		}
		return (T) JSON.parse(new String(data));
	}

	/** 常量 KRYOPOOL. */
	public static final KryoPool KRYOPOOL_FALSE = new KryoPool.Builder(new KryoFactory() {
		public Kryo create() {
			Kryo kryo = new Kryo();
			//			kryo.setReferences(false); //对于list和map对象来说，这个会提高很多效率
			kryo.setRegistrationRequired(false);
			return kryo;
		}
	}).build();

	/** 常量 KRYOPOOL. */
	public static final KryoPool KRYOPOOL_TRUE = new KryoPool.Builder(new KryoFactory() {
		public Kryo create() {
			Kryo kryo = new Kryo();
			//			kryo.setReferences(false); //对于list和map对象来说，这个会提高很多效率
			kryo.setRegistrationRequired(false);
			return kryo;
		}
	}).build();

	/**
	 * 将对象序列化成kryo的byte数组
	 *
	 * @param obj the obj
	 * @return the byte[]
	 */
	public static byte[] object2KryoBytes(Object obj) {
		return object2KryoBytes(obj, false);
	}

	/**
	 * 将对象序列化成kryo的byte数组
	 *
	 * @param obj the obj
	 * @param reference the reference
	 * @return the byte[]
	 */
	public static byte[] object2KryoBytes(Object obj, boolean reference) {
		KryoPool pool = null;
		if (reference) {
			pool = KRYOPOOL_TRUE;
		} else {
			pool = KRYOPOOL_FALSE;
		}
		Kryo kryo = pool.borrow();
		kryo.setReferences(reference);
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			Output _output = new Output(bo);
			kryo.writeObject(_output, obj);
			return _output.toBytes();
		} finally {
			pool.release(kryo);
		}
	}

	/**
	 *  将kryo序列化后的byte数组范序列化成指定class的对象
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param data the data
	 * @return the t
	 */
	public static <T> T KryoBytes2Object(Class<T> type, byte[] data) {
		return KryoBytes2Object(type, data, false);
	}

	/**
	 * 将kryo序列化后的byte数组范序列化成指定class的对象
	 *
	 * @param <T> the generic type
	 * @param type the type
	 * @param data the data
	 * @param reference the reference
	 * @return the t
	 */
	public static <T> T KryoBytes2Object(Class<T> type, byte[] data, boolean reference) {
		if (data == null) {
			return null;
		}
		KryoPool pool = null;
		if (reference) {
			pool = KRYOPOOL_TRUE;
		} else {
			pool = KRYOPOOL_FALSE;
		}
		Kryo kryo = pool.borrow();
		kryo.setReferences(reference);
		try {
			Input input = new Input(data);
			return kryo.readObject(input, type);
		} finally {
			pool.release(kryo);
		}
	}

	/**
	 * 对象采用java默认序列化
	 *
	 * @param obj the obj
	 * @return the byte[]
	 */
	public static byte[] obj2bytesJava(Object obj) {
		try (ByteArrayOutputStream bo = new ByteArrayOutputStream(); ObjectOutputStream oo = new ObjectOutputStream(bo);) {
			oo.writeObject(obj);
			byte[] bytes = bo.toByteArray();
			return bytes;
		} catch (Exception e) {
			throw new BaseRuntimeException("序列化对象失败:" + obj.getClass(), e);
		}
	}

	/**
	 * java默认序列化还原对象
	 *
	 * @param obj the obj
	 * @return the byte[]
	 */
	@SuppressWarnings("unchecked")
	public static <T> T bytes2ObjJava(Class<T> clazz, byte[] bytes) throws ClassNotFoundException, IOException {
		try (ByteArrayInputStream bi = new ByteArrayInputStream(bytes); ObjectInputStream oi = new ObjectInputStream(bi);) {
			return (T) oi.readObject();
		}
	}
}
