/*
 ***************************************************************************************
 * 
 * @Title:  FileUtils.java   
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

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import io.github.junxworks.junx.core.exception.DirectoryNotFoundException;

// TODO: Auto-generated Javadoc
/**
 * 文件工具类
 *
 * @author Michael
 * @since v1.0
 * @date Aug 7, 2015
 */
public final class FileUtils extends org.apache.commons.io.FileUtils {

	/** path separator. */
	private static String PATH_SEPARATOR = "/";

	/** 常量 ENCODING. */
	public static final String ENCODING = System.getProperty("file.encoding");

	/**
	 * 构造一个新的 file utils 对象.
	 */
	private FileUtils() {
	}

	/**
	 * 将child路径以File Path标准拼接到parent上.
	 *
	 * @param parent the parent
	 * @param child the child
	 * @return the string
	 */
	public final static String concatPath(String parent, String child) {
		if (parent == null || parent.trim().length() <= 0) {
			return child;
		}

		if (child == null || child.trim().length() <= 0) {
			return parent;
		}

		if (!parent.endsWith(PATH_SEPARATOR)) {
			if (!child.startsWith(PATH_SEPARATOR)) {
				parent = parent + PATH_SEPARATOR;
			}
		} else {
			if (child.startsWith(PATH_SEPARATOR)) {
				child = child.substring(1);
			}
		}

		return parent.concat(child);
	}

	/**
	 * 根据file的name数据来过滤出指定的file集合.
	 *
	 * @param folderFile 要过滤的文件目录对象
	 * @param filePattern 要过滤的文件名字
	 * @param recursion 是否递归到下级文件夹
	 * @return 过滤后的文件集合
	 */
	public static File[] filterFiles(File folderFile, String filePattern, boolean recursion) {
		return filterFiles(folderFile, filePattern, recursion, false);
	}

	/**
	 * 根据file的name数据来过滤出指定的file集合.
	 *
	 * @param folderFile 要过滤的文件目录对象
	 * @param filePattern 要过滤的文件名字
	 * @param recursion 是否递归到下级文件夹
	 * @param sort 是否需要数组排序
	 * @return the file[]
	 */
	public static File[] filterFiles(File folderFile, String filePattern, boolean recursion, boolean sort) {
		List container = new ArrayList();
		filterFiles(folderFile, filePattern, container, recursion, sort);

		return (File[]) container.toArray(new File[container.size()]);
	}

	/**
	 * 根据file的name数据来过滤出指定的file集合.
	 *
	 * @param folderFile the folder file
	 * @param filePattern the file pattern
	 * @param container the container
	 * @param recursion the recursion
	 * @param sort the sort
	 */
	private static void filterFiles(File folderFile, String filePattern, List container, boolean recursion, boolean sort) {
		File[] childFile = folderFile.listFiles();

		if (childFile != null) {
			if (sort) {
				Arrays.sort(childFile);
			}

			for (int i = 0; i < childFile.length; i++) {
				File file = childFile[i];
				if (file.isFile()) {
					if (file.getName().toLowerCase().matches(filePattern)) {
						container.add(childFile[i]);
					}
				} else {
					if (recursion) {
						filterFiles(file, filePattern, container, true, sort);
					}
				}
			}
		}
	}

	/**
	 * 返回 full file name 属性.
	 *
	 * @param path the path
	 * @return full file name 属性
	 */
	public final static String getFullFileName(String path) {
		return FilenameUtils.getName(path);
	}

	/**
	 * Trim extend name.
	 *
	 * @param qualifiedName the qualified name
	 * @return the string
	 */
	public final static String trimExtendName(String qualifiedName) {
		int pos = qualifiedName.lastIndexOf(".");
		if (pos > 0) {
			return qualifiedName.substring(0, pos);
		} else {
			return qualifiedName;
		}
	}

	/**
	 * 返回 extend name 属性.
	 *
	 * @param qualifiedName the qualified name
	 * @return extend name 属性
	 */
	public final static String getExtendName(String qualifiedName) {
		int pos = qualifiedName.lastIndexOf(".");
		if (pos > 0) {
			return qualifiedName.substring(pos + 1);
		} else {
			return "";
		}
	}

	/**
	 * Write bytes.
	 *
	 * @param file the file
	 * @param bytes the bytes
	 * @param append the append
	 * @throws Exception the exception
	 */
	public final static void writeBytes(File file, byte[] bytes, boolean append) throws Exception {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file, append);
			fos.write(bytes);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * 返回 file names with out extend name 属性.
	 *
	 * @param directory the directory
	 * @return file names with out extend name 属性
	 */
	public final static List<String> getFileNamesWithOutExtendName(String directory) {
		List<String> files = new ArrayList<String>();
		File dFile = new File(directory);
		if (dFile.isDirectory()) {
			File[] _files = dFile.listFiles();
			for (File f : _files) {
				if (f.isDirectory()) {
					files.addAll(getFileNamesWithOutExtendName(f.getPath()));
				} else {
					files.add(getFileNameWithoutExtension(f.getPath()));
				}
			}
		} else {
			throw new DirectoryNotFoundException("Directory [" + directory + "] not found exception.");
		}
		return files;
	}

