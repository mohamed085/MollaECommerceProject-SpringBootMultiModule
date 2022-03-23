package com.molla.site.util;

import javax.servlet.http.HttpServletRequest;

import com.molla.common.entity.Customer;
import com.molla.site.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationControllerHelperUtil {

    private final CustomerService customerService;

    public AuthenticationControllerHelperUtil(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = CustomerAccountUtil.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }
}
