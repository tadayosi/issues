# ENTESB-5788 reproducer

<https://issues.jboss.org/browse/ENTESB-5788>

## Steps to reproduce

- Unzip the project and build it using `mvn clean install`
- In offline mode (no internet connectivity) deploy war file to `${JBOSS_HOME}/standalone/deployments`
- Start EAP, check logs.
- There should be error in logs:

~~~
08:58:01,821 WARN  [org.wildfly.extension.camel.SpringCamelContextFactory$1] (MSC service thread 1-2) Ignored XML validation warning: org.xml.sax.SAXParseException; lineNumber: 10; columnNumber: 78; schema_reference.4: Failed to read schema document 'http://www.springframework.org/schema/jee/spring-jee.xsd', because 1) could not find the document; 2) the document could not be read; 3) the root element of the document is not <xsd:schema>.
	at org.apache.xerces.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:196)
	at org.apache.xerces.util.ErrorHandlerWrapper.warning(ErrorHandlerWrapper.java:97)
	at org.apache.xerces.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:386)
	at org.apache.xerces.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:322)
	at org.apache.xerces.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:281)
	at org.apache.xerces.impl.xs.traversers.XSDHandler.reportSchemaWarning(XSDHandler.java:2529)
	at org.apache.xerces.impl.xs.traversers.XSDHandler.getSchemaDocument(XSDHandler.java:1834)
	at org.apache.xerces.impl.xs.traversers.XSDHandler.parseSchema(XSDHandler.java:521)
	at org.apache.xerces.impl.xs.XMLSchemaLoader.loadSchema(XMLSchemaLoader.java:554)
	at org.apache.xerces.impl.xs.XMLSchemaValidator.findSchemaGrammar(XMLSchemaValidator.java:2526)
	at org.apache.xerces.impl.xs.XMLSchemaValidator.handleStartElement(XMLSchemaValidator.java:1813)
	at org.apache.xerces.impl.xs.XMLSchemaValidator.emptyElement(XMLSchemaValidator.java:744)
	at org.apache.xerces.impl.XMLNSDocumentScannerImpl.scanStartElement(XMLNSDocumentScannerImpl.java:275)
	at org.apache.xerces.impl.XMLDocumentFragmentScannerImpl$FragmentContentDispatcher.dispatch(XMLDocumentFragmentScannerImpl.java:1653)
	at org.apache.xerces.impl.XMLDocumentFragmentScannerImpl.scanDocument(XMLDocumentFragmentScannerImpl.java:324)
	at org.apache.xerces.parsers.XML11Configuration.parse(XML11Configuration.java:845)
	at org.apache.xerces.parsers.XML11Configuration.parse(XML11Configuration.java:768)
	at org.apache.xerces.parsers.XMLParser.parse(XMLParser.java:108)
	at org.apache.xerces.parsers.DOMParser.parse(DOMParser.java:230)
	at org.apache.xerces.jaxp.DocumentBuilderImpl.parse(DocumentBuilderImpl.java:285)
	at org.springframework.beans.factory.xml.DefaultDocumentLoader.loadDocument(DefaultDocumentLoader.java:76)
	at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.doLoadDocument(XmlBeanDefinitionReader.java:429)
	at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.doLoadBeanDefinitions(XmlBeanDefinitionReader.java:391)
	at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.loadBeanDefinitions(XmlBeanDefinitionReader.java:336)
	at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.loadBeanDefinitions(XmlBeanDefinitionReader.java:304)
	at org.wildfly.extension.camel.SpringCamelContextFactory.createCamelContextList(SpringCamelContextFactory.java:112) [wildfly-camel-subsystem-2.3.0.redhat-621084.jar:]
	at org.wildfly.extension.camel.SpringCamelContextFactory.createCamelContextList(SpringCamelContextFactory.java:74) [wildfly-camel-subsystem-2.3.0.redhat-621084.jar:]
	at org.wildfly.extension.camel.deployment.CamelContextCreateProcessor.deploy(CamelContextCreateProcessor.java:59)
	at org.jboss.as.server.deployment.DeploymentUnitPhaseService.start(DeploymentUnitPhaseService.java:159)
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.startService(ServiceControllerImpl.java:1980)
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.run(ServiceControllerImpl.java:1913)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145) [rt.jar:1.7.0_99]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615) [rt.jar:1.7.0_99]
	at java.lang.Thread.run(Thread.java:745) [rt.jar:1.7.0_99]

