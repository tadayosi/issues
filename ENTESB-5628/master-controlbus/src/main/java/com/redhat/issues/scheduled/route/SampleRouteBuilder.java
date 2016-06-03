package com.redhat.issues.scheduled.route;

import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.routepolicy.quartz2.CronScheduledRoutePolicy;
import org.apache.camel.spi.ShutdownStrategy;

public class SampleRouteBuilder extends RouteBuilder {

    private static final String CRON_SCHEDULE = "0/20 * * * * ?";
    private static final long SHUTDOWN_TIMEOUT = 1 * 1000;
    private static final String ROUTE_ID = "MasterControlBusRoute";

    @Override
    public void configure() {
        ShutdownStrategy shutdownStrategy = getContext().getShutdownStrategy();
        shutdownStrategy.setTimeUnit(TimeUnit.MILLISECONDS);
        shutdownStrategy.setTimeout(SHUTDOWN_TIMEOUT);

        CronScheduledRoutePolicy startPolicy = new CronScheduledRoutePolicy();
        startPolicy.setRouteStartTime(CRON_SCHEDULE);

        // @formatter:off
        from("master:issue-master-controlbus:activemq:queue:TEST").routeId(ROUTE_ID)
            .routePolicy(startPolicy).noAutoStartup()
            .toF("controlbus:route?routeId=%s&action=stop&async=true", ROUTE_ID)
            .delay(SHUTDOWN_TIMEOUT)
            .log("${body}");
        // @formatter:on
    }

}
