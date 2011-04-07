JSF Maven archetype Document
Sheetal Vartak 3/17/2011

Goal
I have created a new Maven archetype for JSF that contains the following :
* a web project skeleton with a dependency on Java EE
* a minimal JSF application to use as a starting point

Archetype contents
The JSF archetype is made up of:
* an  archetype descriptor(archetype.xml in directory: src/main/resources/META-INF/maven/). It lists all the files that will be contained in the archetype and categorizes them so they can be processed correctly by the archetype generation mechanism.
* the prototype files that are copied by the archetype plugin (directory: src/main/resources/archetype-resources/)
* the prototype pom (pom.xml in: src/main/resources/archetype-resources)
* a pom for the archetype (pom.xml in the archetype's root directory).The config files listed above get auto generated via the  maven command : create-from-project.Creating the JSF archetype. For the JSF archetype creation, I followed the following steps:
1> create a dir jsf-archetype
2> cd jsf-archetype
Create a hierarchy as follows :
jsf-archetype
|------src
	|----main
		|----java
                        |--com
				|--sun
					|--faces
						|--hello
							|---HelloBean.java
		|----resources
		|----webapp
			|--resources
				|---composite
						|---simpleCompositeComponent.xhtml
				|---simpleCompositeComponentUsingPage.xhml
				|---submit.xhtml
|------pom.xml

3> mvn archetype:create-from-project. This generates the directory tree of the archetype in the target/generated-sources/archetype directory. The target dir gets created at the same level as the src dir and pom.xml.
4> cd target/generated-sources/archetype 
   mvn  install. This installs the archetype in the local maven repository.
5> Now,  Now that the archetype has been created and installed, you can try it on your local system by using the following commands. First create a new dir for the new project from this archetype. mkdir create-project-from-archetype.
6>cd create-project-from-archetype/
7> mvn archetype:generate -DarchetypeCatalog=local 

OR 

after deploying the archetype to a remote repo via "mvn deploy", run the following.
In this command, you need to specify the full information about the archetype you want to use (its groupId, its artifactId, its version) and the information about the new project you want to create (artifactId and groupId).
mvn archetype:create\
-DarchetypeGroupId=<archetype-groupId> \
-DarchetypeArtifactId=<archetype-artifactId>\
-DarchetypeVersion=<archetype-version> \
-DgroupId=<my.groupid> \
-DartifactId=<my-artifactId>
The project that I created off of the archetype could be easily built and deployed to GF.
8> Next I tried the following to make sure that the war created gets deployed on GF.
cd create-project-from-archetype/<new-dir-created>
mvn clean install .. This creates the jsf-sample-1.0.war
9> Deploy the war via asadmin deploy jsf-sample-1.0.war
10> Access http://localhost:8080/jsf-sample-1.0/faces/simpleCompositeComponentUsingPage.xhtml

References http://maven.apache.org/guides/mini/guide-creating-archetypes.html
