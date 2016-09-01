package com.redhat.issues.cxf.rest.async;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GreetingServiceTest {

    @Test
    public void hello() {
        GreetingService service = new GreetingService();
        assertThat(service.hello("CXF"), is("Hello, CXF!"));
    }

}
