package com.molla.site.service.impl;

import org.springframework.data.domain.Page;

import com.molla.common.entity.Customer;
import com.molla.common.entity.Review;
import com.molla.common.exception.ReviewNotFoundException;

public interface IReviewService {

    public Page<Review> listByCustomerByPage(Customer customer, String keyword, int pageNum,
                                             String sortField, String sortDir);

    public Review getByCustomerAndId(Customer customer, Integer reviewId) throws ReviewNotFoundException;
}
