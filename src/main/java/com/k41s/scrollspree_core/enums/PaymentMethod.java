package com.k41s.scrollspree_core.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PaymentMethod {
    @JsonProperty("Card")
    CARD,
    @JsonProperty("Paypal")
    PAYPAL,
    @JsonProperty("Bank_Transfer")
    BANK_TRANSFER,
    @JsonProperty("Cash")
    CASH
}
