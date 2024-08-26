package com.example.service;

import com.example.dto.InventoryResponse;
import com.example.dto.OrderItemsDto;
import com.example.dto.OrderRequest;
import com.example.model.OrderItems;
import com.example.model.Orders;
import com.example.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepo orderRepo;
    private final WebClient.Builder webClientBuilder;

    public void createOrder(OrderRequest orderRequest) {

        Orders orders = new Orders();
        orders.setOrderNumber(UUID.randomUUID().toString());
        List<OrderItems> orderItemsList = orderRequest.getOrderItemsDtoList()
                .stream().map(orderItemsDto -> mapToDto(orderItemsDto))
                .collect(Collectors.toList());

        orders.setOrderItemsList(orderItemsList);

//        Using Builder Pattern
//        Orders orders = Orders.builder()
//                .OrderNumber(UUID.randomUUID().toString())
//                .orderItemsList(orderRequest.getOrderItemsDtoList()
//                .stream().map(orderItemsDto -> mapToDto(orderItemsDto))
//                .collect(Collectors.toList())).build();

        //call Inventory service and place order only if product is in stock
        List<String> skuCodes = orders.getOrderItemsList()
                .stream()
                .map(orderItems -> orderItems.getSkuCode())
                .collect(Collectors.toList());

        InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
                .uri("http://InventoryService/api/inventory/",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve().bodyToMono(InventoryResponse[].class)
                .block();

        boolean result  = Arrays.stream(inventoryResponsesArray).allMatch(inventoryResponse -> inventoryResponse.isInStock());

        if(result){
            orderRepo.save(orders);
        }else{
            throw new IllegalArgumentException("Sorry! One or more product in your order is Out of Stock.");
        }

    }

    private OrderItems mapToDto(OrderItemsDto orderItemsDto) {
        OrderItems orderItems = new OrderItems();
        orderItems.setQuantity(orderItemsDto.getQuantity());
        orderItems.setPrice(orderItemsDto.getPrice());
        orderItems.setSkuCode(orderItemsDto.getSkuCode());
        System.out.println(orderItems);
        return  orderItems;
    }

}
