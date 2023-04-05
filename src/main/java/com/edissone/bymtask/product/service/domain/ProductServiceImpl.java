package com.edissone.bymtask.product.service.domain;

import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.edissone.bymtask.product.dao.repository.ProductRepository;
import com.edissone.bymtask.product.dto.ProductDTO;
import com.edissone.bymtask.product.exception.ProductExistsException;
import com.edissone.bymtask.product.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    @Autowired
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductEntity create(ProductDTO dto, CategoryEntity category) {
        final var exist = repository.findBySku(dto.getSku());

        exist.ifPresent(it -> {
            throw new ProductExistsException("sku=" + dto.getSku());
        });

        final var insertable = ProductEntity.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .sku(dto.getSku())
                .category(category)
                .build();
        final var saved = repository.save(insertable);
        log.info("create product: {}", saved);
        return saved;
    }

    @Override
    public ProductEntity get(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException("id=" + id));
    }

    @Override
    public List<ProductEntity> getAll() {
        final var results = new LinkedList<ProductEntity>();
        repository.findAll().forEach(results::add);
        return results;
    }

    @Override
    public List<ProductEntity> findAllByIdSet(Set<Long> ids) {
        final var resultMap = new HashMap<Long, ProductEntity>();
        repository.findAllById(ids).forEach(it -> resultMap.put(it.getId(), it));
        final var foundIds = resultMap.keySet();

        if (!foundIds.containsAll(ids)) {
            final var nonFoundMessage = "id=[" +
                    ids.stream()
                            .filter(it -> !foundIds.contains(it))
                            .map(String::valueOf)
                            .collect(Collectors.joining(", ")) +
                    "]";
            throw new ProductNotFoundException(nonFoundMessage);
        }

        return new LinkedList<>(resultMap.values());
    }

    @Override
    public ProductEntity update(Long id, ProductDTO dto, CategoryEntity category) {
        final var exist = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("id=" + id));

        final var updatable = ProductEntity.builder()
                .id(exist.getId())
                .category(category != null ? category : exist.getCategory())
                .name(dto.getName() != null ? dto.getName() : exist.getName())
                .sku(dto.getSku() != null ? dto.getSku() : exist.getSku())
                .price(dto.getPrice() != null ? dto.getPrice() : exist.getPrice())
                .build();


        final var saved = repository.save(updatable);
        log.info("update product: {} \n-> {}", exist, saved);
        return saved;
    }

    @Override
    public ProductEntity delete(Long id) {
        final var exist = repository.findById(id).orElseThrow(() -> new ProductNotFoundException("id=" + id));
        repository.delete(exist);
        log.info("delete product: {}", exist);
        return exist;
    }

}
