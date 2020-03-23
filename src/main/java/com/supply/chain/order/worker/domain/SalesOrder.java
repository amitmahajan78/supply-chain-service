package com.supply.chain.order.worker.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class SalesOrder {

    //'{"unitPrice":44,"quantity":1,"sold":14,"remainingQuantity":-4,"productId":"5d6ffa634b69f0a96dcfc54a","saleDate":1568984978252,"store":"SWINDON-TOWN-001"}'
    @javax.persistence.Id
    UUID id;
    double unitPrice;
    int quantity;
    int sold;
    int remainingQuantity;
    String productId;
    Timestamp saleDate;
    String store;
    String status;


}
