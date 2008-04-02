<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>

<%@include file="inc/head.inc" %>

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

<body>

<f:view>

    <t:saveState value="#{firstCollapsiblePanelBean}"/>
    <t:saveState value="#{secondCollapsiblePanelBean}"/>
    <t:saveState value="#{thirdCollapsiblePanelBean}"/>

    <h:form id="form">

        <t:collapsiblePanel id="test1" value="#{firstCollapsiblePanelBean.collapsed}" title="testTitle">
            <h:panelGrid>
                <h:outputText value="#{firstCollapsiblePanelBean.firstName}"/>
                <h:inputText value="#{firstCollapsiblePanelBean.surName}"/>
                <t:inputCalendar value="#{firstCollapsiblePanelBean.birthDate}" renderAsPopup="true"/>
            </h:panelGrid>
        </t:collapsiblePanel>

        <t:collapsiblePanel id="test2" value="#{secondCollapsiblePanelBean.collapsed}" title="testTitle"
                            var="test2collapsed">
            <f:facet name="header">
                <t:div style="width:500px;background-color:#CCCCCC;">
                    <h:outputText value="Person"/>
                    <t:headerLink immediate="true">
                        <h:outputText value="> Details" rendered="#{test2collapsed}"/>
                        <h:outputText value="v Overview" rendered="#{!test2collapsed}"/>
                    </t:headerLink>
                </t:div>
            </f:facet>
            <f:facet name="closedContent">
                <h:panelGroup>
                    <h:outputText value="#{secondCollapsiblePanelBean.firstName}"/>
                    <h:outputText value=" "/>
                    <h:outputText value="#{secondCollapsiblePanelBean.surName}"/>
                    <h:outputText value=", born on: "/>
                    <h:outputText value="#{secondCollapsiblePanelBean.birthDate}"/>
                </h:panelGroup>
            </f:facet>
            <h:panelGrid>
                <h:outputText value="#{secondCollapsiblePanelBean.firstName}"/>
                <h:inputText value="#{secondCollapsiblePanelBean.surName}"/>
                <t:inputCalendar value="#{secondCollapsiblePanelBean.birthDate}" renderAsPopup="true"/>
            </h:panelGrid>
        </t:collapsiblePanel>

        <t:collapsiblePanel id="test3" value="#{thirdCollapsiblePanelBean.collapsed}" title="testTitle"
                            var="test2collapsed">
            <f:facet name="header">
                <t:div style="width:500px;background-color:#CCCCCC;">
                    <h:outputText value="Person"/>
                    <t:headerLink immediate="true">
                        <h:outputText value="> Details" rendered="#{test2collapsed}"/>
                        <h:outputText value="v Overview" rendered="#{!test2collapsed}"/>
                    </t:headerLink>
                </t:div>
            </f:facet>
            <f:facet name="closedContent">
                <h:panelGroup>
                    <h:outputText value="#{thirdCollapsiblePanelBean.firstName}"/>
                    <h:outputText value=" "/>
                    <h:outputText value="#{thirdCollapsiblePanelBean.surName}"/>
                    <h:outputText value=", born on: "/>
                    <h:outputText value="#{thirdCollapsiblePanelBean.birthDate}"/>
                </h:panelGroup>
            </f:facet>
            <h:panelGrid>
                <h:outputText value="#{thirdCollapsiblePanelBean.firstName}"/>
                <h:inputText value="#{thirdCollapsiblePanelBean.surName}"/>
                <t:inputCalendar value="#{thirdCollapsiblePanelBean.birthDate}" renderAsPopup="true"/>
            </h:panelGrid>
        </t:collapsiblePanel>

        <t:dataTable id="test_dt" var="person" value="#{thirdCollapsiblePanelBean.persons}" preserveDataModel="false">
            <h:column>
                <t:collapsiblePanel id="test4" var="test4collapsed">
                    <f:facet name="header">
                        <t:div style="width:500px;background-color:#CCCCCC;">
                            <h:outputText value="Person"/>
                            <t:headerLink immediate="true">
                                <h:outputText value="> Details" rendered="#{test4collapsed}"/>
                                <h:outputText value="v Overview" rendered="#{!test4collapsed}"/>
                            </t:headerLink>
                            <h:commandLink value="test" action="#{person.test}"/>
                        </t:div>
                    </f:facet>
                    <h:inputText id="firstname_input" value="#{person.firstName}"/>
                    <h:commandLink value="test" action="#{person.test}"/>
                </t:collapsiblePanel>
            </h:column>
        </t:dataTable>
    </h:form>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
