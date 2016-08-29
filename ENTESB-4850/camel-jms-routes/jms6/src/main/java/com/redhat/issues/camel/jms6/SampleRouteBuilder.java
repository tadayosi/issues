package com.redhat.issues.camel.jms6;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.wildfly.extension.camel.CamelAware;

import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.ConnectionFactory;

@Startup
@CamelAware
@ApplicationScoped
public class SampleRouteBuilder extends RouteBuilder {

    //@Resource(mappedName = "java:/ConnectionFactory")
    @Inject @Named("PollingConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Override
    public void configure() {
        JmsComponent jms = new JmsComponent();
        jms.setConnectionFactory(connectionFactory);
        getContext().addComponent("jms", jms);

        from("jms:queue:issues.hello1")
            .log("Hello, ${body}!");
    }

}
