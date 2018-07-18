# WildFly JMX Access Arquillian Test (Java Security Manager Enabled)

- <https://issues.jboss.org/browse/ENTESB-9089>

## How to run

1. Copy `standalone.xml` to `$WFLY_HOME/standalone/configuration/` and `test.policy` to `$WFLY_HOME/bin/`
2. Add user `admin`:
    
        $ ./bin/add-user.sh -u admin -p p@ssw0rd
    
3. Add the following line to the bottom of `$WFLY_HOME/bin/standalone.conf`:
    
        JAVA_OPTS="$JAVA_OPTS -Djava.security.policy==$JBOSS_HOME/bin/test.policy"
    
4. Start WildFly 10.1.0.Final with Java Security Manager enabled:
    
        $ ./bin/standalone.sh -secmgr
    
5. Run this test:
    
        $ mvn clean test

    You'll see the test fails showing the following error in the server log:
    > javax.management.JMRuntimeException: WFLYJMX0037: Unauthorized access
