package com.ehizman.inventorymgt.ui.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;


public class CreateProductRequestModel {
    @NotNull(message = "product Name cannot be null")
    @NotBlank(message = "product Name cannot be blank")
    private String productName;

    @NotNull(message = "product description cannot be null")
    @NotBlank(message = "product description cannot be blank")
    private String description;
    @NotNull(message = "initial stock level cannot be null")
    @Min(value = 1, message = "minimum initial stock level is 1")
    private Integer initialStockLevel;

    @NotNull(message = "initial stock level cannot be null")
    @PositiveOrZero(message = "price cannot be negative")
    private Double price;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getInitialStockLevel() {
        return initialStockLevel;
    }

    public void setInitialStockLevel(Integer initialStockLevel) {
        this.initialStockLevel = initialStockLevel;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
