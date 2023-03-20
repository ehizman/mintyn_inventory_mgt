package com.ehizman.inventorymgt.kafka;

import com.ehizman.inventorymgt.exception.OutOfStockException;
import com.ehizman.inventorymgt.exception.ProductNotFoundException;
import com.ehizman.inventorymgt.model.Order;
import com.ehizman.inventorymgt.model.OrderItem;
import com.ehizman.inventorymgt.model.OrderStatus;
import com.ehizman.inventorymgt.model.Product;
import com.ehizman.inventorymgt.repository.OrderRepository;
import com.ehizman.inventorymgt.repository.ProductRepository;
import com.ehizman.inventorymgt.service.OrderService;
import com.ehizman.inventorymgt.service.ProductService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Component
public class KakfaPendingOrderSender {
    private final KafkaTemplate<String, Order> orderKafkaTemplate;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public KakfaPendingOrderSender(KafkaTemplate<String, Order> orderKafkaTemplate, ProductRepository productRepository, OrderRepository orderRepository) {
        this.orderKafkaTemplate = orderKafkaTemplate;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }


    @Transactional
    public CompletableFuture<SendResult<String, Order>>  sendMessage(Order order, String topicName) {
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
        return orderKafkaTemplate.send(topicName, order.getOrderId(), order);
    }
}
