package com.ehizman.inventorymgt.model;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "products")
public class Product {
    @Id
    private String id;
    @NotNull(message = "product id cannot be null")
    @NotBlank(message = "product id cannot be blank")
    private String productId;
    @Min(value = 1)
    private int stockLevel;
    @NotNull(message = "product name cannot be null")
    @NotBlank(message = "product name cannot be blank")
    @Indexed(unique = true)
    private String name;

    @NotNull(message = "product description cannot be null")
    @NotBlank(message = "product description cannot be blank")
    private String description;
    @PositiveOrZero(message = "product price cannot be negative")
    private BigDecimal productPriceInKobo;

    @NotNull(message = "creation time cannot be null")
    private LocalDateTime creationTime;

    private LocalDateTime updateTime;

    public Product(String productId, int stockLevel, String name, BigDecimal productPriceInKobo, String description) {
        this.productId = productId;
        this.stockLevel = stockLevel;
        this.name = name;
        this.productPriceInKobo = productPriceInKobo;
        this.description = description;
        this.creationTime = LocalDateTime.now();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public void increaseStockLevel(){
        stockLevel += 1;
    }

    public void decreaseStockLevel(){
        stockLevel -= 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getProductPriceInKobo() {
        return productPriceInKobo;
    }

    public void setProductPriceInKobo(BigDecimal productPriceInKobo) {
        this.productPriceInKobo = productPriceInKobo;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
