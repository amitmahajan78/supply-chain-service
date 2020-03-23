package com.supply.chain.order.worker.repository;

import com.supply.chain.order.worker.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {


    public Product findByProductId(String productId);

}
