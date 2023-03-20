package com.ehizman.inventorymgt.model;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public class OrderItem {
    @Min(1)
    private int quantity;
    private String productId;

    public OrderItem(int quantity, String productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public OrderItem() {

    }

    private BigDecimal value;

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(){
        this.quantity += 1;
    }

    public void decreaseQuantity(){
        this.quantity -= 1;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "OrderItem{" +
                "quantity=" + quantity +
                ", productId='" + productId + '\'' +
                ", value=" + value +
                '}';
    }
}
