package com.pk.consistency.service;

import com.pk.consistency.pojo.OrderInventory;
import com.pk.consistency.util.Result;

public interface OrderInventoryService {

    Result buy(OrderInventory orderInventory);

    OrderInventory get(Integer id);

    void add(OrderInventory orderInventory);

    String getFromCache(Integer id);
}
