package com.pk.consistency.pojo;

import java.io.Serializable;

public class OrderInventory implements Serializable {

    private Integer id;

    private Integer count;

    public OrderInventory(Integer id, Integer count) {
        this.id = id;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}