package com.edissone.bymtask.order.service.elastic;

import com.edissone.bymtask.order.dto.OrderReportResponse;
import com.edissone.bymtask.order.dto.OrderSearchResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Map;

public interface OrderElasticService {
    OrderSearchResponse search(String queryType, String value, Pageable pageable);
    OrderReportResponse report(String reportType, LocalDateTime from, LocalDateTime to);
    Map<String, String> getHandlers(String handlerType);
}
