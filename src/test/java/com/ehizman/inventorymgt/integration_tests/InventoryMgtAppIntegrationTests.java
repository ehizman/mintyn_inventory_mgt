package com.ehizman.inventorymgt.integration_tests;

import com.ehizman.inventorymgt.model.OrderItem;
import com.ehizman.inventorymgt.ui.model.CreateOrderRequestModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Testcontainers
@Slf4j
@ActiveProfiles("docker")
public class InventoryMgtAppIntegrationTests {
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        this.restTemplate = new RestTemplate();
    }

    @Container
    GenericContainer container = new GenericContainer(
            DockerImageName.parse("inventorymgt-inventory_mgt_app:latest")).withExposedPorts(8090);

    @Test
    @DisplayName("withFixedUrlShouldFail")
    public void testConnection(){
        CreateOrderRequestModel createOrderRequestModel = new CreateOrderRequestModel();
        createOrderRequestModel.setCustomerName("Test User");
        createOrderRequestModel.setCustomerPhoneNumber("+2348028887080");
        Set<OrderItem> orderItemSet = getOrderItems();
        createOrderRequestModel.setOrderItems(orderItemSet);
        String url = "http://" + container.getHost() + ":" + container.getFirstMappedPort();
        log.info("Url --> {}", url);
        HttpEntity<CreateOrderRequestModel> request = new HttpEntity<>(createOrderRequestModel);
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.POST, request, CreateOrderRequestModel.class);
        log.info("Response --> {}", response);
        assertEquals(response.getStatusCode().toString(), HttpStatus.BAD_REQUEST.toString());
    }

    private Set<OrderItem> getOrderItems() {
        Set<OrderItem> orderItems = new HashSet<>();
        OrderItem orderItem1 = new OrderItem(2, "d5548c58-11fa-4676-a1a2-819024e7d030");
        OrderItem orderItem2= new OrderItem(4, "95e4272d-336d-4ef2-bee1-1e29b2978c01");
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);
        return orderItems;
    }

}
