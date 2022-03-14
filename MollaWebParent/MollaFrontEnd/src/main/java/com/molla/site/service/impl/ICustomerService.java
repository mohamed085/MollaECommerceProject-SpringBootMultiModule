package com.molla.site.service.impl;

import com.molla.common.entity.AuthenticationType;
import com.molla.common.entity.Country;
import com.molla.common.entity.Customer;
import com.molla.common.exception.CustomerNotFoundException;

import java.util.List;

public interface ICustomerService {

    List<Country> listAllCountries();
    boolean isEmailUnique(String email);
    void registerCustomer(Customer customer);
    boolean verify(String verificationCode);
    Customer getCustomerByEmail(String email);
    void updateAuthenticationType(Customer customer, AuthenticationType type);
    void addNewCustomerUponOAuthLogin(String name, String email, String countryCode,
                                             AuthenticationType authenticationType);
    void update(Customer customerInForm);
    String updateResetPasswordToken(String email) throws CustomerNotFoundException;
    Customer getByResetPasswordToken(String token);
    void updatePassword(String token, String newPassword) throws CustomerNotFoundException;

}
