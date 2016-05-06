package com.redhat.issues.scheduled.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.routepolicy.quartz2.CronScheduledRoutePolicy;

public class SampleRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        CronScheduledRoutePolicy startPolicy = new CronScheduledRoutePolicy();
        startPolicy.setRouteStartTime("0/15 * * * * ?");

        // @formatter:off
        from("activemq:queue:TEST?transacted=true").routeId("Sample_1_Consumer")
            .routePolicy(startPolicy).noAutoStartup()
            .to("direct:aggregator");

        from("direct:aggregator").routeId("Sample_2_Aggregator")
            .aggregate(constant(true), new SampleAggregationStrategy())
                .completionTimeout(3000)
                .completionPredicate(simple("${exchangeProperty.completed} == true"))
            .toF("controlbus:route?routeId=%s&action=stop&async=true", "Sample_1_Consumer")
            .delay(1000)
            .to("direct:output");

        from("direct:output").routeId("Sample_3_Output")
            .log("OUT: ${body}");
        // @formatter:on
    }
}
