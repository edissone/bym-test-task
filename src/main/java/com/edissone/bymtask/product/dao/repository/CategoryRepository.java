package com.edissone.bymtask.product.dao.repository;

import com.edissone.bymtask.product.dao.model.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);
}
