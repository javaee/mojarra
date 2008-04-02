<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:subview id="subviewInner">
    <br>
    <c:forEach var="i" begin="0" end="3" varStatus="status">
        Array[<c:out value="${i}"/>]:
        <h:outputText value="This component has no ID "/><br>
        <h:inputText value="This component has no ID "/><br>
    </c:forEach>

</f:subview>

