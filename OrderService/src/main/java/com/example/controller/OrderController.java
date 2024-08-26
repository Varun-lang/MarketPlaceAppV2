package com.example.controller;

import com.example.dto.OrderRequest;

import com.example.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory",fallbackMethod = "failureMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.createOrder(orderRequest);
        return "Your Order is placed.";
    }

    public String failureMethod(OrderRequest orderRequest,RuntimeException runtimeException){
        return "404! I am down";
    }

}
