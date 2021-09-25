/*
 ***************************************************************************************
 * 
 * @Title:  ResourceManager.java   
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

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 国际化使用的类，针对国际化相关资源，提供统一的资源加载，资源获取。
 *
 * @author: Michael
 * @date:   2017-5-8 17:57:48
 * @since:  v1.0
 */
public class ResourceManager {

	/** 本地缓存大小，存储的是ResourceManager的对象，每个资源一个对象，当map的大size超过这个值的时候，会从当前map里面剔除最老的ResourceManager对象 */
	private static int LOCALE_CACHE_SIZE = 32;

	/** 资源对象. */
	private final ResourceBundle bundle;

	/** 地区. */
	private final Locale locale;

	/**
	 * 构造一个新的 string manager 对象.
	 *
	 * @param packageName 资源存在的包名
	 * @param locale 地区
	 */
	private ResourceManager(String packageName, String fileName, Locale locale) {
		String bundleName = packageName + "." + fileName;
		ResourceBundle bnd = null;
		try {
			bnd = ResourceBundle.getBundle(bundleName, locale);
		} catch (MissingResourceException ex) {
			// Try from the current loader (that's the case for trusted apps)
			// Should only be required if using a TC5 style classloader structure
			// where common != shared != server
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			if (cl != null) {
				try {
					bnd = ResourceBundle.getBundle(bundleName, locale, cl);
				} catch (MissingResourceException ex2) {
					// Ignore
				}
			}
		}
		bundle = bnd;
		// Get the actual locale, which may be different from the requested one
		if (bundle != null) {
			Locale bundleLocale = bundle.getLocale();
			if (bundleLocale.equals(Locale.ROOT)) {
				this.locale = Locale.ENGLISH;
			} else {
				this.locale = bundleLocale;
			}
		} else {
			this.locale = null;
		}
	}

	/**
	 * 返回 string 属性.
	 *
	 * @param key the key
	 * @return key对应的value
	 */
	public String getString(String key) {
		if (key == null) {
			String msg = "key may not have a null value";

			throw new IllegalArgumentException(msg);
		}

		String str = null;

		try {
			// Avoid NPE if bundle is null and treat it like an MRE
			if (bundle != null) {
				str = bundle.getString(key);
			}
		} catch (MissingResourceException mre) {
			//bad: shouldn't mask an exception the following way:
			//   str = "[cannot find message associated with key '" + key + "' due to " + mre + "]";
			//     because it hides the fact that the String was missing from the calling code.
			//good: could just throw the exception (or wrap it in another)
			//      but that would probably cause much havoc on existing
			//      code.
			//better: consistent with container pattern to
			//      simply return null.  Calling code can then do a null check.
			str = null;
		}

		return str;
	}

	/**
	 * 返回 string 属性，这个string是经过{@link java.text.MessageFormat}格式化的，所以
	 * key对应的string可以是支持MessageFormat模板，类似Stopping thread {0} to avoid potential。
	 *
	 * @param key key
	 * @param args 支持MessageFormat的参数
	 * @return MessageFormat格式化过后的数据
	 */
	public String getString(final String key, final Object... args) {
		String value = getString(key);
		if (value == null) {
			value = key;
		}

		MessageFormat mf = new MessageFormat(value);
		mf.setLocale(locale);
		return mf.format(args, new StringBuffer(), null).toString();
	}

	/**
	 * 返回 locale 属性.
	 *
	 * @return locale 属性
	 */
	public Locale getLocale() {
		return locale;
	}

	// --------------------------------------------------------------
	// STATIC SUPPORT METHODS
	// --------------------------------------------------------------

	/** 常量 managers，用于存储. */
	private static final Map<String, Map<Locale, ResourceManager>> managers = new HashMap<String, Map<Locale, ResourceManager>>();

	/**
	 * 返回 manager 属性.
	 *
	 * @param packageName the package name
	 * @return manager 属性
	 */
	public static final ResourceManager getManager(String packageName, String fileName) {
		return getManager(packageName, fileName, Locale.getDefault());
	}

	/**
	 * 根据指定的包名packageName下的fileName文件，获得对应的ResourceManager对象。
	 *
	 * @param packageName 包名
	 * @param fileName 文件名
	 * @param locale地区
	 * @return ResourceManager对象
	 */
	public static final synchronized ResourceManager getManager(String packageName, String fileName, Locale locale) {
		String name = packageName + "." + fileName;
		Map<Locale, ResourceManager> map = managers.get(name);
		if (map == null) {
			/*
			 * Don't want the HashMap to be expanded beyond LOCALE_CACHE_SIZE.
			 * Expansion occurs when size() exceeds capacity. Therefore keep
			 * size at or below capacity.
			 * removeEldestEntry() executes after insertion therefore the test
			 * for removal needs to use one less than the maximum desired size
			 *
			 */
			map = new LinkedHashMap<Locale, ResourceManager>(LOCALE_CACHE_SIZE, 1, true) {
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean removeEldestEntry(Map.Entry<Locale, ResourceManager> eldest) {
					if (size() > (LOCALE_CACHE_SIZE - 1)) {
						return true;
					}
					return false;
				}
			};
			managers.put(name, map);
		}

		ResourceManager mgr = map.get(locale);
		if (mgr == null) {
			mgr = new ResourceManager(packageName, fileName, locale);
			map.put(locale, mgr);
		}
		return mgr;
	}

	/**
	 * 根据指定的包名packageName下的fileName文件，获得对应的ResourceManager对象。
	 *
	 * @param packageName 包名
	 * @param fileName 文件名
	 * @param requestedLocales 地区的枚举对象
	 * @return manager 属性
	 */
	public static ResourceManager getManager(String packageName, String fileName, Enumeration<Locale> requestedLocales) {
		while (requestedLocales.hasMoreElements()) {
			Locale locale = requestedLocales.nextElement();
			ResourceManager result = getManager(packageName, fileName, locale);
			if (result.getLocale().equals(locale)) {
				return result;
			}
		}
		return getManager(packageName, fileName);
	}
}
