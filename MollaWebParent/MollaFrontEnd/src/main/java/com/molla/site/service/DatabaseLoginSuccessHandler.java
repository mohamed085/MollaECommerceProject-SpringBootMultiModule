package com.molla.site.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.molla.site.model.CustomerUserDetails;
import com.molla.site.repository.CountryRepository;
import com.molla.site.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.molla.common.entity.AuthenticationType;
import com.molla.common.entity.Customer;
import com.molla.site.service.CustomerService;

@Component
@Transactional
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseLoginSuccessHandler.class);

    @Autowired
    private CustomerRepository customerRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        Customer customer = userDetails.getCustomer();

        LOGGER.info("DatabaseLoginSuccessHandler | onAuthenticationSuccess |  customer : " + customer.toString());

        updateAuthenticationType(customer, AuthenticationType.DATABASE);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    void updateAuthenticationType(Customer customer, AuthenticationType type) {
        // TODO Auto-generated method stub

        LOGGER.info("CustomerService | updateAuthenticationType |  customer : " + customer.toString());
        LOGGER.info("CustomerService | updateAuthenticationType |  type : " + type);

        if (customer.getAuthenticationType() == null || !customer.getAuthenticationType().equals(type)) {
            customerRepo.updateAuthenticationType(customer.getId(), type);
            LOGGER.info("CustomerService | updateAuthenticationType |  AuthenticationType updated");
        }
    }
}
