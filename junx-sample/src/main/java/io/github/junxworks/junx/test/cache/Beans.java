package io.github.junxworks.junx.test.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import io.github.junxworks.junx.cache.CacheManager;
import io.github.junxworks.junx.cache.CacheProvider;
import io.github.junxworks.junx.cache.EnvInitializer;
import io.github.junxworks.junx.cache.initializer.ConfigInitializer;

@Configuration
public class Beans {
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public EnvInitializer cacheInitializer() {
		ConfigInitializer configInitializer = new ConfigInitializer();
		configInitializer.setConfigPath("/cache.properties");
		configInitializer.setCachePrefix("sys.cache.");
		return configInitializer;
	}

	@Bean(name = "cacheManager", initMethod = "start", destroyMethod = "stop")
	public CacheManager cacheManager(EnvInitializer envInitializer) {
		CacheManager manager = new CacheManager();
		manager.setName("Cache-Manager");
		manager.setEnvInitializer(envInitializer);
		return manager;
	}

	@Bean(name = "redisProvider")
	public CacheProvider redisProvider(CacheManager manager) {
		return manager.getProvider("myredis");
	}

	@Bean(name = "ehcacheProvider")
	public CacheProvider ehcacheProvider(CacheManager manager) {
		return manager.getProvider("myeh");
	}

	@Bean(name = "aerospikeProvider")
	public CacheProvider aerospikeProvider(CacheManager manager) {
		return manager.getProvider("myas");
	}
}
