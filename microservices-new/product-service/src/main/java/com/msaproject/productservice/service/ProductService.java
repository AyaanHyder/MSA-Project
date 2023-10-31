package com.msaproject.productservice.service;

import com.msaproject.productservice.dto.ProductRequest;
import com.msaproject.productservice.dto.ProductResponse;
import com.msaproject.productservice.model.Product;
import com.msaproject.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor //creates a constructor that takes productRepository as an argument.
@Slf4j  //for logging
public class ProductService {

    private final ProductRepository productRepository;



    public void createProduct(ProductRequest productRequest){  //a builder pattern to create a Product object using the data from productRequest.
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved",product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
