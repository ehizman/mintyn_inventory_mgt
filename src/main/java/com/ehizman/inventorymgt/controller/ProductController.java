package com.ehizman.inventorymgt.controller;

import com.ehizman.inventorymgt.dto.ProductDto;
import com.ehizman.inventorymgt.service.ProductService;
import com.ehizman.inventorymgt.ui.model.CreateProductRequestModel;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequestModel productRequestModel){
        ProductDto productDto = productService.createProduct(productRequestModel);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }
    @GetMapping(produces = { "application/hal+json" })
    public ResponseEntity<?> getAllProducts(
            @RequestParam(name = "pageSize", defaultValue = "5") String pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "0") String pageNumber){
        Page<ProductDto> productDtoPage = productService.getAllProducts(pageNumber, pageSize);
        return new ResponseEntity<>(productDtoPage, HttpStatus.OK);
    }

}
