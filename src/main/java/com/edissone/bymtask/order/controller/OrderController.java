package com.edissone.bymtask.order.controller;

import com.edissone.bymtask.context.util.mapper.EntityMapper;
import com.edissone.bymtask.order.dao.domain.model.OrderEntity;
import com.edissone.bymtask.order.dto.OrderDTO;
import com.edissone.bymtask.order.dto.OrderReportResponse;
import com.edissone.bymtask.order.dto.OrderSearchResponse;
import com.edissone.bymtask.order.service.facade.OrderFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final EntityMapper<OrderEntity, OrderDTO> mapper;
    private final OrderFacadeService service;

    @Autowired
    public OrderController(EntityMapper<OrderEntity, OrderDTO> mapper, OrderFacadeService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO create(@RequestBody OrderDTO dto) {
        return mapper.dto(service.create(dto));
    }

    @GetMapping
    public OrderDTO get(@RequestParam("id") String id) {
        return mapper.dto(service.get(id));
    }

    @GetMapping("/all")
    public Page<OrderDTO> getAll(@RequestParam(value = "page_size", defaultValue = "20") int pageSize,
                                 @RequestParam(value = "page_number", defaultValue = "0") int pageNumber) {
        return service.getAll(pageNumber, pageSize).map(mapper::dto);
    }

    @GetMapping("/search")
    public OrderSearchResponse search(
            @RequestParam(value = "query_type", defaultValue = "item-product-name-like", required = false)
            String queryType, @RequestParam(value = "value") String value,
            @RequestParam(value = "page_size", defaultValue = "20") int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0") int pageNumber
    ) {
        return service.search(queryType, value, pageNumber, pageSize);
    }

    @GetMapping("/search/types")
    public Map<String, String> searchTypes() {
        return service.getHandlers("search");
    }

    @GetMapping("/report")
    public OrderReportResponse report(
            @RequestParam(value = "report_type", defaultValue = "total-amount-per-day", required = false)
            String reportType, @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        return service.report(reportType, from, to);
    }

    @GetMapping("/report/types")
    public Map<String, String> reportTypes() {
        return service.getHandlers("report");
    }

    @PutMapping("/{id}")
    public OrderDTO update(@PathVariable String id, @RequestBody OrderDTO dto) {
        return mapper.dto(service.update(id, dto));
    }

    @DeleteMapping
    public OrderDTO delete(@RequestParam("id") String id) {
        return mapper.dto(service.delete(id));
    }
}
