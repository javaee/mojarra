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
                <h:commandButton id="delete" styleClass="command-multiple"
                                 immediate="false"
                                 action="#{testbean.deleteService}"
                                 value="Delete"/>

                <f:facet name="footer">
                    <h:commandButton id="add" styleClass="command-multiple"
                                     immediate="false"
                                     action="#{testbean.addService}"
                                     value="Add"/>
                </f:facet>
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:outputText value="Port"/>
                </f:facet>

                <h:dataTable id="inner" value="#{service.ports}"
                             var="portNumber">
                    <h:column>
                        <h:inputText id="portNumber"
                                     value="#{portNumber.portNumber}" size="5"/>
                        <h:commandButton styleClass="command-multiple"
                                         immediate="false"
                                         action="#{testbean.deletePortNumber}"
                                         value="Delete"/>

                        <f:facet name="footer">
                            <h:commandButton id="add-port"
                                             styleClass="command-multiple"
                                             immediate="false"
                                             action="#{testbean.addPortNumber}"
                                             value="Add port"/>
                        </f:facet>
                    </h:column>

                </h:dataTable>
            </h:column>
        </h:dataTable>
        <hr/>
        <h:commandButton id="reload" value="reload"
                         action="#{testbean.printTree}"/>

        <p>Current state after previous load: <h:outputText escape="false"
                                                            value="#{testbean.currentStateTable}"/></p>
    </h:form>
    </body>
    </html>
</f:view>

