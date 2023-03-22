package com.ehizman.inventorymgt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public class OrderItem {
    @Min(1)
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("productId")
    private String productId;

    public OrderItem(int quantity, String productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public OrderItem() {

    }
    @JsonProperty("value")
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
