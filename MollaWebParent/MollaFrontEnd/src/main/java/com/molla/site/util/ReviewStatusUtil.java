package com.molla.site.util;

import com.molla.common.entity.Customer;
import com.molla.common.entity.Order;
import com.molla.common.entity.OrderDetail;
import com.molla.common.entity.Product;
import com.molla.site.service.ReviewService;

import java.util.Iterator;

public class ReviewStatusUtil {

    public static void setProductReviewableStatus(Customer customer, Order order, ReviewService reviewService) {
        Iterator<OrderDetail> iterator = order.getOrderDetails().iterator();

        while(iterator.hasNext()) {
            OrderDetail orderDetail = iterator.next();
            Product product = orderDetail.getProduct();
            Integer productId = product.getId();

            boolean didCustomerReviewProduct = reviewService.didCustomerReviewProduct(customer, productId);
            product.setReviewedByCustomer(didCustomerReviewProduct);

            if (!didCustomerReviewProduct) {
                boolean canCustomerReviewProduct = reviewService.canCustomerReviewProduct(customer, productId);
                product.setCustomerCanReview(canCustomerReviewProduct);
            }

        }
    }

}
