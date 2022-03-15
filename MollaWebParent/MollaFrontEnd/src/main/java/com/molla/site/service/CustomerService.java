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

    public void update(Customer customerInForm) {
        Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();

        if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
            if (!customerInForm.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
                customerInForm.setPassword(encodedPassword);
            } else {
                customerInForm.setPassword(customerInDB.getPassword());
            }
        } else {
            customerInForm.setPassword(customerInDB.getPassword());
        }

        customerInForm.setEnabled(customerInDB.isEnabled());
        customerInForm.setCreatedTime(customerInDB.getCreatedTime());
        customerInForm.setVerificationCode(customerInDB.getVerificationCode());
        customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
        customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

        customerRepo.save(customerInForm);
    }

    @Override
    public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
        // TODO Auto-generated method stub
        Customer customer = customerRepo.findByEmail(email);
        if (customer != null) {
            String token = RandomString.make(30);
            customer.setResetPasswordToken(token);
            customerRepo.save(customer);

            return token;
        } else {
            throw new CustomerNotFoundException("Could not find any customer with the email " + email);
        }
    }

    @Override
    public Customer getByResetPasswordToken(String token) {
        // TODO Auto-generated method stub
        return customerRepo.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
        // TODO Auto-generated method stub
        Customer customer = customerRepo.findByResetPasswordToken(token);
        if (customer == null) {
            throw new CustomerNotFoundException("No customer found: invalid token");
        }

        customer.setPassword(newPassword);
        customer.setResetPasswordToken(null);
        CustomerRegisterUtil.encodePassword(customer, passwordEncoder);

        customerRepo.save(customer);
    }
}
