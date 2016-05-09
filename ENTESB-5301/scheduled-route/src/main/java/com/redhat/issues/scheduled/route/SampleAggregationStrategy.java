package com.redhat.issues.scheduled.route;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.CompletionAwareAggregationStrategy;
import org.apache.camel.processor.aggregate.TimeoutAwareAggregationStrategy;

public class SampleAggregationStrategy implements TimeoutAwareAggregationStrategy, CompletionAwareAggregationStrategy {

    private static final String PROPERTY_COMPLETED = "completed";
    private static final String INDENT = "    ";

    private boolean started = false;

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null || !started) {
            started = true;
            newExchange.getIn().setBody(INDENT + newExchange.getIn().getBody(String.class));
            return newExchange;
        }

        String oldBody = oldExchange.getIn().getBody(String.class);
        oldExchange.getIn().setBody(oldBody + "\n" + INDENT + newExchange.getIn().getBody());
        if (oldExchange.getIn().getBody(String.class).split("\n").length >= 10) {
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
