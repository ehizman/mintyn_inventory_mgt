package com.ehizman.inventorymgt;

import com.ehizman.inventorymgt.model.OrderItem;
import com.ehizman.inventorymgt.ui.model.CreateOrderRequestModel;

import java.util.HashSet;
import java.util.Set;

public class CreateOrderRequestTestModelFactory {
    public static CreateOrderRequestModel getCreateOrderRequestModel() {
        CreateOrderRequestModel requestModel = new CreateOrderRequestModel();
        requestModel.setCustomerName("Test Customer");
        requestModel.setCustomerPhoneNumber("+2348028887080");
        Set<OrderItem> orderItems = new HashSet<>();
        OrderItem orderItem1 = new OrderItem(2, "d5548c58-11fa-4676-a1a2-819024e7d030");
        OrderItem orderItem2= new OrderItem(4, "95e4272d-336d-4ef2-bee1-1e29b2978c01");
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);
        requestModel.setOrderItems(orderItems);

        return requestModel;
    }
}
