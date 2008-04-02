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

<%@ page contentType="text/html"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.el.ValueBinding"
%><%@ page import="com.sun.faces.systest.model.TestBean"
%><%

  // Instantiate our test bean in request scope
  TestBean bean = new TestBean();
  bean.setStringProperty("foo");
  FacesContext context = FacesContext.getCurrentInstance();
  context.getExternalContext().getRequestMap().put
   ("testVB", bean);

  // Retrieve a simple String property with a value binding expression
  ValueBinding vb;
  Object result;
  try {
    vb = context.getApplication().createValueBinding
     ("#{testVB.stringProperty} and #{testVB.stringProperty}");
    result = vb.getValue(context);
  } catch (Exception e) {
    out.println("/valueBinding07.jsp FAILED - getValue() exception: " + e);
    e.printStackTrace(System.out);
    return;
  }

  // Validate the result
  if (result == null) {
    out.println("/valueBinding07.jsp FAILED - getValue() returned null");
  } else if (!(result instanceof String)) {
    out.println("/valueBinding07.jsp FAILED - getValue() returned " + result);
  } else if (!"foo and foo".equals((String) result)) {
    out.println("/valueBinding07.jsp FAILED - getValue() returned " + result);
  } else {
    out.println("/valueBinding07.jsp PASSED");
  }

%>
