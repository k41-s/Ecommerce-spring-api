package com.k41s.ecommerce_api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.k41s.ecommerce_api.enums.OrderStatus;
import com.k41s.ecommerce_api.services.OrderService;
import com.k41s.ecommerce_api.services.PayPalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/webhooks/paypal")
@RequiredArgsConstructor
public class PayPalWebhookController {

    private final PayPalService payPalService;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Void> handlePayPalWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload) {

        try {
            boolean isValid = payPalService.verifyWebhookSignature(headers, payload);

            if (!isValid) {
                log.warn("SECURITY ALERT: Invalid PayPal Webhook signature detected. Rejecting payload.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            JsonNode jsonNode = objectMapper.readTree(payload);
            String eventType = jsonNode.get("event_type").asText();
            JsonNode resource = jsonNode.get("resource");

            if ("CHECKOUT.ORDER.APPROVED".equals(eventType)) {
                String paypalOrderId = resource.get("id").asText();
                log.info("Webhook received: Order {} approved. Attempting capture...", paypalOrderId);

                boolean isCaptured = payPalService.captureOrder(paypalOrderId);

                if (isCaptured) {
                    orderService.updateOrderStatusByPaypalId(paypalOrderId, OrderStatus.COMPLETED);
                    log.info("Successfully captured funds. Database updated to COMPLETED.");
                } else {
                    orderService.updateOrderStatusByPaypalId(paypalOrderId, OrderStatus.FAILED);
                    log.error("Failed to capture funds. Database updated to FAILED.");
                }
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Error processing PayPal Webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}