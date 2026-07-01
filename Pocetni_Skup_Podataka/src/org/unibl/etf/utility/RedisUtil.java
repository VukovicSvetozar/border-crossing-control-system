package org.unibl.etf.utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import redis.clients.jedis.*;

public class RedisUtil {

	private static RedisUtil instance;

	private final String REDIS_HOST;
	private final Integer REDIS_PORT;

	public static RedisUtil getInstance() {
		if (instance == null) {
			instance = new RedisUtil();
		}
		return instance;
	}

	private RedisUtil() {
		REDIS_HOST = PropertiesUtil.vratiSvojstvo("REDIS_HOST", String.class);
		REDIS_PORT = PropertiesUtil.vratiSvojstvo("REDIS_PORT", Integer.class);
	}

	public <T> void storeObject(String key, T object) {
		JedisPool pool = new JedisPool(REDIS_HOST, REDIS_PORT);
		try (Jedis jedis = pool.getResource()) {
			Gson gson = new Gson();
			String json = gson.toJson(object);
			jedis.set(key, json);
		}
		pool.close();
	}

	public <T> T restoreObject(String key, Class<T> type) {
		JedisPool pool = new JedisPool(REDIS_HOST, REDIS_PORT);
		T object = null;
		try (Jedis jedis = pool.getResource()) {
			Gson gson = new Gson();
			String json = jedis.get(key);
			object = gson.fromJson(json, type);
		}
		pool.close();
		return object;
	}

	public List<String> restoreKeys(String keys) {
		List<String> keysList = new ArrayList<>();
		JedisPool pool = new JedisPool(REDIS_HOST, REDIS_PORT);
		try (Jedis jedis = pool.getResource()) {
			Set<String> redisKeys = jedis.keys(keys + "*");
			Iterator<String> it = redisKeys.iterator();
			while (it.hasNext()) {
				String data = it.next();
				keysList.add(data);
			}
		}
		pool.close();
		return keysList;
	}

	public <T> void deleteObject(String key, T object) {
		JedisPool pool = new JedisPool(REDIS_HOST, REDIS_PORT);
		try (Jedis jedis = pool.getResource()) {
			jedis.del(key);
		}
		pool.close();
	}

	public void clearRedis() {
		JedisPool pool = new JedisPool(REDIS_HOST, REDIS_PORT);
		try (Jedis jedis = pool.getResource()) {
			jedis.flushAll();
		}
		pool.close();
	}

}
