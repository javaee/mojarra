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
