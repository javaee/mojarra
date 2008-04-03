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

<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

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

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head> <title>WCAG Table Test</title> </head>
    <%@ page contentType="application/xhtml+xml" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <body bgcolor="white">
    <f:view>
    <h:form>

      <p>This is the sample table from the HTML 4.0.1 specificion
      section 11.5.  You can see it at &lt;<a
      href="http://www.w3.org/TR/html401/struct/tables.html#h-11.5">http://www.w3.org/TR/html401/struct/tables.html#h-11.5</a>&gt;.
      </p>

      <h:dataTable value="#{dataSource.codePageData}" var="row"
                   frame="hsides" rules="groups"
                   summary="Code page support in different versions of MS Windows." bodyrows="0,10">
        <f:facet name="colgroups">
          <h:panelGroup>
	    <colgroup align="center" />
	    <colgroup align="left" />
	    <colgroup align="center" span="2" />
	    <colgroup align="center" span="3" />
          </h:panelGroup>
        </f:facet>

        <f:facet name="caption">
          <h:outputText value="CODE-PAGE SUPPORT IN MICROSOFT WINDOWS" />
        </f:facet>

	<h:column>
	  <f:facet name="header">
	    <h:outputText value="Code-Page ID" />
	  </f:facet>
	  <h:outputText value="#{row.codePageId}" />
	</h:column>

	<h:column>
	  <f:facet name="header">
	    <h:outputText value="Name" />
	  </f:facet>
	  <h:outputText value="#{row.name}" />
	</h:column>

	<h:column>
	  <f:facet name="header">
	    <h:outputText value="ACP" />
	  </f:facet>
	  <h:outputText value="#{row.ACP}" />
	</h:column>

	<h:column>
	  <f:facet name="header">
	    <h:outputText value="OEMCP" />
	  </f:facet>
	  <h:outputText value="#{row.OEMCP}" />
	</h:column>

	<h:column rowHeader="true">
	  <f:facet name="header">
	    <h:outputText value="Windows NT 3.1" />
	  </f:facet>
	  <h:outputText value="#{row.winNT31}" />
	</h:column>

	<h:column>
	  <f:facet name="header">
	    <h:outputText value="Windows NT 3.51" />
	  </f:facet>
	  <h:outputText value="#{row.winNT351}" />
	</h:column>

	<h:column>
	  <f:facet name="header">
	    <h:outputText value="Windows 95" />
	  </f:facet>
	  <h:outputText value="#{row.win95}" />
	</h:column>

      </h:dataTable>

    </h:form>
    </f:view>

    <p>
      <a href="http://validator.w3.org/check?uri=referer"><img
          src="http://www.w3.org/Icons/valid-xhtml10"
          alt="Valid XHTML 1.0!" height="31" width="88" /></a>
    </p>
    </body>
</html>  
