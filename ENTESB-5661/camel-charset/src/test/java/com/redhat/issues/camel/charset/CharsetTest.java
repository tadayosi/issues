package com.redhat.issues.camel.charset;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharsetTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:input")
                        .log("${body}")
                        //.convertBodyTo(String.class, "UTF-8")
                        .convertBodyTo(String.class, "ISO-8859-1")
                        .log("${body}")
                        .to("mock:result");
            }
        };
    }

    @Test
    public void convertBodyTo() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedBodiesReceived("éabcd");
        //result.expectedBodiesReceived("あいうえお");

        //template.sendBody("direct:input", "éabcd".getBytes("UTF-8"));
        template.sendBody("direct:input", "éabcd".getBytes("ISO-8859-1"));
        //template.sendBody("direct:input", "あいうえお".getBytes("UTF-8"));
        //template.sendBody("direct:input", "あいうえお".getBytes("ISO-8859-1"));
        assertMockEndpointsSatisfied();
    }

}
