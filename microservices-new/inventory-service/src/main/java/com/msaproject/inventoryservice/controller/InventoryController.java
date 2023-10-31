package com.msaproject.inventoryservice.controller;

import com.msaproject.inventoryservice.dto.InventoryResponse;
import com.msaproject.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    //http://localhost:8082/api/inventory/iphone-13,iphone13-red

    //http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red

    @GetMapping //take in sku code as request parameter or path variable
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){ //take in skucode and verify whether product is in stock or not
        return inventoryService.isInStock(skuCode); //cretead an endpoint which takes in skucode and queries the database
        // based on skucode and will return the response whether product is in stock

        //we are taking the list of string as a request parameter and passing skucode list of string
        //instock method and querying the repository to find out all the invetory object for the sku code
        //then mapping the inventory objects to invetory response object and finally sending the list of invetory response objects as response


    }
}
