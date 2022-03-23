package com.molla.site.service;

import java.util.List;

import javax.transaction.Transactional;

import com.molla.site.util.CheckoutUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.molla.site.service.impl.ICheckoutService;
import com.molla.site.model.CheckoutInfo;
import com.molla.common.entity.CartItem;
import com.molla.common.entity.ShippingRate;

@Service
@Transactional
public class CheckoutService implements ICheckoutService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutService.class);

    private static final int DIM_DIVISOR = 139;

    @Override
    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {

        LOGGER.info("CheckoutService | prepareCheckout is called");

        // TODO Auto-generated method stub
        CheckoutInfo checkoutInfo = new CheckoutInfo();

        float productCost = CheckoutUtil.calculateProductCost(cartItems);
        float productTotal = CheckoutUtil.calculateProductTotal(cartItems);
        float shippingCostTotal = CheckoutUtil.calculateShippingCost(cartItems, shippingRate, DIM_DIVISOR);
        float paymentTotal = productTotal + shippingCostTotal;

        LOGGER.info("CustomerController | prepareCheckout | productCost : " + productCost);
        LOGGER.info("CustomerController | prepareCheckout | productTotal : " + productTotal);
        LOGGER.info("CustomerController | prepareCheckout | shippingCostTotal : " + shippingCostTotal);
        LOGGER.info("CustomerController | prepareCheckout | paymentTotal : " + paymentTotal);

        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setShippingCostTotal(shippingCostTotal);
        checkoutInfo.setPaymentTotal(paymentTotal);

        checkoutInfo.setDeliverDays(shippingRate.getDays());
        checkoutInfo.setCodSupported(shippingRate.isCodSupported());

        LOGGER.info("CustomerController | prepareCheckout | checkoutInfo : " + checkoutInfo.toString());

        return checkoutInfo;
    }
}
