package com.ehizman.inventorymgt;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
public class InventoryMgtApplication {
    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(InventoryMgtApplication.class, args);
    }

}
