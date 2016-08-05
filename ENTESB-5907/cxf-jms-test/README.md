# Simple CXF JMS Transport Test

## Prepare

Run JBoss Fuse/A-MQ and create the following two queues in ActiveMQ:

- `sample.ws.greeting.request`
- `sample.ws.greeting.response`

## Server

Run the server locally:

    $ mvn clean compile exec:java

## Client

Run the test client:

    $ mvn test -Dtest=GreetingClient
