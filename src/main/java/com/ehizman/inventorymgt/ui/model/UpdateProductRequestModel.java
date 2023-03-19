package com.ehizman.inventorymgt.ui.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class UpdateProductRequestModel {
    @NotNull(message = "product Name cannot be null")
    @NotBlank(message = "product Name cannot be blank")
    private String productName;

    @NotNull(message = "product description cannot be null")
    @NotBlank(message = "product description cannot be blank")
    private String description;
    @NotNull(message = "stock level cannot be null")
    @PositiveOrZero(message = "stock level cannot be negative")
    private Integer stockLevel;

    @NotNull(message = "initial stock level cannot be null")
    @PositiveOrZero(message = "price cannot be negative")
    private Double price;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(Integer stockLevel) {
        this.stockLevel = stockLevel;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
