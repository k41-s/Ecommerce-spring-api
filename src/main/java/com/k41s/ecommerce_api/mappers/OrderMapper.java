package com.k41s.ecommerce_api.mappers;

import com.k41s.ecommerce_api.entities.Order;
import com.k41s.ecommerce_api.dtos.OrderDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mappings({
            @Mapping(target = "productId", source = "product.id"),
            @Mapping(target = "productName", source = "product.name"),
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "userName", expression = "java(order.getUser() != null ? order.getUser().getName() + \" \" + order.getUser().getSurname() : null)")
    })
    OrderDTO toDto(Order order);

    @Mappings({
            @Mapping(target = "product", ignore = true),
            @Mapping(target = "user", ignore = true)
    })
    Order toEntity(OrderDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(OrderDTO dto, @MappingTarget Order entity);

}
