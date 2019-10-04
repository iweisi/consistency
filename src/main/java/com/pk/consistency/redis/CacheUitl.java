package com.pk.consistency.redis;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CacheUitl {

    @Autowired
    private RedisTemplate redisTemplate;

    public void set(String key, Object value){
        if (value instanceof String) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));
        }
    }

    public String get(String key){
        String value = (String)redisTemplate.opsForValue().get(key);
        return value;
    }

    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}
