package com.redhat.issues.wildfly;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.security.PrivilegedAction;
import java.util.Arrays;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.security.auth.Subject;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.as.core.security.RealmUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/jmx")
public class JmxServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(JmxServlet.class);

    private static final String DOMAIN = "test-domain";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "p@ssw0rd";

    private MBeanServer mBeanServer;

    @Override
    public void init() {
        mBeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("GET: {}", request.getRequestURI());
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Subject subject = login();
        LOG.info("Subject: {}", subject);

        StringBuilder value = new StringBuilder();
        value.append(readMBeanAttribute(request));
        LOG.info("value = {}", value);

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.print(value);
        out.close();
    }

    private Subject login() {
        Subject subject = new Subject();
        LOG.info("Subject: {}", subject);
        try {
            new LoginContext(DOMAIN, subject, cs ->
                Arrays.stream(cs).forEach(c -> {
                    if (c instanceof NameCallback) {
                        ((NameCallback) c).setName(USERNAME);
                    } else if (c instanceof PasswordCallback) {
                        ((PasswordCallback) c).setPassword(PASSWORD.toCharArray());
                    } else {
                        LOG.debug("Unknown callback class: {}", c.getClass().getName());
                    }
                })).login();
            //subject.getPrincipals().add(new RealmUser(USERNAME)); // Uncommenting this resolves the issue
        } catch (LoginException e) {
            e.printStackTrace();
        }
        return subject;
    }

    private Object readMBeanAttribute(HttpServletRequest request) {
        try {
            return mBeanServer.getAttribute(
                new ObjectName(request.getParameter("name")),
                request.getParameter("attribute"));
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
