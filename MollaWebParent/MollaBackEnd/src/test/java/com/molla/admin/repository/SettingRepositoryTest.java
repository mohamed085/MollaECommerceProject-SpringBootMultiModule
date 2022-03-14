package com.molla.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.molla.admin.repository.SettingRepository;
import com.molla.common.entity.Setting;
import com.molla.common.entity.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class SettingRepositoryTest {
    @Autowired
    SettingRepository repo;

    @Test
    public void testCreateGeneralSettings() {
        Setting siteName = new Setting("SITE_NAME", "Molla", SettingCategory.GENERAL);
        Setting siteLogo = new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL);
        Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2021 Molla Ltd.", SettingCategory.GENERAL);

        repo.saveAll(List.of(siteName, siteLogo, copyright));

        Iterable<Setting> iterable = repo.findAll();

        assertThat(iterable).size().isGreaterThan(0);
    }

    @Test
    public void testCreateCurrencySettings() {
        Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
        Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
        Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
        Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
        Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
        Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);

        repo.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType,
                decimalDigits, thousandsPointType));

    }

    @Test
    public void testCreateEmailSettings() {
        List<Setting> settings = Arrays.asList(
                new Setting("MAIL_HOST", "smtp.gmail.com", SettingCategory.MAIL_SERVER),
                new Setting("MAIL_PORT", "587", SettingCategory.MAIL_SERVER),
                new Setting("MAIL_USERNAME", "Mohamed085555@gmail.com", SettingCategory.MAIL_SERVER),
                new Setting("MAIL_PASSWORD", "", SettingCategory.MAIL_SERVER),
                new Setting("SMTP_AUTH", "true", SettingCategory.MAIL_SERVER),
                new Setting("SMTP_SECURED", "true", SettingCategory.MAIL_SERVER),
                new Setting("MAIL_FROM", "Molla.company@gmail.com", SettingCategory.MAIL_SERVER),
                new Setting("MAIL_SENDER_NAME", "Molla Company", SettingCategory.MAIL_SERVER),
                new Setting("CUSTOMER_VERIFY_SUBJECT", "", SettingCategory.MAIL_TEMPLATES),
                new Setting("CUSTOMER_VERIFY_CONTENT", "", SettingCategory.MAIL_TEMPLATES)
        );

        repo.saveAll(settings);
    }

    @Test
    public void testListSettingsByCategory() {
        List<Setting> settings = repo.findByCategory(SettingCategory.GENERAL);

        settings.forEach(System.out::println);
    }

}