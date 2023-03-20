package com.ehizman.inventorymgt.service;

import com.ehizman.inventorymgt.dto.ProductDto;
import com.ehizman.inventorymgt.exception.ProductNotFoundException;
import com.ehizman.inventorymgt.ui.model.CreateProductRequestModel;
import com.ehizman.inventorymgt.ui.model.UpdateProductRequestModel;
import org.springframework.data.domain.Page;


public interface ProductService {
    ProductDto createProduct(CreateProductRequestModel productRequestModel);
    Page<ProductDto> getAllProducts(String pageNumber, String pageSize);
    ProductDto updateProduct(String productId, UpdateProductRequestModel updateProductRequestModel) throws ProductNotFoundException;
}
