package com.redhat.issues.camel;

import static org.hamcrest.Matchers.*;

import java.net.URLEncoder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpTest extends CamelTestSupport {

    private static final Logger LOG = LoggerFactory.getLogger(HttpTest.class);

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                // @formatter:off
                from("netty-http:http://localhost:9000/camel?matchOnUriPrefix=true")
                    .to("http4://localhost:9000/fred?bridgeEndpoint=true");
                
                from("jetty:http://localhost:9001/camel?matchOnUriPrefix=true")
                    .to("http4://localhost:9001/fred?bridgeEndpoint=true");
                // @formatter:on
            }
        };
    }

    @Test
    public void netty_http4() throws Exception {
        String encoded = URLEncoder.encode(" :/?#[]@!$&'()+,;=", "UTF-8");
        LOG.info("Encoded value = {}", encoded);
        HttpResponse response = Request.Get("http://localhost:9000/camel/?x=" + encoded).execute().returnResponse();

        assertThat(response.getStatusLine().getStatusCode(), is(200));
    }

    @Test
    public void jetty_http4() throws Exception {
        String encoded = URLEncoder.encode(" :/?#[]@!$&'()+,;=", "UTF-8");
        LOG.info("Encoded value = {}", encoded);
        HttpResponse response = Request.Get("http://localhost:9001/camel/?x=" + encoded).execute().returnResponse();

        assertThat(response.getStatusLine().getStatusCode(), is(200));
    }

}
