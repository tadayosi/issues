# Programmatical Camel CXF Example with Hawtio Console

<https://issues.jboss.org/browse/ENTESB-6605>

## How to run

1. Run the following command:

        $ mvn hawtio:camel

2. Access hawtio Camel tab <http://localhost:8080/hawtio/>. Start and stop the route `cxf-greeting` several times.
3. Go to hawtio JMX tab and check CXF endpoint MBeans are growing.
