package com.molla.site.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.molla.common.entity.Customer;
import com.molla.common.exception.CustomerNotFoundException;
import com.molla.site.service.CustomerService;

public class CustomerShoppingCartandAddressUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerShoppingCartandAddressUtil.class);

    public static Customer getAuthenticatedCustomer(HttpServletRequest request, CustomerService customerService)
            throws CustomerNotFoundException {

        LOGGER.info("CustomerShoppingCartandAddressUtil | getAuthenticatedCustomer is called");

        String email = CustomerAccountUtil.getEmailOfAuthenticatedCustomer(request);

        LOGGER.info("CustomerShoppingCartandAddressUtil | getAuthenticatedCustomer | email : " + email);

        if (email == null) {
            throw new CustomerNotFoundException("No authenticated customer");
        }

        LOGGER.info("CustomerShoppingCartandAddressUtil | getAuthenticatedCustomer | customerService.getCustomerByEmail(email) : " + customerService.getCustomerByEmail(email).toString());

        return customerService.getCustomerByEmail(email);
    }

}

