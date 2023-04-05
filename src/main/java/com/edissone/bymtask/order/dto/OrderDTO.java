package com.edissone.bymtask.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    @JsonProperty("order_id")
    private String id;
    @JsonProperty("order_amount")
    private Double amount;
    @JsonProperty("order_items")
    @Singular
    private List<OrderItemDTO> items;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public Set<Long> extractItemIDs() {
        return items.stream().map(OrderItemDTO::getProductId).collect(Collectors.toSet());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OrderItemDTO {
        @JsonProperty("product_id")
        private Long productId;
        @JsonProperty("product_name")
        private String productName;
        @JsonProperty("product_sku")
        private String productSku;
        @JsonProperty("product_price")
        private Double productPrice;
        @JsonProperty("order_quantity")
        private Integer quantity;
    }
}