	/**
	 * 返回 file name without extension 属性.
	 *
	 * @param pathOrName the path or name
	 * @return file name without extension 属性
	 */
	public static String getFileNameWithoutExtension(String pathOrName) {
		String fileName = pathOrName.substring(pathOrName.lastIndexOf(File.separator) == -1 ? 0 : pathOrName.lastIndexOf(File.separator) + 1);
		return fileName.lastIndexOf(".") > 0 ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
	}

	/**
	 * 返回 file absolute directory 属性.
	 *
	 * @param file the file
	 * @return file absolute directory 属性
	 */
	public static String getFileAbsoluteDirectory(File file) {
		String path = file.getAbsolutePath();
		return path.substring(0, path.lastIndexOf(File.separator));
	}

	/**
	 * 返回 file name 属性.
	 *
	 * @param file the file
	 * @return file name 属性
	 */
	public static String getFileName(File file) {
		String path = file.getAbsolutePath();
		return path.substring(path.lastIndexOf(File.separator) + 1, path.length());
	}

	/**
	 * Nio transfer copy.
	 *
	 * @param source the source
	 * @param target the target
	 */
	public static void nioTransferCopy(File source, File target) throws IOException {
		try (FileInputStream inStream = new FileInputStream(source); FileChannel in = inStream.getChannel(); FileOutputStream outStream = new FileOutputStream(target); FileChannel out = outStream.getChannel();) {
			in.transferTo(0, in.size(), out);
		}
	}

	public static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
		try {
			int readBytes = 0;
			byte[] buffer = new byte[10000];
			while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
				outputStream.write(buffer, 0, readBytes);
			}
			outputStream.flush();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 将指定的文件拷贝到目标地址
	 * @param tempFile
	 * @param targetFilePath
	 * @throws IOException 
	 */
	public static void copyFile(File tempFile, String targetFilePath, int buffSize) throws IOException {
		try (FileOutputStream outputStream = new FileOutputStream(targetFilePath); FileChannel outChannel = outputStream.getChannel(); FileInputStream inputStream = new FileInputStream(tempFile); FileChannel inChannel = inputStream.getChannel();) {
			ByteBuffer bb = ByteBuffer.allocate(buffSize);
			while (inChannel.read(bb) != -1) {
				bb.flip();
				outChannel.write(bb);
				bb.clear();
			}
		} catch (IOException e) {
			throw e;
		}
	}

	public static void copyFile(File tempFile, String targetFilePath) throws IOException {
		copyFile(tempFile, targetFilePath, 8 * 1024);
	}

	public static void moveFile(File tempFile, String targetFilePath) throws IOException {
		copyFile(tempFile, targetFilePath);
		tempFile.delete();
	}

	public static void moveFile(File tempFile, String targetFilePath, int buffSize) throws IOException {
		copyFile(tempFile, targetFilePath, buffSize);
		tempFile.delete();
	}

	/**
	 * Close.
	 *
	 * @param obj the obj
	 */
	private static void close(Closeable obj) {
		if (obj != null) {
			try {
				obj.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static String getMD5String(File file) throws Exception {
		try (FileInputStream fi = new FileInputStream(file)) {
			return getMD5String(fi);
		}
	}

	public static String getMD5String(InputStream in) throws Exception {
		byte[] buffer = new byte[8 * 1024];
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		try (DigestInputStream digestInputStream = new DigestInputStream(in, messageDigest);) {
			while (digestInputStream.read(buffer) > 0)
				;
			messageDigest = digestInputStream.getMessageDigest();
			BigInteger bi = new BigInteger(1, messageDigest.digest());
			return bi.toString(16);
		}
	}

	/**
	 * 获取文件流的MD5值
	 * @param in
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static String getMD5String(byte[] in) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(in);
		BigInteger bi = new BigInteger(1, md5.digest());
		return bi.toString(16);
	}

	/**
	 * 探测targetFile所在的目录，如果targetFile不存在，则返回null
	 *
	 * @param sourceDir 源目录，从此目录开始寻找targetFile
	 * @param targetFileName 目标文件名
	 * @param targetFileExtension 目标文件扩展名
	 * @return 目标文件所在的目录
	 */
	public static File detectDir(File sourceDir, String targetFileName, String targetFileExtension, boolean detectSubDirs) {
		if (sourceDir == null || !sourceDir.exists()) {
			return null;
		}
		Collection<File> files = FileUtils.listFiles(sourceDir, new String[] { targetFileExtension }, detectSubDirs);
		for (File f : files) {
			if (FileUtils.trimExtendName(f.getName()).equals(targetFileName)) {
				return sourceDir;
			}
		}
		return detectDir(sourceDir.getParentFile(), targetFileName, targetFileExtension, detectSubDirs);
	}

	public static File detectDir(File sourceDir, String targetFileName, String targetFileExtension) {
		return detectDir(sourceDir, targetFileName, targetFileExtension, false);
	}
}