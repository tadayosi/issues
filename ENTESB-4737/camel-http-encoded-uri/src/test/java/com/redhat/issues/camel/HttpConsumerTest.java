package com.redhat.issues.camel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConsumerTest extends CamelTestSupport {

    private static final Logger LOG = LoggerFactory.getLogger(HttpConsumerTest.class);

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                Processor log = new LogProcessor();
                from("jetty:http://localhost:9000/camel?matchOnUriPrefix=true").process(log).to("mock:result");
                from("netty-http:http://localhost:9001/camel?matchOnUriPrefix=true").process(log).to("mock:result");
                from("netty4-http:http://localhost:9002/camel?matchOnUriPrefix=true").process(log).to("mock:result");
            }
        };
    }

    private static class LogProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            for (String key : exchange.getIn().getHeaders().keySet()) {
                //if (key.startsWith("CamelHttp")) {
                LOG.info("{}\t= {}", key, exchange.getIn().getHeader(key));
                //}
            }
        }
    }

    private static final String URI;
    static {
        try {
            String encoded = URLEncoder.encode(" :/?#[]@!$&'()+,;=", "UTF-8");
            URI = String.format("aaa%s?x=%s", encoded, encoded);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void jetty() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedMessageCount(1);
        // @formatter:off
        result.expectedHeaderReceived(Exchange.HTTP_PATH,      "/aaa+%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2B%2C%3B%3D");
        result.expectedHeaderReceived(Exchange.HTTP_QUERY,     "x=+%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2B%2C%3B%3D");
        result.expectedHeaderReceived(Exchange.HTTP_RAW_QUERY, null);
        result.expectedHeaderReceived(Exchange.HTTP_URI,       "/camel/aaa+%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2B%2C%3B%3D");
        result.expectedHeaderReceived(Exchange.HTTP_URL,       "http://localhost:9000/camel/aaa+%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2B%2C%3B%3D");
        // @formatter:on

        Request.Get("http://localhost:9000/camel/" + URI).execute();
        assertMockEndpointsSatisfied();
    }

    @Test
    public void netty() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedMessageCount(1);
        // @formatter:off
        result.expectedHeaderReceived(Exchange.HTTP_PATH,      "/aaa+%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2B%2C%3B%3D");
        result.expectedHeaderReceived(Exchange.HTTP_QUERY,     "x=+:/?#[]@!$&'()+,;=");
        result.expectedHeaderReceived(Exchange.HTTP_RAW_QUERY, "x=+%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2B%2C%3B%3D");
        result.expectedHeaderReceived(Exchange.HTTP_URI,       "/camel/aaa+:/?#[]@!$&'()+,;=");
        result.expectedHeaderReceived(Exchange.HTTP_URL,       "http://localhost:9001/camel/aaa+%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2B%2C%3B%3D");
        // @formatter:on

        Request.Get("http://localhost:9001/camel/" + URI).execute();
        assertMockEndpointsSatisfied();
    }

    @Test
    public void netty4() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedMessageCount(1);
        // @formatter:off
        result.expectedHeaderReceived(Exchange.HTTP_PATH,      "/aaa+%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2B%2C%3B%3D");
        result.expectedHeaderReceived(Exchange.HTTP_QUERY,     "x=+:/?#[]@!$&'()+,;=");
        result.expectedHeaderReceived(Exchange.HTTP_RAW_QUERY, "x=+%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2B%2C%3B%3D");
        result.expectedHeaderReceived(Exchange.HTTP_URI,       "/camel/aaa+:/?#[]@!$&'()+,;=");
        result.expectedHeaderReceived(Exchange.HTTP_URL,       "http://localhost:9002/camel/aaa+%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2B%2C%3B%3D");
        // @formatter:on

        Request.Get("http://localhost:9002/camel/" + URI).execute();
        assertMockEndpointsSatisfied();
    }

}
