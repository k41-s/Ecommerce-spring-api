package com.k41s.ecommerce_api.dtos;

import com.k41s.ecommerce_api.enums.OrderStatus;
import com.k41s.ecommerce_api.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer id;
    private Integer userId;
    private String userName;
    private LocalDateTime orderedAt;
    private PaymentMethod paymentMethod;
    private String notes;
    private List<OrderItemDTO> items;
    private OrderStatus status;
    private String paypalOrderId;
    private String approvalUrl;
}