package com.molla.admin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.molla.common.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
