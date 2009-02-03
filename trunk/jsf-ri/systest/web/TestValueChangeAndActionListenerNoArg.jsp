<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test that method expressions pointing to no-arg methods work for valueChangeListener and actionListener</title>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
  </head>

  <body>
    <h1>Test that method expressions pointing to no-arg methods work for valueChangeListener and actionListener</h1>


<f:view>
    <h:form id="form" prependId="false">

      <p>

         <h:inputText id="username" value="#{test1.stringProperty}" 
                required="true"
             valueChangeListener="#{test1.valueChange0}"/>

      </p>

      <p>

	<h:commandButton id="loginEvent" value="Login" 
             actionListener="#{test1.actionListener0}">

	</h:commandButton>  

      </p>

<p>valueChange0Called: <h:outputText value="#{valueChange0Called}" /></p>

<p>actionListener0Called: <h:outputText value="#{actionListener0Called}" /></p>

    </h:form>
</f:view>

  </body>
</html>
