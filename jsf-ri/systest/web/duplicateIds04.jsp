<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%--
  - @@copyright@@
  --%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<f:view>
  <html>
    <head>
      <title>Search Criteria</title>
    </head>
    <body>
      <h2>Search for items that:</h2>
      <h:form>
        <h:panelGrid columns="3" border="1" cellpadding="5" cellspacing="0"
                     binding="#{duplicateIds04.panelGrid}" />
        <br>
        <h:commandButton value="redisplay" />
      </h:form>
    </body>
  </html>
</f:view>
