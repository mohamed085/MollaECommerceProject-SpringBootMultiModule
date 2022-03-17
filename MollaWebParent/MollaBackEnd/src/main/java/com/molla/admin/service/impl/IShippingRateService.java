package com.molla.admin.service.impl;

import java.util.List;

import com.molla.admin.paging.PagingAndSortingHelper;
import com.molla.common.entity.Country;
import com.molla.common.entity.ShippingRate;
import com.molla.common.exception.ShippingRateAlreadyExistsException;
import com.molla.common.exception.ShippingRateNotFoundException;

public interface IShippingRateService {

    public void listByPage(int pageNum, PagingAndSortingHelper helper);
    public List<Country> listAllCountries();
    public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException;
    public ShippingRate get(Integer id) throws ShippingRateNotFoundException;
    public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException;
    public void delete(Integer id) throws ShippingRateNotFoundException;

}

