<%@ page import="javax.servlet.jsp.JspException"%>
<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<%-- Confirm duplicate ID's are found --%>
<% try { %>
<f:view>
    <h:outputText id="duplicate1" value="one"/>
    <h:outputText id="output2" value="two"/>
    <h:outputText id="duplicate1" value="three"/>
</f:view>
<%
    } catch (JspException je) {
        je.printStackTrace();
        if (!(je.getRootCause() instanceof IllegalStateException)) {
            throw je;
        }
    }
%>
