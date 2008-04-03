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

<%@ page contentType="text/html"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.el.ValueExpression"
%><%@ page import="com.sun.faces.systest.model.TestBean"
%><%

// This test exercices the config system's ability to load information
// from a faces configuration file specified as a servlet context
// initialization parameter, in addition to one that is specified
// under WEB-INF.

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/configd01.jsp FAILED - No FacesContext returned");
    return;
  }

  // Acquire a ValueExpression for the bean to be created
  // "mybean" exists in a Faces configuration file specified as
  // as a servlet context init parameter.
  //
  ValueExpression valueExpression = appl.getExpressionFactory().
      createValueExpression(facesContext.getELContext(),"#{mybean}", Object.class);
  if (valueExpression == null) {
    out.println("/config01.jsp FAILED - No ValueExpression returned");
    return;
  }

  // Evaluate the value binding and check for bean creation
  Object result = valueExpression.getValue(facesContext.getELContext());
  if (result == null) {
    out.println("/config01.jsp FAILED - getValue() returned null");
    return;
  }

  Object scoped = request.getAttribute("mybean");
  if (scoped == null) {
    out.println("/config01.jsp FAILED - not created in request scope");
    return;
  }

  // Acquire a ValueExpression for the bean to be created
  // "test1" exists in a Faces configuration file under WEB-INF. 
  //
  valueExpression = appl.getExpressionFactory().createValueExpression(facesContext.getELContext(),"#{test1}", 
     Object.class);
  if (valueExpression == null) {
    out.println("/config01.jsp FAILED - No ValueExpression returned");
    return;
  }

  // Evaluate the value binding and check for bean creation
  result = valueExpression.getValue(facesContext.getELContext());
  if (result == null) {
    out.println("/config01.jsp FAILED - getValue() returned null");
    return;
  }

  scoped = request.getAttribute("test1");
  if (scoped == null) {
    out.println("/config01.jsp FAILED - not created in request scope");
    return;
  }

  out.println("/config01.jsp PASSED");
%>
