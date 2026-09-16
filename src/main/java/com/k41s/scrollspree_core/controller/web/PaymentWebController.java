package com.k41s.scrollspree_core.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class PaymentWebController {

    @GetMapping("/success")
    public String paymentSuccess() {
        return "payments/payment-success";
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "payments/payment-cancel";
    }
}