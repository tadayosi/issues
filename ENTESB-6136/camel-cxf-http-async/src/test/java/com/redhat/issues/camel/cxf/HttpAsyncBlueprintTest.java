package com.redhat.issues.camel.cxf;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HttpAsyncBlueprintTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return getClass().getSimpleName() + ".xml";
    }

    @Test
    @Ignore("Issue can be reproduced only with HttpAsyncTest")
    public void cxfHttpAsync() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedBodiesReceived("test");

        template.sendBody("seda:input",
            IntStream.range(0, 100).mapToObj(Integer::toString).collect(Collectors.joining(",")));
        assertMockEndpointsSatisfied(5, TimeUnit.SECONDS);
    }

}
