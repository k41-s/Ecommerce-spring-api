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
    public Boolean isProductDeleted;
    public Integer mainImgId;
    public int userId;
    public String userName;
    public LocalDateTime orderedAt;
    public PaymentMethod paymentMethod;
    public String notes;

    public OrderDTO(Integer id, Integer productId, String productName, Boolean isProductDeleted,
                    Integer userId, String userName, LocalDateTime orderedAt, PaymentMethod paymentMethod,
                    String notes, Integer mainImageId) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.isProductDeleted = isProductDeleted;
        this.userId = userId;
        this.userName = userName;
        this.orderedAt = orderedAt;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.mainImgId = mainImageId;
    }
}
