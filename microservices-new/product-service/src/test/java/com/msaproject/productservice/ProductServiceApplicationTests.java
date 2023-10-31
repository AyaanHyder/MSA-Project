package com.msaproject.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msaproject.productservice.dto.ProductRequest;
import com.msaproject.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers //Junit5 understands that we will use testcontainers for this test
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container //first test will start by downloading mongodb container image
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper; //helps to converts json to objects and vice versa

//	after starting container it will getreplicaseturl and add it to the spring.data.mongodb.uri
//	dynamically at the time of creating test

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
		//we have to provide this dynamically since we are not using the db locally

	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);//This line converts the productRequest object into a JSON string.
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")//way to simulate sending HTTP requests to your application. In this case, it's simulating sending a POST request to create a product.
				.contentType(MediaType.APPLICATION_JSON)//telling the server that the request body is in JSON format.
				.content(productRequestString))//This is where the JSON data (the product request) is added to the request body.
				.andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());//product repo contains the product or not

	}

//	Calls getProductRequest() to get a sample product request.
//	Converts the request to a JSON string.
//	Sends a POST request to "/api/product" (a hypothetical API endpoint for creating a product) with the JSON data.
//	Expects the HTTP status to be "Created" (which is HTTP status code 201).
	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("iPhone 13")
				.description("iPhone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
