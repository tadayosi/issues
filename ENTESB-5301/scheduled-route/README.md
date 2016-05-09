# Camel Scheduled Route Sample

## To install

    $ mvn clean install

## To deploy

Run JBoss Fuse and in the karaf console run the following command:

    JBossFuse:karaf@root> install -s mvn:com.redhat.issues/scheduled-route/1.0

## To run

In the karaf console create test messages on the `TEST` queue:

    JBossFuse:karaf@root> activemq:producer --user admin --password admin
