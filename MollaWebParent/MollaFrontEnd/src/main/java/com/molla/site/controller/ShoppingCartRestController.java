package com.molla.site.controller;

import javax.servlet.http.HttpServletRequest;

import com.molla.common.entity.ShippingRate;
import com.molla.site.service.AddressService;
import com.molla.site.service.ShippingRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.molla.common.entity.Customer;
import com.molla.common.exception.CustomerNotFoundException;
import com.molla.common.exception.ShoppingCartException;
import com.molla.site.service.CustomerService;
import com.molla.site.service.ShoppingCartService;
import com.molla.site.util.CustomerShoppingCartandAddressUtil;

@RestController
public class ShoppingCartRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartRestController.class);

    private ShoppingCartService cartService;
    private CustomerService customerService;


    @Autowired
    public ShoppingCartRestController(ShoppingCartService cartService, CustomerService customerService) {
        super();
        this.cartService = cartService;
        this.customerService = customerService;
    }


    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable("productId") Integer productId,
                                   @PathVariable("quantity") Integer quantity, HttpServletRequest request) {

        LOGGER.info("ShoppingCartRestController | addProductToCart is called");

        try {

            Customer customer = CustomerShoppingCartandAddressUtil.getAuthenticatedCustomer(request, customerService);

            LOGGER.info("ShoppingCartRestController | addProductToCart | customer : " + customer.toString());

            Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);

            LOGGER.info("ShoppingCartRestController | addProductToCart | updatedQuantity : " + updatedQuantity);

            return updatedQuantity + " item(s) of this product were added to your shopping cart.";
        } catch (CustomerNotFoundException ex) {
            return "You must login to add this product to cart.";
        } catch (ShoppingCartException ex) {
            return ex.getMessage();
        }

    }


    @DeleteMapping("/cart/remove/{productId}")
    public String removeProduct(@PathVariable("productId") Integer productId,
                                HttpServletRequest request) {

        LOGGER.info("ShoppingCartRestController | removeProduct is called");

        try {

            Customer customer = CustomerShoppingCartandAddressUtil.getAuthenticatedCustomer(request, customerService);

            LOGGER.info("ShoppingCartRestController | removeProduct | customer : " + customer.toString());

            cartService.removeProduct(productId, customer);

            return "The product has been removed from your shopping cart.";

        } catch (CustomerNotFoundException e) {
            return "You must login to remove product.";
        }
    }
}

