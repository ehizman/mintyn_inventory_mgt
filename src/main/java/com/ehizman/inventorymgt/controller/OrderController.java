package com.ehizman.inventorymgt.controller;

import com.ehizman.inventorymgt.dto.OrderCreationResponse;
import com.ehizman.inventorymgt.dto.OrderEntityDto;
import com.ehizman.inventorymgt.service.OrderService;
import com.ehizman.inventorymgt.ui.model.CreateOrderRequestModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<?> createOrder(@NotNull @Valid @RequestBody CreateOrderRequestModel createOrderRequestModel){
        OrderCreationResponse orderCreationResponse = orderService.createOrder(createOrderRequestModel);
        return new ResponseEntity<>(orderCreationResponse, HttpStatus.CREATED);
    }
}
