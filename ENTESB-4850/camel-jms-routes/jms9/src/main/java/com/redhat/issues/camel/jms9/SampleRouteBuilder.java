package com.redhat.issues.camel.jms9;

import org.apache.camel.builder.RouteBuilder;
import org.wildfly.extension.camel.CamelAware;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;

@Startup
@CamelAware
@ApplicationScoped
public class SampleRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        from("jms:queue:issues.hello9?connectionFactory=#PollingConnectionFactory")
            .log("Hello, ${body}!");
    }

}