08:58:01,834 ERROR [org.jboss.msc.service.fail] (MSC service thread 1-2) MSC000001: Failed to start service jboss.deployment.unit."sync-0.0.1.war".POST_MODULE: org.jboss.msc.service.StartException in service jboss.deployment.unit."sync-0.0.1.war".POST_MODULE: JBAS018733: Failed to process phase POST_MODULE of deployment "sync-0.0.1.war"
	at org.jboss.as.server.deployment.DeploymentUnitPhaseService.start(DeploymentUnitPhaseService.java:166) [jboss-as-server-7.5.0.Final-redhat-21.jar:7.5.0.Final-redhat-21]
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.startService(ServiceControllerImpl.java:1980) [jboss-msc-1.1.5.Final-redhat-1.jar:1.1.5.Final-redhat-1]
	at org.jboss.msc.service.ServiceControllerImpl$StartTask.run(ServiceControllerImpl.java:1913) [jboss-msc-1.1.5.Final-redhat-1.jar:1.1.5.Final-redhat-1]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145) [rt.jar:1.7.0_99]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615) [rt.jar:1.7.0_99]
	at java.lang.Thread.run(Thread.java:745) [rt.jar:1.7.0_99]
Caused by: java.lang.IllegalStateException: Cannot create camel context: sync-0.0.1.war
	at org.wildfly.extension.camel.deployment.CamelContextCreateProcessor.deploy(CamelContextCreateProcessor.java:63)
	at org.jboss.as.server.deployment.DeploymentUnitPhaseService.start(DeploymentUnitPhaseService.java:159) [jboss-as-server-7.5.0.Final-redhat-21.jar:7.5.0.Final-redhat-21]
	... 5 more
Caused by: org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException: Line 10 in XML document from URL [vfs:/content/sync-0.0.1.war/META-INF/jboss-camel-context.xml] is invalid; nested exception is org.xml.sax.SAXParseException; lineNumber: 10; columnNumber: 78; cvc-complex-type.2.4.c: The matching wildcard is strict, but no declaration can be found for element 'jee:jndi-lookup'.
	at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.doLoadBeanDefinitions(XmlBeanDefinitionReader.java:399)
	at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.loadBeanDefinitions(XmlBeanDefinitionReader.java:336)
	at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.loadBeanDefinitions(XmlBeanDefinitionReader.java:304)
	at org.wildfly.extension.camel.SpringCamelContextFactory.createCamelContextList(SpringCamelContextFactory.java:112)
	at org.wildfly.extension.camel.SpringCamelContextFactory.createCamelContextList(SpringCamelContextFactory.java:74)
	at org.wildfly.extension.camel.deployment.CamelContextCreateProcessor.deploy(CamelContextCreateProcessor.java:59)
	... 6 more
Caused by: org.xml.sax.SAXParseException; lineNumber: 10; columnNumber: 78; cvc-complex-type.2.4.c: The matching wildcard is strict, but no declaration can be found for element 'jee:jndi-lookup'.
	at org.apache.xerces.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:196)
	at org.apache.xerces.util.ErrorHandlerWrapper.error(ErrorHandlerWrapper.java:132)
	at org.apache.xerces.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:390)
	at org.apache.xerces.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:322)
	at org.apache.xerces.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:281)
	at org.apache.xerces.impl.xs.XMLSchemaValidator$XSIErrorReporter.reportError(XMLSchemaValidator.java:446)
	at org.apache.xerces.impl.xs.XMLSchemaValidator.reportSchemaError(XMLSchemaValidator.java:3271)
	at org.apache.xerces.impl.xs.XMLSchemaValidator.handleStartElement(XMLSchemaValidator.java:1993)
	at org.apache.xerces.impl.xs.XMLSchemaValidator.emptyElement(XMLSchemaValidator.java:744)
	at org.apache.xerces.impl.XMLNSDocumentScannerImpl.scanStartElement(XMLNSDocumentScannerImpl.java:275)
	at org.apache.xerces.impl.XMLDocumentFragmentScannerImpl$FragmentContentDispatcher.dispatch(XMLDocumentFragmentScannerImpl.java:1653)
	at org.apache.xerces.impl.XMLDocumentFragmentScannerImpl.scanDocument(XMLDocumentFragmentScannerImpl.java:324)
	at org.apache.xerces.parsers.XML11Configuration.parse(XML11Configuration.java:845)
	at org.apache.xerces.parsers.XML11Configuration.parse(XML11Configuration.java:768)
	at org.apache.xerces.parsers.XMLParser.parse(XMLParser.java:108)
	at org.apache.xerces.parsers.DOMParser.parse(DOMParser.java:230)
	at org.apache.xerces.jaxp.DocumentBuilderImpl.parse(DocumentBuilderImpl.java:285)
	at org.springframework.beans.factory.xml.DefaultDocumentLoader.loadDocument(DefaultDocumentLoader.java:76)
	at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.doLoadDocument(XmlBeanDefinitionReader.java:429)
	at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.doLoadBeanDefinitions(XmlBeanDefinitionReader.java:391)
	... 11 more
~~~
