package com.redhat.issues.wildfly;

import java.io.IOException;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class JmxServletTest {

    private static final String TEST_NAME = JmxServletTest.class.getSimpleName();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, TEST_NAME + ".war")
            .addClass(JmxServlet.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsManifestResource("jboss-deployment-structure.xml", "jboss-deployment-structure.xml");
    }

    @Test
    public void test() throws IOException {
        String name = "java.lang:type=Memory";
        String attr = "HeapMemoryUsage";
        Content response = Request
            .Get(String.format("http://localhost:8080/%s/jmx?name=%s&attribute=%s", TEST_NAME, name, attr))
            .execute().returnContent();
        assertThat(response.asString()).doesNotContain("Unauthorized access");
    }

}
