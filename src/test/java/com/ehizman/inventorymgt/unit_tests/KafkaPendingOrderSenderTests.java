package com.ehizman.inventorymgt.unit_tests;

import com.ehizman.inventorymgt.exception.OutOfStockException;
import com.ehizman.inventorymgt.exception.ProductNotFoundException;
import com.ehizman.inventorymgt.kafka.KafkaPendingOrderSender;
import com.ehizman.inventorymgt.model.Order;
import com.ehizman.inventorymgt.model.OrderItem;
import com.ehizman.inventorymgt.model.OrderStatus;
import com.ehizman.inventorymgt.model.Product;
import com.ehizman.inventorymgt.repository.OrderRepository;
import com.ehizman.inventorymgt.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class KafkaPendingOrderSenderTests {
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    private KafkaPendingOrderSender kafkaPendingOrderSender;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    public void setUp() {
        kafkaPendingOrderSender = new KafkaPendingOrderSender(kafkaTemplate, productRepository, orderRepository);
    }

    @Test
    public void testSendMessage_Successful() {
        Product product = new Product(
                "P123",
                10,
                "Test Product",
                new BigDecimal("20000.0"),
                "Product Description"
        );

        Order order = new Order();
        order.setOrderId("O123");
        order.setOrderStatus(OrderStatus.PENDING);

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId("P123");
        orderItem.setQuantity(3);

        order.setOrderItems(Set.of(orderItem));

        when(productRepository.findProductByProductId(orderItem.getProductId())).thenReturn(Optional.of(product));

        kafkaPendingOrderSender.sendMessage(order, "test-topic");

        verify(productRepository, times(1)).findProductByProductId(orderItem.getProductId());
        verify(productRepository, times(1)).saveAll(anyList());
        verify(kafkaTemplate, times(1)).send(eq("test-topic"), anyString(), stringArgumentCaptor.capture());

        String actualOrderJson = stringArgumentCaptor.getValue();

        assertThat(actualOrderJson).contains("\"orderId\":\"O123\"");
        assertThat(actualOrderJson).contains("\"productId\":\"P123\"");
        assertThat(actualOrderJson).contains("\"quantity\":3");
        assertThat(new BigDecimal("60000.0")).isEqualTo(order.getTotalValue());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    public void testSendMessage_ProductNotFound() {
        Order order = new Order();
        order.setOrderId("O123");

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId("P123");
        orderItem.setQuantity(3);

        order.setOrderItems(Set.of(orderItem));

        when(productRepository.findProductByProductId(orderItem.getProductId())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> kafkaPendingOrderSender.sendMessage(order, "test-topic"));

        verify(productRepository, times(1)).findProductByProductId(orderItem.getProductId());
        verify(productRepository, never()).saveAll(anyList());
        verify(orderRepository, never()).save(order);
        verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    public void testSendMessage_OutOfStock() {
        // Arrange
        Product product = new Product(
                "P123",
                2,
                "Test Product",
                new BigDecimal("20000.0"),
                "Product Description"
        );

        Order order = new Order();
        order.setOrderId("O123");

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId("P123");
        orderItem.setQuantity(3);

        order.setOrderItems(Set.of(orderItem));

        when(productRepository.findProductByProductId(orderItem.getProductId())).thenReturn(Optional.of(product));

        assertThrows(OutOfStockException.class, () -> kafkaPendingOrderSender.sendMessage(order, "test-topic"));
    }
}
