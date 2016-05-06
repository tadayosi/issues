package com.redhat.issues.scheduled.route;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.CompletionAwareAggregationStrategy;
import org.apache.camel.processor.aggregate.TimeoutAwareAggregationStrategy;

public class SampleAggregationStrategy implements TimeoutAwareAggregationStrategy, CompletionAwareAggregationStrategy {

    private static final String PROPERTY_COMPLETED = "completed";

    private boolean started = false;

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null || !started) {
            started = true;
            List<String> body = new ArrayList<>();
            body.add(newExchange.getIn().getBody(String.class));
            newExchange.getIn().setBody(body);
            return newExchange;
        }

        @SuppressWarnings("unchecked")
        List<String> body = oldExchange.getIn().getBody(List.class);
        body.add(newExchange.getIn().getBody(String.class));
        if (body.size() >= 10) {
            completed(oldExchange);
        }
        return oldExchange;
    }

    @Override
    public void onCompletion(Exchange exchange) {
        completed(exchange);
    }

    @Override
    public void timeout(Exchange oldExchange, int index, int total, long timeout) {
        completed(oldExchange);
    }

    private void completed(Exchange exchange) {
        exchange.setProperty(PROPERTY_COMPLETED, true);
        started = false;
    }

}
