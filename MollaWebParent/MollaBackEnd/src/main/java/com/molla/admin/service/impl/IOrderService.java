package com.molla.admin.service.impl;

import com.molla.admin.error.OrderNotFoundException;
import com.molla.admin.paging.PagingAndSortingHelper;
import com.molla.common.entity.Order;

public interface IOrderService {

    public void listByPage(int pageNum, PagingAndSortingHelper helper);

    public Order get(Integer id) throws OrderNotFoundException;

    public void delete(Integer id) throws OrderNotFoundException;
}
