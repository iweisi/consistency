package com.pk.consistency.mapper;

import com.pk.consistency.pojo.OrderInventory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInventoryMapper {

    int update(OrderInventory orderInventory);

    OrderInventory select(Integer id);

    void insert(OrderInventory orderInventory);
}