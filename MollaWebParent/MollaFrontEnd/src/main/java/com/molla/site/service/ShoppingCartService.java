package com.molla.site.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.molla.common.entity.CartItem;
import com.molla.common.entity.Customer;
import com.molla.common.entity.Product;
import com.molla.common.exception.ShoppingCartException;
import com.molla.site.repository.CartItemRepository;
import com.molla.site.repository.ProductRepository;
import com.molla.site.service.impl.IShoppingCartService;

@Service
@Transactional
public class ShoppingCartService implements IShoppingCartService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartService.class);

    private CartItemRepository cartRepo;

    private ProductRepository productRepo;


    @Autowired
    public ShoppingCartService(CartItemRepository cartRepo, ProductRepository productRepo) {
        super();
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    @Override
    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
        // TODO Auto-generated method stub

        LOGGER.info("ShoppingCartService | addProduct is called");

        Integer updatedQuantity = quantity;

        LOGGER.info("ShoppingCartService | addProduct | updatedQuantity(at first) : " + updatedQuantity);

        Product product = new Product(productId);

        LOGGER.info("ShoppingCartService | addProduct | product : " + product.toString());

        CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);

        if (cartItem != null) {

            LOGGER.info("ShoppingCartService | addProduct | cartItem : " + cartItem.toString());


            updatedQuantity = cartItem.getQuantity() + quantity;

            LOGGER.info("ShoppingCartService | addProduct | updatedQuantity(updated) : " + updatedQuantity);

            if (updatedQuantity > 5) {

                LOGGER.info("ShoppingCartService | addProduct | updatedQuantity > 5 : " + (updatedQuantity > 5));

                throw new ShoppingCartException("Could not add more " + quantity + " item(s)"
                        + " because there's already " + cartItem.getQuantity() + " item(s) "
                        + "in your shopping cart. Maximum allowed quantity is 5.");
            }
        } else {
            LOGGER.info("ShoppingCartService | addProduct | cartItem != null : " + (cartItem != null));

            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }

        LOGGER.info("ShoppingCartService | addProduct | updatedQuantity(at last) : " + updatedQuantity);

        cartItem.setQuantity(updatedQuantity);

        LOGGER.info("ShoppingCartService | addProduct | cartItem : " + cartItem.toString());

        cartRepo.save(cartItem);

        return updatedQuantity;
    }

    @Override
    public List<CartItem> listCartItems(Customer customer) {
        // TODO Auto-generated method stub

        LOGGER.info("ShoppingCartService | listCartItems is called");

        LOGGER.info("ShoppingCartService | listCartItems | listCartItems size : " + cartRepo.findByCustomer(customer).size());

        return cartRepo.findByCustomer(customer);
    }

    @Override
    public float updateQuantity(Integer productId, Integer quantity, Customer customer) {

        LOGGER.info("ShoppingCartService | updateQuantity is called");

        cartRepo.updateQuantity(quantity, customer.getId(), productId);

        Product product = productRepo.findById(productId).get();

        LOGGER.info("ShoppingCartService | updateQuantity | product : " + product.toString());

        float subtotal = product.getDiscountPrice() * quantity;

        LOGGER.info("ShoppingCartService | updateQuantity | subtotal : " + subtotal);

        return subtotal;

    }

    @Override
    public void removeProduct(Integer productId, Customer customer) {
        // TODO Auto-generated method stub

        LOGGER.info("ShoppingCartService | removeProduct is called");

        cartRepo.deleteByCustomerAndProduct(customer.getId(), productId);
    }

    public void deleteByCustomer(Customer customer) {
        cartRepo.deleteByCustomer(customer.getId());
    }

}
