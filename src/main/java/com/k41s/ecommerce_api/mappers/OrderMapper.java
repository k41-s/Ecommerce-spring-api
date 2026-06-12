package com.k41s.ecommerce_api.mappers;

import com.k41s.ecommerce_api.dtos.OrderItemDTO;
import com.k41s.ecommerce_api.entities.Order;
import com.k41s.ecommerce_api.dtos.OrderDTO;
import com.k41s.ecommerce_api.entities.OrderItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mappings({
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "userName",
                    expression = "java(order.getUser() != null " +
                            "? order.getUser().getName() + \" \" + order.getUser().getSurname() " +
                            ": null)"
            )
    })
    OrderDTO toDto(Order order);

    @Mappings({
            @Mapping(target = "items", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Order toEntity(OrderDTO dto);

    @Mappings({
            @Mapping(target = "productId", source = "product.id"),
            @Mapping(target = "productName", source = "product.name"),
            @Mapping(target = "price", source = "product.price"),
            @Mapping(target = "isProductDeleted", source = "product.deleted"),
            @Mapping(target = "mainImgId",
                    expression = "java(orderItem.getProduct().getImages() != null " +
                            "&& !orderItem.getProduct().getImages().isEmpty()" +
                            "? orderItem.getProduct().getImages().get(0).getId()" +
                            ": null)"
            )
    })
    OrderItemDTO toItemDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(OrderDTO dto, @MappingTarget Order entity);

}
