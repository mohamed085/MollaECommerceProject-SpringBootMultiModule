package com.molla.site.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.molla.common.entity.Address;
import com.molla.common.entity.ShippingRate;
import com.molla.site.service.AddressService;
import com.molla.site.service.ShippingRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.molla.common.entity.CartItem;
import com.molla.common.entity.Customer;
import com.molla.common.exception.CustomerNotFoundException;
import com.molla.site.service.CustomerService;
import com.molla.site.service.ShoppingCartService;
import com.molla.site.util.CustomerShoppingCartandAddressUtil;

@Controller
public class ShoppingCartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);

    private final CustomerService customerService;
    private final ShoppingCartService cartService;
    private final AddressService addressService;
    private final ShippingRateService shipService;

    public ShoppingCartController(CustomerService customerService, ShoppingCartService cartService, AddressService addressService, ShippingRateService shippingRateService) {
        super();
        this.customerService = customerService;
        this.cartService = cartService;
        this.addressService = addressService;
        this.shipService = shippingRateService;
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) throws CustomerNotFoundException {

        LOGGER.info("ShoppingCartController | viewCart is called");

        Customer customer = CustomerShoppingCartandAddressUtil.getAuthenticatedCustomer(request,customerService);
        List<CartItem> cartItems = cartService.listCartItems(customer);

        LOGGER.info("ShoppingCartController | viewCart | customer : " + customer.toString());
        LOGGER.info("ShoppingCartController | viewCart | cartItems : " + cartItems.size());

        float estimatedTotal = 0.0F;

        for (CartItem item : cartItems) {
            LOGGER.info("ShoppingCartController | viewCart | item.getSubtotal() : " + item.getSubtotal());
            estimatedTotal += item.getSubtotal();
        }

        Address defaultAddress = addressService.getDefaultAddress(customer);

        ShippingRate shippingRate = null;
        boolean usePrimaryAddressAsDefault = false;

        if (defaultAddress != null) {
            shippingRate = shipService.getShippingRateForAddress(defaultAddress);
        } else {
            usePrimaryAddressAsDefault = true;
            shippingRate = shipService.getShippingRateForCustomer(customer);
        }

        LOGGER.info("ShoppingCartController | viewCart | usePrimaryAddressAsDefault : " + usePrimaryAddressAsDefault);
        LOGGER.info("ShoppingCartController | viewCart | shippingSupported : " + (shippingRate != null));

        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
        model.addAttribute("shippingSupported", shippingRate != null);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("estimatedTotal", estimatedTotal);

        LOGGER.info("ShoppingCartController | viewCart | estimatedTotal : " + estimatedTotal);

        return "cart/shopping_cart";
    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable("productId") Integer productId,
                                 @PathVariable("quantity") Integer quantity, HttpServletRequest request) {

        LOGGER.info("ShoppingCartController | updateQuantity is called");

        try {
            Customer customer = CustomerShoppingCartandAddressUtil.getAuthenticatedCustomer(request,customerService);

            LOGGER.info("ShoppingCartController | updateQuantity | customer : " + customer.toString());

            float subtotal = cartService.updateQuantity(productId, quantity, customer);

            LOGGER.info("ShoppingCartController | updateQuantity | subtotal : " + subtotal);

            return String.valueOf(subtotal);
        } catch (CustomerNotFoundException ex) {
            return "You must login to change quantity of product.";
        }
    }

}