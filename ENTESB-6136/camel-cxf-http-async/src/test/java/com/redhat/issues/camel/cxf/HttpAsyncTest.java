package com.redhat.issues.camel.cxf;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HttpAsyncTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                // @formatter:off
                from("seda:input")
                    .log("IN:  ${body}")
                    .split().tokenize(",")
                        .doTry()
                            .to("cxf://http://localhost:12345/test?dataFormat=RAW")
                        .doCatch(Throwable.class)
                            //.handled(true)
                        .end()
                        .log("OUT: ${body}")
                        .to("mock:result")
                    .end()
                    .delay(1000)
                    .log("OUT: ${body}")
                    .to("mock:result");
                // @formatter:on
            }
        };
    }

    @Test
    public void test() throws Exception {
        int count = 100;
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedMessageCount(count + 1);

        template.sendBody("seda:input",
            IntStream.range(0, count).mapToObj(Integer::toString).collect(Collectors.joining(",")));
        assertMockEndpointsSatisfied(5, TimeUnit.SECONDS);
    }

}
