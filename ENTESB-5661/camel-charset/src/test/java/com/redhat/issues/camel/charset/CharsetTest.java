package com.redhat.issues.camel.charset;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

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

    @Test
    public void javaString() throws Exception {
        String s1 = "éabcd";
        String s2 = "あいうえお";

        assertThat(new String(s1.getBytes("UTF-8"), "UTF-8"), is(s1));
        assertThat(new String(s1.getBytes("ISO-8859-1"), "ISO-8859-1"), is(s1));
        //assertThat(new String(s1.getBytes("ISO-8859-1"), "UTF-8"), is(s1)); // Fail!

        assertThat(new String(s2.getBytes("UTF-8"), "UTF-8"), is(s2));
        String garbled = new String(s2.getBytes("UTF-8"), "ISO-8859-1");
        assertThat(new String(garbled.getBytes("ISO-8859-1"), "UTF-8"), is(s2));
    }

}
