package com.k41s.ecommerce_api.dtos;

import com.k41s.ecommerce_api.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// NEXT UP: implement orders passing around ids of products' images to be displayed

@Getter
@Setter
public class OrderDTO {
    public Integer id;
    public int productId;
    public String productName;
    public int userId;
    public String userName;
    public LocalDateTime orderedAt;
    public PaymentMethod paymentMethod;
    public String notes;
}
