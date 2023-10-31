package com.msaproject.productservice.controller;

import com.msaproject.productservice.dto.ProductRequest;
import com.msaproject.productservice.dto.ProductResponse;
import com.msaproject.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Spring MVC controller that handles HTTP requests and returns the response directly.
@RequestMapping("/api/product") //controller will handle requests with the base URL path of /api/product
@RequiredArgsConstructor
public class ProductController {  //handling HTTP requests related to products.

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //sets the HTTP response status to 201 (Created) to indicate that a new resource has been successfully created.
    public void createProduct(@RequestBody ProductRequest productRequest){
        //RequestBody ProductRequest  means that the productRequest parameter should be populated with the data from the request body. The data is expected to be in the format specified by the ProductRequest class.
//ProductRequest object as a request body. This is the data used to create a new product.
        productService.createProduct(productRequest); // calls the createProduct method of the ProductService to handle the creation of the product.



    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProduct(){
        return productService.getAllProducts();
    }

}
