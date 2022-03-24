package com.molla.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.molla.admin.dto.OrderResponseDTO;
import com.molla.admin.service.OrderService;

@RestController

public class OrderRestController {

    @Autowired
    private OrderService service;

    @GetMapping("/orders_shipper/update/{id}/{status}")
    public OrderResponseDTO updateOrderStatus(@PathVariable("id") Integer orderId,
                                              @PathVariable("status") String status) {
        service.updateStatus(orderId, status);
        return new OrderResponseDTO(orderId, status);
    }

}
