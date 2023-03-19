package com.ehizman.inventorymgt.model;

public class OrderItem {
    private int quantity;
    private String productId;

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
}
