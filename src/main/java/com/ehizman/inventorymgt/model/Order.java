package com.ehizman.inventorymgt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "orders")
public class Order {

    @Version
    private final int version = 0;
    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("orderStatus")
    private OrderStatus orderStatus;
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("orderItems")
    private Set<OrderItem> orderItems;
    @JsonProperty("creationTime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime creationTime;
    @JsonProperty("customerName")
    private String customerName;
    @JsonProperty("customerPhoneNumber")
    private String customerPhoneNumber;
    @JsonProperty("totalValue")
    private BigDecimal totalValue;

    public Order(OrderStatus orderStatus, String orderId, String customerName, String customerPhoneNumber, Set<OrderItem> orderItems, LocalDateTime orderCreationTime) {
        this.orderStatus = orderStatus;
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.orderItems = orderItems;
        this.totalValue = new BigDecimal(0);
        this.creationTime = orderCreationTime;
    }

    public Order(String id, OrderStatus orderStatus, String orderId, Set<OrderItem> orderItems, LocalDateTime creationTime, String customerName, String customerPhoneNumber, BigDecimal totalValue) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.creationTime = creationTime;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.totalValue = totalValue;
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

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
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
