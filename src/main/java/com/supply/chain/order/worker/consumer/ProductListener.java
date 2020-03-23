package com.supply.chain.order.worker.consumer;


import com.supply.chain.order.worker.domain.Product;
import com.supply.chain.order.worker.domain.StockProduct;
import com.supply.chain.order.worker.repository.ProductRepository;
import com.supply.chain.order.worker.utils.RestCommunication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ProductListener {


    @Autowired
    ProductRepository productRepository;

    @Autowired
    RestCommunication restCommunication;

    @KafkaListener(topics = "${app.topic.products}", containerFactory = "kafkaListenerProductContainerFactory")
    public void receive(@Payload Product data,
                        @Headers MessageHeaders headers) {
        log.info("received data='{}'", data.toString());

        headers.keySet().forEach(key -> {
            log.info("{}: {}", key, headers.get(key));
        });

        // saving product records in stock db
        data.setId(UUID.randomUUID());

        // long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

        data.setSku(data.getProductId());
        productRepository.save(data);

        restCommunication.addProduct(mapProduct(data), restCommunication.authenticate());

    }

    private StockProduct mapProduct(Product product) {
        StockProduct stockProduct = new StockProduct();
        stockProduct.setCategory(product.getCategory());
        stockProduct.setProductName(product.getName());
        stockProduct.setQuantity(product.getDepotquantity());
        stockProduct.setSku(product.getSku());
        stockProduct.setSupplier(product.getSupplier());

        return stockProduct;
    }
}
