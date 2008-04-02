<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<!--
/*
 * Copyright 2005 The Apache Software Foundation.
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
<html>

<%@include file="inc/head.inc" %>

<body>

<f:view>

    <span style="font-family:verdana">
        <b>Tree2 w/showRootNode=false</b><br/>
    </span>
    <br/>

    <t:tree2 id="clientTree" value="#{treeBacker.treeData}" var="node" varNodeToggler="t" showRootNode="false">
        <f:facet name="person">
            <h:panelGroup>
                <f:facet name="expand">
                    <t:graphicImage value="images/person.png" rendered="#{t.nodeExpanded}" border="0"/>
                </f:facet>
                <f:facet name="collapse">
                    <t:graphicImage value="images/person.png" rendered="#{!t.nodeExpanded}" border="0"/>
                </f:facet>
                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="foo-folder">
            <h:panelGroup>
                <f:facet name="expand">
                    <t:graphicImage value="images/yellow-folder-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                </f:facet>
                <f:facet name="collapse">
                    <t:graphicImage value="images/yellow-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                </f:facet>
                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="bar-folder">
            <h:panelGroup>
                <f:facet name="expand">
                    <t:graphicImage value="images/blue-folder-open.gif" rendered="#{t.nodeExpanded}" border="0"/>
                </f:facet>
                <f:facet name="collapse">
                    <t:graphicImage value="images/blue-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                </f:facet>
                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="document">
            <h:panelGroup>
                <h:commandLink immediate="true" styleClass="#{t.nodeSelected ? 'documentSelected':'document'}" actionListener="#{t.setNodeSelected}">
                    <t:graphicImage value="images/document.png" border="0"/>
                    <h:outputText value="#{node.description}"/>
                    <f:param name="docNum" value="#{node.identifier}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
    </t:tree2>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>

