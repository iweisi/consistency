package com.pk.consistency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 解决缓存与数据库双写及缓存穿透两种问题
 * @Author: pengke
 * @Date: 2019年09月22日
 **/
@SpringBootApplication
@EnableCaching
public class Application {

	private static ApplicationContext ac = null;

	public static void main(String[] args) {
		ac = SpringApplication.run(Application.class, args);
	}

	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
		StringRedisSerializer redisSerializer = new StringRedisSerializer();
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(factory);
		redisTemplate.setDefaultSerializer(redisSerializer);
		return redisTemplate;
	}

	public static <T> T getBean(Class<T> clazz) {
		return ac.getBean(clazz);
	}

}
