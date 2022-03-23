package com.molla.site.repository;

import org.springframework.data.repository.CrudRepository;

import com.molla.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

}
