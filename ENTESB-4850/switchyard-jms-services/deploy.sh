#!/bin/sh

if [ -z $EAP_HOME ]; then
    echo Specify EAP_HOME
    exit
fi

if [ "$1" = "clean" ]; then
    echo "Cleaning up $EAP_HOME/standalone/deployments/"
    rm -v $EAP_HOME/standalone/deployments/switchyard-jms-services-jms*.jar*
else
    echo "Deploying to $EAP_HOME/standalone/deployments/"
    cp -v jms*/target/switchyard-jms-services-jms*.jar $EAP_HOME/standalone/deployments/
fi
