package com.ehizman.inventorymgt.kafka;

import com.ehizman.inventorymgt.util.GsonFactory;
import com.ehizman.inventorymgt.model.Order;
import com.ehizman.inventorymgt.model.OrderFactory;
import com.ehizman.inventorymgt.model.OrderStatus;
import com.ehizman.inventorymgt.service.OrderService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class ProcessOrderWorker {
    private final OrderService orderService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProcessOrderWorker(OrderService orderService, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderService = orderService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    @KafkaListener(
            id = "process",
            containerGroup = "a",
            topics = "process",
            containerFactory = "orderKafkaListenerContainerFactory",
            groupId = "inventory-app")
    void listener(ConsumerRecord<String, String> record) {
        Order order = OrderFactory.fromJson(record.value());

        log.info("CustomOrderListener [{}]", order);

        order.setOrderStatus(OrderStatus.SUCCESSFUL);

        Order savedOrder = orderService.saveOrder(order);
        Gson gson = GsonFactory.getGsonObject();
        kafkaTemplate.executeInTransaction(
                kafkaTemplate -> kafkaTemplate.send("report",savedOrder.getOrderId(), gson.toJson(savedOrder))
        );
    }
}
