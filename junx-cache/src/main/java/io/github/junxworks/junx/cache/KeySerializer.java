package io.github.junxworks.junx.cache;

public interface KeySerializer {
	public byte[] serialize(String key);
}
