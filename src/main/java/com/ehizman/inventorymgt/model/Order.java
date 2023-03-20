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
    private String orderId;
    private Set<OrderItem> orderItems;
    private LocalDateTime creationTime;
    private String customerName;
    private String customerPhoneNumber;
    private BigDecimal totalValue;

    public Order(OrderStatus orderStatus, String orderId, String customerName, String customerPhoneNumber, Set<OrderItem> orderItems, LocalDateTime orderCreationTime) {
        this.orderStatus = orderStatus;
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.creationTime = LocalDateTime.now();
        this.orderItems = orderItems;
        this.totalValue = new BigDecimal(0);
        this.creationTime = orderCreationTime;
    }

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void addItemToOrder(OrderItem orderItem){
        this.orderItems.add(orderItem);
        this.totalValue = totalValue.add(orderItem.getValue());
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setTotalValue(BigDecimal orderTotalValue) {
        this.totalValue = orderTotalValue;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", orderStatus=" + orderStatus +
                ", orderId='" + orderId + '\'' +
                ", orderItems=" + orderItems +
                ", creationTime=" + creationTime +
                ", customerName='" + customerName + '\'' +
                ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                ", totalValue=" + totalValue +
                '}';
    }
}
