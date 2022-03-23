package com.molla.site.service.impl;

import com.molla.common.entity.EmailSettingBag;
import com.molla.common.entity.Setting;
import com.molla.site.model.CurrencySettingBag;
import com.molla.site.model.PaymentSettingBag;

import java.util.List;

public interface ISettingService {

    public List<Setting> getGeneralSettings();
    public EmailSettingBag getEmailSettings();
    public CurrencySettingBag getCurrencySettings();
    public String getCurrencyCode();
    public PaymentSettingBag getPaymentSettings();

}
