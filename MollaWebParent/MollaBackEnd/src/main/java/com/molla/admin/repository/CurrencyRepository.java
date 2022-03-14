package com.molla.admin.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.molla.common.entity.Currency;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

	public List<Currency> findAllByOrderByNameAsc();
}