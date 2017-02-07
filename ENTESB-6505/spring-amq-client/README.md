# Spring ActiveMQ Client Sample

https://issues.jboss.org/browse/ENTESB-6505

## How to reproduce

1. Install a JBoss Fuse 6 instance and append the following lines to `etc/users.properties`:

        #test=test,admin,manager,viewer,Operator, Maintainer, Deployer, Auditor, Administrator, SuperUser
        test=test,User

2. Launch JBoss Fuse.

3. Run this sample:

        $ mvn spring-boot:run

4. Change `etc/users.properties` as follows and restart JBoss Fuse (while keep this sample running):

        test=test,admin,manager,viewer,Operator, Maintainer, Deployer, Auditor, Administrator, SuperUser
        #test=test,User

5. Check the number of consumers connecting to queue `TEST`. 
