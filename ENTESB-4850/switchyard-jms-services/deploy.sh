#!/bin/sh

if [ -z $EAP_HOME ]; then
    echo Specify EAP_HOME
    exit
fi

cp -v jms*/target/switchyard-jms-services-jms*.jar $EAP_HOME/standalone/deployments/
