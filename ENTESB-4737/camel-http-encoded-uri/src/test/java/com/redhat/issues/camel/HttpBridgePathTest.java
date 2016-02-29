package com.redhat.issues.camel;

import static org.hamcrest.Matchers.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpBridgePathTest extends BaseHttpBridgeTest {

    private static final Logger LOG = LoggerFactory.getLogger(HttpBridgePathTest.class);

    @Override
    protected String serverResponse(HttpServletRequest request) {
        return request.getPathInfo().substring("/fred/".length());
    }

    private static String PATH;
    static {
        try {
            PATH = URLEncoder.encode(" :/?#[]@!$&'()+,;=", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void netty_http4() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9000/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

    @Test
    public void jetty_http4() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9001/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

    @Test
    public void netty4http_http4() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9002/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

    @Test
    public void netty_netty4http() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9003/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

    @Test
    public void jetty_netty4http() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9004/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

    @Test
    public void netty4http_netty4http() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9005/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

    @Test
    public void netty_jetty() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9006/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

    @Test
    public void jetty_jetty() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9007/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

    @Test
    public void netty4http_jetty() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9008/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

    @Test
    public void netty_nettyhttp() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9009/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

    @Test
    public void jetty_nettyhttp() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9010/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

    @Test
    public void netty4http_nettyhttp() throws Exception {
        LOG.info("Encoded path   = {}", PATH);
        Content response = Request.Get("http://localhost:9011/camel/" + PATH).execute().returnContent();
        assertThat(response.asString(), is(PATH));
    }

}
