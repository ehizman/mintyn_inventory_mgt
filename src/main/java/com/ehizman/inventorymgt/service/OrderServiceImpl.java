package com.ehizman.inventorymgt.service;

import com.ehizman.inventorymgt.dto.OrderEntityDto;
import com.ehizman.inventorymgt.model.Order;
import com.ehizman.inventorymgt.model.OrderItem;
import com.ehizman.inventorymgt.model.OrderStatus;
import com.ehizman.inventorymgt.model.Product;
import com.ehizman.inventorymgt.repository.OrderRepository;
import com.ehizman.inventorymgt.ui.model.CreateOrderRequestModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private ProductService productService;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper,  ProductService productService) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @Override
    public OrderEntityDto createOrder(CreateOrderRequestModel createOrderRequestModel) {
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(
                OrderStatus.PENDING,
                orderId,
                createOrderRequestModel.getCustomerName(),
                createOrderRequestModel.getCustomerPhoneNumber()
        );
        for (OrderItem orderItem: createOrderRequestModel.getOrderItems()) {
            Product product = productService.findProductById(orderItem.getProductId());
            orderItem.setValue(product.getProductPriceInKobo().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            order.addItemToOrder(orderItem);
        }
        Order savedOrder = orderRepository.save(order);

        //TODO Publish to queue
        // Wait for queue's response before sending response
        return modelMapper.map(savedOrder, OrderEntityDto.class);
    }
}
