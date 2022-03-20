package com.molla.site.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.molla.common.entity.Customer;

public class CustomerUserDetails implements UserDetails {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerUserDetails.class);

    private static final long serialVersionUID = 1L;

    private Customer customer;

    public CustomerUserDetails(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return customer.isEnabled();
    }

    public String getFullName() {
        return customer.getFirstName() + " " + customer.getLastName();
    }

    public Customer getCustomer() {
        return this.customer;
    }

}
