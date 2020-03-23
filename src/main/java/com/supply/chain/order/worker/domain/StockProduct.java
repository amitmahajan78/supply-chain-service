package com.supply.chain.order.worker.domain;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class StockProduct {

    private String sku;
    private int quantity;
    private String supplier;
    private String category;
    private String productName;

}
