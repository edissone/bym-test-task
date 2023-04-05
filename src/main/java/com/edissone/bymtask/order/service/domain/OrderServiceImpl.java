package com.edissone.bymtask.order.service.domain;

import com.edissone.bymtask.order.dao.domain.model.OrderEntity;
import com.edissone.bymtask.order.dao.embedded.model.OrderItem;
import com.edissone.bymtask.order.dao.domain.repository.OrderRepository;
import com.edissone.bymtask.order.dto.OrderDTO;
import com.edissone.bymtask.order.exception.OrderNotFoundException;
import com.edissone.bymtask.product.dao.model.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;

    @Autowired
    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrderEntity create(OrderDTO dto, List<ProductEntity> products) {
        final var insertable = OrderEntity.builder()
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());

        final var amount = new AtomicReference<>(0.00);
        final var idToQuantity = extractOrderIdToQuantity(dto.getItems().stream());

        products.stream()
                .map(mapOrderItem(idToQuantity))
                .forEach(insertAndCalculate(amount, insertable));

        insertable.amount(amount.get());

        final var saved = repository.save(insertable.build());
        log.info("order created: {}", saved);
        return saved;
    }

    @Override
    public OrderEntity get(String id) {
        return repository.findById(id).orElseThrow(() -> new OrderNotFoundException("id=" + id));
    }

    @Override
    public OrderEntity update(String id, OrderDTO dto, List<ProductEntity> products) {
        final var exist = repository.findById(id).orElseThrow(() -> new OrderNotFoundException("id=" + id));

        final var updatable = OrderEntity.builder()
                .id(exist.getId())
                .createdAt(exist.getCreatedAt())
                .updatedAt(LocalDateTime.now());

        if (products != null && !products.isEmpty()) {
            final var amount = new AtomicReference<>(0.00);
            final var idToQuantity = extractOrderIdToQuantity(dto.getItems().stream());
            products.stream()
                    .map(mapOrderItem(idToQuantity))
                    .forEach(insertAndCalculate(amount, updatable));
            updatable.amount(amount.get());
        }

        final var saved = repository.save(updatable.build());
        log.info("order updated: {}\n-> {}", exist, saved);
        return saved;
    }

    @Override
    public Page<OrderEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public OrderEntity delete(String id) {
        final var exist = repository.findById(id).orElseThrow(() -> new OrderNotFoundException("id=" + id));
        repository.delete(exist);
        log.info("delete order: {}", exist);
        return exist;
    }

    private Map<Long, Integer> extractOrderIdToQuantity(Stream<OrderDTO.OrderItemDTO> itemStream) {
        return itemStream.collect(Collectors.toMap(OrderDTO.OrderItemDTO::getProductId, OrderDTO.OrderItemDTO::getQuantity));
    }

    private Consumer<OrderItem> insertAndCalculate(AtomicReference<Double> amount, OrderEntity.OrderEntityBuilder builder) {
        return oi -> {
            amount.getAndUpdate(a -> a + (oi.getProductPrice() * oi.getQuantity()));
            builder.item(oi);
        };
    }

    private Function<ProductEntity, OrderItem> mapOrderItem(Map<Long, Integer> idToQuantity) {
        return it -> new OrderItem(it, idToQuantity.get(it.getId()));
    }
}
