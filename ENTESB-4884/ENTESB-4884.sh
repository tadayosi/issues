#!/bin/sh

if [ "$1" == "rollback" ]; then
  echo "Rolling back..."
  cp ENTESB-4884/fabric8-karaf-1.2.0.redhat-621084-features.xml.orig system/io/fabric8/fabric8-karaf/1.2.0.redhat-621084/fabric8-karaf-1.2.0.redhat-621084-features.xml
  cp ENTESB-4884/apache-camel-2.15.1.redhat-621084-features.xml.orig system/org/apache/camel/karaf/apache-camel/2.15.1.redhat-621084/apache-camel-2.15.1.redhat-621084-features.xml
else
  echo "Upgrading swagger..."
  cp ENTESB-4884/fabric8-karaf-1.2.0.redhat-621084-features.xml system/io/fabric8/fabric8-karaf/1.2.0.redhat-621084/fabric8-karaf-1.2.0.redhat-621084-features.xml
  cp ENTESB-4884/apache-camel-2.15.1.redhat-621084-features.xml system/org/apache/camel/karaf/apache-camel/2.15.1.redhat-621084/apache-camel-2.15.1.redhat-621084-features.xml
fi
