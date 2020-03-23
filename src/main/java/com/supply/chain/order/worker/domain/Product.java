package com.supply.chain.order.worker.domain;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Product {

    @Id
    UUID id;
    String productId;
    String sku; //
    int quantity; //
    int depotquantity; //
    Double price; //
    String location; //
    String supplier; //
    String name; //
    String category; //

}
