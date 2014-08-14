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
		
		|----resources
                        |----archetype-resources
                                |---src
                                     |----main
                                            |----java
                                                  |--com
                                                      |--sun
                                                          |--faces
                                                                |--hello
                                                                    |---HelloBean.java
                                            |----webapp
                                                    |--resources
                                                            |---composite
                                                                    |---simpleCompositeComponent.xhtml
                                                    |---simpleCompositeComponentUsingPage.xhml
                                                    |---submit.xhtml
|------pom.xml

3> Run "mvn  install" from under jsf-archetype/. This installs the archetype in the local maven repository.
4> Now,  Now that the archetype has been created and installed, you can try it on your local system by using the following commands. First create a new dir for the new project from this archetype. mkdir create-project-from-archetype.
5> mkdir create-project-from-archetype
   cd create-project-from-archetype/
6> mvn archetype:generate -DarchetypeCatalog=local

OR 

after deploying the archetype to a remote repo via "mvn deploy", run the following.
In this command, you need to specify the full information about the archetype you want to use (its groupId, its artifactId, its version) and the information about the new project you want to create (artifactId and groupId).
mvn archetype:generate -DarchetypeGroupId=com.sun.faces
-DarchetypeArtifactId=simple-jsf -DarchetypeVersion=1.0 -DgroupId=simple-jsf
-DartifactId=simple-jsf -DarchetypeRepository=local
The project that I created off of the archetype could be easily built and deployed to GF.
7> Next I tried the following to make sure that the war created gets deployed on GF.
cd create-project-from-archetype/<new-dir-created>
mvn clean install .. This creates the jsf-simple-1.0.war
8> Deploy the war via asadmin deploy jsf-simple-1.0.war
9> Access http://localhost:8080/jsf-sample-1.0/faces/simpleCompositeComponentUsingPage.xhtml

I have added some ant targets to do what is stated above.
Please run "ant make-archetype" to perform step 3 and 4.

To deploy the archetype to the maven repo (http://download.java.net/maven/2), run "ant deploy-archetype".

Alternatively, you can make modifications similar to the following to
the generated pom.xml and simply run mvn deploy in the
target/generated-sources/archetype directory.

bash-2.05b$ diff -u pom.xml ~/Documents/chaff/pom-generated-from-project.xml
--- pom.xml	Wed May  4 11:51:26 2011
+++ /home/ejburns/Documents/chaff/pom-generated-from-project.xml	Wed May  4 11:49:40 2011
@@ -16,6 +16,11 @@
         <artifactId>archetype-packaging</artifactId>
         <version>2.0</version>
       </extension>
+            <extension>
+                <groupId>org.jvnet.wagon-svn</groupId>
+                <artifactId>wagon-svn</artifactId>
+                <version>1.8</version>
+            </extension>
     </extensions>
 
     <pluginManagement>
@@ -26,5 +31,24 @@
         </plugin>
       </plugins>
     </pluginManagement>
+
   </build>
+
+    <distributionManagement>
+      <repository>
+        <uniqueVersion>false</uniqueVersion>
+        <id>java.net-maven2-repository</id>
+        <url>svn:https://svn.java.net/svn/maven2-repository~svn/trunk/repository</url> 
+      </repository>
+    </distributionManagement>
+
+    <repositories>
+        <repository>
+            <id>java.net-maven2-repository</id>
+            <name>Java.net Repository for Maven</name>
+            <url>http://download.java.net/maven/2/</url>
+            <layout>default</layout>
+        </repository>
+    </repositories>
+
 </project>

This assumes that your ~/.m2/settings.xml file has the following
information.

  <servers>
    <server>
        <id>java.net-maven2-repository</id>
        <username>your userid</username>
        <password>your password</password>
    </server>
  </servers>

References http://maven.apache.org/guides/mini/guide-creating-archetypes.html

