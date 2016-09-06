package com.redhat.issues.jetty;

import org.apache.http.client.fluent.Request;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;

public class ContinuationTest {

    private static final Logger LOG = LoggerFactory.getLogger(ContinuationTest.class);

    private Server server;

    @Before
    public void startServer() throws Exception {
        server = new Server(18080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(new HelloServlet()), "/hello/*");
        handler.addServletWithMapping(new ServletHolder(new HelloJettyServlet()), "/hello-jetty/*");
        server.start();
    }

    @After
    public void stopServer() throws Exception {
        server.stop();
        server.destroy();
    }

    @Test
    public void servlet() throws Exception {
        String response = Request
            .Get("http://localhost:18080/hello/Kayoko%20Ann%20Patterson")
            .execute().returnContent().asString();
        assertThat(response).isEqualTo("Hello, Kayoko%20Ann%20Patterson!");
    }

    @Test
    public void jetty() throws Exception {
        String response = Request
            .Get("http://localhost:18080/hello-jetty/Kayoko%20Ann%20Patterson")
            .execute().returnContent().asString();
        assertThat(response).isEqualTo("Hello, Kayoko%20Ann%20Patterson!");
    }

    static class HelloServlet extends HttpServlet {
        private boolean started = false;
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            if (!started) {
                LOG.info("Initial");
                final AsyncContext context = req.startAsync(req, resp);
                context.addListener(new AsyncListener() {
                    @Override
                    public void onTimeout(AsyncEvent event) throws IOException {
                        context.dispatch();
                    }

                    @Override
                    public void onComplete(AsyncEvent event) {}
                    @Override
                    public void onError(AsyncEvent event) {}
                    @Override
                    public void onStartAsync(AsyncEvent event) {}
                });
                context.setTimeout(1000);
                started = true;
            } else {
                LOG.info("Resumed");
                PrintWriter out = resp.getWriter();
                String message = String.format("Hello, %s!", req.getRequestURI().substring(req.getServletPath().length() + 1));
                out.print(message);
            }
            LOG.info("RequestURI = {}", req.getRequestURI());
            LOG.info("RequestURL = {}", req.getRequestURL());
        }
    }

    static class HelloJettyServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Continuation cont = ContinuationSupport.getContinuation(req);
            if (cont.isInitial()) {
                LOG.info("Initial");
                cont.setTimeout(1000);
                cont.suspend();
            } else {
                LOG.info("Resumed");
                PrintWriter out = resp.getWriter();
                String message = String.format("Hello, %s!", req.getRequestURI().substring(req.getServletPath().length() + 1));
                out.print(message);
            }
            LOG.info("RequestURI = {}", req.getRequestURI());
            LOG.info("RequestURL = {}", req.getRequestURL());
        }
    }

}
