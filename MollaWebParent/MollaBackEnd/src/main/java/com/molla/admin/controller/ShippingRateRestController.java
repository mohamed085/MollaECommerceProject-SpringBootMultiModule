package com.molla.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.molla.admin.service.ShippingRateService;
import com.molla.common.exception.ShippingRateNotFoundException;

@RestController
public class ShippingRateRestController {

    @Autowired
    private ShippingRateService service;


    @PostMapping("/get_shipping_cost")
    public String getShippingCost(Integer productId, Integer countryId, String state)
            throws ShippingRateNotFoundException {
        float shippingCost = service.calculateShippingCost(productId, countryId, state);
        return String.valueOf(shippingCost);
    }
}
