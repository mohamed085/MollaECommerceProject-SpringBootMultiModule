package com.molla.admin.service.impl;

import java.util.List;

import com.molla.admin.util.GeneralSettingBag;
import com.molla.common.entity.Setting;

public interface ISettingService {

	public List<Setting> listAllSettings();
	
	public GeneralSettingBag getGeneralSettings();
	
	public void saveAll(Iterable<Setting> settings);
	
	public List<Setting> getMailServerSettings();
	
	public List<Setting> getMailTemplateSettings();

	public List<Setting> getCurrencySettings();
}
