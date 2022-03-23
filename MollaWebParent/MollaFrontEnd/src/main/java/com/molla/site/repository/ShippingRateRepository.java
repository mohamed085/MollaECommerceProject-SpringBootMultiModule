package com.molla.site.repository;

import org.springframework.data.repository.CrudRepository;

import com.molla.common.entity.Country;
import com.molla.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

    public ShippingRate findByCountryAndState(Country country, String state);

}