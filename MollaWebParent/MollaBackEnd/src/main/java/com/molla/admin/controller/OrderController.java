package com.molla.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.molla.admin.error.OrderNotFoundException;
import com.molla.admin.paging.PagingAndSortingHelper;
import com.molla.admin.paging.PagingAndSortingParam;
import com.molla.admin.service.OrderService;
import com.molla.admin.service.SettingService;
import com.molla.admin.util.OrderUtil;
import com.molla.common.entity.Order;

@Controller
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

    private OrderService orderService;

    private SettingService settingService;

    @Autowired
    public OrderController(OrderService orderService, SettingService settingService) {
        super();
        this.orderService = orderService;
        this.settingService = settingService;
    }

    @GetMapping("/orders")
    public String listFirstPage() {

        LOGGER.info("OrderController | listFirstPage is called");

        return defaultRedirectURL;
    }

    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(
            @PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders") PagingAndSortingHelper helper,
            @PathVariable(name = "pageNum") int pageNum,
            HttpServletRequest request) {

        LOGGER.info("OrderController | listByPage is called");

        orderService.listByPage(pageNum, helper);
        OrderUtil.loadCurrencySetting(request, settingService);

        return "orders/orders";
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
                                   RedirectAttributes ra, HttpServletRequest request) {

        LOGGER.info("OrderController | viewOrderDetails is called");

        try {
            Order order = orderService.get(id);

            LOGGER.info("OrderController | viewOrderDetails | order : " + order.toString());

            OrderUtil.loadCurrencySetting(request, settingService);
            model.addAttribute("order", order);

            return "orders/order_details_modal";
        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("messageError", ex.getMessage());

            LOGGER.info("OrderController | viewOrderDetails | messageError : " + ex.getMessage());

            return defaultRedirectURL;
        }

    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {

        LOGGER.info("OrderController | deleteOrder is called");

        try {
            orderService.delete(id);
            ra.addFlashAttribute("messageSuccess", "The order ID " + id + " has been deleted.");

            LOGGER.info("OrderController | deleteOrder | messageSuccess : " + "The order ID " + id + " has been deleted.");

        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("messageError", ex.getMessage());

            LOGGER.info("OrderController | deleteOrder | order : " + ex.getMessage());
        }

        return defaultRedirectURL;
    }

}
