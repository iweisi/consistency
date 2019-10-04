package com.pk.consistency.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class JedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.jedis.pool.max-active}")
    private String maxActive;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private String maxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private String mindle;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Integer.valueOf(maxIdle));
        config.setMaxTotal(Integer.valueOf(maxActive));
        JedisPool jedisPool = new JedisPool(config, host, Integer.valueOf(port));
        return jedisPool;
    }
}
