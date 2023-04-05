package com.edissone.bymtask.order.dao.domain.model;

import com.edissone.bymtask.order.dao.embedded.model.OrderItem;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private String id;

    @Column(name = "order_amount")
    private Double amount;

    @Type(JsonType.class)
    @Column(name = "order_items", columnDefinition = "jsonb")
    @Singular
    private List<OrderItem> items;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
