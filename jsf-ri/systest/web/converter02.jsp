<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<jsp:useBean id="numConverter" class="javax.faces.convert.NumberConverter" scope="session" />
<%
   // configure the converter
   numConverter.setType("currency");
%>
<f:view>
    <h:outputText id="id1" value="#{test1.doubleProperty}" converter="#{numConverter}" />
</f:view>
