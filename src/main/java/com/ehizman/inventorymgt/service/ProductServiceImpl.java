package com.ehizman.inventorymgt.service;

import com.ehizman.inventorymgt.dto.ProductDto;
import com.ehizman.inventorymgt.model.Product;
import com.ehizman.inventorymgt.repository.ProductRepository;
import com.ehizman.inventorymgt.ui.model.CreateProductRequestModel;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto createProduct(CreateProductRequestModel productRequestModel) {
        BigDecimal price = BigDecimal.valueOf(productRequestModel.getPrice() * 100);
        String productId = UUID.randomUUID().toString();
        Product product = new Product(
                productId,
                productRequestModel.getInitialStockLevel(),
                productRequestModel.getProductName(),
                price,
                productRequestModel.getDescription()
        );
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public Page<ProductDto> getAllProducts(String pageNumber, String pageSize) {
        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize));
        Page<Product> products =  productRepository.findAll(pageable);
        return products.map(product -> modelMapper.map(product, ProductDto.class));
    }
}
