#!/bin/sh

#
# Set the following variables before running this script:
#    JSF_JAR - javax.faces.jar
#    DEMO_HOME - the top level location of this demo 
#        ex: MOJARRA_2_2X_ROLLING/jsf-demo/sandbox/html5/matrix
#

java -cp $JSF_JAR:$DEMO_HOME/target/classes:$DEMO_HOME/target/matrix/WEB-INF/lib/* -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=9009,suspend=n matrix.Main

