package com.molla.admin.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;

import com.molla.admin.paging.PagingAndSortingHelper;
import com.molla.common.entity.Product;
import com.molla.common.exception.ProductNotFoundException;

public interface IProductService {

	public List<Product> listAll();

	public Product save(Product product);

	public String checkUnique(Integer id, String name);

	public void updateProductEnabledStatus(Integer id, boolean enabled);

	public void delete(Integer id) throws ProductNotFoundException;

	public Product get(Integer id) throws ProductNotFoundException;

	public void listByPage(int pageNum, PagingAndSortingHelper helper, Integer categoryId);

	public void saveProductPrice(Product productInForm);
	
}
