# Camel Route Sample: Master + Control Bus

## Install

    $ mvn clean install

## Deploy

Run JBoss Fuse in Fabric mode. Next, run:

    $ mvn fabric8:deploy

and then in the karaf console run the following command:

    JBossFuse:karaf@root> fabric:container-add-profile root issue-master-controlbus

## Run

In the karaf console create test messages on the `TEST` queue:

    JBossFuse:karaf@root> activemq:producer --user admin --password admin

Then observe ZK entries growing indefinitely by this command:

    JBossFuse:karaf@root> watch zk:list -r -d /fabric/registry/clusters/camel/master/issue-master-controlbus
