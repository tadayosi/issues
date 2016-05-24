# Camel Scheduled Route Sample

## To install

    $ mvn clean install

## To deploy

Run JBoss Fuse in Fabric mode. Next, run:

    $ mvn fabric8:deploy

and then in the karaf console run the following command:

    JBossFuse:karaf@root> fabric:container-add-profile root issue-scheduled-route

## To run

Run:

    $ mvn exec:java

or in the karaf console create test messages on the `TEST` queue:

    JBossFuse:karaf@root> activemq:producer --user admin --password admin
