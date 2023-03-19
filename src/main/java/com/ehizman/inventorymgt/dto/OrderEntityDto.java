package com.ehizman.inventorymgt.dto;

import com.ehizman.inventorymgt.model.OrderItem;
import com.ehizman.inventorymgt.model.OrderStatus;

import java.math.BigDecimal;
import java.util.Set;

public class OrderEntityDto {
    private OrderStatus orderStatus;
    private String orderId;
    private Set<OrderItem> orderItems;
    private String customerName;
    private String customerPhoneNumber;

    private BigDecimal value;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
