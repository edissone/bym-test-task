package com.edissone.bymtask.order.service.mapper;

import com.edissone.bymtask.context.util.mapper.EntityMapper;
import com.edissone.bymtask.order.dao.domain.model.OrderEntity;
import com.edissone.bymtask.order.dao.embedded.model.OrderItem;
import com.edissone.bymtask.order.dto.OrderDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper implements EntityMapper<OrderEntity, OrderDTO> {
    @Override
    public OrderDTO dto(OrderEntity entity) {
        final var dto = OrderDTO.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt());

        entity.getItems().stream().map(this::mapItem).forEach(dto::item);
        return dto.build();
    }

    private OrderDTO.OrderItemDTO mapItem(OrderItem item) {
        return OrderDTO.OrderItemDTO.builder()
                .productId(item.getProductId())
                .productName(item.getProductName())
                .productPrice(item.getProductPrice())
                .productSku(item.getProductSku())
                .quantity(item.getQuantity())
                .build();
    }
}
