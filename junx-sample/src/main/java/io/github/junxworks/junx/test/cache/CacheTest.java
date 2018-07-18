package io.github.junxworks.junx.test.cache;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import io.github.junxworks.junx.cache.Cache;
import io.github.junxworks.junx.cache.CacheProvider;
import io.github.junxworks.junx.cache.KV;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CacheApplication.class)
public class CacheTest {
	@Autowired
	@Qualifier("redisProvider")
	private CacheProvider cacheProvider;

//	@Autowired
//	@Qualifier("ehcacheProvider")
//	private CacheProvider cacheProvider;

//	@Autowired
//	@Qualifier("aerospikeProvider")
//	private CacheProvider cacheProvider;

	private static final String group = "test";

	private static final String group2 = "test2";

	@Test
	public void cacheTest() throws IOException, Exception {
		KV kv = new KV(group, "1");
		try (Cache cache = cacheProvider.getCache();) {
			cache.get(kv);
			System.out.println(kv.getString());
			kv.setValue("aaaaa".getBytes());
			cache.set(kv);
			kv = new KV(group, "1");
			cache.get(kv);
			System.out.println(kv.getString());
			cache.delete(kv);
			kv = new KV(group, "1");
			cache.get(kv);
			System.out.println(kv.getString());
		}
	}

	@Test
	public void cacheTestWithoutGroup() throws IOException, Exception {
		KV kv = new KV("1");
		try (Cache cache = cacheProvider.getCache();) {
			cache.get(kv);
			System.out.println(kv.getString());
			kv.setValue("aaaaa".getBytes());
			cache.set(kv);
			kv = new KV("1");
			cache.get(kv);
			System.out.println(kv.getString());
			cache.delete(kv);
			kv = new KV("1");
			cache.get(kv);
			System.out.println(kv.getString());
		}
	}

	@Test
	public void batchTest() throws IOException, Exception {
		List<KV> kvs = creatKVs(group, 5);
		try (Cache cache = cacheProvider.getCache();) {
			cache.get(kvs);
			printKVs(kvs);
			initKVs(kvs);
			cache.set(kvs);
			kvs = creatKVs(group, 5);
			cache.get(kvs);
			printKVs(kvs);
			cache.delete(kvs);
			kvs = creatKVs(group, 5);
			cache.get(kvs);
			printKVs(kvs);
		}
	}

	@Test
	public void batchTestWithoutGroup() throws IOException, Exception {
		List<KV> kvs = creatKVs(null, 5);
		try (Cache cache = cacheProvider.getCache();) {
			cache.get(kvs);
			printKVs(kvs);
			initKVs(kvs);
			cache.set(kvs);
			kvs = creatKVs(null, 5);
			cache.get(kvs);
			printKVs(kvs);
			cache.delete(kvs);
			kvs = creatKVs(null, 5);
			cache.get(kvs);
			printKVs(kvs);
		}
	}

	@Test
	public void groupTest() throws IOException, Exception {
		List<KV> kvs = creatKVs(group, 5);
		List<KV> kvs2 = creatKVs(group2, 5);
		try (Cache cache = cacheProvider.getCache();) {
			initKVs(kvs);
			initKVs(kvs2);
			cache.set(kvs);
			cache.set(kvs2);
			printKVs(cache.getGroupValues(group));
			cache.delete(kvs);
			cache.delete(kvs2);
		}
	}

	@Test
	public void groupTestWithoutGroupName() throws IOException, Exception {
		List<KV> kvs = creatKVs(null, 5);
		try (Cache cache = cacheProvider.getCache();) {
			initKVs(kvs);
			cache.set(kvs);
			printKVs(cache.getGroupValues(null));
			cache.delete(kvs);
		}
	}

	private List<KV> creatKVs(String group, int count) {
		List<KV> kvs = Lists.newArrayList();
		for (int i = 0; i < count; i++) {
			kvs.add(new KV(group, String.valueOf(i)));
		}
		return kvs;
	}

	private void initKVs(List<KV> kvs) {
		for (KV kv : kvs)
			kv.setValue(UUID.randomUUID().toString().getBytes());
	}

	private void printKVs(List<KV> kvs) {
		kvs.stream().forEach(kv -> {
			System.out.println("key:" + kv.getKey() + " value:" + kv.getString());
		});
	}
}
