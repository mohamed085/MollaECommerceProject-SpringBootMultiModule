package com.molla.site.service.impl;

import com.molla.site.model.CheckoutInfo;
import com.molla.common.entity.CartItem;
import com.molla.common.entity.ShippingRate;

import java.util.List;

public interface ICheckoutService {

    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate);

}
