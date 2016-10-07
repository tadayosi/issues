# Camel Transport for CXF Test

<https://issues.jboss.org/browse/ENTESB-6028>

## How to run

    $ mvn test -Dtest=CamelTransportJAXRSTest

The test should eventually fail with the following error:
~~~
javax.ws.rs.ProcessingException: Response timeout
	at org.apache.cxf.jaxrs.client.AbstractClient.waitForResponseCode(AbstractClient.java:617)
	at org.apache.cxf.jaxrs.client.AbstractClient.checkClientException(AbstractClient.java:598)
	at org.apache.cxf.jaxrs.client.AbstractClient.preProcessResult(AbstractClient.java:580)
	at org.apache.cxf.jaxrs.client.WebClient.doResponse(WebClient.java:1098)
	at org.apache.cxf.jaxrs.client.WebClient.doChainedInvocation(WebClient.java:1035)
	at org.apache.cxf.jaxrs.client.WebClient.doInvoke(WebClient.java:892)
	at org.apache.cxf.jaxrs.client.WebClient.doInvoke(WebClient.java:863)
	at org.apache.cxf.jaxrs.client.WebClient.invoke(WebClient.java:329)
	at org.apache.camel.component.cxf.jaxrs.CxfRsProducer.invokeHttpClient(CxfRsProducer.java:210)
	at org.apache.camel.component.cxf.jaxrs.CxfRsProducer.process(CxfRsProducer.java:89)
	at org.apache.camel.util.AsyncProcessorConverterHelper$ProcessorToAsyncProcessorBridge.process(AsyncProcessorConverterHelper.java:61)
	at org.apache.camel.processor.SendProcessor.process(SendProcessor.java:145)
	at org.apache.camel.management.InstrumentationProcessor.process(InstrumentationProcessor.java:77)
	at org.apache.camel.processor.RedeliveryErrorHandler.process(RedeliveryErrorHandler.java:468)
	at org.apache.camel.processor.CamelInternalProcessor.process(CamelInternalProcessor.java:197)
	at org.apache.camel.processor.Pipeline.process(Pipeline.java:121)
	at org.apache.camel.processor.Pipeline.process(Pipeline.java:83)
	at org.apache.camel.processor.CamelInternalProcessor.process(CamelInternalProcessor.java:197)
	at org.apache.camel.component.direct.DirectProducer.process(DirectProducer.java:62)
	at org.apache.camel.processor.CamelInternalProcessor.process(CamelInternalProcessor.java:197)
	at org.apache.camel.util.AsyncProcessorHelper.process(AsyncProcessorHelper.java:109)
	at org.apache.camel.processor.UnitOfWorkProducer.process(UnitOfWorkProducer.java:68)
	at org.apache.camel.impl.ProducerCache$2.doInProducer(ProducerCache.java:412)
	at org.apache.camel.impl.ProducerCache$2.doInProducer(ProducerCache.java:380)
	at org.apache.camel.impl.ProducerCache.doInProducer(ProducerCache.java:270)
	at org.apache.camel.impl.ProducerCache.sendExchange(ProducerCache.java:380)
	at org.apache.camel.impl.ProducerCache.send(ProducerCache.java:221)
	at org.apache.camel.impl.DefaultProducerTemplate.send(DefaultProducerTemplate.java:124)
	at org.apache.camel.impl.DefaultProducerTemplate.sendBody(DefaultProducerTemplate.java:137)
	at org.apache.camel.impl.DefaultProducerTemplate.sendBody(DefaultProducerTemplate.java:144)
	at com.redhat.issues.camel.cxf.CamelTransportJAXRSTest.test(CamelTransportJAXRSTest.java:51)
	[...]
~~~
