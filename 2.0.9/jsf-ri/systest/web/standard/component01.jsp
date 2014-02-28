<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
 Contributor(s):
 
 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>component01.jsp</title>

<%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.component.UIInput"
%><%@ page import="javax.el.ValueExpression"
%>
 <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
 <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<% request.setAttribute("attrName", "attrValue"); %>
  </head>
  <body>
      <f:view>
          <h:inputText id="username" binding="#{usernamecomponent}" size="20" onkeypress="#{requestScope.attrName}"/> 
      </f:view>
  </body>
</html>
<%

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/component01.jsp FAILED - No FacesContext returned");
    return;
  }

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  ValueExpression binding = appl.getExpressionFactory().
    createValueExpression(facesContext.getELContext(),"usernamecomponent", Object.class);
  Object result = binding.getValue(facesContext.getELContext());
  if (result == null || !(result instanceof UIInput)) {
      System.out.println("/component01.jsp FAILED - Couldn't retrieve component.");
      return;
  }

  UIInput usernamecomponent = (UIInput)result;
  String size = (String) usernamecomponent.getAttributes().get("size");
  if ( !(size.equals("20"))) {
      System.out.println("/component01.jsp FAILED - Invalid value for size attribute");
      return;
  } 

  String maxlength = (String) usernamecomponent.getAttributes().get("maxlength");
  if ( !(maxlength.equals("32"))) {
      System.out.println("/component01.jsp FAILED - Invalid value for maxlength attribute");
      return;
  } 
%>

