package com.ehizman.inventorymgt.repository.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

@Component
public class ProductNameIndexCreator {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ProductNameIndexCreator(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void createIndex() {
        mongoTemplate.indexOps("products")
                .ensureIndex(new Index().on("name", Sort.Direction.ASC)
                        .unique().named("name_index"));
    }
}
