package com.ehizman.inventorymgt.unit_tests;

import com.ehizman.inventorymgt.CreateOrderRequestTestModelFactory;
import com.ehizman.inventorymgt.dto.OrderCreationResponse;
import com.ehizman.inventorymgt.kafka.KakfaPendingOrderSender;
import com.ehizman.inventorymgt.model.Order;
import com.ehizman.inventorymgt.model.OrderItem;
import com.ehizman.inventorymgt.model.OrderStatus;
import com.ehizman.inventorymgt.repository.OrderRepository;
import com.ehizman.inventorymgt.service.OrderService;
import com.ehizman.inventorymgt.service.OrderServiceImpl;
import com.ehizman.inventorymgt.ui.model.CreateOrderRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private KakfaPendingOrderSender kakfaPendingOrderSender;
    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;

    @BeforeEach
    void setup(){
        orderService = new OrderServiceImpl(orderRepository, kakfaPendingOrderSender);
    }

    @Test
    @DisplayName("should return success")
    void testCreateOrder(){
        CreateOrderRequestModel requestModel = CreateOrderRequestTestModelFactory.getCreateOrderRequestModel();
        LocalDateTime orderCreationTime = LocalDateTime.now();
        when(orderRepository.save(any(Order.class))).thenAnswer( order ->
            new Order(
                    "id",
                    OrderStatus.PENDING,
                    UUID.randomUUID().toString(),
                    requestModel.getOrderItems(),
                    orderCreationTime,
                    requestModel.getCustomerName(),
                    requestModel.getCustomerPhoneNumber(),
                    BigDecimal.ZERO
            )
        );
        OrderCreationResponse response = orderService.createOrder(requestModel);
        verify(kakfaPendingOrderSender, times(1)).sendMessage(orderArgumentCaptor.capture(), eq("process"));

        Order capturedOrder = orderArgumentCaptor.getValue();
        assertThat(capturedOrder.getId()).isEqualTo("id");
        assertThat(capturedOrder.getOrderId()).isNotNull();
        assertThat(capturedOrder.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(capturedOrder.getOrderItems().size()).isEqualTo(2);
        assertThat(capturedOrder.getCustomerName()).isEqualTo("Test Customer");
        assertThat(capturedOrder.getTotalValue()).isEqualTo(BigDecimal.ZERO);
        assertThat(capturedOrder.getCustomerPhoneNumber()).isEqualTo("+2348028887080");

        assertThat(response.getCreationTime()).isNotNull();
        assertThat(response.getMessage()).isEqualTo("Order created successfully");
    }
}
