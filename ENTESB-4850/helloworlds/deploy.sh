#!/bin/sh

if [ -z $EAP_HOME ]; then
    echo Specify EAP_HOME
    exit
fi

if [ "$1" = "clean" ]; then
    echo "Cleaning up $EAP_HOME/standalone/deployments/"
    rm -v $EAP_HOME/standalone/deployments/helloworld*.war*
else
    echo "Deploying to $EAP_HOME/standalone/deployments/"
    cp -v helloworld*/target/helloworld*.war $EAP_HOME/standalone/deployments/
fi
