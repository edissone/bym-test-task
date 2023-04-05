package com.edissone.bymtask.context.util.elastic.handler.report;

import com.edissone.bymtask.context.util.elastic.handler.BaseHandler;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

public abstract class BaseReportHandler<R> extends BaseHandler implements ReportHandler<R> {
    protected final ElasticsearchOperations template;

    protected BaseReportHandler(ElasticsearchOperations template, String description) {
        super(description);
        this.template = template;
    }


}
