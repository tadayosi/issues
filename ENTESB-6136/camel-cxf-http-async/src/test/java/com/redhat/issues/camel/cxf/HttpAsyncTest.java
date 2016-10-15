package com.redhat.issues.camel.cxf;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduit;
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduitFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HttpAsyncTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                context.getRegistry(JndiRegistry.class).bind("testBus", BusFactory.getDefaultBus());
                ExecutorService testPool = context.getExecutorServiceManager()
                    .newFixedThreadPool("testPool", "Test Thread Pool", 30);
                // @formatter:off
                from("seda:input")
                    .log("IN:  ${body}")
                    .split().tokenize(",")
                        //.executorService(testPool)
                        .threads().executorService(testPool)
                            .doTry()
                                .to("cxf://http://0.0.0.1/test?dataFormat=RAW&bus=#testBus")
                            .doCatch(Throwable.class)
                                .handled(true)
                                //.log(LoggingLevel.ERROR, "${exception.stacktrace}")
                                //.log(LoggingLevel.ERROR, "${exception}")
                            .end()
                        .end()
                    .end()
                    .log("OUT: ${body}")
                    .delay(1000)
                    .setBody(constant("test"))
                    .to("mock:result");
                // @formatter:on
            }
        };
    }

    @Before
    public void setUpAsyncHttp() {
        Bus bus = BusFactory.getDefaultBus();
        bus.setProperty(AsyncHTTPConduit.USE_ASYNC, true);
        bus.setProperty(AsyncHTTPConduitFactory.THREAD_COUNT, 10);
        bus.setProperty(AsyncHTTPConduitFactory.SO_KEEPALIVE, true);
        bus.setProperty(AsyncHTTPConduitFactory.SELECT_INTERVAL, 10);
        bus.setProperty(AsyncHTTPConduitFactory.USE_POLICY, "ASYNC_ONLY");
    }

    @Test
    public void cxfHttpAsync() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedBodiesReceived("test");

        template.sendBody("seda:input",
            IntStream.range(0, 100).mapToObj(Integer::toString).collect(Collectors.joining(",")));
        assertMockEndpointsSatisfied(5, TimeUnit.SECONDS);
    }

}
