package com.edissone.bymtask.order.dao.embedded.model;

import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @JsonProperty("product_id")
    @Field(value = "product_id", type = FieldType.Long)
    private Long productId;
    @JsonProperty("product_name")
    @Field(value = "product_name", type = FieldType.Text)
    private String productName;
    @JsonProperty("product_sku")
    @Field(value = "product_sku", type = FieldType.Text)
    private String productSku;
    @JsonProperty("product_price")
    @Field(value = "product_price", type = FieldType.Double)
    private Double productPrice;
    @JsonProperty("order_quantity")
    @Field(value = "order_quantity", type = FieldType.Integer)
    private Integer quantity;

    public OrderItem(ProductEntity product, int quantity) {
        this(product.getId(), product.getName(), product.getSku(), product.getPrice(), quantity);
    }
}
