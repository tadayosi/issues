package com.redhat.issues.camel.cxf;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.cxf.message.Message;
import org.junit.Test;

public class CamelTransportJAXRSTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return getClass().getSimpleName() + ".xml";
    }

    @Override
    protected String getBundleDirectives() {
        // CXF Transport conduit wildcard ("*.camel-conduit") requires this
        return "blueprint.aries.xml-validation:=false";
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                // Camel Transport
                from("direct:input.camel").id("route-cxf-client").streamCaching()
                    .log("IN:  ${body} : ${headers}")
                    .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                    .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                    .setHeader(Exchange.HTTP_URI, simple("/greeting/hello/${body}"))
                    //.setHeader(Exchange.HTTP_PATH, simple("/greeting/hello/${body}"))
                    .inOut("cxfrs:bean:rsClient")
                    .log("OUT: ${body} : ${headers}")
                    .to("mock:result");
                from("direct:cxf.in").id("route-cxf-server").streamCaching()
                    .log("> IN:  ${body} : ${headers}")
                    .to("direct:cxf.out")
                    // workaround
                    //.setHeader(Message.RESPONSE_CODE, header(Exchange.HTTP_RESPONSE_CODE))
                    .log("> OUT: ${body} : ${headers}");

                // HTTP Transport
                from("direct:input.http").id("route-http").streamCaching()
                    .log("IN:  ${body} : ${headers}")
                    .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                    .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                    //.setHeader(Exchange.HTTP_URI, simple("/greeting/hello/${body}"))
                    .setHeader(Exchange.HTTP_PATH, simple("/greeting/hello/${body}"))
                    .inOut("cxfrs:bean:rsClient_http")
                    .log("OUT: ${body} : ${headers}")
                    .to("mock:result");
            }
        };
    }

    @Test
    public void camelTransport() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedBodiesReceived("Hello, Test!");

        template.sendBody("direct:input.camel", "Test");
        assertMockEndpointsSatisfied();
    }

    @Test
    public void httpTransport() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedBodiesReceived("Hello, Test!");

        template.sendBody("direct:input.http", "Test");
        assertMockEndpointsSatisfied();
    }

}
