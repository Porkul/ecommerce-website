package com.katrinporkul.ecommerce.dto;

import com.katrinporkul.ecommerce.entity.Address;
import com.katrinporkul.ecommerce.entity.Customer;
import com.katrinporkul.ecommerce.entity.Order;
import com.katrinporkul.ecommerce.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {
    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    // orderItems collection from set to JSON arr and vice versa
    private Set<OrderItem> orderItems;
}
