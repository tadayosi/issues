package com.redhat.issues.camel.cxf;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfComponent;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.main.Main;
import org.apache.camel.util.jndi.CamelInitialContextFactory;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.management.jmx.InstrumentationManagerImpl;

public class GreetingRouteBuilder extends RouteBuilder {

    public static void main(String[] args) throws Exception {
        System.setProperty("java.naming.factory.initial", CamelInitialContextFactory.class.getName());
        configureCxf();

        Main main = new Main();
        main.addRouteBuilder(new GreetingRouteBuilder());
        main.run(args);
    }

    private static void configureCxf() {
        Bus bus = BusFactory.getDefaultBus();
        InstrumentationManagerImpl im = new InstrumentationManagerImpl(bus);
        im.setEnabled(true);
        im.setUsePlatformMBeanServer(true);
        im.init();
    }

    @Override
    public void configure() {
        configureCxfEndpoint();
        from("cxf:bean:greeting").id("cxf-greeting")
            .log("body: ${body}")
            .transform().constant("OK");
    }

    private void configureCxfEndpoint() {
        CxfComponent cxf = new CxfComponent(getContext());
        CxfEndpoint endpoint = new CxfEndpoint("/greeting/", cxf);
        endpoint.setBus(BusFactory.getDefaultBus());
        endpoint.setServiceClass(GreetingService.class);
        getContext().getRegistry(JndiRegistry.class).bind("greeting", endpoint);
    }

}
