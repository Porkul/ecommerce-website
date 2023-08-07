package com.katrinporkul.ecommerce.dto;

import lombok.Data;
import lombok.NonNull;

// Use this class to send back a Java Object as JSON
@Data
//    generates constructor for final fields
//    or without final:
//    @NonNull
//    private  String orderTrackingNumber;
public class PurchaseResponse {
    private final String orderTrackingNumber;
}
