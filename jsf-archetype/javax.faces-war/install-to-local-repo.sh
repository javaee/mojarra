#!/bin/sh
cd create-from-project-2.2-m12

mvn clean archetype:create-from-project

patch -p0 < ../apply_to_pom.patch 

find target \( -name UserBean.java -or -name web.xml \) -exec perl -pi.bak -e "s/.[\{]groupId[\}]/javax.faces/g" {} \; -print

find . -name "*~" -exec rm -f {} \; -print; find . -name ".*~" -exec rm -f {} \; -print; find . -name ".*.bak" -exec rm -f {} \; -print; find . -name "*backup*" -exec rm -f {} \; -print; find . -name "*.bak" -exec rm -f {} \; -print;

cd target/generated-sources/archetype

mvn install

NOW=`date '+%Y%m%d-%H%M'`

echo "Execute these steps to test the just-installed archetype: mkdir /tmp/$NOW; cd /tmp/$NOW; mvn archetype:generate -DarchetypeCatalog=local"

echo "Manually inspect the created project in /tmp/$NOW"


