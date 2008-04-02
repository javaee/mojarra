<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test Tag ivars are cleared properly</title>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

  </head>

  <body>
    <h1>Test Tag ivars are cleared properly</h1>

<p><a
href="https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=36">https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=36</a></p>


<f:view>

<h:form>

<p>  <h:outputText value="component 1" /> </p>

<p>  <h:outputText value="component 2" /> </p>

<p>  <h:outputText value="component 3" /> </p>

<p>  <% Object hello = ""; hello = null; Object goodBye = hello.toString(); %> </p>

</h:form>

</f:view>

    <hr>
    <address><a href="mailto:b_edward@bellsouth.net">Ed Burns</a></address>
<!-- Created: Tue Aug 31 13:26:22 EDT 2004 -->
<!-- hhmts start -->
Last modified: Tue Aug 31 13:42:12 EDT 2004
<!-- hhmts end -->
  </body>
</html>
