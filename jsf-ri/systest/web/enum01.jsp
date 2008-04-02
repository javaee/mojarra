<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->
<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
    <h:form id="form">
        <h:commandButton id="go" value="go to hello"
                         action="#{test1.returnSpades}"/>
        <p/>
        <h:commandButton id="stay" value="stay here"
                         action="#{test1.returnDiamonds}"/>
    </h:form>
</f:view>