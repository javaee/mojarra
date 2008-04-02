<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<%-- $Id: UIPanel.jsp,v 1.5 2004/02/05 16:25:19 rlubke Exp $ --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<f:view>  
<html>

  <head>
    <title>UIPanel</title>
    <link rel="stylesheet" type="text/css"
       href='<%= request.getContextPath() + "/stylesheet.css" %>'>
  </head>

  <body>

    <h1>UIPanel</h1>

    <fmt:setBundle basename="standard.Resources"
                      scope="session"
                        var="standardBundle"/>


         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="table_header.jsp" %>

           <%@ include file="panel_grid_row.jsp" %>

<%--
           <%@ include file="panelGroup_row.jsp" %>
--%>

         </table>

  <hr>
  <p><a href='<%= request.getContextPath() + "/" %>'>Back</a>
  to home page.</p>

</body>
</html>
</f:view>
