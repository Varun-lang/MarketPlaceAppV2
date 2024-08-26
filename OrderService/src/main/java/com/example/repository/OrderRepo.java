package com.example.repository;

import com.example.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepo extends JpaRepository<Orders,Long> {
}
