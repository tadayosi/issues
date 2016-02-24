package com.redhat.issues.camel;

import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpQueryTest extends CamelTestSupport {

    private static final Logger LOG = LoggerFactory.getLogger(HttpQueryTest.class);

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                // @formatter:off
                // to 'http4'
                from("netty-http:http://localhost:9000/camel?matchOnUriPrefix=true")
                    .to("http4://localhost:10000/fred?bridgeEndpoint=true");
                from("jetty:http://localhost:9001/camel?matchOnUriPrefix=true")
                    .to("http4://localhost:10000/fred?bridgeEndpoint=true");
                from("netty4-http:http://localhost:9002/camel?matchOnUriPrefix=true")
                    .to("http4://localhost:10000/fred?bridgeEndpoint=true");

                // to 'netty4-http'
                from("netty-http:http://localhost:9003/camel?matchOnUriPrefix=true")
                    .to("netty4-http:http://localhost:10000/fred?bridgeEndpoint=true");
                from("jetty:http://localhost:9004/camel?matchOnUriPrefix=true")
                    .to("netty4-http:http://localhost:10000/fred?bridgeEndpoint=true");
                from("netty4-http:http://localhost:9005/camel?matchOnUriPrefix=true")
                    .to("netty4-http:http://localhost:10000/fred?bridgeEndpoint=true");

                // to 'jetty'
                from("netty-http:http://localhost:9006/camel?matchOnUriPrefix=true")
                    .to("jetty:http://localhost:10000/fred?bridgeEndpoint=true");
                from("jetty:http://localhost:9007/camel?matchOnUriPrefix=true")
                    .to("jetty:http://localhost:10000/fred?bridgeEndpoint=true");
                from("netty4-http:http://localhost:9008/camel?matchOnUriPrefix=true")
                    .to("jetty:http://localhost:10000/fred?bridgeEndpoint=true");

                // to 'netty-http'
                from("netty-http:http://localhost:9009/camel?matchOnUriPrefix=true")
                    .to("netty-http:http://localhost:10000/fred?bridgeEndpoint=true");
                from("jetty:http://localhost:9010/camel?matchOnUriPrefix=true")
                    .to("netty-http:http://localhost:10000/fred?bridgeEndpoint=true");
                from("netty4-http:http://localhost:9011/camel?matchOnUriPrefix=true")
                    .to("netty-http:http://localhost:10000/fred?bridgeEndpoint=true");
                // @formatter:on
            }
        };
    }

    private static Server server;

    @BeforeClass
    public static void startServer() throws Exception {
        server = new Server(10000);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                LOG.info("Received query = {}", request.getQueryString());

                response.setContentType("text/plain; charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(request.getQueryString());
                baseRequest.setHandled(true);
            }
        });
        server.start();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        if (server == null) return;
        server.stop();
        server = null;
    }

    private static final String QUERY;
    static {
        try {
            QUERY = "x=" + URLEncoder.encode(" :/?#[]@!$&'()+,;=", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void netty_http4() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9000/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

    @Test
    public void jetty_http4() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9001/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

    @Test
    public void netty4http_http4() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9002/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

    @Test
    public void netty_netty4http() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9003/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

    @Test
    public void jetty_netty4http() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9004/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

    @Test
    public void netty4http_netty4http() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9005/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

    @Test
    public void netty_jetty() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9006/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

    @Test
    public void jetty_jetty() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9007/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

    @Test
    public void netty4http_jetty() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9008/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

    @Test
    public void netty_nettyhttp() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9009/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

    @Test
    public void jetty_nettyhttp() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9010/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

    @Test
    public void netty4http_nettyhttp() throws Exception {
        LOG.info("Encoded query  = {}", QUERY);
        Content response = Request.Get("http://localhost:9011/camel/?" + QUERY).execute().returnContent();
        assertThat(response.asString(), is(QUERY));
    }

}
