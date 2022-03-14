package com.molla.site.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.molla.common.entity.Country;
import com.molla.common.entity.State;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends CrudRepository<State, Integer> {

    public List<State> findByCountryOrderByNameAsc(Country country);
}
