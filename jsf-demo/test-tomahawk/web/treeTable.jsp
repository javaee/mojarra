<%@ page session="true" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>

<!--
/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//-->

<%@include file="inc/head.inc" %>

<body>

<f:view>

    <t:tree id="tree" value="#{treeTable.treeModel}"
                    var="treeItem"
                    styleClass="tree"
                nodeClass="treenode"
                headerClass="treeHeader"
                footerClass="treeFooter"
            rowClasses="a, b"
            columnClasses="col1, col2"
                selectedNodeClass="treenodeSelected"
                expandRoot="true">
            <h:column>
                    <f:facet name="header">
                    <h:outputText value="Header 1" />
            </f:facet>
            <h:outputText value="#{treeItem.isoCode}" />
        </h:column>
            <t:treeColumn>
                    <f:facet name="header">
                    <h:outputText value="Header 2" />
            </f:facet>
                    <h:outputText value="#{treeItem.name}" />
            </t:treeColumn>
            <h:column>
                    <f:facet name="header">
                    <h:outputText value="Header 3" />
            </f:facet>
                    <h:outputText value="#{treeItem.description}" />
        </h:column>
        <f:facet name="footer">
            <h:outputText value="Footer" />
        </f:facet>
    </t:tree>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
