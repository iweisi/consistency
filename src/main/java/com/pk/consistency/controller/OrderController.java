package com.pk.consistency.controller;

import com.alibaba.fastjson.JSONObject;
import com.pk.consistency.pojo.OrderInventory;
import com.pk.consistency.service.OrderInventoryService;
import com.pk.consistency.util.CacheEnum;
import com.pk.consistency.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/order")
public class OrderController {

    public static final Map<Integer, Boolean> flags = new ConcurrentHashMap<>();
    @Autowired
    private OrderInventoryService orderInventoryService;

    @GetMapping("/get/{id}")
    public Result getInventory(@PathVariable Integer id){

        if (id == null) {
            return Result.err("请求数据为空！");
        }
        /*
         * 从缓存中取数据
         */
        String cache = orderInventoryService.getFromCache(id);
        OrderInventory orderInventory = null;
        if (cache != null) {
            if (CacheEnum.DEFAULT_STRING_NULL.equals(cache)) {
                return Result.ok();
            } else {
                orderInventory = JSONObject.parseObject(cache, OrderInventory.class);
                return Result.ok(orderInventory);
            }
        }
        /*
         * 缓存中数据为空时，再从数据库中查询数据，并更新缓存
         */
        orderInventory = orderInventoryService.get(id);

        return Result.ok(orderInventory);
    }

    @GetMapping("/buy/{id}/{count}")
    public Result buy(@PathVariable Integer id, @PathVariable int count){
        if (id == null || id <= 0) {
            return Result.err("商品不存在！");
        }
        if (count <= 0) {
            return Result.err("商品数量不合法！");
        }
        return orderInventoryService.buy(new OrderInventory(id, count));
    }

    @GetMapping("/add/{id}/{count}")
    public Result add(@PathVariable Integer id, @PathVariable int count){
        if (id == null || id <= 0) {
            return Result.err("商品不存在！");
        }
        if (count <= 0) {
            return Result.err("商品数量不合法！");
        }
        orderInventoryService.add(new OrderInventory(id, count));
        return Result.ok();
    }
}
