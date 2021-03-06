package com.molla.site.controller;

import com.molla.common.entity.*;
import com.molla.common.exception.CustomerNotFoundException;
import com.molla.common.exception.PayPalApiException;
import com.molla.site.model.CheckoutInfo;
import com.molla.site.model.PaymentSettingBag;
import com.molla.site.service.*;
import com.molla.site.util.AuthenticationControllerHelperUtil;
import com.molla.site.util.OrderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CheckoutController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutController.class);

    private final CheckoutService checkoutService;
    private final AddressService addressService;
    private final ShippingRateService shipService;
    private final ShoppingCartService cartService;
    private final SettingService settingService;
    private final OrderService orderService;
    private final PayPalService paypalService;
    private final AuthenticationControllerHelperUtil authenticationControllerHelperUtil;

    public CheckoutController(CheckoutService checkoutService, AddressService addressService,
                              ShippingRateService shipService, ShoppingCartService cartService,
                              SettingService settingService, OrderService orderService,
                              PayPalService paypalService,
                              AuthenticationControllerHelperUtil authenticationControllerHelperUtil) {
        this.checkoutService = checkoutService;
        this.addressService = addressService;
        this.shipService = shipService;
        this.cartService = cartService;
        this.settingService = settingService;
        this.orderService = orderService;
        this.paypalService = paypalService;
        this.authenticationControllerHelperUtil = authenticationControllerHelperUtil;
    }


    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest request) throws CustomerNotFoundException {

        LOGGER.info("CheckoutController | showCheckoutPage is called");

        Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);

        LOGGER.info("CheckoutController | showCheckoutPage | customer : " + customer.toString());

        Address defaultAddress = addressService.getDefaultAddress(customer);

        ShippingRate shippingRate = null;

        if (defaultAddress != null) {
            model.addAttribute("shippingAddress", defaultAddress.toString());
            shippingRate = shipService.getShippingRateForAddress(defaultAddress);

            LOGGER.info("CheckoutController | showCheckoutPage | defaultAddress != null | shippingRate " + shippingRate.toString());

        } else {
            model.addAttribute("shippingAddress", customer.toString());
            shippingRate = shipService.getShippingRateForCustomer(customer);

            LOGGER.info("CheckoutController | showCheckoutPage | defaultAddress == null | shippingRate " + shippingRate.toString());
        }

        LOGGER.info("CheckoutController | showCheckoutPage | shippingRate " + shippingRate.toString());

        if (shippingRate == null) {
            LOGGER.info("CheckoutController | showCheckoutPage | \"redirect:/cart\" ");
            return "redirect:/cart";
        }

        List<CartItem> cartItems = cartService.listCartItems(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

        // Paypal
        String currencyCode = settingService.getCurrencyCode();
        PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
        String paypalClientId = paymentSettings.getClientID();

        LOGGER.info("CheckoutController | showCheckoutPage | paypalClientId " + paypalClientId);
        LOGGER.info("CheckoutController | showCheckoutPage | currencyCode " + currencyCode);

        model.addAttribute("paypalClientId", paypalClientId);
        model.addAttribute("currencyCode", currencyCode);
        model.addAttribute("customer", customer);

        LOGGER.info("CheckoutController | showCheckoutPage | cartItems " + cartItems.toString());
        LOGGER.info("CheckoutController | showCheckoutPage | checkoutInfo " + checkoutInfo.toString());

        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);

        return "checkout/checkout";
    }

    @PostMapping("/place_order")
    public String placeOrder(HttpServletRequest request) throws CustomerNotFoundException {

        LOGGER.info("CheckoutController | placeOrder is called");

        String paymentType = request.getParameter("paymentMethod");

        LOGGER.info("CheckoutController | placeOrder | paymentType :  " + paymentType);

        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

        LOGGER.info("CheckoutController | placeOrder | paymentMethod :  " + paymentMethod.toString());

        Customer customer = authenticationControllerHelperUtil.getAuthenticatedCustomer(request);

        LOGGER.info("CheckoutController | placeOrder | customer :  " + customer.toString());

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;

        LOGGER.info("CheckoutController | placeOrder | defaultAddress != null :  " + (defaultAddress != null));

        if (defaultAddress != null) {
            shippingRate = shipService.getShippingRateForAddress(defaultAddress);

            LOGGER.info("CheckoutController | placeOrder | shippingRate :  " + shippingRate.toString());

        } else {
            shippingRate = shipService.getShippingRateForCustomer(customer);

            LOGGER.info("CheckoutController | placeOrder | shippingRate :  " + shippingRate.toString());
        }

        List<CartItem> cartItems = cartService.listCartItems(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

        LOGGER.info("CheckoutController | placeOrder | cartItems " + cartItems.toString());
        LOGGER.info("CheckoutController | placeOrder | checkoutInfo " + checkoutInfo.toString());

        Order createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);

        cartService.deleteByCustomer(customer);

        try {
            OrderUtil.sendOrderConfirmationEmail(request, createdOrder, settingService);
        } catch (UnsupportedEncodingException | MessagingException e) {
            // TODO Auto-generated catch block
            LOGGER.info("CheckoutController | placeOrder | OrderUtil.sendOrderConfirmationEmail failed");
            e.printStackTrace();
        }

        return "checkout/order_completed";
    }

    @PostMapping("/process_paypal_order")
    public String processPayPalOrder(HttpServletRequest request, Model model)
            throws UnsupportedEncodingException, MessagingException, CustomerNotFoundException {

        LOGGER.info("CheckoutController | processPayPalOrder is called");

        String orderId = request.getParameter("orderId");

        LOGGER.info("CheckoutController | processPayPalOrder | orderId :  " + orderId);

        String pageTitle = "Checkout Failure";
        String message = null;

        LOGGER.info("CheckoutController | processPayPalOrder | pageTitle :  " + pageTitle);

        try {
            if (paypalService.validateOrder(orderId)) {
                LOGGER.info("CheckoutController | processPayPalOrder | paypalService.validateOrder(orderId) :  " + (paypalService.validateOrder(orderId)));
                return placeOrder(request);
            } else {
                pageTitle = "Checkout Failure";
                message = "ERROR: Transaction could not be completed because order information is invalid";

                LOGGER.info("CheckoutController | processPayPalOrder | pageTitle :  " + pageTitle);
                LOGGER.info("CheckoutController | processPayPalOrder | message :  " + message);

            }
        } catch (PayPalApiException e) {
            message = "ERROR: Transaction failed due to error: " + e.getMessage();
            LOGGER.info("CheckoutController | processPayPalOrder | message :  " + e.getMessage());
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("title", pageTitle);
        model.addAttribute("message", message);

        LOGGER.info("CheckoutController | processPayPalOrder | pageTitle :  " + pageTitle);
        LOGGER.info("CheckoutController | processPayPalOrder | title :  " + pageTitle);
        LOGGER.info("CheckoutController | processPayPalOrder | message :  " + message);

        return "message";
    }
}
