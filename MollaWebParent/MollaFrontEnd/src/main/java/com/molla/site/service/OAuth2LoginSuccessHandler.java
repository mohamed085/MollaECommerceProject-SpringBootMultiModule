package com.molla.site.service;

import com.molla.common.entity.AuthenticationType;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.molla.common.entity.Customer;
import com.molla.site.model.CustomerOAuth2User;
import com.molla.site.repository.CountryRepository;
import com.molla.site.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();

        String name = oauth2User.getName();
        String email = oauth2User.getEmail();
        String countryCode = request.getLocale().getCountry();
        String clientName = oauth2User.getClientName();

        LOGGER.info("OAuth2LoginSuccessHandler | onAuthenticationSuccess |  name : " + name);
        LOGGER.info("OAuth2LoginSuccessHandler | onAuthenticationSuccess |  email : " + email);
        LOGGER.info("OAuth2LoginSuccessHandler | onAuthenticationSuccess |  countryCode : " + countryCode);
        LOGGER.info("OAuth2LoginSuccessHandler | onAuthenticationSuccess |  clientName : " + clientName);

        AuthenticationType authenticationType = getAuthenticationType(clientName);

        Customer customer = customerRepo.findByEmail(email);
        if (customer == null) { addNewCustomerUponOAuthLogin(name, email, countryCode, authenticationType);
        } else {
            oauth2User.setFullName(customer.getFullName());
            updateAuthenticationType(customer, authenticationType);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthenticationType(String clientName) {
        if (clientName.equals("Google")) {
            return AuthenticationType.GOOGLE;
        } else if (clientName.equals("Facebook")) {
            return AuthenticationType.FACEBOOK;
        } else {
            return AuthenticationType.DATABASE;
        }
    }

    private void addNewCustomerUponOAuthLogin(String name, String email, String countryCode,
                                             AuthenticationType authenticationType) {
        Customer customer = new Customer();
        customer.setEmail(email);
        setName(name, customer);

        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(authenticationType);
        customer.setPassword("");
        customer.setAddressLine1("");
        customer.setCity("");
        customer.setState("");
        customer.setPhoneNumber("");
        customer.setPostalCode("");
        customer.setCountry(countryRepo.findByCode(countryCode));

        customerRepo.save(customer);
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

    void setName(String name, Customer customer) {
        String[] nameArray = name.split(" ");
        if (nameArray.length < 2) {
            customer.setFirstName(name);
            customer.setLastName("");
        } else {
            String firstName = nameArray[0];
            customer.setFirstName(firstName);

            String lastName = name.replaceFirst(firstName + " ", "");
            customer.setLastName(lastName);
        }
    }
}
