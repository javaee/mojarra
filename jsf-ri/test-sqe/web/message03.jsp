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
%><%@ page import="java.util.Locale"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.application.FacesMessage"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="com.sun.faces.util.MessageFactory"
%><%@ page import="javax.faces.component.UIViewRoot"
%><%

  // Initialize list of message ids
  String list[] = {
    "Custom1A",
    "Custom1B",
    "Custom1C",
  };

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/message03.jsp FAILED - No FacesContext returned");
    return;
  }
  facesContext.setViewRoot(new UIViewRoot());
  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();
  if (appl == null) {
    out.println("/message03.jsp FAILED - No Application returned");
    return;
  }

  FacesMessage message = null;

  // Check message identifiers that should be present (en_US)
  facesContext.getViewRoot().setLocale(new Locale("en", "US"));
  for (int i = 0; i < list.length; i++) {
    message = MessageFactory.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message03.jsp FAILED - Missing en_US message '" +
                  list[i] + "'");
      return;
    }
  }

  // Check specific message characteristics (en_US)
  message = MessageFactory.getMessage(facesContext, "Custom1B");
  if (!"This Is Custom1B Detail (en)".equals(message.getDetail())) {
    out.println("/message03.jsp FAILED - Bad en_US detail '" +
                message.getDetail() + "'");
    return;
  }
  if (!"This Is Custom1B Summary (en)".equals(message.getSummary())) {
    out.println("/message03.jsp FAILED - Bad en_US summary '" +
                message.getSummary() + "'");
    return;
  }

  // Check message identifiers that should be present (fr_FR)
  facesContext.getViewRoot().setLocale(new Locale("fr", "FR"));
  for (int i = 0; i < list.length; i++) {
    message = MessageFactory.getMessage(facesContext, list[i]);
    if (message == null) {
      out.println("/message03.jsp FAILED - Missing fr_FR message '" +
                  list[i] + "'");
      return;
    }
  }

  // Check specific message characteristics (fr_FR)
  message = MessageFactory.getMessage(facesContext, "Custom1B");
  if (!"This Is Custom1B Detail (fr)".equals(message.getDetail())) {
    out.println("/message03.jsp FAILED - Bad fr_FR detail '" +
                message.getDetail() + "'");
    return;
  }
  if (!"This Is Custom1B Summary (fr)".equals(message.getSummary())) {
    out.println("/message03.jsp FAILED - Bad fr_FR summary '" +
                message.getSummary() + "'");
    return;
  }

  // All tests passed
  out.println("/message03.jsp PASSED");

%>
