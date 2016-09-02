# CXF JAX-RS Continuation Sample

<https://issues.jboss.org/browse/ENTESB-5856>

## How to run

1. Run the server:

        $ mvn clean compile exec:java

2. Request an URL including `%20` to the server:

        $ curl http://localhost:9000/greeting/hello/Kayoko%20Ann%20Patterson
