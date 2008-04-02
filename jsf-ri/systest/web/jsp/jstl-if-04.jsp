<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="s" uri="/WEB-INF/taglib.tld" %>

<f:view>
<html>
<head>
<title>jstl-if-04</title>
</head>
<body>
[1]
<c:if test="${param.cond}">
  <h:outputText       id="other2"  value="[2]"/>
  [3]
  <s:children id="kids">
    <s:output_verbatim id="kids4a">[4a]</s:output_verbatim>
    <h:outputText     id="kids4b" value="[4b]"/>
    <s:output_verbatim id="kids4c">[4c]</s:output_verbatim>
  </s:children>
  <h:outputText       id="other5" value="[5]"/>
  [6]
</c:if>
[7]
</body>
</html>
</f:view>
