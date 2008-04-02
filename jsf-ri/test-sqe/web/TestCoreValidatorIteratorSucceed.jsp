<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<html>
<title>Validator Test Page</title>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
</head>
<body>

<h1>TLV c:iterator with JSF id</h1>
This page should succeed.
<br>
<br>

<f:view>

  <c:forEach begin="0" end="3"  var="i" varStatus="status">
    <c:set var="i" scope="request" value="${i}"/>
    <c:set var="status" scope="request" value="${status}"/>
    <c:set var="id" scope="request" value="foo${status.index}"/>
    Array[<c:out value="${i}"/>]: 
    <h:outputText id="#{id}" value="#{i}"/><br>
  </c:forEach>

</f:view>

</body>
</head>
</html>
