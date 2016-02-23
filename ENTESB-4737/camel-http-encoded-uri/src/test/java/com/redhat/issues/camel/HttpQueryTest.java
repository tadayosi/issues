package com.redhat.issues.camel;

import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.junit.After;
import org.junit.Before;
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
                from("netty-http:http://localhost:9000/camel?matchOnUriPrefix=true")
                    .to("http4://localhost:9009/fred?bridgeEndpoint=true");

                from("jetty:http://localhost:9001/camel?matchOnUriPrefix=true")
                    .to("http4://localhost:9009/fred?bridgeEndpoint=true");

                from("netty4-http:http://localhost:9002/camel?matchOnUriPrefix=true")
                    .to("http4://localhost:9009/fred?bridgeEndpoint=true");

                from("netty4-http:http://localhost:9003/camel?matchOnUriPrefix=true")
                    .to("netty4-http:http://localhost:9009/fred?bridgeEndpoint=true");
                // @formatter:on
            }
        };
    }

    private Server server;

    @Before
    public void startServer() throws Exception {
        server = new Server(9009);
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

    @After
    public void stopServer() throws Exception {
        if (server == null) return;
        server.stop();
        server = null;
    }

    @Test
    public void netty_http4() throws Exception {
        String query = "x=" + URLEncoder.encode(" :/?#[]@!$&'()+,;=", "UTF-8");
        LOG.info("Encoded query  = {}", query);
        Content response = Request.Get("http://localhost:9000/camel/?" + query).execute().returnContent();

        assertThat(response.asString(), is(query));
    }

    @Test
    public void jetty_http4() throws Exception {
        String query = "x=" + URLEncoder.encode(" :/?#[]@!$&'()+,;=", "UTF-8");
        LOG.info("Encoded query  = {}", query);
        Content response = Request.Get("http://localhost:9001/camel/?" + query).execute().returnContent();

        assertThat(response.asString(), is(query));
    }

    @Test
    public void netty4http_http4() throws Exception {
        String query = "x=" + URLEncoder.encode(" :/?#[]@!$&'()+,;=", "UTF-8");
        LOG.info("Encoded query  = {}", query);
        Content response = Request.Get("http://localhost:9002/camel/?" + query).execute().returnContent();

        assertThat(response.asString(), is(query));
    }

    @Test
    public void netty4http_netty4http() throws Exception {
        String query = "x=" + URLEncoder.encode(" :/?#[]@!$&'()+,;=", "UTF-8");
        LOG.info("Encoded query  = {}", query);
        Content response = Request.Get("http://localhost:9003/camel/?" + query).execute().returnContent();

        assertThat(response.asString(), is(query));
    }

}
