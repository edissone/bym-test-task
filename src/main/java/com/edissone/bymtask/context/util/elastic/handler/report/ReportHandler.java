package com.edissone.bymtask.context.util.elastic.handler.report;

import com.edissone.bymtask.order.dto.OrderReportResponse;

import java.time.LocalDateTime;

public interface ReportHandler<R> {
    R report(LocalDateTime from, LocalDateTime to);
}
