package com.InventoryService.controller;

import com.InventoryService.dto.InventoryResponse;
import com.InventoryService.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> getStock(@RequestParam("skuCode") List<String> skuCode){
       return inventoryService.getStock(skuCode);
    }
}
