package com.edissone.bymtask.order.service.facade;

import com.edissone.bymtask.order.dao.domain.model.OrderEntity;
import com.edissone.bymtask.order.dto.OrderDTO;
import com.edissone.bymtask.order.dto.OrderReportResponse;
import com.edissone.bymtask.order.dto.OrderSearchResponse;
import com.edissone.bymtask.order.service.domain.OrderService;
import com.edissone.bymtask.order.service.elastic.OrderElasticService;
import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.edissone.bymtask.product.exception.ProductNotFoundException;
import com.edissone.bymtask.product.service.domain.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class OrderFacadeServiceImpl implements OrderFacadeService {
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderElasticService elasticService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    @Autowired
    public OrderFacadeServiceImpl(ProductService productService, OrderService orderService, OrderElasticService elasticService) {
        this.productService = productService;
        this.orderService = orderService;
        this.elasticService = elasticService;
    }

    @Override
    public OrderEntity create(OrderDTO dto) {
        final var products = productService.findAllByIdSet(dto.extractItemIDs());
        return orderService.create(dto, products);
    }

    @Override
    public OrderEntity get(String id) {
        return orderService.get(id);
    }

    @Override
    public OrderEntity update(String id, OrderDTO dto) {
        List<ProductEntity> products;
        try {
            products = productService.findAllByIdSet(dto.extractItemIDs());
        } catch (ProductNotFoundException ex) {
            products = null;
        }
        return orderService.update(id, dto, products);
    }

    @Override
    public Page<OrderEntity> getAll(int pageNum, int pageSize) {
        return orderService.getAll(PageRequest.of(pageNum, pageSize));
    }

    @Override
    public OrderEntity delete(String id) {
        return orderService.delete(id);
    }

    @Override
    public OrderSearchResponse search(String queryType, String value, int pageNum, int pageSize) {
        return elasticService.search(queryType, value, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public OrderReportResponse report(String reportType, String from, String to) {
        return elasticService.report(reportType, parse(from), parse(to));
    }

    @Override
    public Map<String, String> getHandlers(String handlerType) {
        return elasticService.getHandlers(handlerType);
    }

    private LocalDateTime parse(@Nullable String timeString) {
        return timeString != null ? LocalDateTime.parse(timeString, FORMATTER) : null;
    }
}
