package com.InventoryService.service;

import com.InventoryService.dto.InventoryResponse;
import com.InventoryService.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> getStock(List<String> skuCode){
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                    .skuCode(inventory.getSkuCode())
                    .isInStock(inventory.getQuantity()>0) //if qty > 0 then true else false
                    .build()
                ).collect(Collectors.toList());
    }
}
