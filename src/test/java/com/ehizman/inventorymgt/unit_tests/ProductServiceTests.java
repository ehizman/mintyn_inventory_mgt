package com.ehizman.inventorymgt.unit_tests;

import com.ehizman.inventorymgt.CreateProductRequestModelTestFactory;
import com.ehizman.inventorymgt.dto.ProductDto;
import com.ehizman.inventorymgt.exception.ProductNotFoundException;
import com.ehizman.inventorymgt.model.Product;
import com.ehizman.inventorymgt.repository.ProductRepository;
import com.ehizman.inventorymgt.service.ProductService;
import com.ehizman.inventorymgt.service.ProductServiceImpl;
import com.ehizman.inventorymgt.ui.model.CreateProductRequestModel;
import com.ehizman.inventorymgt.ui.model.UpdateProductRequestModel;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setup(){
        productService = new ProductServiceImpl(productRepository, modelMapper);
    }

    @Test
    @DisplayName("testCreateProductShouldReturnSuccess")
    void testCreateProduct(){
        CreateProductRequestModel requestModel = CreateProductRequestModelTestFactory.getCreateOrderRequestModel();
        String productId = UUID.randomUUID().toString();
        when(productRepository.save(any(Product.class))).thenAnswer(product->
                getProduct(requestModel, productId)
        );
        when(modelMapper.map(any(Product.class), eq(ProductDto.class))).thenAnswer(productDto->{
            ProductDto productDto1 = new ProductDto();
            productDto1.setProductId(getProduct(requestModel, productId).getProductId());
            productDto1.setProductPriceInKobo(getProduct(requestModel, productId).getProductPriceInKobo());
            productDto1.setName(getProduct(requestModel, productId).getName());
            productDto1.setDescription(getProduct(requestModel, productId).getDescription());
            return productDto1;
        });

        ProductDto productDto = productService.createProduct(requestModel);
        assertThat(productDto.getProductId()).isEqualTo(productId);
        assertThat(productDto.getProductPriceInKobo()).isEqualTo(BigDecimal.valueOf(10000.0));
        assertThat(productDto.getName()).isEqualTo("Test Product");
        assertThat(productDto.getDescription()).isEqualTo("Test Description");
    }

    @Test
    @DisplayName("testThatCanGetAllProductsShouldReturnSuccess")
    void testGetAllProducts(){
        when(productRepository.findAll(any(Pageable.class))).thenAnswer(page -> new
                PageImpl<>(getProducts(), PageRequest.of(0, 5), 20));

        when(modelMapper.map(any(Product.class), eq(ProductDto.class))).thenAnswer(productDto -> {
            ProductDto productDto1 = new ProductDto();
                    productDto1.setDescription("Description");
                    productDto1.setName("ProductDto");
                    productDto1.setProductPriceInKobo(BigDecimal.valueOf(1000.0));
                    productDto1.setProductId(UUID.randomUUID().toString());

                    return productDto1;
        });

        Page<ProductDto> page = productService.getAllProducts("0", "5");
        assertThat(page.getContent().size()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(20);
        assertThat(page.getTotalPages()).isEqualTo(4);

    }

    @Test
    @DisplayName("testUpdateProductShouldReturnSuccess")
    void testUpdateProduct(){
        when(productRepository.findProductByProductId(anyString())).thenAnswer(product -> {
            Product productEntity = new Product(
                    "Test ProductId",
                    20,
                    "Test Product Name",
                    BigDecimal.valueOf(100.0),
                    "Description");
            productEntity.setId("id");
            return Optional.of(productEntity);
        });
        UpdateProductRequestModel requestModel = new UpdateProductRequestModel();
        requestModel.setDescription("Edited Product Description");
        requestModel.setProductName("Edited Product Name");
        requestModel.setPrice(1000.0);
        requestModel.setStockLevel(50);
        when(productRepository.save(any(Product.class))).thenAnswer(product ->
             new Product(
                    "Test ProductId",
                    requestModel.getStockLevel(),
                    requestModel.getProductName(),
                    BigDecimal.valueOf(requestModel.getPrice()),
                    requestModel.getDescription()
            ));

        when(modelMapper.map(any(Product.class), eq(ProductDto.class))).thenAnswer(productDto -> {
            ProductDto productDto1 = new ProductDto();
                    productDto1.setProductId("Test ProductId");
                    productDto1.setName(requestModel.getProductName());
                    productDto1.setDescription(requestModel.getDescription());
                    productDto1.setProductPriceInKobo(BigDecimal.valueOf(requestModel.getPrice()*100));

                    return productDto1;
        });

        ProductDto productDto = productService.updateProduct("Test ProductId", requestModel);

        assertThat(productDto.getProductId()).isEqualTo("Test ProductId");
        assertThat(productDto.getProductPriceInKobo()).isEqualTo(BigDecimal.valueOf(100000.0));
        assertThat(productDto.getName()).isEqualTo("Edited Product Name");
        assertThat(productDto.getDescription()).isEqualTo("Edited Product Description");
    }

    @Test
    @DisplayName("testUpdateProductShouldThrowException")
    void testUpdateProductFail(){
        when(productRepository.findProductByProductId(anyString())).thenReturn(Optional.empty());
        UpdateProductRequestModel requestModel = new UpdateProductRequestModel();
        requestModel.setDescription("Edited Product Description");
        requestModel.setProductName("Edited Product Name");
        requestModel.setPrice(1000.0);
        requestModel.setStockLevel(50);
        assertThatThrownBy(()-> productService.updateProduct("ProductId",requestModel)).isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product: ProductId not found");
    }
    @NotNull
    private Product getProduct(CreateProductRequestModel requestModel, String productId) {
        Product product = new Product(
                productId,
                requestModel.getInitialStockLevel(),
                requestModel.getProductName(),
                BigDecimal.valueOf(requestModel.getPrice() * 100),
                requestModel.getDescription());
        product.setId("id");
        return product;
    }



    private List<Product> getProducts() {
        Product product1 = new Product(
                "product1 Id",
               3,
                "Product 1",
                BigDecimal.valueOf(100.0* 100),
                "Product 1 Description");
        product1.setId("id1");

        Product product2 = new Product(
                "product2 Id",
                5,
                "Product 2",
                BigDecimal.valueOf(50.0 * 100),
                "Product 2 Description");
        product2.setId("id2");
        return List.of(product1, product2);
    }
}
