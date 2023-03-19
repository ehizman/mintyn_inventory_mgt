package com.ehizman.inventorymgt.service;

import com.ehizman.inventorymgt.dto.OrderEntityDto;
import com.ehizman.inventorymgt.ui.model.CreateOrderRequestModel;

public interface OrderService {
    OrderEntityDto createOrder(CreateOrderRequestModel createOrderRequestModel);
}
