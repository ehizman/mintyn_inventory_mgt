package com.ehizman.inventorymgt.dto;



import java.time.LocalDateTime;

public class OrderCreationResponse {
    private LocalDateTime creationTime;
    private String message;

    public static OrderCreationResponse builder(){
        return new OrderCreationResponse();
    }
    public OrderCreationResponse creationTime(LocalDateTime creationTime){
        this.creationTime = creationTime;
        return this;
    }

    public OrderCreationResponse message(String message){
        this.message = message;
        return this;
    }

    public OrderCreationResponse build(){
        return this;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OrderCreationResponse{" +
                ", creationTime=" + creationTime +
                ", message=" + message +
                '}';
    }
}
