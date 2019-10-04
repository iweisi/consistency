package com.pk.consistency.service.impl;

import com.pk.consistency.mapper.OrderInventoryMapper;
import com.pk.consistency.pojo.OrderInventory;
import com.pk.consistency.redis.CacheUitl;
import com.pk.consistency.service.OrderInventoryService;
import com.pk.consistency.util.CacheEnum;
import com.pk.consistency.util.LockUtil;
import com.pk.consistency.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.Lock;

@Service
public class OrderInventoryServiceImpl implements OrderInventoryService {

    private static final String CACHE_PREX = "ORDER:INVENTORY:";

    @Autowired
    private OrderInventoryMapper mapper;
    @Autowired
    private CacheUitl cacheUitl;

    /**
     *  在向数据库中添加数据时，要先清除可能已经存在于缓存中的数据
     * 为了缓存数据库的双写一致性还是要加上分布式锁，与查询时的锁必须是同一个把锁，即同一个key
     * @param orderInventory
     * @return com.pk.consistency.util.Result
     * @Author: pengke
     * @Date: 2019年10月04日
     **/
    @Override
    public Result buy(OrderInventory orderInventory) {

        Integer id = orderInventory.getId();
        String key = CACHE_PREX + id;
        LockUtil.lockWithRetry(key, 3);
        try {
            // 先删除缓存
            cacheUitl.delete(key);
            // 后更新数据库
            int i = mapper.update(orderInventory);
            if (i == 1) {
                return Result.ok();
            } else {
                return Result.build(500, "hahha");
            }
        } finally {
            LockUtil.unlock(key);
        }
    }

    /**
     * 将查询到的数据添加到缓存时需要加上分布式锁来解决缓存与数据库双写时的数据一致性问题
     * @param id
     * @return com.pk.consistency.pojo.OrderInventory
     * @Author: pengke
     * @Date: 2019年10月04日
     **/
    @Override
    public OrderInventory get(Integer id) {
        String key = CACHE_PREX + id;
        LockUtil.lockWithRetry(key, 3);
        try {
            OrderInventory orderInventory = mapper.select(id);
            // 如果数据库中有值的话就刷新缓存
            if (orderInventory != null) {
                cacheUitl.set(key, orderInventory);
            } else {
                // 如果数据库中没有值，就在缓存里设置一个默认的值，防止缓存穿透
                cacheUitl.set(key, CacheEnum.DEFAULT_STRING_NULL);
            }
            return orderInventory;
        } finally {
            LockUtil.unlock(key);
        }
    }

    /**
     * 因为有防止缓存穿透的默认值，所以在向数据库中添加数据时，要先清除可能已经存在于缓存中的数据
     * 为了缓存数据库的双写一致性还是要加上分布式锁，与查询时的锁必须是同一个把锁，即同一个key
     * @param orderInventory
     * @return void
     * @Author: pengke
     * @Date: 2019年10月04日
     **/
    @Override
    public void add(OrderInventory orderInventory) {
        String key = CACHE_PREX + orderInventory.getId();
        LockUtil.lockWithRetry(key, 3);
        try {
            // 先删除缓存
            cacheUitl.delete(key);
            // 后更新数据库
            mapper.insert(orderInventory);
        } finally {
            LockUtil.unlock(key);
        }
    }

    /**
     * 在进行数据查询时先查询缓存，此时是无需加锁的，如果缓存中的数据为空，
     * 并且没有防止缓存穿透的默认值的情况下，需要去查询数据库，并且更新缓存，
     * 如果有数据则保存到缓存中，如果没有数据则向缓存中添加一个默认的值
     * @param id
     * @return java.lang.String
     * @Author: pengke
     * @Date: 2019年10月04日
     **/
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String getFromCache(Integer id) {
        String key = CACHE_PREX + id;
        // 先查询缓存，如果缓存中有值的话就直接返回
        String cache = cacheUitl.get(key);
//        OrderInventory orderInventory = null;
//        if (cache != null) {
//            if (CacheEnum.DEFAULT_STRING_NULL.equals(cache)) {
//                return null;
//            } else {
//                orderInventory = JSONObject.parseObject(cache, OrderInventory.class);
//            }
//        }
//        return orderInventory;
        return cache;
    }

}
