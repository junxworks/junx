/*
 ***************************************************************************************
 * 
 * @Title:  EncryptUtils.java   
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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 提供了通用的加密解密工具类，包含了用于数字加密的{@link #simpleEncrypt(String)}{@link #simpleDecrypt(String)}，
 * base64加密{@link #base64Encrypt(String)}{@link #base64Decrypt(String)}，DES加密{@link #desEncrypt(String)}，{@link #desDecrypt(String)}，
 * 以及自主实现的非重复加密{@link #roachEncrypt(String)}{@link #roachDecrypt(String)}，
 * 通过添加salt，使同一个字符串每次加密结果不一样，但是加密后长度会增加3倍左右，请谨慎使用。
 *
 * @author: Michael
 * @date: 2017-5-16 11:24:27
 * @since: v1.0
 */
public class EncryptUtils {

	/** 常量 KEY. */
	private static final String KEY = "gb6yhn7ujm8ik";// 默认KEY

	private static final String CHARSET = "UTF-8";

	private static final String RSA = "RSA";

	private static final String SIGN_ALGORITHM = "SHA1withRSA";

	private static final int KEY_LENGTH = 1024;

	/** RSA最大加密明文大小 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/** RSA最大解密密文大小. */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 移位异或，不改变字符串长度，只用于数字类型的字符串
	 * @param str
	 * @return
	 */
	public static String simpleEncrypt(String str) {
		if (str == null) {
			return null;
		}
		try {
			return new String(xor(str.getBytes(CHARSET), KEY.getBytes(CHARSET)));
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	/**
	 * 移位异或，不改变字符串长度,只用于数字类型的字符串
	 * @param str
	 * @return
	 */
	public static String simpleDecrypt(String str) {
		if (str == null) {
			return null;
		}
		try {
			return new String(xor(str.getBytes(CHARSET), KEY.getBytes(CHARSET)));
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	/**
	 * 返回默认KEY的加密字符串
	 * @param str 需要加密的字符串
	 * @return 返回默认KEY的加密字符串
	 */
	public static String base64Encrypt(String str) {
		return base64Encrypt(str, KEY);
	}

	/**
	 * 返回默认KEY的解密字符串
	 * @param str 需要解密的字符串
	 * @return 返回默认KEY的解密字符串
	 */
	public static String base64Decrypt(String str) {
		return base64Decrypt(str, KEY);
	}

	/**
	 * Base 64 encrypt.
	 *
	 * @param str the str
	 * @param key the key
	 * @return the string
	 */
	// 加密
	public static String base64Encrypt(String str, String key) {
		if (str == null) {
			return null;
		}
		try {
			return Base64.encodeBase64String(xor(str.getBytes(CHARSET), key.getBytes(CHARSET)));
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	/**
	 * Base 64 decrypt.
	 *
	 * @param str the str
	 * @param key the key
	 * @return the string
	 */
	// 解密
	public static String base64Decrypt(String str, String key) {
		if (str == null) {
			return null;
		}
		try {
			byte[] bStr = Base64.decodeBase64(str);
			return new String(xor(bStr, key.getBytes(CHARSET)), CHARSET);
		} catch (Exception ex) {
			throw new RuntimeException("解密失败", ex);
		}
	}

	/**
	 * Xor.
	 *
	 * @param d1 the d 1
	 * @param d2 the d 2
	 * @return the byte[]
	 */
	private static byte[] xor(byte[] d1, byte[] d2) {
		int i = 0;
		for (int j = 0; i < d1.length; j++) {
			if (j == d2.length)
				j = 0;
			d1[i] = (byte) (d1[i] ^ d2[j]);

			i++;
		}
		return d1;
	}

	/**
	 * DES加密
	 * @param str
	 * @return
	 */
	public static String desEncrypt(String str) {
		return desEncrypt(str, KEY);
	}

	/**
	 * DES解密
	 * @param str
	 * @param key
	 * @return
	 */
	public static String desDecrypt(String str) {
		return desDecrypt(str, KEY);
	}

	/**
	 * Des encrypt.
	 *
	 * @param str the str
	 * @param key the key
	 * @return the string
	 */
	public static String desEncrypt(String str, String key) {
		if (str == null) {
			return null;
		}
		try {
			return Desc.encryptString(str, key);
		} catch (Exception ex) {
			throw new RuntimeException("DES加密失败", ex);
		}
	}

	/**
	 * Des decrypt.
	 *
	 * @param str the str
	 * @param key the key
	 * @return the string
	 */
	public static String desDecrypt(String str, String key) {
		if (str == null) {
			return null;
		}
		try {
			return Desc.decryptString(str, key);
		} catch (Exception ex) {
			throw new RuntimeException("DES解密失败", ex);
		}
	}

	/**
	 * {类的详细说明}.
	 *
	 * @author: Michael
	 * @date: 2017-5-16 11:24:27
	 * @since: v1.0
	 */
	private static class Desc {

		/**
		 * 返回 key 属性.
		 *
		 * @param strKey the str key
		 * @return key 属性
		 * @throws Exception the exception
		 */
		private static Key getKey(String strKey) throws Exception {
			KeyGenerator _generator = KeyGenerator.getInstance("DES");
			_generator.init(new SecureRandom(strKey.getBytes(CHARSET)));

			return _generator.generateKey();
		}

		/**
		 * Encrypt.
		 *
		 * @param data the data
		 * @param key the key
		 * @return the byte[]
		 * @throws Exception the exception
		 */
		public static byte[] encrypt(byte[] data, String key) throws Exception {
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(1, getKey(key));

			return cipher.doFinal(data);
		}

		/**
		 * Decrypt.
		 *
		 * @param data the data
		 * @param key the key
		 * @return the byte[]
		 * @throws Exception the exception
		 */
		public static byte[] decrypt(byte[] data, String key) throws Exception {
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(2, getKey(key));

			return cipher.doFinal(data);
		}

		/**
		 * Encrypt string.
		 *
		 * @param str the str
		 * @param key the key
		 * @return the string
		 * @throws Exception the exception
		 */
		public static String encryptString(String str, String key) throws Exception {
			byte[] ret = encrypt(str.getBytes(CHARSET), key);
			return Base64.encodeBase64String(ret);
		}

		/**
		 * Decrypt string.
		 *
		 * @param str the str
		 * @param key the key
		 * @return the string
		 * @throws Exception the exception
		 */
		public static String decryptString(String str, String key) throws Exception {
			byte[] ret = decrypt(Base64.decodeBase64(str), key);
			return new String(ret, CHARSET);
		}
	}

	/** 常量 ROACH_KEY. */
	private static final String ROACH_KEY = "1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik,9ol.0p;/!@#$%^&*()";

	/**
	 * 通过增加数据冗余(3-4倍)来增加数据加密安全性，即使同一个字符串，每次加密也是不一样的 适合数据量较小的加密，例如密码，手机号等
	 * @param str 需要加密的字符串
	 * @return 加密后的字符串
	 */
	public static final String roachEncrypt(String str) {
		return roachEncrypt(str, ROACH_KEY);
	}

	/**
	 * Concat str.
	 *
	 * @param a the a
	 * @return the string
	 */
	private static String concatStr(char... a) {
		return new String(a);
	}

	/**
	 * @Description: Roach解密 @param str @return 参数 String 返回类型 @throws
	 */
	public static final String roachDecrypt(String str) {
		return roachDecrypt(str, ROACH_KEY);
	}

	/**
	 * 返回 tokens 属性.
	 *
	 * @param str the str
	 * @return tokens 属性
	 */
	private static String[] getTokens(String str) {
		int x = 0, b = 2;
		int len = str.length() / b;
		String[] res = new String[len];
		for (int i = 0; i < len; x = (i + 1) * b, i++) {
			res[i] = str.substring(x, (i + 1) * b);
		}
		return res;
	}

	private static char getSalt() {
		byte[] b = ROACH_KEY.getBytes();
		int len = b.length;
		int x = (int) (Math.random() * len);
		return (char) b[x];
	}

	/**
	 * 通过增加数据冗余(3-4倍)来增加数据加密安全性，即使同一个字符串，每次加密也是不一样的 适合数据量较小的加密，例如密码，手机号等
	 * @param str 需要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String roachEncrypt(String str, String key) {
		int strlength = str.length();
		boolean even = str.length() % 2 == 0;// 加密字符串长度是否是偶数
		if (even) {
			strlength += 1;// 如果是偶数需要补位
		}
		String[] finalString = new String[strlength];
		char[] targetChars = new char[strlength];
		char[] originalChars = str.toCharArray();
		if (even) {
			for (int i = 0, len = originalChars.length; i < len; i++) {
				targetChars[i] = originalChars[i];
			}
			targetChars[strlength - 1] = 'a';// 偶数长度的字符串加密补位
		} else {
			targetChars = originalChars;
		}
		int targetCharsLength = targetChars.length;
		for (int i = 0; i < targetChars.length; i++) {
			if (i % 2 == 0) {// 偶数位
				finalString[i] = concatStr(getSalt(), targetChars[targetCharsLength - i - 1]);
			} else {// 奇数位
				finalString[i] = concatStr(targetChars[i], getSalt());
			}
		}
		StringBuilder fs = new StringBuilder();
		for (int i = 0; i < finalString.length; i++) {
			if (even && i == 0) {
				continue;
			}
			fs.append(finalString[i]);
		}
		fs = fs.append(concatStr(getSalt(), getSalt()));
		return base64Encrypt(fs.toString(), key);
	}

	/**
	 * @Description: Roach解密 @param str @return 参数 String 返回类型 @throws
	 */
	public static String roachDecrypt(String str, String key) {
		str = base64Decrypt(str, key);
		String[] finalString;
		String[] tokens = getTokens(str);
		int finallength = tokens.length;
		int strlength = tokens.length;
		if (strlength % 2 != 0) {
			strlength += 1;
		}
		int count = strlength - 1;
		String[] model = new String[count];
		finalString = new String[count];
		boolean odd = finallength % 2 != 0;
		for (int i = 0; i < count; i++) {
			if (odd) {
				if (i == 0) {
					model[i] = "wx";// 仅用来辅助解密
				} else {
					model[i] = tokens[i - 1];
				}
			} else {
				model[i] = tokens[i];
			}
		}
		for (int i = 0; i < count; i++) {
			if (i % 2 == 0) {
				finalString[count - i - 1] = String.valueOf(model[i].charAt(model[i].length() - 1));
			} else {
				finalString[i] = String.valueOf(model[i].charAt(0));
			}
		}
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < finalString.length; i++) {
			if (odd && i == finallength - 1)
				break;
			res.append(finalString[i]);
		}
		return res.toString();
	}
	
	/**
	 * 生成RSA密钥对
	 *
	 * @return the RSA key pair
	 * @throws Exception the exception
	 */
	public static KeyPair generateRSAKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
		generator.initialize(KEY_LENGTH);
		return generator.generateKeyPair();
	}

	/**
	* 获取私钥
	* 
	* @param privateKey 私钥字符串
	* @return
	*/
	public static PrivateKey getRSAPrivateKey(String privateKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
		return keyFactory.generatePrivate(keySpec);
	}

	public static String privateKeyToString(PrivateKey privateKey) {
		return new String(Base64.encodeBase64(privateKey.getEncoded()));
	}

	/**
	 * 获取公钥
	 * 
	 * @param publicKey 公钥字符串
	 * @return
	 */
	public static PublicKey getRSAPublicKey(String publicKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
		return keyFactory.generatePublic(keySpec);
	}

	public static String publicKeyToString(PublicKey publicKey) {
		return new String(Base64.encodeBase64(publicKey.getEncoded()));
	}

	/**
	* RSA加密
	* 
	* @param data 待加密数据
	* @param publicKey 公钥
	* @return
	*/
	public static String encryptRSA(String data, String publicKey) throws Exception {
		return encryptRSA(data, getRSAPublicKey(publicKey));
	}

	/**
	* RSA加密
	* 
	* @param data 待加密数据
	* @param publicKey 公钥
	* @return
	*/
	public static String encryptRSA(String data, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance(RSA);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		int inputLen = data.getBytes().length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offset = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offset > 0) {
			if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
			}
			out.write(cache, 0, cache.length);
			i++;
			offset = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		// 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
		// 加密后的字符串
		return new String(Base64.encodeBase64String(encryptedData));
	}

	/**
	 * RSA解密
	 * 
	 * @param data 待解密数据
	 * @param privateKey 私钥
	 * @return
	 */
	public static String decryptRSA(String data, String privateKey) throws Exception {
		return decryptRSA(data, getRSAPrivateKey(privateKey));
	}

	/**
	 * RSA解密
	 * 
	 * @param data 待解密数据
	 * @param privateKey 私钥
	 * @return
	 */
	public static String decryptRSA(String data, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance(RSA);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] dataBytes = Base64.decodeBase64(data);
		int inputLen = dataBytes.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offset = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offset > 0) {
			if (inputLen - offset > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
			}
			out.write(cache, 0, cache.length);
			i++;
			offset = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		// 解密后的内容 
		return new String(decryptedData, "UTF-8");
	}

	/**
	 * 签名
	 * 
	 * @param data 待签名数据
	 * @param privateKey 私钥
	 * @return 签名
	 */
	public static String signRSA(String data, String algorithm, PrivateKey privateKey) throws Exception {
		byte[] keyBytes = privateKey.getEncoded();
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PrivateKey key = keyFactory.generatePrivate(keySpec);
		Signature signature = Signature.getInstance(algorithm);
		signature.initSign(key);
		signature.update(data.getBytes());
		return new String(Base64.encodeBase64(signature.sign()));
	}

	public static String signRSA(String data, PrivateKey privateKey) throws Exception {
		return signRSA(data, SIGN_ALGORITHM, privateKey);
	}

	public static String signRSA(String data, String privateKey) throws Exception {
		return signRSA(data, SIGN_ALGORITHM, getRSAPrivateKey(privateKey));
	}

	/**
	 * 验签
	 * 
	 * @param srcData 原始字符串
	 * @param publicKey 公钥
	 * @param sign 签名
	 * @return 是否验签通过
	 */
	/**
	* 验签
	* 
	* @param srcData 原始字符串
	* @param publicKey 公钥
	* @param sign 签名
	* @return 是否验签通过
	*/
	public static boolean verifyRSASign(String srcData, String algorithm, PublicKey publicKey, String sign) throws Exception {
		byte[] keyBytes = publicKey.getEncoded();
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PublicKey key = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(algorithm);
		signature.initVerify(key);
		signature.update(srcData.getBytes());
		return signature.verify(Base64.decodeBase64(sign.getBytes()));
	}

	public static boolean verifyRSASign(String srcData, PublicKey publicKey, String sign) throws Exception {
		return verifyRSASign(srcData, SIGN_ALGORITHM, publicKey, sign);
	}

	public static boolean verifyRSASign(String srcData, String publicKey, String sign) throws Exception {
		return verifyRSASign(srcData, SIGN_ALGORITHM, getRSAPublicKey(publicKey), sign);
	}

	/**
	 * AES 加密.
	 *
	 * @param content the content
	 * @param password the password
	 * @return the string
	 */
	public static String AESEncrypt(int length, String content, String password) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(length, new SecureRandom(password.getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");//默认ECB模式
		byte[] byteContent = content.getBytes("UTF-8");
		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(byteContent);
		return new Base64().encodeToString(result); // Base64转码
	}

	/**
	 * AES 解密.
	 *
	 * @param content the content
	 * @param password the password
	 * @return the string
	 */
	public static String AESDecrypt(int length, String content, String password) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(length, new SecureRandom(password.getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(Base64.decodeBase64(content));
		return new String(result); // 加密
	}
	
}
