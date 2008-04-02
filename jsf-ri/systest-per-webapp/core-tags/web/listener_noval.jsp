
<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>ActionListeners and ValueChangeListeners</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>ActionListeners and ValueChangeListeners</h1>

<f:view>

  <h:form>

<%-- ValueChangeListener with no attributes --%>

      <h:inputText id="text0" > 
          <f:valueChangeListener />
      </h:inputText>

<%-- ActionListener with no attributes --%>
      <h:commandButton id="button0" value="submit with listener no typebinding" > 
          <f:actionListener />
      </h:commandButton>

  </h:form>

</f:view>

    <hr>
  </body>
</html>
