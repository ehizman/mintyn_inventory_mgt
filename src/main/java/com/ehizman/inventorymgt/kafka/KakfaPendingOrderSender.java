package com.ehizman.inventorymgt.kafka;

import com.ehizman.inventorymgt.exception.OutOfStockException;
import com.ehizman.inventorymgt.exception.ProductNotFoundException;
import com.ehizman.inventorymgt.model.*;
import com.ehizman.inventorymgt.repository.OrderRepository;
import com.ehizman.inventorymgt.repository.ProductRepository;
import com.ehizman.inventorymgt.util.GsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@Slf4j
public class KakfaPendingOrderSender {
    private final KafkaTemplate<String, String> orderKafkaTemplate;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public KakfaPendingOrderSender(KafkaTemplate<String, String> orderKafkaTemplate, ProductRepository productRepository, OrderRepository orderRepository) {
        this.orderKafkaTemplate = orderKafkaTemplate;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }


    @Transactional
    public void sendMessage(Order order, String topicName) {
        BigDecimal orderTotalValue = new BigDecimal(0);
        for (OrderItem orderItem: order.getOrderItems()) {
            Product product = productRepository.findProductByProductId(orderItem.getProductId()).orElseThrow(
                    () -> new ProductNotFoundException("Product: " + orderItem.getProductId() + " not found")
            );
            if (product.getStockLevel() < orderItem.getQuantity()){
                order.setOrderStatus(OrderStatus.FAILED);
                orderRepository.save(order);
                throw new OutOfStockException("product: " + product.getProductId() + " is out of stock");
            }
            product.setStockLevel(product.getStockLevel() - orderItem.getQuantity());
            productRepository.save(product);

            orderItem.setValue(product.getProductPriceInKobo().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            orderTotalValue = orderTotalValue.add(orderItem.getValue());
        }
        order.setTotalValue(orderTotalValue);
        Gson gson = GsonFactory.getGsonObject();
        log.info("Json from producer --> {}", gson.toJson(order));
        orderKafkaTemplate.send(topicName, order.getOrderId() , gson.toJson(order));
    }
}
