package com.msaproject.orderservice.service;

import com.msaproject.orderservice.dto.InventoryResponse;
import com.msaproject.orderservice.dto.OrderLineItemsDto;
import com.msaproject.orderservice.dto.OrderRequest;
import com.msaproject.orderservice.model.Order;
import com.msaproject.orderservice.model.OrderLineItems;
import com.msaproject.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional //automatically create and commit the transaction
public class OrderService {

    private final OrderRepository orderRepository; //instead of creating constructor manually we add the Required args anno
    private final WebClient.Builder webClientBuilder;


    public void placeOrder(OrderRequest orderRequest){ //will take the order request coming from the controller
       Order order = new Order();
       order.setOrderNumber(UUID.randomUUID().toString()); //set random order number

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        //call inventory service and place order if product is in stock.. Using web client
        InventoryResponse[] inventoryResponsArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", //making call directly to inventory service by hardcoding the uri
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponsArray)
                .allMatch(InventoryResponse::isInStock); //we got list of inventory response array and then converting into a stream and then calling the all match method so it will check the isinstock variable is true inside the array or not

        if(allProductsInStock){
            orderRepository.save(order);
        } else{
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }



    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;

    }
}
