# WildFly JMX Access Arquillian Test

- <https://issues.jboss.org/browse/ENTESB-6281>
- <https://issues.jboss.org/browse/WFCORE-2061>

## How to run

1. Copy `standalone.xml` to `$WFLY_HOME/standalone/configuration/`
2. Add user `admin`:
    
        $ ./bin/add-user.sh -u admin -p p@ssw0rd
    
3. Start WildFly 10.1.0.Final:
    
        $ ./bin/standalone.sh
    
4. Run this test:
    
        $ mvn clean test

    You'll see the test fails showing the following error in the server log:
    > javax.management.JMRuntimeException: WFLYJMX0037: Unauthorized access
