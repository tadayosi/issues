#!/bin/sh

if [ -e org.apache.servicemix.bundles.xalan-2.7.2_2.jar ]; then
  echo "Xalan 2.7.2_3 -> 2.7.2_2"
  mv org.apache.servicemix.bundles.xalan-2.7.2_2.jar lib/endorsed/
  mv lib/endorsed/org.apache.servicemix.bundles.xalan-2.7.2_3-SNAPSHOT.jar .
else
  echo "Xalan 2.7.2_2 -> 2.7.2_3"
  mv org.apache.servicemix.bundles.xalan-2.7.2_3-SNAPSHOT.jar lib/endorsed/
  mv lib/endorsed/org.apache.servicemix.bundles.xalan-2.7.2_2.jar .
fi
