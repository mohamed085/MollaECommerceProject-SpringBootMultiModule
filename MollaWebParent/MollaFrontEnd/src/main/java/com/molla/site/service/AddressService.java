package com.molla.site.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.molla.common.entity.Address;
import com.molla.common.entity.Customer;
import com.molla.site.repository.AddressRepository;
import com.molla.site.service.impl.IAddressService;

@Service
@Transactional
public class AddressService implements IAddressService{

    @Autowired
    private AddressRepository repo;

    @Override
    public List<Address> listAddressBook(Customer customer) {
        // TODO Auto-generated method stub
        return repo.findByCustomer(customer);
    }

    @Override
    public void save(Address address) {
        repo.save(address);
    }

    @Override
    public Address get(Integer addressId, Integer customerId) {
        return repo.findByIdAndCustomer(addressId, customerId);
    }

    @Override
    public void delete(Integer addressId, Integer customerId) {
        repo.deleteByIdAndCustomer(addressId, customerId);
    }

    @Override
    public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
        if (defaultAddressId > 0) {
            repo.setDefaultAddress(defaultAddressId);
        }

        repo.setNonDefaultForOthers(defaultAddressId, customerId);
    }

}

