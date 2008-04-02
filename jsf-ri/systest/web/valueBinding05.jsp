<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/plain"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.el.ValueBinding"
%><%@ page import="com.sun.faces.systest.model.TestBean"
%><%

  // Instantiate our test bean in request scope
  TestBean bean = new TestBean();
  FacesContext context = FacesContext.getCurrentInstance();
  context.getExternalContext().getRequestMap().put
   ("testVB", bean);

  // Retrieve a simple integer expression with a value binding expression
  ValueBinding vb = context.getApplication().createValueBinding
   ("#{testVB.intProperty + 1}");
  Object result;
  try {
    result = vb.getValue(context);
  } catch (Exception e) {
    out.println("/valueBinding05.jsp FAILED - getValue() exception: " + e);
    e.printStackTrace(System.out);
    return;
  }

  // Validate the result
  if (result == null) {
    out.println("/valueBinding05.jsp FAILED - getValue() returned null");
  } else if (!(result instanceof Number)) {
    out.println("/valueBinding05.jsp FAILED - getValue() returned " + result);
  } else if (124 != ((Number) result).intValue()) {
    out.println("/valueBinding05.jsp FAILED - getValue() returned " + result);
  } else {
    out.println("/valueBinding05.jsp PASSED");
  }

%>
