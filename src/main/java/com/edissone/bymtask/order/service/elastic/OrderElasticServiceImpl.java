package com.edissone.bymtask.order.service.elastic;

import com.edissone.bymtask.context.exception.HandlerNotImplementedException;
import com.edissone.bymtask.context.exception.UnrecognizedHandlerTypeException;
import com.edissone.bymtask.context.util.elastic.handler.BaseHandler;
import com.edissone.bymtask.context.util.elastic.handler.report.ReportHandler;
import com.edissone.bymtask.context.util.elastic.handler.search.SearchHandler;
import com.edissone.bymtask.order.dao.elastic.model.OrderDocument;
import com.edissone.bymtask.order.dto.OrderReportResponse;
import com.edissone.bymtask.order.dto.OrderSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderElasticServiceImpl implements OrderElasticService {
    private final Map<String, SearchHandler<OrderDocument>> searchHandlers;
    private final Map<String, ReportHandler<OrderReportResponse>> reportHandlers;

    @Autowired
    public OrderElasticServiceImpl(
            Map<String, SearchHandler<OrderDocument>> searchHandlers,
            Map<String, ReportHandler<OrderReportResponse>> reportHandlers
    ) {
        this.searchHandlers = searchHandlers;
        this.reportHandlers = reportHandlers;
    }

    @Override
    public OrderSearchResponse search(String queryType, String value, Pageable pageable) {
        final var results = Optional.ofNullable(searchHandlers.get(queryType))
                .orElseThrow(() -> new HandlerNotImplementedException("search " + queryType)).search(value, pageable);
        return OrderSearchResponse.builder()
                .value(value)
                .queryType(queryType)
                .results(results)
                .build();
    }

    @Override
    public OrderReportResponse report(String reportType, LocalDateTime from, LocalDateTime to) {
        return Optional.ofNullable(reportHandlers.get(reportType))
                .orElseThrow(() -> new HandlerNotImplementedException("report " + reportType)).report(from, to);
    }

    @Override
    public Map<String, String> getHandlers(String handlerType) {
        return switch (handlerType) {
            case "report" -> nameToDescription(reportHandlers);
            case "search" -> nameToDescription(searchHandlers);
            default -> throw new UnrecognizedHandlerTypeException(handlerType);
        };
    }

    private Map<String, String> nameToDescription(Map<String, ?> map) {
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                e -> ((BaseHandler) e.getValue()).description()));
    }

}
