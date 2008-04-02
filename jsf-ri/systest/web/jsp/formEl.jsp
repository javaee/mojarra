<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test form enctype EL feature</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Test form enctype EL feature</h1>

<h2>Literal ids with prependId literal</h2>

<f:view>

    <h:outputText value="#{true ? 'multipart/form-data' :
        'application/x-www-form-urlencoded'}" />

    <h:form id="multiPart"
        enctype="#{true ? 'multipart/form-data' : 'application/x-www-form-urlencoded'}">
    </h:form>

    <h:form id="urlEncoded"
        enctype="#{false ? 'multipart/form-data' : 'application/x-www-form-urlencoded'}">
    </h:form>

    <h:form id="stringLiteral" enctype="hi"></h:form>

    <h:form id="elLiteral" enctype="#{'multipart/form-data'}"></h:form>
</f:view>

    <hr>
  </body>
</html>
