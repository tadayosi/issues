package com.redhat.issues.camel.cxf;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TryCatchTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                // @formatter:off
                from("direct:input")
                    .log("IN:  ${body}")
                    .doTry()
                        .log("TRY: ${body}")
                        .to("cxf://http://localhost:12345/test?dataFormat=RAW")
                    .doCatch(Throwable.class)
                        .log("ERR: ${body} - ${exception}")
                    .end()
                    .log("OUT: ${body} - ${exception}")
                    .to("mock:result");
                // @formatter:on
            }
        };
    }

    @Test
    @Ignore("This is not used to reproduce the issue")
    public void test() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedBodiesReceived("test");

        template.sendBody("direct:input", "test");
        assertMockEndpointsSatisfied(60, TimeUnit.MINUTES);
    }

}
