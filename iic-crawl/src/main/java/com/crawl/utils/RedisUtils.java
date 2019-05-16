package com.crawl.utils;

import redis.clients.jedis.Jedis;

public class RedisUtils {
	static Jedis jedis =null;
	
	static {
		 //连接本地的 Redis 服务
        jedis = new Jedis("localhost");
	}
	public static Jedis getJedis() {
        try {
            jedis = new Jedis("localhost");
            return jedis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	public static String read(String key) {
		try {
			return  jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void write(String key,String value) {
		try {
			jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void write(String key,String value,int expTime) {
		try {
			jedis.set(key, value);
			jedis.expire(key, expTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}