
 Introduction
 ============

 Running tests against a Mojarra code base is using Maven as its carrier.
 In order for you to be able productive we are using several profiles so
 that you can pick and choose what you need for a particular scenario.

 So far the following scenarios have been identified:

	1. agnostic, tests not specific to a particular server.
	2. cluster, tests for a clustered server.
	3. selenium, tests that are browser specific.
	4. virtual, tests that execute on a virtual server.

 In the test master POM we currently have the following profiles available:

	1. integration-failsafe, runs the integration tests.
        2. integration-glassfish-embedded, starts and stops the Embedded Glassfish.
        3. integration-glassfish-cargo, starts and stops an installed Glassfish.
        4. integration-tomcat-cargo, starts and stops Tomcat 7.x
        5. integration-selenium, runs the tests using Selenium.

 Running tests
 =============

 If you want to run the tests you can do it in several ways. The main reason why
 there are different ways is because we want to be able to run tests using  a 
 CI system, but also to run a single test by a developer. See below in which 
 ways we support testing:

    1. Running all tests against a running container.
    2. Running all tests against an installed version of Glassfish (not running).
    3. Running all tests against Tomcat 7.x.
    4. Running a single test against a running container.
    5. Running a single test against an installed version of Glassfish (not running).
    6. Running a single test method against a running container.

 Scenario 1
 ----------

 To run all the tests against a running container use the following command line:

    mvn -Pintegration-failsafe clean verify

 Scenario 2
 ----------

 To run all the tests against an installed version of Glassfish (not running):

    mvn -Pintegration-failsafe,integration-glassfish-cargo clean verify

 Scenario 3
 ----------

 To run all the tests against Tomcat 7.x:

    mvn -Pintegration-failsafe,integration-tomcat-cargo clean verify

 Scenario 4
 ----------

 To run a single test against a running container:

    mvn -Pintegration-failsafe -Dit.test=IndexPageIT clean verify

 where IndexPageIT is the name of the test class (note you can use regular
 expressions to match more than one test class here).

 Scenario 5
 ----------

 To run a single test against an installed version of Glassfish (not running).

    mvn -Pintegration-failsafe,integration-glassfish-cargo -Dit.test=IndexPageIT clean verify

 Scenario 6
 ----------

 To run a single test and single method against a running container use:

    mvn -Pintegration-failsafe -Dit.test=VersionPageIT#testVerifyMojarraVersion clean verify

 where VersionPageIT is the name of the test class, and testVerifyMojarraVersion
 the name of the test method (note you can use regular expressions to match 
 multiple test and methods).


 System Properties
 =================

 As part of the test harness we use several system properties that can be 
 passed in on the command line to affect how the tests are run. The table
 below lists some of the properties used, see the master POMs for more 
 details.

    Properties used by integration-failsafe profile
    ***********************************************

        property name                       property default value
        -------------                       ----------------------
        integration.serverName              localhost
        integration.serverPort              8080
        integration.protocol                http
        jsf.artifactId                      javax.faces
        jsf.groupId                         org.glassfish
        jsf.version                         ${project.version}    
        it.test                             <no default>


    Properties used by integration-glassfish-embedded profile
    *********************************************************

        No specific properties, use the ones defined in the
        integration-failsafe profile.


    Properties used by integration-glassfish-cargo profile
    ******************************************************

        property name                       property default value
        -------------                       ----------------------
        integration.container.id            glassfish3x
        integration.container.home          C:/Glassfish3.1.1


    Properties used by integration-tomcat-cargo profile
    ****************************************************

        property name                       property default value
        -------------                       ----------------------
        integration.container.id            tomcat7x
        integration.container.downloadUrl   http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.16/bin/apache-tomcat-7.0.16.zip
        integration.container.downloadDir   C:/Temp/Cargo/download
        integration.container.extractDir    C:/Temp/Cargo/extracts
        integration.container.home          C:/Temp/Cargo/tomcat7                
        tomcat.version                      7.0.16


 Pitfalls
 ========

 Since we are assuming a particular way of deployment you as the developer will
 have to be aware of the following pitfalls that might crop up if you are doing
 testing against different application servers.

    1. Make sure the context root in glassfish-web.xml matches the project.build.finalName,
       if you do not the test harness will not be able to run the tests since it
       relies on the integration.url used by the integration-failsafe profile to
       be build up in the following way 

        ${integration.protocol}://${integration.serverName}:$integration.serverPort}/${project.build.finalName}/

 Writing tests
 =============

 To make it easier to write tests and to specify in which version this test is 
 relevant use the following 2 annotations.

 Eg.

   @RunWith(value=JsfTestRunner.class)
   @JsfTest(JsfVersion.JSF_2_1_XX)
   public class IssueXxxIT {
   }
 
--END
