package com.molla.admin.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.molla.common.entity.Setting;
import com.molla.common.entity.SettingCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends CrudRepository<Setting, String> {

	public List<Setting> findByCategory(SettingCategory category);
}
