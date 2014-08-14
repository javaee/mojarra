<%--

    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

    Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.

    The contents of this file are subject to the terms of either the GNU
    General Public License Version 2 only ("GPL") or the Common Development
    and Distribution License("CDDL") (collectively, the "License").  You
    may not use this file except in compliance with the License.  You can
    obtain a copy of the License at
    https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
    or packager/legal/LICENSE.txt.  See the License for the specific
    language governing permissions and limitations under the License.

    When distributing the software, include this License Header Notice in each
    file and include the License file at packager/legal/LICENSE.txt.

    GPL Classpath Exception:
    Oracle designates this particular file as subject to the "Classpath"
    exception as provided by Oracle in the GPL Version 2 section of the License
    file that accompanied this code.

    Modifications:
    If applicable, add the following below the License Header, with the fields
    enclosed by brackets [] replaced by your own identifying information:
    "Portions Copyright [year] [name of copyright owner]"

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



<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
  <html>
    <head>
      <title>Nested Tables</title>
    </head>
    <body>
      <h:form id="form">
	    <h:dataTable id="outer" value="#{testbean.services}" var="service">
		  <h:column>
		    <f:facet name="header">
		      <h:outputText value="Service"/>
		    </f:facet>
		
	        <h:inputText value="#{service.name}" required="true"/>
		    <h:commandButton id="delete" styleClass="command-multiple" immediate="false" action="#{testbean.deleteService}" value="Delete"/>
			
		    <f:facet name="footer">
		      <h:commandButton id="add" styleClass="command-multiple" immediate="false" action="#{testbean.addService}" value="Add"/>
		    </f:facet>
		  </h:column>
		
		  <h:column>
		    <f:facet name="header">
		      <h:outputText value="Port"/>
		    </f:facet>

		    <h:dataTable id="inner" value="#{service.ports}" var="portNumber">
		      <h:column>
			    <h:inputText id="portNumber" value="#{portNumber.portNumber}" size="5"/>
			    <h:commandButton styleClass="command-multiple" immediate="false" action="#{testbean.deletePortNumber}" value="Delete"/>
				    
                <f:facet name="footer">
				  <h:commandButton id="add-port" styleClass="command-multiple" immediate="false"  action="#{testbean.addPortNumber}" value="Add port"/>
	            </f:facet>
			  </h:column>
				
			</h:dataTable>
		  </h:column>
	    </h:dataTable>  
<hr />
          <h:commandButton id="reload" value="reload" action="#{testbean.printTree}"/>
          
          <p>Current state after previous load: <h:outputText escape="false" value="#{testbean.currentStateTable}" /></p>
      </h:form>
    </body>
  </html>
</f:view>

