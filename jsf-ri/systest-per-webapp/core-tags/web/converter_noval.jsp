
<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Converters</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Converters</h1>

<f:view>

  <h:form>

<%-- Custom Converter with no no attributes --%>

      <h:inputText id="text1"> 
        <f:converter />
      </h:inputText>

<%-- DateTime Converter with no  attributes --%>

      <h:inputText id="text05" value="10:00:01 PM" size="10" maxlength="20">
         <f:convertDateTime />
      </h:inputText>

<%-- Number Converter with no attributes --%>

      <h:inputText id="texti09" value="9" size="10" maxlength="20">
         <f:convertNumber />
      </h:inputText>

      <h:commandButton value="submit converter page" />

  </h:form>

</f:view>

    <hr>
  </body>
</html>
