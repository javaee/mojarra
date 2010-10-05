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

<%@ page import="java.util.ArrayList" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>dtablewithemptybody.jsp</title>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>


    <%
    	ArrayList<String> emptyList = new ArrayList<String>();
        request.setAttribute("emptyList", emptyList);
        ArrayList<String> someList = new ArrayList<String>();
        request.setAttribute("someList", someList);
        someList.add("AAAA");
        someList.add("BBBB");
        someList.add("CCCC");
    %>
</head>
<body>
<f:view>
    <h:dataTable value="#{requestScope.emptyList}" id="Empty"
                 var="row">
        <h:column rendered="false">
        	<h:outputText value="not rendered" />
        </h:column>
        <h:column>
        	<h:outputText value="#{row}"/>
        </h:column>
        <h:column>
            <h:outputText value="#{row}"/>
        </h:column>
    </h:dataTable>
    <h:dataTable value="#{requestScope.someList}" id="Some"
                 var="row">
        <f:facet name="header"><h:outputText value="Table Header" /></f:facet>
        <h:column rendered="false">
        	<f:facet name="header"><h:outputText value="Header1" /></f:facet>
        	<h:outputText value="not rendered" />
        </h:column>
        <h:column rendered="false">
        	<f:facet name="header"><h:outputText value="Header2" /></f:facet>
        	<h:outputText value="#{row}"/>
        </h:column>
        <h:column rendered="false">
        	<f:facet name="header"><h:outputText value="Header3" /></f:facet>
            <h:outputText value="#{row}"/>
        </h:column>
        <f:facet name="footer"><h:outputText value="Table Footer" /></f:facet>
    </h:dataTable>
    
    <h:dataTable id="PureEmptyDataTable">
    </h:dataTable>
    
    <h:panelGrid id="PureEmptyPanelGrid">
    </h:panelGrid>
    
    <h:panelGrid id="NoRenderedContentPanelGrid" columns="2">
    	<f:facet name="header"><h:outputText value="Header" /></f:facet>
    	<h:outputText value="AAA" rendered="false" />
    	<h:outputText value="BBB" rendered="false" />
    	<h:outputText value="CCC" rendered="false" />
    </h:panelGrid>
</f:view>
</body>
</html>
