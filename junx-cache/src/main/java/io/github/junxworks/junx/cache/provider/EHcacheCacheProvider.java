/*
 ***************************************************************************************
 * 
 * @Title:  EHcacheCacheProvider.java   
 * @Package io.github.junxworks.junx.cache.provider   
 * @Description: (用一句话描述该文件做什么)   
 * @author: Michael
 * @date:   2018-7-11 15:38:51   
 * @version V1.0 
 * @Copyright: 2018 JunxWorks. All rights reserved. 
 * 
 *  ---------------------------------------------------------------------------------- 
 * 文件修改记录
 *     文件版本：         修改人：             修改原因：
 ***************************************************************************************
 */
package io.github.junxworks.junx.cache.provider;

import java.util.concurrent.TimeUnit;

import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import io.github.junxworks.junx.cache.Cache;
import io.github.junxworks.junx.cache.adapter.EHCacheCacheAdapter;
import io.github.junxworks.junx.core.exception.NullParameterException;
import io.github.junxworks.junx.core.util.StringUtils;

/**
 * EHCache的CacheProvider，能够生产EHCache适配器对象。
 * 本地定义了很多EHCache相关的配置参数，目前只定义了核心的一部分配置参数，配置参数不全，后期可根据实际情况添加。
 * 具体参数可以参考属性定义。
 *
 * @ClassName:  EHcacheCacheProvider
 * @author: Michael
 * @date:   2017-7-25 16:29:17
 * @since:  v1.0
 */
public class EHcacheCacheProvider extends AbstractCacheProvider {
	/** key的java类型，默认Object */
	private static final String CONFIG_KEY_TYPE = "keyType";

	/** value的java类型，默认Object */
	private static final String CONFIG_VALUE_TYPE = "valueType";

	/** 用来配置磁盘缓存使用的物理目录，配置的是服务器的临时文件存储路径 */
	private static final String CONFIG_DISK_DIR = "diskDir";

	/** JVM堆中可以存放的缓存大小，单位MB. */
	private static final String CONFIG_MAX_MB_IN_HEAP = "maxMBInHeap";

	/** 堆外内存中可以存放的缓存大小，要比maxMBInHeap配置大，单位MB. */
	private static final String CONFIG_MAX_MB_OFFHEAP = "maxMBOffHeap";

	/** 磁盘缓存中最多可以存放的缓存大小，单位GB . */
	private static final String CONFIG_MAX_MB_ON_DISK = "maxGBOnDisk";

	/** 缓存中对象是否永久有效,即是否永驻内存,true时将忽略timeToIdleSeconds和timeToLiveSeconds. */
	private static final String CONFIG_ETERNAL = "eternal";

	/** 缓存数据在失效前的允许闲置时间(单位:秒),仅当eternal=false时使用,默认值是0表示可闲置时间无穷大,此为可选属性，
	 * 即访问这个cache中元素的最大间隔时间,若超过这个时间没有访问此Cache中的某个元素,那么此元素将被从Cache中清除. */
	private static final String CONFIG_TIME_TO_IDLE = "timeToIdleSeconds";

	/** 缓存数据在失效前的允许存活时间(单位:秒),仅当eternal=false时使用,默认值是0表示可存活时间无穷大，
	 * 即从创建开始计时,当超过这个时间时,此元素将从Cache中清除 */
	private static final String CONFIG_TIME_TO_LIVE = "timeToLiveSeconds";

	/** 是否持久化磁盘缓存,当这个属性的值为true时,系统在初始化时会在磁盘中查找文件名为cache名称,后缀名为index的文件，
	 * 这个文件中存放了已经持久化在磁盘中的cache的index,找到后会把cache加载到内存. */
	private static final String CONFIG_DISK_PERSISTENT = "diskPersistent";

	/** 
	 * 内存存储与释放策略,即达到maxElementsInMemory限制时,Ehcache会根据指定策略清理内存:
	 * 共有三种策略,分别为LRU(最近最少使用)、LFU(使用次数最少)、FIFO(先进先出)
	 * ehcache 中缓存的3 种清空策略：
	 *	FIFO ，first in first out ，这个是大家最熟的，先进先出，不多讲了
	 *	LFU ， Less Frequently Used ，就是上面例子中使用的策略，直白一点就是讲一直以来最少被使用的。如上面所讲，缓存的元素有一个hit 属性，hit 值最小的将会被清出缓存。
	 *	LRU ，Least Recently Used ，最近最少使用的，缓存的元素有一个时间戳，当缓存容量满了，而又需要腾出地方来缓存新的元素的时候，那么现有缓存元素中时间戳离当前时间最远的元素将被清出缓存。
	 */
	private static final String CONFIG_MEMORY_EVICTION_POLICY = "memoryStoreEvictionPolicy";

