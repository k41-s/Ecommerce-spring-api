package com.k41s.scrollspree_core.dtos.paypal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PayPalOrderResponse {
    private String paypalOrderId;
    private String approvalUrl;
}