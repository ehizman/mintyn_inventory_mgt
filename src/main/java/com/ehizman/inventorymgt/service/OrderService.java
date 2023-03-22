package com.ehizman.inventorymgt.service;

import com.ehizman.inventorymgt.dto.OrderCreationResponse;
import com.ehizman.inventorymgt.model.Order;
import com.ehizman.inventorymgt.ui.model.CreateOrderRequestModel;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderService {
    OrderCreationResponse createOrder(CreateOrderRequestModel createOrderRequestModel) throws JsonProcessingException;
    Order saveOrder(Order order);
}
