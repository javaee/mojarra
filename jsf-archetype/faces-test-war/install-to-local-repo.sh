#!/bin/sh
cd create-from-project-2.2

mvn clean archetype:create-from-project

. ../run-after-create-from-project

cd target/generated-sources/archetype

mvn install

NOW=`date '+%Y%m%d-%H%M'`

echo "Execute these steps to test the just-installed archetype: mkdir /tmp/$NOW; cd /tmp/$NOW; mvn archetype:generate -DarchetypeCatalog=local"

echo "Manually inspect the created project in /tmp/$NOW"


