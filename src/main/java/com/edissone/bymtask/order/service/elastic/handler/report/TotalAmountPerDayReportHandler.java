package com.edissone.bymtask.order.service.elastic.handler.report;

import co.elastic.clients.elasticsearch._types.aggregations.*;
import com.edissone.bymtask.context.util.elastic.handler.report.BaseReportHandler;
import com.edissone.bymtask.order.dao.elastic.model.OrderDocument;
import com.edissone.bymtask.order.dto.OrderReportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Service("total-amount-per-day")
public class TotalAmountPerDayReportHandler extends BaseReportHandler<OrderReportResponse> {
    protected final static String REPORT_TYPE_LABEL = "total-amount-per-day";
    private final static String TOTAL_AMOUNT_PER_DAY_LABEL = "total_amount_per_day";
    private final static String TOTAL_AMOUNT_LABEL = "total_amount";
    private final static String CREATED_AT_FIELD = "created_at";
    private final static String ORDER_AMOUNT_FIELD = "order_amount";

    @Autowired
    public TotalAmountPerDayReportHandler(ElasticsearchOperations template) {
        super(template, "Report type for getting total amounts per day");
    }

    @Override
    public OrderReportResponse report(LocalDateTime from, LocalDateTime to) {
        final var query = prepareQuery();

        final var hits = template.search(query, OrderDocument.class);

        final var responseAggregations = Optional.ofNullable(
                (ElasticsearchAggregations) hits.getAggregations()
        );

        return responseAggregations.map(mapOrderResponse(from, to))
                .orElse(OrderReportResponse.builder()
                        .size(0)
                        .type(REPORT_TYPE_LABEL)
                        .from(from)
                        .to(to)
                        .build()
                );
    }

    private NativeQuery prepareQuery() {
        final var dateHistAgg = Aggregation.of(
                a -> a.dateHistogram(
                                d -> d.calendarInterval(CalendarInterval.Day).field(CREATED_AT_FIELD))
                        .aggregations(TOTAL_AMOUNT_LABEL,
                                SumAggregation.of(
                                        sb -> sb.field(ORDER_AMOUNT_FIELD)
                                )._toAggregation()
                        )
        );

        final var query = QueryBuilders.matchAllQuery()._toQuery();

        return NativeQuery.builder()
                .withQuery(query)
                .withAggregation(TOTAL_AMOUNT_PER_DAY_LABEL, dateHistAgg)
                .withMaxResults(0)
                .build();
    }

    private Function<ElasticsearchAggregations, OrderReportResponse> mapOrderResponse(LocalDateTime from, LocalDateTime to) {
        return aggregations -> prepareReport(aggregations, from, to)
                .type(REPORT_TYPE_LABEL)
                .from(from)
                .to(to)
                .build();
    }

    private OrderReportResponse.OrderReportResponseBuilder prepareReport(ElasticsearchAggregations aggregation, LocalDateTime from, LocalDateTime to) {
        final var report = OrderReportResponse.builder();

        final Predicate<OrderReportResponse.ReportItem> fromFilter = ri -> ri.getDate().isAfter(from);
        final Predicate<OrderReportResponse.ReportItem> toFilter = ri -> ri.getDate().isBefore(to);
        Predicate<OrderReportResponse.ReportItem> filterPredicate = ri -> true;

        filterPredicate = from != null ? filterPredicate.and(fromFilter) : filterPredicate;
        filterPredicate = to != null ? filterPredicate.and(toFilter) : filterPredicate;

        final var items = ((DateHistogramAggregate) aggregation.aggregationsAsMap()
                .get(TOTAL_AMOUNT_PER_DAY_LABEL)
                .aggregation()
                .getAggregate()
                ._get()
        ).buckets()
                .array()
                .stream()
                .map(mapReportItem())
                .filter(filterPredicate)
                .toList();

        report.size(items.size())
                .items(items);

        return report;
    }

    private Function<DateHistogramBucket, OrderReportResponse.ReportItem> mapReportItem() {
        return bucket -> {
            final var totalAmount =
                    ((SumAggregate) bucket.aggregations().get(TOTAL_AMOUNT_LABEL)._get()).value();
            return OrderReportResponse.ReportItem.builder()
                    .date(toLocalDateTime(bucket.key()))
                    .totalAmount(totalAmount)
                    .build();
        };
    }

    private LocalDateTime toLocalDateTime(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

}
