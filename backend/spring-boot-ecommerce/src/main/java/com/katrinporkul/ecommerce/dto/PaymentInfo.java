package com.katrinporkul.ecommerce.dto;

import lombok.Data;

@Data
public class PaymentInfo {
    // cents
    private int amount;
    private String currency;
    private String receiptEmail;
}
