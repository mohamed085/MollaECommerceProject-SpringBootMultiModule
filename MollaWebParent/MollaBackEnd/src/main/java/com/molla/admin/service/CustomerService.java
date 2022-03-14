package com.molla.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.molla.admin.error.CustomerNotFoundException;
import com.molla.admin.paging.PagingAndSortingHelper;
import com.molla.admin.repository.CountryRepository;
import com.molla.admin.repository.CustomerRepository;
import com.molla.admin.service.impl.ICustomerService;
import com.molla.common.entity.Country;
import com.molla.common.entity.Customer;
import com.molla.common.entity.User;

@Service
@Transactional
public class CustomerService implements ICustomerService{

	public static final int CUSTOMERS_PER_PAGE = 10;

	private final CustomerRepository customerRepo;
	private final CountryRepository countryRepo;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired 
	public CustomerService(CustomerRepository customerRepo, CountryRepository countryRepo,
			PasswordEncoder passwordEncoder) {
		super();
		this.customerRepo = customerRepo;
		this.countryRepo = countryRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, CUSTOMERS_PER_PAGE, customerRepo);
	}

	@Override
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		// TODO Auto-generated method stub
		customerRepo.updateEnabledStatus(id, enabled);
	}

	@Override
	public List<Country> listAllCountries() {
		// TODO Auto-generated method stub
		return countryRepo.findAllByOrderByNameAsc();
	}

	@Override
	public boolean isEmailUnique(Integer id, String email) {
		// TODO Auto-generated method stub
		Customer existCustomer = customerRepo.findByEmail(email);

		if (existCustomer != null && existCustomer.getId() != id) {
			// found another customer having the same email
			return false;
		}

		return true;
	}

	@Override
	public void save(Customer customerInForm) {
		
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
		
		
		if (!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);			
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
	public void delete(Integer id) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		Long count = customerRepo.countById(id);
		if (count == null || count == 0) {
			throw new CustomerNotFoundException("Could not find any customers with ID " + id);
		}

		customerRepo.deleteById(id);
	}

	@Override
	public Customer get(Integer id) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Could not find any customers with ID " + id);
		}
	}
	
	@Override
	public List<Customer> listAll() {
		
		Sort firstNameSorting =  Sort.by("id").ascending();
		
		List<Customer> customerList = new ArrayList<>();
		customerRepo.findAll(firstNameSorting).forEach(customerList::add);
		return customerList;
	}

}
