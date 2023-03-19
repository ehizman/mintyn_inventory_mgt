package com.ehizman.inventorymgt.repository.mongodb;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MongoIndexCreator {

    private final ProductNameIndexCreator indexCreator;

    public MongoIndexCreator(ProductNameIndexCreator indexCreator) {
        this.indexCreator = indexCreator;
    }

    @PostConstruct
    public void createIndex() {
        indexCreator.createIndex();
    }
}
