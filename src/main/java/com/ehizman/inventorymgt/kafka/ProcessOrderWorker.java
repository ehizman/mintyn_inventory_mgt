package com.ehizman.inventorymgt.kafka;

import com.ehizman.inventorymgt.model.Order;
import com.ehizman.inventorymgt.model.OrderStatus;
import com.ehizman.inventorymgt.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class ProcessOrderWorker {
    private final OrderService orderService;

    public ProcessOrderWorker(OrderService orderService) {
        this.orderService = orderService;
    }

    @Transactional
    @KafkaListener(
            id = "process",
            containerGroup = "a",
            topics = "process",
            containerFactory = "orderKafkaListenerContainerFactory",
            groupId = "inventory-app")
    void listener(ConsumerRecord<String, Order> record) {
        Order order = record.value();
        log.info("CustomOrderListener [{}]", order);

        order.setOrderStatus(OrderStatus.SUCCESSFUL);
        orderService.saveOrder(order);
        //PRINT RECEIPT
    }
}
