package com.edissone.bymtask.order.controller;

import com.edissone.bymtask.context.exception.utils.ErrorMessage;
import com.edissone.bymtask.context.util.mapper.EntityMapper;
import com.edissone.bymtask.order.dao.domain.model.OrderEntity;
import com.edissone.bymtask.order.dto.OrderDTO;
import com.edissone.bymtask.order.dto.OrderReportResponse;
import com.edissone.bymtask.order.dto.OrderSearchResponse;
import com.edissone.bymtask.order.service.facade.OrderFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "order", description = "API to operate with orders")
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
    @Operation(tags = {"order"}, summary = "Create new order",
            description = "Creates new order with nested order items. Order items with product_id should be according to exists products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new order"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Nested product(s) with provided ID(s) not found.",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    public OrderDTO create(@RequestBody OrderDTO dto) {
        return mapper.dto(service.create(dto));
    }

    @GetMapping
    @Operation(tags = {"order"}, summary = "Get order by ID",
            description = "Get full order info by provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Order with provided ID not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    public OrderDTO get(@Parameter(required = true) @RequestParam("id") String id) {
        return mapper.dto(service.get(id));
    }

    @GetMapping("/all")
    @Operation(tags = {"order"}, summary = "Get all orders",
            description = "Get all orders info with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public Page<OrderDTO> getAll(@Parameter(allowEmptyValue = true)
                                 @RequestParam(value = "page_size", defaultValue = "20") int pageSize,
                                 @Parameter(allowEmptyValue = true)
                                 @RequestParam(value = "page_number", defaultValue = "0") int pageNumber) {
        return service.getAll(pageNumber, pageSize).map(mapper::dto);
    }

    @GetMapping("/search")
    @Operation(tags = {"order"}, summary = "Search orders",
            description = "Perform searching depends on different query types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "501", description = "Handler for query_type not implemented",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    public OrderSearchResponse search(
            @Parameter(allowEmptyValue = true, description = "Using for determinate search handler for target query.")
            @RequestParam(value = "query_type", defaultValue = "item-product-name-like", required = false)
            String queryType,
            @Parameter(allowEmptyValue = true, description = "Value used for search")
            @RequestParam(value = "value") String value,
            @Parameter(allowEmptyValue = true)
            @RequestParam(value = "page_size", defaultValue = "20") int pageSize,
            @Parameter(allowEmptyValue = true)
            @RequestParam(value = "page_number", defaultValue = "0") int pageNumber
    ) {
        return service.search(queryType, value, pageNumber, pageSize);
    }

    @GetMapping("/search/types")
    @Operation(tags = {"order"}, summary = "Search Query Types",
            description = "Show map of implemented search handlers with their descriptions")
    public Map<String, String> searchTypes() {
        return service.getHandlers("search");
    }

    @GetMapping("/report")
    @Operation(tags = {"order"}, summary = "Get report",
            description = "Provide report depends on target report_type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "501", description = "Handler for report_type not implemented",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    public OrderReportResponse report(
            @Parameter(allowEmptyValue = true, description = "Using for determinate report handler for target report.")
            @RequestParam(value = "report_type", defaultValue = "total-amount-per-day", required = false)
            String reportType,
            @Parameter(allowEmptyValue = true, description = "Starts from date")
            @RequestParam(value = "from", required = false) String from,
            @Parameter(allowEmptyValue = true, description = "End to date")
            @RequestParam(value = "to", required = false) String to
    ) {
        return service.report(reportType, from, to);
    }

    @GetMapping("/report/types")
    @Operation(tags = {"order"}, summary = "Report Types",
            description = "Show map of implemented report handlers with their descriptions")
    @ApiResponse(responseCode = "200", description = "OK")
    public Map<String, String> reportTypes() {
        return service.getHandlers("report");
    }

    @PutMapping("/{id}")
    @Operation(tags = {"order"}, summary = "Update order",
            description = "Update order. Requires only DTO fields to update ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Order with target ID not found\nNested product(s) with <set-of-id> not found (at least one)",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    })
    public OrderDTO update(
            @Parameter(required = true) @PathVariable String id
            , @RequestBody OrderDTO dto) {
        return mapper.dto(service.update(id, dto));
    }

    @DeleteMapping
    @Operation(tags = {"order"}, summary = "Delete order",
            description = "Delete order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Order with target ID not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    })
    public OrderDTO delete(@Parameter(required = true) @RequestParam("id") String id) {
        return mapper.dto(service.delete(id));
    }
}
