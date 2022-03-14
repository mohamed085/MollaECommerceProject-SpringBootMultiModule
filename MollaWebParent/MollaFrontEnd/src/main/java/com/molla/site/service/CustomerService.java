package com.molla.site.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.molla.site.util.CustomerRegisterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.molla.common.entity.AuthenticationType;
import com.molla.common.entity.Country;
import com.molla.common.entity.Customer;
import com.molla.common.exception.CustomerNotFoundException;
import com.molla.site.repository.CountryRepository;
import com.molla.site.repository.CustomerRepository;
import com.molla.site.service.impl.ICustomerService;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService implements ICustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Country> listAllCountries() {
        return countryRepo.findAllByOrderByNameAsc();
    }

    @Override
    public boolean isEmailUnique(String email) {
        Customer customer = customerRepo.findByEmail(email);
        return customer == null;
    }

    @Override
    public void registerCustomer(Customer customer) {
        CustomerRegisterUtil.encodePassword(customer, passwordEncoder);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);
        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);

        customerRepo.save(customer);
    }

    @Override
    public boolean verify(String verificationCode) {
        Customer customer = customerRepo.findByVerificationCode(verificationCode);

        if (customer == null || customer.isEnabled()) {
            return false;
        } else {
            customerRepo.enable(customer.getId());
            return true;
        }
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepo.findByEmail(email);
    }

    @Override
    public void updateAuthenticationType(Customer customer, AuthenticationType type) {

    }

    @Override
    public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode, AuthenticationType authenticationType) {

    }

    @Override
    public void update(Customer customerInForm) {

    }

    @Override
    public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
        return null;
    }

    @Override
    public Customer getByResetPasswordToken(String token) {
        return null;
    }

    @Override
    public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {

    }
}