	private CacheManager cacheManager;

	private String cacheName;

	private Class<?> keyType; //目前都用String

	private Class<?> valueType;//目前都用byte[]

	@Override
	public Cache getCache() throws Exception {
		org.ehcache.Cache<String, byte[]> ehcache = cacheManager.getCache(cacheName, String.class, byte[].class);
		EHCacheCacheAdapter ehcacheAdapter = new EHCacheCacheAdapter();
		ehcacheAdapter.setEhcache(ehcache);
		return ehcacheAdapter;
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStart()
	 */
	@Override
	protected void doStart() throws Throwable {
		cacheName = cacheEnv.getCacheId();
		keyType = Class.forName(cacheEnv.getString(CONFIG_KEY_TYPE, String.class.getName()));
		valueType = Class.forName(cacheEnv.getString(CONFIG_VALUE_TYPE, byte[].class.getName()));
		String diskDir = cacheEnv.getString(CONFIG_DISK_DIR, null);
		if (StringUtils.isNull(diskDir)) {
			throw new NullParameterException("Configuration \"diskDir\" can not be null for EHCache id=\"%s\"", cacheName);
		}
		CacheManagerBuilder<PersistentCacheManager> cacheBuilder = CacheManagerBuilder.newCacheManagerBuilder().with(CacheManagerBuilder.persistence(diskDir));
		//资源配置
		ResourcePoolsBuilder resourceBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder();
		Integer maxMBInHeap = cacheEnv.getInteger(CONFIG_MAX_MB_IN_HEAP, null);
		if (maxMBInHeap != null) {
			resourceBuilder = resourceBuilder.heap(maxMBInHeap, MemoryUnit.MB);
		}
		Integer maxMBOffHeap = cacheEnv.getInteger(CONFIG_MAX_MB_OFFHEAP, null);
		if (maxMBOffHeap != null) {
			resourceBuilder = resourceBuilder.offheap(maxMBOffHeap, MemoryUnit.MB);
		}
		Integer maxGBOnDisk = cacheEnv.getInteger(CONFIG_MAX_MB_ON_DISK, null);
		boolean persistence = cacheEnv.getBoolean(CONFIG_DISK_PERSISTENT, true);
		if (maxGBOnDisk != null) {
			resourceBuilder = resourceBuilder.disk(maxGBOnDisk, MemoryUnit.GB, persistence);
		}
		//Cache配置
		CacheConfigurationBuilder configBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder(keyType, valueType, resourceBuilder);
		boolean eternal = cacheEnv.getBoolean(CONFIG_ETERNAL, true);//对象不过期
		if (eternal) {
			configBuilder = configBuilder.withExpiry(Expirations.noExpiration());//没有对象过期时间
		} else {
			Integer timeToIdleSeconds = cacheEnv.getInteger(CONFIG_TIME_TO_IDLE, null);
			if (timeToIdleSeconds != null) {
				configBuilder = configBuilder.withExpiry(Expirations.timeToIdleExpiration(Duration.of(timeToIdleSeconds, TimeUnit.SECONDS)));
			}
			Integer timeToLiveSeconds = cacheEnv.getInteger(CONFIG_TIME_TO_LIVE, null);
			if (timeToIdleSeconds != null) {
				configBuilder = configBuilder.withExpiry(Expirations.timeToLiveExpiration(Duration.of(timeToLiveSeconds, TimeUnit.SECONDS)));
			}
		}
		//		configBuilder = configBuilder.withValueSerializer(new ObjectValueSerializer());
		//		configBuilder=configBuilder.withEvictionAdvisor(EvictionAdvisor.)//过期策略，默认小范围LRU
		cacheManager = cacheBuilder.withCache(cacheName, configBuilder).build(true);
	}

	/**
	 * @see io.github.junxworks.junx.core.lifecycle.Service#doStop()
	 */
	@Override
	protected void doStop() throws Throwable {
		cacheManager.close();
	}

	//	private static class ObjectValueSerializer implements Serializer<Object>{
	//		private Kryo kryo = new Kryo();
	//		@Override
	//		public ByteBuffer serialize(Object object) throws SerializerException {
	//			Output out = new Output();
	//			kryo.writeObject(out, object);
	//			return null;
	//		}
	//
	//		@Override
	//		public Object read(ByteBuffer binary) throws ClassNotFoundException, SerializerException {
	//			// TODO Auto-generated method stub
	//			return null;
	//		}
	//
	//		@Override
	//		public boolean equals(Object object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
	//			// TODO Auto-generated method stub
	//			return false;
	//		}
	//		
	//	}

}
