package com.ehizman.inventorymgt.repository;

import com.ehizman.inventorymgt.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
