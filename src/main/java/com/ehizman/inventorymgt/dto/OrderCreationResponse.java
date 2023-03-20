package com.ehizman.inventorymgt.dto;

import com.ehizman.inventorymgt.model.OrderStatus;

import java.time.LocalDateTime;

public class OrderCreationResponse {
    private OrderStatus orderStatus;
    private LocalDateTime creationTime;
    private String orderId;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderCreationResponse{" +
                "orderStatus=" + orderStatus +
                ", creationTime=" + creationTime +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
