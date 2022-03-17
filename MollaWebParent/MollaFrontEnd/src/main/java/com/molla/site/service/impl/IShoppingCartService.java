package com.molla.site.service.impl;

import java.util.List;

import com.molla.common.entity.CartItem;
import com.molla.common.entity.Customer;
import com.molla.common.exception.ShoppingCartException;

public interface IShoppingCartService {

    public Integer addProduct(Integer productId, Integer quantity, Customer customer)
            throws ShoppingCartException;

    public List<CartItem> listCartItems(Customer customer);

    public float updateQuantity(Integer productId, Integer quantity, Customer customer);

    public void removeProduct(Integer productId, Customer customer);
}