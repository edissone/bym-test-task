package com.edissone.bymtask.product.service.facade;

import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.edissone.bymtask.product.dto.ProductDTO;
import com.edissone.bymtask.product.exception.CategoryDoesNotProvidedException;
import com.edissone.bymtask.product.service.domain.CategoryService;
import com.edissone.bymtask.product.service.domain.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductFacadeServiceImpl implements ProductFacadeService {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductFacadeServiceImpl(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Override
    public ProductEntity create(ProductDTO dto) {
        final var categoryID = Optional.ofNullable(dto.getCategory())
                .orElseThrow(CategoryDoesNotProvidedException::new).getCategoryId();
        final var category = categoryService.get(categoryID);
        return productService.create(dto, category);
    }

    @Override
    public ProductEntity get(Long id) {
        return productService.get(id);
    }

    @Override
    public List<ProductEntity> getAll() {
        return productService.getAll();
    }

    @Override
    public ProductEntity update(Long id, ProductDTO dto) {
        final var categoryStub = Optional.ofNullable(dto.getCategory());
        final var category = categoryStub.map(stub -> categoryService.get(stub.getCategoryId()))
                .orElse(null);
        return productService.update(id, dto, category);
    }

    @Override
    public ProductEntity delete(Long id) {
        return productService.delete(id);
    }
}
