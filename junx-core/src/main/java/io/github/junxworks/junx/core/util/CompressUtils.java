/*
 ***************************************************************************************
 * 
 * @Title:  CompressUtils.java   
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

public class CompressUtils {
	private static final LZ4Factory lz4Factory = LZ4Factory.fastestInstance();

	/***
	  * 压缩GZip
	  * 
	  * @param data
	  * @return
	  */
	public static byte[] gZip(byte[] data) throws Exception {
		byte[] b = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); GZIPOutputStream gzip = new GZIPOutputStream(bos);) {
			gzip.write(data);
			gzip.finish();
			b = bos.toByteArray();
		} catch (Exception ex) {
			throw ex;
		}
		return b;
	}

	/***
	  * 解压GZip
	  * 
	  * @param data
	  * @return
	  */
	public static byte[] unGZip(byte[] data) throws Exception {
		byte[] b = null;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data); GZIPInputStream gzip = new GZIPInputStream(bis); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			byte[] buf = new byte[1024];
			int num = -1;
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			b = baos.toByteArray();
		} catch (Exception ex) {
			throw ex;
		}
		return b;
	}

	/***
	  * 压缩Zip
	  * 
	  * @param data
	  * @return
	  */
	public static byte[] zip(byte[] data) throws Exception {
		byte[] b = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ZipOutputStream zip = new ZipOutputStream(bos);) {
			ZipEntry entry = new ZipEntry("zip");
			entry.setSize(data.length);
			zip.putNextEntry(entry);
			zip.write(data);
			zip.closeEntry();
			b = bos.toByteArray();
		} catch (Exception ex) {
			throw ex;
		}
		return b;
	}

	/***
	  * 解压Zip
	  * 
	  * @param data
	  * @return
	  */
	public static byte[] unZip(byte[] data) throws Exception {
		byte[] b = null;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data); ZipInputStream zip = new ZipInputStream(bis); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			while (zip.getNextEntry() != null) {
				byte[] buf = new byte[1024];
				int num = -1;
				while ((num = zip.read(buf, 0, buf.length)) != -1) {
					baos.write(buf, 0, num);
				}
			}
			b = baos.toByteArray();
		} catch (Exception ex) {
			throw ex;
		}
		return b;
	}

	/**
	  * 把字节数组转换成16进制字符串
	  * 
	  * @param bArray
	  * @return
	  */
	public static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * LZ4压缩算法压缩
	 * 参考样例：https://github.com/lz4/lz4-java
	* @param data
	* @return
	*/
	public static byte[] lz4(byte[] data) throws Exception {
		final int decompressedLength = data.length;
		LZ4Compressor compressor = lz4Factory.fastCompressor();
		int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
		byte[] compressed = new byte[maxCompressedLength];
		compressor.compress(data, 0, decompressedLength, compressed, 0, maxCompressedLength);
		return compressed;
	}

	/**
	 * LZ4压缩算法解压
	* @param data
	* @param originalDataLength 压缩前数据大小，需要压缩前记录
	* @return
	*/
	public static byte[] unlz4(byte[] data, int originalDataLength) throws Exception {
		LZ4FastDecompressor decompressor = lz4Factory.fastDecompressor();
		byte[] restored = new byte[originalDataLength];
		decompressor.decompress(data, 0, restored, 0, originalDataLength);
		return restored;
	}
}
