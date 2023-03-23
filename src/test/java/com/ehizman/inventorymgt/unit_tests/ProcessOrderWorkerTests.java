package com.ehizman.inventorymgt.unit_tests;

import com.ehizman.inventorymgt.kafka.ProcessOrderWorker;
import com.ehizman.inventorymgt.model.Order;
import com.ehizman.inventorymgt.model.OrderFactory;
import com.ehizman.inventorymgt.model.OrderStatus;
import com.ehizman.inventorymgt.service.OrderService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessOrderWorkerTests {
    @Mock
    private OrderService orderService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private ProcessOrderWorker processOrderWorker;

    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;
    @Captor
    private ArgumentCaptor<KafkaOperations.OperationsCallback> operationsCallbackArgumentCaptor;

    @BeforeEach
    void setUp() {
        processOrderWorker = new ProcessOrderWorker(orderService, kafkaTemplate);
    }

    @Test
    public void testListener() {
        // Given
        String orderId = UUID.randomUUID().toString();
        String json = "{\"orderId\":\"" + orderId + "\",\"orderStatus\":\"PENDING\",\"orderItems\":[{\"productId\":\"123\",\"quantity\":2}]}";

        ConsumerRecord<String, String> record = new ConsumerRecord<>("process", 0, 0, "key", json);

        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderStatus(OrderStatus.SUCCESSFUL);

        when(orderService.saveOrder(any(Order.class))).thenReturn(order);

        // When
        processOrderWorker.listener(record);

        // Then
        verify(orderService).saveOrder(orderArgumentCaptor.capture());
        Order capturedOrder = orderArgumentCaptor.getValue();
        assertThat(capturedOrder.getOrderId()).isEqualTo(orderId);
        assertThat(capturedOrder.getOrderStatus()).isEqualTo(OrderStatus.SUCCESSFUL);
        verify(kafkaTemplate, times(1)).executeInTransaction(operationsCallbackArgumentCaptor.capture());
    }

    @Test
    void testListener_SaveOrderException() {
        ProcessOrderWorker worker = new ProcessOrderWorker(orderService, kafkaTemplate);
        String orderJson = "{\"orderId\":\"123\",\"customerName\":\"John\",\"totalValue\":200.0,\"orderStatus\":\"PENDING\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("process", 0, 0, "key", orderJson);
        Order order = OrderFactory.fromJson(orderJson);
        RuntimeException exception = new RuntimeException("Failed to save order");
        when(orderService.saveOrder(any(Order.class))).thenThrow(exception);

        assertThrows(RuntimeException.class, () -> {
            worker.listener(record);
        });

        verifyNoInteractions(kafkaTemplate);
        assertEquals(OrderStatus.PENDING, order.getOrderStatus());
    }

}
