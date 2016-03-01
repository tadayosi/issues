package com.redhat.issues.camel;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseHttpBridgeTest extends CamelTestSupport {

    private static final Logger LOG = LoggerFactory.getLogger(BaseHttpBridgeTest.class);

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

    private Server server;

    @Before
    public void startServer() throws Exception {
        server = new Server(10000);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                LOG.info("Received URI   = {}", request.getRequestURI());
                LOG.info("Received path  = {}", request.getPathInfo());
                LOG.info("Received query = {}", request.getQueryString());

                response.setContentType("text/plain; charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(serverResponse(request));
                baseRequest.setHandled(true);
            }
        });
        server.start();
    }

    protected abstract String serverResponse(HttpServletRequest request);

    @After
    public void stopServer() throws Exception {
        if (server == null) return;
        server.stop();
        server = null;
    }

}
