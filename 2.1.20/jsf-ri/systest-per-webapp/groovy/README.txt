 This test has been disabled for now since it needs all the following jars in the web/WEB-INF/lib dir.
 Need to make this work with all the jars in the app server land.

 Groovy support in JSFs is working. The following JSF artifacts have been implemented as Groovy scripts :

- ManagedBeans
- Converter
- Validator
- UIComponent
- Renderer

The package structure for jsf-ri/systest-per-webapp/groovy looks as follows :

src/java/com/sun/faces/systest/GroovyTestCase.java
src/java/com/sun/faces/systest/Name.java (just a POJO)

web/WEB-INF/groovy/hello/AgeComponent.groovy -->    Groovy script as a UIComponent
web/WEB-INF/groovy/hello/MessageOutput.groovy   --> Groovy script as a UIComponent  (does'nt do much. But I see it getting initialized in the server log)
web/WEB-INF/groovy/hello/NameConverter.groovy --> as a converter (converts the name to upper case)
web/WEB-INF/groovy/hello/HelloBean.groovy        --> as a managedbean (holds the name and age)
web/WEB-INF/groovy/hello/MessageRenderer.groovy  --> as a renderer (sends back the message in a certain HTML format).
web/WEB-INF/groovy/hello/NameValidator.groovy  --> as a validator (makes sure the age is between 0 and 65)

Add the following groovy related jars to web/WEB-INF/lib:
web/WEB-INF/lib/antlr-2.7.7.jar
web/WEB-INF/lib/asm-commons-3.2.jar
web/WEB-INF/lib/groovy-1.7.3.jar
web/WEB-INF/lib/asm-3.2.jar
web/WEB-INF/lib/asm-tree-3.2.jar
web/WEB-INF/lib/asm-analysis-3.2.jar
web/WEB-INF/lib/asm-util-3.2.jar

Next are the JSF related jars (add them to web/WEB-INF/lib):
web/WEB-INF/lib/jsf-api.jar
web/WEB-INF/lib/jsf-impl.jar

The next 2 jars are needed from the GF land (else we see ClassCastExceptions related to casting a class loaded by GF class loader to a class loaded by the Mojarra class loader. I'm not very happy about putting GF jars into the app. Will need to find a new solution later)
web/WEB-INF/lib/weld-integration.jar
web/WEB-INF/lib/osgi-web-container.jar

web/WEB-INF/web.xml (has some key settings needed to enable Groovy)
web/Web-inf/sun-web.xml (has the setting to use the libraries that are packaged and over ride the ones in the GF install dir)
web/hello.xhtml
web/submit.xhtml


What worked :
1>Package the devtest with web/WEB-INF/lib containing 1> the groovy jars (groovy-all-*.jar or groovy.jar with the asm jars and the antlr jar) 2> weld-integration.jar 3> osgi-web-container.jar 4> jsf RI and API jars
2> add a sun-web.xml to ignore the default impl and use what's packaged with the app.
3> add faces-config.xml with contents as follows :
<managed-bean>
    <managed-bean-name>hello</managed-bean-name>
    <managed-bean-class>hello/HelloBean</managed-bean-class>
    <managed-bean-scope>session</managed-bean-scope>
  </managed-bean>
.........

Notice how the bean has been specified with a '/' and not a '.' for the package name. One may or may not add the '.groovy' extension. This change seems a bit odd since '.' seems to be the right way of specifying a class. Maybe the Mojarra class loader needs to be modified to accept '.' and convert it to '/' ?

what did not work as of now :
I tried adding the jars to Glassfish v3 lib dir instead of packaging them into the app. Added the OSGi module of groovy.jar to the modules dir in GF. But I'm still having some trouble getting it to work. It might just be something trivial. Will spend some time on this a little later.
