package com.programmingtech.inventoryservice.service;

import com.programmingtech.inventoryservice.dto.InventoryResponse;
import com.programmingtech.inventoryservice.model.Inventory;
import com.programmingtech.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
        List<Inventory> inventoryList = inventoryRepository.findAll();

        return inventoryList.stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0 )
                            .build()).toList();
    }
}
