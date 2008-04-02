<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page contentType="text/plain"
%><%@ page import="javax.faces.context.FacesContext"
%><%

  // Set the attribute key and values we'll use throughout the test
  String key = "/external03.jsp";
  String value1 = "From Servlet";
  String value2 = "From Faces";
  String actual = null;

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/managed01.jsp FAILED - No FacesContext returned");
    return;
  }

  // Eliminate any current attribute under this key
  application.removeAttribute(key);
  if (application.getAttribute(key) != null) {
    out.println("/external03.jsp FAILED - can not remove ServletContext attribute");
    return;
  }
  facesContext.getExternalContext().getApplicationMap().remove(key);
  if (facesContext.getExternalContext().getApplicationMap().get(key) != null) {
    out.println("/external03.jsp FAILED - can not remove application scope attribute");
    return;
  }

  // Set via Servlet API and check via Faces API
  application.setAttribute(key, value1);
  actual = (String) application.getAttribute(key);
  if (!value1.equals(actual)) {
    out.println("/external03.jsp FAILED - ServletContext attribute set to '" +
                value1 + "' but Servlet API returned '" + actual + "'");
    return;
  }
  actual = (String) facesContext.getExternalContext().getApplicationMap().get(key);
  if (!value1.equals(actual)) {
    out.println("/external03.jsp FAILED - ServletContext attribute set to '" +
                value1 + "' but Faces API returned '" + actual + "'");
  }

  // Set via Faces API and check via Servlet API
  facesContext.getExternalContext().getApplicationMap().put(key, value2);
  actual = (String) facesContext.getExternalContext().getApplicationMap().get(key);
  if (!value2.equals(actual)) {
    out.println("/external03.jsp FAILED - Faces attribute set to '" +
                value2 + "' but Faces API returned '" + actual + "'");
    return;
  }
  actual = (String) application.getAttribute(key);
  if (!value2.equals(actual)) {
    out.println("/external03.jsp FAILED - Faces attribute set to '" +
                value2 + "' but Servlet API returned '" + actual + "'");
    return;
  }

  out.println("/external03.jsp PASSED");
%>
