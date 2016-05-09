package com.redhat.issues.scheduled.route;

import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.routepolicy.quartz2.CronScheduledRoutePolicy;
import org.apache.camel.spi.ShutdownStrategy;

public class SampleRouteBuilder extends RouteBuilder {

    private static final long SHUTDOWN_TIMEOUT = 1000;
    private static final long COMPLETION_TIMEOUT = 3000;

    @Override
    public void configure() {
        ShutdownStrategy shutdownStrategy = getContext().getShutdownStrategy();
        shutdownStrategy.setTimeUnit(TimeUnit.MILLISECONDS);
        shutdownStrategy.setTimeout(SHUTDOWN_TIMEOUT);

        CronScheduledRoutePolicy startPolicy = new CronScheduledRoutePolicy();
        startPolicy.setRouteStartTime("0/15 * * * * ?");

        // @formatter:off
        from("activemq:queue:TEST?transacted=true").routeId("Sample_1_Consumer")
            .routePolicy(startPolicy).noAutoStartup()
            .to("direct:aggregator");

        from("direct:aggregator").routeId("Sample_2_Aggregator")
            .aggregate(constant(true), new SampleAggregationStrategy())
                .completionTimeout(COMPLETION_TIMEOUT)
                .completionPredicate(simple("${exchangeProperty.completed} == true"))
            .toF("controlbus:route?routeId=%s&action=stop&async=true", "Sample_1_Consumer")
            .delay(SHUTDOWN_TIMEOUT)
            .to("direct:output");

        from("direct:output").routeId("Sample_3_Output")
            .delay(5000)
            .log("OUT: ******************************\n${body}")
            .log("***********************************");
        // @formatter:on
    }
}
