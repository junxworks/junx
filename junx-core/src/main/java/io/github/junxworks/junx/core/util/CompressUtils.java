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
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.jcraft.jzlib.DeflaterOutputStream;
import com.jcraft.jzlib.InflaterInputStream;

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
	public static byte[] gZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			gzip.write(data);
			gzip.finish();
			gzip.close();
			b = bos.toByteArray();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/***
	  * 解压GZip
	  * 
	  * @param data
	  * @return
	  */
	public static byte[] unGZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			GZIPInputStream gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			b = baos.toByteArray();
			baos.flush();
			baos.close();
			gzip.close();
			bis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/***
	  * 压缩Zip
	  * 
	  * @param data
	  * @return
	  */
	public static byte[] zip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ZipOutputStream zip = new ZipOutputStream(bos);
			ZipEntry entry = new ZipEntry("zip");
			entry.setSize(data.length);
			zip.putNextEntry(entry);
			zip.write(data);
			zip.closeEntry();
			zip.close();
			b = bos.toByteArray();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/***
	  * 解压Zip
	  * 
	  * @param data
	  * @return
	  */
	public static byte[] unZip(byte[] data) {
		byte[] b = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ZipInputStream zip = new ZipInputStream(bis);
			while (zip.getNextEntry() != null) {
				byte[] buf = new byte[1024];
				int num = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((num = zip.read(buf, 0, buf.length)) != -1) {
					baos.write(buf, 0, num);
				}
				b = baos.toByteArray();
				baos.flush();
				baos.close();
			}
			zip.close();
			bis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	//	/***
	//	  * 压缩BZip2
	//	  * 
	//	  * @param data
	//	  * @return
	//	  */
	//	 public static byte[] bZip2(byte[] data) {
	//	  byte[] b = null;
	//	  try {
	//	   ByteArrayOutputStream bos = new ByteArrayOutputStream();
	//	   CBZip2OutputStream bzip2 = new CBZip2OutputStream(bos);
	//	   bzip2.write(data);
	//	   bzip2.flush();
	//	   bzip2.close();
	//	   b = bos.toByteArray();
	//	   bos.close();
	//	  } catch (Exception ex) {
	//	   ex.printStackTrace();
	//	  }
	//	  return b;
	//	 }

	/***
	 * 解压BZip2
	 * 
	 * @param data
	 * @return
	 */
	//	 public static byte[] unBZip2(byte[] data) {
	//	  byte[] b = null;
	//	  try {
	//	   ByteArrayInputStream bis = new ByteArrayInputStream(data);
	//	   CBZip2InputStream bzip2 = new CBZip2InputStream(bis);
	//	   byte[] buf = new byte[1024];
	//	   int num = -1;
	//	   ByteArrayOutputStream baos = new ByteArrayOutputStream();
	//	   while ((num = bzip2.read(buf, 0, buf.length)) != -1) {
	//	    baos.write(buf, 0, num);
	//	   }
	//	   b = baos.toByteArray();
	//	   baos.flush();
	//	   baos.close();
	//	   bzip2.close();
	//	   bis.close();
	//	  } catch (Exception ex) {
	//	   ex.printStackTrace();
	//	  }
	//	  return b;
	//	 }

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
	  *jzlib 压缩数据
	  * 
	  * @param object
	  * @return
	  * @throws IOException
	  */
	public static byte[] jzlib(byte[] object) {
		byte[] data = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DeflaterOutputStream zOut = new DeflaterOutputStream(out);
			DataOutputStream objOut = new DataOutputStream(zOut);
			objOut.write(object);
			objOut.flush();
			zOut.close();
			data = out.toByteArray();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 *jzLib压缩的数据
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] unjzlib(byte[] object) {
		byte[] data = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(object);
			InflaterInputStream zIn = new InflaterInputStream(in);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = zIn.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			data = baos.toByteArray();
			baos.flush();
			baos.close();
			zIn.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * LZ4压缩算法压缩
	 * 参考样例：https://github.com/lz4/lz4-java
	* @param data
	* @return
	*/
	public static byte[] lz4(byte[] data) {
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
	public static byte[] unlz4(byte[] data, int originalDataLength) {
		LZ4FastDecompressor decompressor = lz4Factory.fastDecompressor();
		byte[] restored = new byte[originalDataLength];
		decompressor.decompress(data, 0, restored, 0, originalDataLength);
		return restored;
	}

}
