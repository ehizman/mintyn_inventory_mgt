package com.ehizman.inventorymgt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProductDto implements Serializable {
    @NotNull(message = "product id cannot be null")
    @NotBlank(message = "product id cannot be blank")
    private String productId;
    @NotNull(message = "product name cannot be null")
    @NotBlank(message = "product name cannot be blank")
    private String name;

    @NotNull(message = "product description cannot be null")
    @NotBlank(message = "product description cannot be blank")
    private String description;
    @PositiveOrZero(message = "product price cannot be negative")
    private BigDecimal productPriceInKobo;



    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String productName) {
        this.name = productName;
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
}