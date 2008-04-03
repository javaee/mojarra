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
<%@ page contentType="text/html" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<%@ page import="java.util.*"%>

<%
        for (int j = 0; j < 3; ++j)
        {
            String mapName = "input" + (j + 1);
            if (pageContext.getAttribute(mapName) == null)
            {
                Map map = new LinkedHashMap();
                for (int i = 0; i < 3; ++i)
                    map.put("inputText" + (j * 3 + i + 1), "input" + (j * 3 + i + 1));
                pageContext.setAttribute(mapName, map, pageContext.SESSION_SCOPE);
            }
        }
%>
<f:view>
    <html>
    <head>
    <title>Test Interaction of c:forEach, h:label and h:message</title>
    <style type="text/css">
    label { font-family: sans-serif; font-weight: bold; font-size: 0.8em; }
    .message { color: red; font-weight: bold; }
    .subheading { font-weight: bold; background-color: #cfffcf; }
    </style>
    </head>
    <body>

    <br>
    <h:form id="myform">
        <h:panelGrid>
            <f:facet name="header">
                <h:outputText
                    value="Test Interaction of c:forEach, h:label and h:message" />
            </f:facet>

            <!-- list all messages -->
            <h:messages id="messages" layout="table" styleClass="message" />

            <!-- label without "id" -->
            <h:outputText styleClass="subheading"
                value="Test simple label and inputText and no id on the label" />
            <h:outputLabel for="inputInt1" value="Label for intProperty below" />
            <h:message for="inputInt1" styleClass="message" />
            <h:inputText id="inputInt1" value="#{forEachBean1.intProperty}"
                required="true" />

            <h:inputText id="inputByte1" value="#{forEachBean1.byteProperty}"
                required="true" />
            <h:message for="inputByte1" styleClass="message" />
            <h:outputLabel for="inputByte1" value="Label for byteProperty above" />

            <h:outputText styleClass="subheading"
                value="Test c:ForEach with label and inputText and no id on the label" />
            <c:forEach var="item" items="#{input1}">
                <h:outputLabel for="inputId1" value="Label for #{item.key} below" />
                <h:message for="inputId1" styleClass="message" />
                <h:inputText id="inputId1" value="#{input1[item.key]}" required="true" />
            </c:forEach>

            <!-- label with "id" -->
            <h:outputText styleClass="subheading"
                value="Test simple label and inputText with an id on the label" />
            <h:outputLabel id="inputLong1Label" for="inputLong1"
                value="Label for longProperty below" />
            <h:message id="inputLong1Msg" for="inputLong1" styleClass="message" />
            <h:inputText id="inputLong1" value="#{forEachBean1.longProperty}"
                required="true" />

            <h:inputText id="inputShort1" value="#{forEachBean1.longProperty}"
                required="true" />
            <h:message id="inputShort1Msg" for="inputShort1" styleClass="message" />
            <h:outputLabel id="inputShort1Label" for="inputShort1"
                value="Label for shortProperty above" />

            <h:outputText styleClass="subheading"
                value="Test c:ForEach with label and inputText with an id on the label" />
            <c:forEach var="item" items="#{input2}">
                <h:inputText id="inputId2" value="#{input2[item.key]}" required="true" />
                <h:message id="inputId2Msg" for="inputId2" styleClass="message" />
                <h:outputLabel id="inputId2Label" for="inputId2"
                    value="Label for #{item.key} above" />
            </c:forEach>

            <h:outputText styleClass="subheading"
                value="Test c:ForEach with transposed table" />
            <h:panelGroup>
                <h:panelGrid columns="#{fn:length(input3)}">
                    <c:forEach var="item" items="#{input3}">
                        <h:outputLabel for="inputId3" value="Label for #{item.key} below" />
                    </c:forEach>
                    <c:forEach var="item" items="#{input3}">
                        <h:message for="inputId3" styleClass="message" />
                    </c:forEach>
                    <c:forEach var="item" items="#{input3}">
                        <h:inputText id="inputId3" value="#{input3[item.key]}"
                            required="true" />
                    </c:forEach>
                </h:panelGrid>
            </h:panelGroup>
        </h:panelGrid>
        
        <jsp:include page="forEach03Include.jsp" />       

        <h:commandButton id="submit" value="Submit" />
    </h:form>
    </body>
    </html>
</f:view>