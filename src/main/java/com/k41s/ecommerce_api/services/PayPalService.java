package com.k41s.ecommerce_api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.k41s.ecommerce_api.dtos.paypal.PayPalOrderResponse;
import com.k41s.ecommerce_api.exceptions.PaymentGatewayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayPalService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.api.base}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    @Value("${paypal.webhook.id}")
    private String webhookId;

    public boolean verifyWebhookSignature(Map<String, String> headers, String rawBody) {
        String url = baseUrl + "/v1/notifications/verify-webhook-signature";
        String accessToken = getAccessToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            Map<String, Object> payload = new HashMap<>();

            payload.put("auth_algo", headers.get("paypal-auth-algo"));
            payload.put("cert_url", headers.get("paypal-cert-url"));
            payload.put("transmission_id", headers.get("paypal-transmission-id"));
            payload.put("transmission_sig", headers.get("paypal-transmission-sig"));
            payload.put("transmission_time", headers.get("paypal-transmission-time"));
            payload.put("webhook_id", webhookId);

            payload.put("webhook_event", objectMapper.readTree(rawBody));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, httpHeaders);
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);

            String verificationStatus = response.getBody().get("verification_status").asText();
            return "SUCCESS".equals(verificationStatus);

        } catch (Exception e) {
            log.error("Error during Webhook signature verification", e);
            return false;
        }
    }

    private String getAccessToken() {
        String url = baseUrl + "/v1/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
            return response.getBody().get("access_token").asText();
        } catch (Exception e) {
            log.error("Failed to get PayPal access token", e);
            throw new PaymentGatewayException("Payment Gateway Error");
        }
    }

    public PayPalOrderResponse createOrder(BigDecimal totalAmount, String currencyCode) {
        String url = baseUrl + "/v2/checkout/orders";
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> amount = new HashMap<>();
        amount.put("currency_code", currencyCode);
        amount.put("value", String.format("%.2f", totalAmount).replace(",", "."));

        Map<String, Object> purchaseUnit = new HashMap<>();
        purchaseUnit.put("amount", amount);

        Map<String, Object> applicationContext = getApplicationContext();

        Map<String, Object> payload = new HashMap<>();
        payload.put("intent", "CAPTURE");
        payload.put("purchase_units", List.of(purchaseUnit));
        payload.put("application_context", applicationContext);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
            JsonNode body = response.getBody();

            String paypalOrderId = body.get("id").asText();
            String approvalUrl = null;

            for (JsonNode link : body.get("links")) {
                if ("approve".equals(link.get("rel").asText())) {
                    approvalUrl = link.get("href").asText();
                    break;
                }
            }

            return new PayPalOrderResponse(paypalOrderId, approvalUrl);

        } catch (Exception e) {
            log.error("Failed to create PayPal order", e);
            throw new PaymentGatewayException("Could not initialize payment");
        }
    }

    private static @NonNull Map<String, Object> getApplicationContext() {
        Map<String, Object> applicationContext = new HashMap<>();
        applicationContext.put("user_action", "PAY_NOW");

        String myServerUrl = "https://ecommerce-spring-api-hnbl.onrender.com";

        applicationContext.put("return_url", myServerUrl + "/payment/success");
        applicationContext.put("cancel_url", myServerUrl + "/payment/cancel");

        return applicationContext;
    }

    public boolean captureOrder(String paypalOrderId) {
        String url = baseUrl + "/v2/checkout/orders/" + paypalOrderId + "/capture";
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>("", headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
            JsonNode body = response.getBody();

            String status = body.get("status").asText();
            return "COMPLETED".equals(status);

        } catch (Exception e) {
            log.error("Failed to capture PayPal order for ID: " + paypalOrderId, e);
            return false;
        }
    }
}