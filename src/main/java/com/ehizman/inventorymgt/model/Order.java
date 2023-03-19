package com.ehizman.inventorymgt.model;

import liquibase.ContextExpression;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;

    private OrderStatus orderStatus;
    private final String orderId;
    private final Set<OrderItem> orderItems;
    private final LocalDateTime creationTime;
    private final String customerName;
    private final String customerPhoneNumber;
    private BigDecimal value;

    public Order(OrderStatus orderStatus, String orderId, String customerName, String customerPhoneNumber) {
        this.orderStatus = orderStatus;
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.creationTime = LocalDateTime.now();
        this.orderItems = new HashSet<>();
        this.value = new BigDecimal(0);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void addItemToOrder(OrderItem orderItem){
        this.orderItems.add(orderItem);
        this.value = value.add(orderItem.getValue());
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
}
