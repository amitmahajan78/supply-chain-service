package com.supply.chain.order.worker.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProductDetails implements Serializable {

    String sku;
    int quantity;
}
