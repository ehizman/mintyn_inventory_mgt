package com.ehizman.inventorymgt.service;

import com.ehizman.inventorymgt.dto.OrderCreationResponse;
import com.ehizman.inventorymgt.kafka.KakfaPendingOrderSender;
import com.ehizman.inventorymgt.model.Order;
import com.ehizman.inventorymgt.model.OrderStatus;
import com.ehizman.inventorymgt.repository.OrderRepository;
import com.ehizman.inventorymgt.ui.model.CreateOrderRequestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final KakfaPendingOrderSender kakfaPendingOrderSender;

    public OrderServiceImpl(OrderRepository orderRepository, KakfaPendingOrderSender kafkaPendingOrderSender) {
        this.orderRepository = orderRepository;
        this.kakfaPendingOrderSender = kafkaPendingOrderSender;
    }

    @Override
    public OrderCreationResponse createOrder(CreateOrderRequestModel createOrderRequestModel){
        String orderId = UUID.randomUUID().toString();
        LocalDateTime orderCreationTime = LocalDateTime.now();
        Order order = new Order(
                OrderStatus.PENDING,
                orderId,
                createOrderRequestModel.getCustomerName(),
                createOrderRequestModel.getCustomerPhoneNumber(),
                createOrderRequestModel.getOrderItems(),
                orderCreationTime
        );
        Order savedOrder = orderRepository.save(order);
        kakfaPendingOrderSender.sendMessage(savedOrder, "process");
        OrderCreationResponse orderCreationResponse = OrderCreationResponse.builder()
                .creationTime(LocalDateTime.now())
                .message("Order created successfully")
                .build();

        log.info("{}", orderCreationResponse);
        return orderCreationResponse;
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
