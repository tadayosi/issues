package com.redhat.issues.cxf.local;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class LocalTransportTest {

    private static final Logger LOG = LoggerFactory.getLogger(GreetingServiceImpl.class);

    public static final String ADDRESS = "local://greeting";

    private Optional<Server> server = Optional.empty();
    private Optional<Exception> exception;

    @Before
    public void setUp() {
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setAddress(ADDRESS);
        factory.setServiceBean(new GreetingServiceImpl());
        factory.setServiceClass(GreetingService.class);
        factory.getFeatures().add(new LoggingFeature());
        server = Optional.of(factory.create());

        exception = Optional.empty();
    }

    @After
    public void tearDown() {
        server.ifPresent(s -> s.stop());
    }

    private void doTest() {
        try {
            String name = Thread.currentThread().getName();
            String result = createClient().hello(name);
            LOG.info("{}", result);
            assertThat(result).isEqualTo("Hello, " + name + "!");
        } catch (Exception e) {
            exception = Optional.of(e);
        }
    }

    @Test
    public void sameThread() {
        doTest();
        exception.ifPresent(e -> fail(e.getMessage()));
    }

    @Test
    public void differentThread() throws InterruptedException {
        Thread t = new Thread(() -> doTest());
        t.start();
        t.join();
        exception.ifPresent(e -> fail(e.getMessage()));
    }

    private GreetingService createClient() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setAddress(ADDRESS);
        factory.setServiceClass(GreetingService.class);
        factory.getFeatures().add(new LoggingFeature());
        return (GreetingService) factory.create();
    }

}
