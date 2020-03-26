package com.supply.chain.order.worker.task;

import com.supply.chain.order.worker.domain.*;
import com.supply.chain.order.worker.repository.ProductRepository;
import com.supply.chain.order.worker.repository.SalesOrderRepository;
import com.supply.chain.order.worker.utils.RestCommunication;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
@Slf4j
public class OrderCreationTask {

    @Autowired
    SalesOrderRepository salesOrderRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    RestCommunication restCommunication;

    @Autowired
    private Environment env;


    @Scheduled(cron = "*/5 * * * * *")
    @SchedulerLock(name = "OrderCreation_scheduledTask",
            lockAtLeastForString = "PT5M", lockAtMostForString = "PT6M")
    public void orderCreationScheduledTask() {
        log.info("OrderCreation_scheduledTask" + LocalDateTime.now());

        AuthDetails authDetails = new AuthDetails();

        // Getting all sales
        ArrayList<SalesOrderSelect> salesOrders = salesOrderRepository.findSalesOrderForReplenishment();

        if (!salesOrders.isEmpty()) {
            // Obtaining Auth token
            authDetails = restCommunication.authenticate();

        }

        if (authDetails.getAccess_token() != null && !authDetails.getAccess_token().isEmpty()) {

            // Preparing orders for fulfilment
            for (SalesOrderSelect salesOrder : salesOrders) {

                salesOrderRepository.updateSalesOrderStatus(salesOrder.getProductId(),
                        "NEW", salesOrder.getStore());

                Product product = productRepository.findByProductId(salesOrder.getProductId());


                // Build order submission request


                /**
                 {
                 "shipFrom" : "SWINDON",
                 "shipTo": "Reading",
                 "booking" : {
                 "vehicleNumber" : "ABC123",
                 "appointment" : "2020-01-26T18:25:43.511Z"
                 },
                 "orderDetailList" : [
                 {
                 "sku" : "1100000021",
                 "quantity" : 1
                 },
                 {
                 "sku" : "1100000022",
                 "quantity" : 1
                 }
                 ]
                 }
                 **/

                OrderDetails orderDetails = new OrderDetails();
                Booking booking = new Booking();
                ProductDetails productDetails = new ProductDetails();

                booking.setAppointment(LocalDateTime.now().plusDays(1).toString());
                booking.setVehicleNumber("VIL00111");

                productDetails.setQuantity(salesOrder.getQuantity());
                productDetails.setSku(product.getSku());

                ArrayList<ProductDetails> productDetails1 = new ArrayList<>();
                productDetails1.add(productDetails);

                orderDetails.setShipFrom(product.getLocation());
                orderDetails.setShipTo(salesOrder.getStore());
                orderDetails.setBooking(booking);
                orderDetails.setOrderDetailList(productDetails1);

                // Calling order fulfillment
                restCommunication.postOrder(orderDetails, authDetails);

            }
        }


    }


}
