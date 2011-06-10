The maven modules in this directory are empty projects which are used as
input to the mvn archetype:create-from-project plugin invocation.  This
README documents how to produce an archetype from one of these maven
modules and deploy it to the java.net maven2 archetype-catalog of the
java.net maven2 repository, at
<http://download.java.net/maven/2/archetype-catalog.xml>.

The example will use jsf2-simple as the maven module, but you can
substitute any other module within this directory for jsf2-simple.

1. Make sure everything is cleaned

  cd jsf2-simple

  mvn clean

2. Generate the archetype

  mvn archetype:create-from-project

3. Modify the generated pom so it can be deployed to the java.net repository

  cd target/generated-sources/archetype/

  $EDITOR pom.xml

  Add the following XML elements.

  3.a To the <build><extensions> section add

      <extension>
        <groupId>org.jvnet.wagon-svn</groupId>
        <artifactId>wagon-svn</artifactId>
        <version>1.8</version>
      </extension>

  3.b To the <repositories> section (create if necessary) add

    <repository>
      <id>maven2-repository.dev.java.net</id>
      <name>Java.net Repository for Maven</name>
      <url>http://download.java.net/maven/2/</url>
    </repository>

  3.c To the <distributionManagement> section (create if necessary) add

    <repository>
      <id>java.net-m2-repository</id>
      <uniqueVersion>false</uniqueVersion>
      <url>java-net:/maven2-repository/trunk/repository/</url>
    </repository>

  3.d Fix the groupId, artifactId, name, and version to be correct.  For
   jsf2-simple the correct values are:

  <groupId>javax.faces</groupId>
  <artifactId>jsf2-simple-example-archetype</artifactId>
  <name>example-archetype</name>
  <version>0.1-SNAPSHOT</version>

    Of course, version must be updated.

4. Secure proper permission from Kohsuke to deploy to the java.net
   repository and do

   mvn install deploy

5. Update the java.net maven2 repository archetype-catalog.xml

   Wait about an hour after successful deployment and verify that the
   archetype artifact does indeed appear in the public repository.  When
   it does, edit the archetype-catalog.xml to add the deployed archetype
   so it can be available to users.

% svn co --depth files https://maven2-repository.dev.java.net/svn/maven2-repository/trunk/repository maven2-repository
% cd maven2-repository
% $EDITOR archetype-catalog.xml

  Add the necessary entry for the new archetype.  The jsf2-simple
  archetype looks like this.

    <archetype>
      <groupId>javax.faces</groupId>
      <artifactId>jsf2-simple-example-archetype</artifactId>
      <version>0.1-SNAPSHOT</version>
      <repository>http://download.java.net/maven/2</repository>
      <description>Simple JSF project with no non-JavaEE dependencies</description>
    </archetype>

  And appears within the <archetypes> element.

% svn ci   
