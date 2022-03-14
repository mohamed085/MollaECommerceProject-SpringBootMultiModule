package com.molla.site.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import com.molla.site.util.CustomerRegisterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.molla.common.entity.Country;
import com.molla.common.entity.Customer;
import com.molla.site.service.CustomerService;
import com.molla.site.service.SettingService;

@Controller
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        LOGGER.info("CustomerController | showRegisterForm is called");

        List<Country> listCountries = customerService.listAllCountries();

        LOGGER.info("CustomerController | listCountries : " + listCountries.toString());

        model.addAttribute("listCountries", listCountries);
        model.addAttribute("pageTitle", "Customer Registration");
        model.addAttribute("customer", new Customer());

        return "register/register_form";
    }


    @PostMapping("/create_customer")
    public String createCustomer(Customer customer, Model model,
                                 HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {

        LOGGER.info("CustomerController | createCustomer is called");

        customerService.registerCustomer(customer);

        LOGGER.info("CustomerController | createCustomer | customer : " + customer);

        CustomerRegisterUtil.sendVerificationEmail(request, customer, settingService);

        model.addAttribute("pageTitle", "Registration Succeeded!");

        return "/register/register_success";
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String code, Model model) {

        LOGGER.info("CustomerController | verifyAccount is called");

        boolean verified = customerService.verify(code);

        LOGGER.info("CustomerController | verifyAccount | verified : " + verified);

        return "register/" + (verified ? "verify_success" : "verify_fail");
    }


}
