package com.redhat.issues.cxf.jms;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GreetingServiceTest {

    @Test
    public void hello() {
        GreetingService service = new GreetingServiceImpl();
        assertThat(service.hello("CXF"), is("Hello, CXF!"));
    }

}
