package com.ehizman.inventorymgt.ui.model;

import com.ehizman.inventorymgt.model.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class CreateOrderRequestModel {
    @Valid
    private Set<OrderItem> orderItems;
    @NotNull(message = "customer name cannot be null")
    @NotBlank(message = "customer name cannot be blank")
    @Size(min = 2, max = 50, message = "minimum length of a customer name is 2 and maximum length is 50")
    private String customerName;
    @Pattern(regexp = "^(?:\\+234|0)[789][01]\\d{8}$")
    @NotNull(message = "customer phone number cannot be null")
    @NotBlank(message = "customer phone number cannot be blank")
    private String customerPhoneNumber;

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }
}
