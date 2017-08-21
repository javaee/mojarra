# Mojarra

Oracle's implementation of the JavaServer Faces specification 


## Minimum Requirements

- Java 1.8
- Servlet 3.0 (4.0 recommended)
- EL 3.0 (3.1 recommended)
- CDI 1.2 (2.0 recommended)
- JSTL 1.2
- JSONP 1.1 (optional, only when `<f:websocket>` is used)
- BV 1.1 (2.0 recommended; optional, only when `<f:validateBean>` or `<f:validateWholeBean>` is used)

Servlet 4.0 will enable JSF 2.3 to serve resources via HTTP/2 push. CDI is explicitly required because since JSF 2.3 the `javax.faces.bean.*` annotations such as `@ManagedBean` are **deprecated**, and several implicit EL objects are produced via CDI producers, and `<f:websocket>` manages the WS sessions and events via CDI.


## Installation

Depending on the server used, JSF may already be built-in (full fledged Java EE containers such as [WildFly][1], [JBoss AS/EAP][2], [TomEE][3], [Payara][4], [GlassFish][5], [Liberty][6], etc.), or not (barebones JSP/Servlet containers such as [Tomcat][7], [Jetty][8], etc.). If the server doesn't ship with JSF built-in, then you need to manually install JSF 2.3 along with CDI 1.2+, JSONP 1.1+ and JSTL 1.2+ as those servlet containers usually also don't even ship with those JSF dependencies.

### Non-Maven

- **Java EE containers (WildFly, JBoss, TomEE, Payara, GlassFish, WebSphere, etc)**

    You don't need to add any JARs to `/WEB-INF/lib`!

- **Servletcontainers (Tomcat, Jetty, etc)**

    Add below JARs to `/WEB-INF/lib`:

    - [`javax.faces.2.3.x.jar`][9]
    - [`weld-servlet-shaded-3.0.0.Final.jar`][10]
    - [`jstl-1.2.jar`][11]
    - [`javax.json-api-1.1.jar`][12] (optional, only when `<f:websocket>` is used)
    - [`validation-api-2.0.0.Final.jar`][13](optional, only when `<f:validateBean>` or `<f:validateWholeBean>` is used)
    - [`hibernate-validator-6.0.1.Final.jar`][14](optional, only when `<f:validateBean>` or `<f:validateWholeBean>` is used)

    Substitute `x` with latest 2.3.x version number.

### Maven

In case you're using Maven, you can find below the necessary coordinates:

- **Java EE containers (WildFly, JBoss, TomEE, Payara, GlassFish, WebSphere, etc)**

    ```xml
    <dependency>
        <groupId>javax</groupId>
        <artifactId>javaee-web-api</artifactId>
        <version>7.0</version>
        <scope>provided</scope>
    </dependency>
    ```

    Note that Java EE 8.0 is currently not available yet. You should for now manually upgrade any JSF 2.2 library to JSF 2.3 depending on the server used.

- **Servletcontainers (Tomcat, Jetty, etc)**

    ```xml
    <dependency>
        <groupId>org.glassfish</groupId>
        <artifactId>javax.faces</artifactId>
        <version><!-- 2.3.x --></version>
    </dependency>
    <dependency>
        <groupId>org.jboss.weld.servlet</groupId>
        <artifactId>weld-servlet-shaded</artifactId>
        <version>3.0.0.Final</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
        <version>1.2</version>
    </dependency>
    <dependency> <!-- Optional, only when <f:websocket> is used. -->
        <groupId>javax.json</groupId>
        <artifactId>javax.json-api</artifactId>
        <version>1.1</version>
    </dependency>
    <dependency> <!-- Optional, only when <f:validateBean> or <f:validateWholeBean> is used. -->
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>6.0.1.Final</version>
    </dependency>
    ```

    You can check [`org.glassfish:javax.faces`][15] repository for current latest Mojarra release version.


## Hello World Example

We assume that you already know how to create an empty Maven WAR Project or Dynamic Web Project in your favourite IDE with a `/WEB-INF/web.xml` deployment descriptor file. Don't forget to add JSF JARs or configure pom.xml if necessary, as instructed in previous chapter.


### Controller

First register the `FacesServlet` in `/WEB-INF/web.xml` as below:

