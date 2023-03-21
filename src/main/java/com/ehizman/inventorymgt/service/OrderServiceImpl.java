package com.ehizman.inventorymgt.service;

import com.ehizman.inventorymgt.dto.OrderCreationResponse;
import com.ehizman.inventorymgt.kafka.KakfaPendingOrderSender;
import com.ehizman.inventorymgt.model.Order;
import com.ehizman.inventorymgt.model.OrderStatus;
import com.ehizman.inventorymgt.repository.OrderRepository;
import com.ehizman.inventorymgt.ui.model.CreateOrderRequestModel;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final KakfaPendingOrderSender kakfaPendingOrderSender;

    public OrderServiceImpl(OrderRepository orderRepository, KakfaPendingOrderSender kafkaPendingOrderSender) {
        this.orderRepository = orderRepository;
        this.kakfaPendingOrderSender = kafkaPendingOrderSender;
    }

    @Override
    public OrderCreationResponse createOrder(CreateOrderRequestModel createOrderRequestModel) {
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
        AtomicReference<OrderCreationResponse> orderCreationResponseAtomicReference = new AtomicReference<>();
        CompletableFuture<SendResult<String, Order>> future = kakfaPendingOrderSender.sendMessage(savedOrder, "process");
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                OrderCreationResponse orderCreationResponse = orderCreationResponseAtomicReference.get();
                orderCreationResponse.setOrderId(savedOrder.getOrderId());
                orderCreationResponse.setOrderStatus(OrderStatus.SUCCESSFUL);
                orderCreationResponse.setCreationTime(orderCreationTime);
            }
        });
        return orderCreationResponseAtomicReference.get();
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
