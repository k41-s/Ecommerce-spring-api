package com.k41s.ecommerce_api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class PaymentWebController {

    @GetMapping("/success")
    public String paymentSuccess() {
        return "payment-success";
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "payment-cancel";
    }
}