```xml
<servlet>
    <servlet-name>facesServlet</servlet-name>
    <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>facesServlet</servlet-name>
    <url-pattern>*.xhtml</url-pattern>
</servlet-mapping>
```

Noted should be that JSF 2.2+ is already "implicitly" registered and mapped on `*.jsf`, `*.faces` and `/faces/*` when running on a Servlet 3.0+ container. This will be overridden altogether when explicitly registering as above. [The `*.xhtml` URL pattern is preferred over above for security and clarity reasons][16]. JSF 2.3+ adds `*.xhtml` to set of default patterns.

### Model

Then create a backing bean class as below:

```java
package com.example;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Hello {

    private String name;
    private String message;

    public void createMessage() {
        message = "Hello, " + name + "!";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

}
```

Noted should be that in reality in the average Java EE application the above "model" is further breakdown into a JPA entity, an EJB service and a smaller backing bean. The JPA entity and EJB service then basically act as a true "model" and the backing bean becomes a "controller" for that model. This may in first place be confusing to starters, but it all depends on the point of view. See also [What components are MVC in JSF MVC framework?](https://stackoverflow.com/q/5104094/157882) and [JSF Controller, Service and DAO](https://stackoverflow.com/q/30639785/157882).

### View

Finally create a [Facelets][17] file `/hello.xhtml` as below:

```xml
<!DOCTYPE html>
<html lang="en"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:f="http://xmlns.jcp.org/jsf/core"
    xmlns:h="http://xmlns.jcp.org/jsf/html">
    <h:head>
        <title>Hello, World!</title>
    </h:head>
    <h:body>
        <h:form>
            <h:outputLabel for="name" value="Enter your name" required="true" />
            <h:inputText id="name" value="#{hello.name}" />
            <h:message for="name" />
            <br />
            <h:commandButton value="Say hello" action="#{hello.createMessage}">
                <f:ajax execute="@form" render="@form" />
            </h:commandButton>
            <br />
            #{hello.message}
        </h:form>
    </h:body>
</html>
```

Start the server and open it by `http://localhost:8080/contextname/hello.xhtml`.


## Resources

- [JSF 2.3 Specification (JSR 372)][18]
- [JSF 2.3 API documentation][19]
- [JSF 2.3 VDL documentation][20]
- [JSF 2.3 JS documentation][21]
- [Oracle Java EE 7 tutorial - JavaServer Faces Technology (JSF 2.2)][22]
- [What's new in JSF 2.3?][23]


  [1]: http://wildfly.org/
  [2]: http://www.jboss.org/jbossas
  [3]: http://tomee.apache.org
  [4]: http://www.payara.fish
  [5]: https://javaee.github.io/glassfish/
  [6]: https://developer.ibm.com/wasdev/websphere-liberty/
  [7]: http://tomcat.apache.org
  [8]: http://www.eclipse.org/jetty/
  [9]: http://central.maven.org/maven2/org/glassfish/javax.faces/
  [10]: http://central.maven.org/maven2/org/jboss/weld/servlet/weld-servlet-shaded/3.0.0.Final/weld-servlet-shaded-3.0.0.Final.jar
  [11]: http://central.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar
  [12]: http://central.maven.org/maven2/javax/json/javax.json-api/1.1/javax.json-api-1.1.jar
  [13]: http://central.maven.org/maven2/javax/validation/validation-api/2.0.0.Final/validation-api-2.0.0.Final.jar
  [14]: http://central.maven.org/maven2/org/hibernate/validator/hibernate-validator/6.0.1.Final/hibernate-validator-6.0.1.Final.jar
  [15]: http://mvnrepository.com/artifact/org.glassfish/javax.faces
  [16]: https://stackoverflow.com/q/3008395/157882
  [17]: http://docs.oracle.com/javaee/7/tutorial/jsf-facelets.htm
  [18]: http://download.oracle.com/otn-pub/jcp/jsf-2_3-final-eval-spec/JSF_2.3.pdf
  [19]: https://javaserverfaces.github.io/docs/2.3/javadocs/index.html
  [20]: https://javaserverfaces.github.io/docs/2.3/vdldoc/index.html
  [21]: https://javaserverfaces.github.io/docs/2.3/jsdocs/index.html
  [22]: http://docs.oracle.com/javaee/7/tutorial/jsf-intro.htm
  [23]: http://arjan-tijms.omnifaces.org/p/jsf-23.html
