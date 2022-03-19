package com.molla.admin.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.molla.admin.controller.OrderController;
import com.molla.admin.service.SettingService;
import com.molla.common.entity.Setting;

public class OrderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderUtil.class);

    public static void loadCurrencySetting(HttpServletRequest request, SettingService settingService) {

        LOGGER.info("OrderUtil | loadCurrencySetting is called");

        List<Setting> currencySettings = settingService.getCurrencySettings();

        LOGGER.info("OrderUtil | loadCurrencySetting | currencySettings : " + currencySettings.toString());

        for (Setting setting : currencySettings) {
            LOGGER.info("OrderUtil | loadCurrencySetting | setting | key : " + setting.getKey() + " , value : " + setting.getValue());
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }
}