package com.redhat.issues.cxf.rest.async;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServletTransportServer {

    public static void main(String[] args) {
        System.setProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME, "org.apache.cxf.bus.CXFBusFactory");
        try {
            CXFNonSpringServlet cxf = new CXFNonSpringServlet();
            httpServer(cxf).start();
            serverFactory(cxf.getBus()).create();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static Server httpServer(CXFNonSpringServlet cxf) throws Exception {
        Server server = new Server(9000);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(cxf), "/*");
        return server;
    }

    private static JAXRSServerFactoryBean serverFactory(Bus bus) {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBus(bus);
        factory.setResourceClasses(GreetingService.class);
        factory.setAddress("/");
        factory.getFeatures().add(new LoggingFeature());
        return factory;
    }

}
