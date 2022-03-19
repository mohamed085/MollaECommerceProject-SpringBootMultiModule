package com.molla.site.service;

import java.util.List;

import javax.transaction.Transactional;

import com.molla.site.model.CurrencySettingBag;
import com.molla.site.model.PaymentSettingBag;
import com.molla.site.repository.SettingRepository;
import com.molla.site.service.impl.ISettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.molla.common.entity.EmailSettingBag;
import com.molla.common.entity.Setting;
import com.molla.common.entity.SettingCategory;

@Service
@Transactional
public class SettingService implements ISettingService {

    @Autowired
    private SettingRepository repo;

    @Override
    public List<Setting> getGeneralSettings() {
        // TODO Auto-generated method stub
        return repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    @Override
    public EmailSettingBag getEmailSettings() {
        List<Setting> settings = repo.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingBag(settings);
    }

    @Override
    public CurrencySettingBag getCurrencySettings() {
        return null;
    }

    @Override
    public String getCurrencyCode() {
        return null;
    }

    @Override
    public PaymentSettingBag getPaymentSettings() {
        return null;
    }

}
