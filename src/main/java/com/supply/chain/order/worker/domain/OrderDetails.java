package com.supply.chain.order.worker.domain;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class OrderDetails implements Serializable {

    String shipFrom;
    String shipTo;
    Booking booking;
    ArrayList<ProductDetails> orderDetailList;

}

