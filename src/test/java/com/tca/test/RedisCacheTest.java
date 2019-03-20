package com.tca.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tca.cache.RedisCache;

public class RedisCacheTest extends BaseTest{

	@Autowired
	private RedisCache redis;
	
	@Test
	public void test01() {
		String value = redis.get("test_key");
		System.out.println("value = " + value);
	}
}
