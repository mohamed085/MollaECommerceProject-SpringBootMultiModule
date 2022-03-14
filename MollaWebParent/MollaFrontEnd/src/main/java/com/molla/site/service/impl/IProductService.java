package com.molla.site.service.impl;

import org.springframework.data.domain.Page;

import com.molla.common.entity.Product;
import com.molla.common.exception.ProductNotFoundException;

public interface IProductService {

    public Page<Product> listByCategory(int pageNum, Integer categoryId);

    public Product getProduct(String alias) throws ProductNotFoundException;

    public Page<Product> search(String keyword, int pageNum);
}
