package com.supply.chain.order.worker.domain;

import lombok.*;

public interface SalesOrderSelect {

    String getProductId();
    int getQuantity();
    String getStore();

}
