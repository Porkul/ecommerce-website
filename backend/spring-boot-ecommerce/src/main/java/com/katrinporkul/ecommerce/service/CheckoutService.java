package com.katrinporkul.ecommerce.service;

import com.katrinporkul.ecommerce.dto.PaymentInfo;
import com.katrinporkul.ecommerce.dto.Purchase;
import com.katrinporkul.ecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {
    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
