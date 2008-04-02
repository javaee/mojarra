<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page import="javax.servlet.RequestDispatcher" %><%

  // Generically link to a Faces Response for a View Id
  //
  // Query Parameters:
  //  responseViewId           View Id to display via Faces

  String toViewId = request.getParameter("responseViewId");
  RequestDispatcher rd = application.getRequestDispatcher
    ("/faces" + toViewId);
  rd.forward(request, response);

  // Should always forward or throw an exception

%>
