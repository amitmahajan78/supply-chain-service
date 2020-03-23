package com.supply.chain.order.worker.consumer;

import com.supply.chain.order.worker.domain.SalesOrder;
import com.supply.chain.order.worker.repository.SalesOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SalesOrderListener {

    private static final Logger LOG = LoggerFactory.getLogger(SalesOrderListener.class);

    @Autowired
    SalesOrderRepository salesOrderRepository;

    @KafkaListener(topics = "${app.topic.sales-order-consumer}", containerFactory = "kafkaListenerSalesOrderContainerFactory")
    public void receive(@Payload SalesOrder data,
                        @Headers MessageHeaders headers) {
        LOG.info("received data='{}'", data.toString());

        headers.keySet().forEach(key -> {
            LOG.info("{}: {}", key, headers.get(key));
        });

        // saving sales order records in supply chain db
        data.setId(UUID.randomUUID());
        data.setStatus("NEW");
        salesOrderRepository.save(data);
    }

}
