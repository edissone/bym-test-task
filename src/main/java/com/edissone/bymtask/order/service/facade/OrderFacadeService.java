package com.edissone.bymtask.order.service.facade;

import com.edissone.bymtask.order.dao.domain.model.OrderEntity;
import com.edissone.bymtask.order.dto.OrderDTO;
import com.edissone.bymtask.order.dto.OrderReportResponse;
import com.edissone.bymtask.order.dto.OrderSearchResponse;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface OrderFacadeService {
    OrderEntity create(OrderDTO dto);

    OrderEntity get(String id);

    OrderEntity update(String id, OrderDTO dto);

    Page<OrderEntity> getAll(int pageNum, int pageSize);

    OrderEntity delete(String id);

    OrderSearchResponse search(String queryType, String value, int pageNum, int pageSize);

    OrderReportResponse report(String reportType, String from, String to);

    Map<String, String> getHandlers(String handlerType);
}
