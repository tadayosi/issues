package com.redhat.issues.jetty;

import org.apache.http.client.fluent.Request;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;

public class ContinuationTest {

    private static final Logger LOG = LoggerFactory.getLogger(ContinuationTest.class);

    @Test
    public void test() throws Exception {
        Server server = new Server(18080);
        try {
            ServletHandler handler = new ServletHandler();
            server.setHandler(handler);
            handler.addServletWithMapping(new ServletHolder(new HelloServlet()), "/hello/*");
            server.start();

            String response = Request
                .Get("http://localhost:18080/hello/Kayoko%20Ann%20Patterson")
                .execute().returnContent().asString();
            assertThat(response).isEqualTo("Hello, Kayoko Ann Patterson!");
        } finally {
            server.stop();
            server.destroy();
        }
    }

    static class HelloServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            LOG.info("Async      = {}", req.isAsyncSupported());
            LOG.info("RequestURI = {}", req.getRequestURI());
            LOG.info("RequestURL = {}", req.getRequestURL());

            Continuation cont = ContinuationSupport.getContinuation(req);
            if (cont.isInitial()) {
                LOG.info("Initial");
                cont.setTimeout(1000);
                cont.suspend();
            } else {
                LOG.info("Resumed");
                PrintWriter out = resp.getWriter();
                String message = String.format("Hello, %s!", req.getPathInfo().substring(1));
                out.print(message);
            }
        }
    }

}
