package com.molla.admin.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import com.molla.admin.error.CustomerNotFoundException;
import com.molla.admin.paging.PagingAndSortingHelper;
import com.molla.common.entity.Country;
import com.molla.common.entity.Customer;

public interface ICustomerService {

	public void listByPage(int pageNum, PagingAndSortingHelper helper);
	public void updateCustomerEnabledStatus(Integer id, boolean enabled);
	public List<Country> listAllCountries();
	public boolean isEmailUnique(Integer id, String email);
	public void save(Customer customerInForm);
	public void delete(Integer id) throws CustomerNotFoundException;
	public Customer get(Integer id) throws CustomerNotFoundException;
	List<Customer> listAll();
}
