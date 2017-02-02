package com.redhat.issues.twilio;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TwilioTestBase {

    protected static final int SERVER_PORT = 18080;

    private static final String NGROK_PATH = "/usr/local/bin/ngrok";
    private static Optional<Process> NGROK_PROCESS = Optional.empty();

    @BeforeClass
    public static void startNgrok() throws Exception {
        NGROK_PROCESS = Optional.of(
            new ProcessBuilder(NGROK_PATH, "http", Integer.toString(SERVER_PORT)).start());
    }

    @AfterClass
    public static void stopNgrok() {
        NGROK_PROCESS.ifPresent(p -> p.destroy());
    }

    protected void start(Server server) throws Exception {
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                resp.setContentType("application/xml");
                resp.getWriter().print("<message>Hello!</message>");
            }
        }), "/*");
        server.start();
    }

    protected void stop(Server server) throws Exception {
        server.stop();
        server.destroy();
    }

}
