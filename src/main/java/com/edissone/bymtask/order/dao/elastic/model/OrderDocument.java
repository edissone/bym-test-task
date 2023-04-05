package com.edissone.bymtask.order.dao.elastic.model;

import com.edissone.bymtask.order.dao.embedded.model.OrderItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "orders")
public class OrderDocument {
    @Id
    @JsonProperty("order_id")
    @Field(name = "order_id")
    private String id;

    @Field(name = "order_amount", type = FieldType.Double, format = {})
    @JsonProperty("order_amount")
    private Double amount;

    @Field(name = "order_items", type = FieldType.Nested, includeInParent = true)
    @JsonProperty("order_items")
    @Singular
    private List<OrderItem> items;

    @Field(name = "created_at", type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd HH:mm:ss")
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Field(name = "updated_at", type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd HH:mm:ss")
    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
