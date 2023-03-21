package com.ehizman.inventorymgt.service;

import com.ehizman.inventorymgt.dto.OrderCreationResponse;
import com.ehizman.inventorymgt.model.Order;
import com.ehizman.inventorymgt.ui.model.CreateOrderRequestModel;

public interface OrderService {
    OrderCreationResponse createOrder(CreateOrderRequestModel createOrderRequestModel);
    Order saveOrder(Order order);
}
