package com.ehizman.inventorymgt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;

    private OrderStatus orderStatus;
    private final String orderId;
    private final Set<OrderItem> orderItems;
    private final String customerName;
    private final String customerPhoneNumber;

    private BigDecimal value;

    public Order(OrderStatus orderStatus, String orderId, String customerName, String customerPhoneNumber) {
        this.orderStatus = orderStatus;
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.orderItems = new HashSet<>();
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

    public void addToOrderValue(BigDecimal valueOfOrderItem){
        this.value = this.value.add(valueOfOrderItem);
    }
}
