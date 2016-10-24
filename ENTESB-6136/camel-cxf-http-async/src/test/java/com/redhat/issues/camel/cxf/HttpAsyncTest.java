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
                from("direct:input")
                    .log("IN:  ${body}")
                    .split().tokenize(",")
                        .log("> IN:  ${body}")
                        .doTry()
                            .log("> TRY: ${body}")
                            .to("cxf://http://localhost:12345/test?dataFormat=RAW")
                        .doCatch(Throwable.class)
                            //.handled(true)
                            .log("> ERR: ${body} - ${exception}")
                        .end()
                        .log("> OUT: ${body} - ${exception}")
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
        int count = 20;
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedMessageCount(count + 1);

        template.sendBody("direct:input",
            IntStream.range(0, count).mapToObj(Integer::toString).collect(Collectors.joining(",")));
        assertMockEndpointsSatisfied(5, TimeUnit.SECONDS);
    }

}
