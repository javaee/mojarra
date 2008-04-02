<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
<%@ page session="false" contentType="text/html;charset=utf-8"%>
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

<!--
managed beans used:
    validateForm
-->

<f:view>

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

    <h:form id="forceIdForm" >
        <h:panelGrid columns="3">

            <h:outputText id="forceOneOutput" value="#{example_messages['forceOne']}"/>
            <h:inputText required="true" id="forceOne" value="#{forceIdBean.valueOne}"/>
            <h:message id="forceOneMessage" for="forceOne" styleClass="error" />

            <h:outputText id="forceTwoOutput" value="#{example_messages['forceTwo']}"/>
            <t:inputText required="true" id="forceTwo" value="#{forceIdBean.valueTwo}" forceId="true"/>
            <h:message id="forceTwoMessage" for="forceTwo" styleClass="error" />

            <h:panelGroup/>
            <t:commandLink forceId="true" id="button" value="#{example_messages['button_submit']}" action="go_home"/>
            <h:panelGroup/>

            <t:inputHidden forceId="true" id="hidden-foo"/>

        </h:panelGrid>
    </h:form>

    <h:form id="dataTable">
        <h:dataTable value="#{forceIdBean.users}" var="user">
            <h:column>
                <h:outputText value="Username"/>
                <t:inputText id="username" value="#{user.username}" forceId="true"/>
                <h:outputText value="Password"/>
                <t:inputText id="passwd" value="#{user.password}" forceId="true"/>
                <t:commandButton id="button" forceId="true" value="Update" action="#{user.update}"/>
            </h:column>
        </h:dataTable>

        <br/>

        Table data <b>without</b> forceId/forceIdIndex
        <br/>
        <h:dataTable value="#{forceIdBean.choices}" var="choice">
            <h:column>
                <h:inputText id="widget" value="#{choice}"/>
            </h:column>
        </h:dataTable>

        <br/>
        Table data <b>with</b> forceId/forceIdIndex
        <br/>        
        <t:dataTable value="#{forceIdBean.choices}" var="choice">
            <h:column>
                <t:inputText id="widget" value="#{choice}" forceId="true" forceIdIndex="true"/>
            </h:column>            
        </t:dataTable>        
        
        <br/>
<%-- doesn't work yet (see MYFACES-91) 

        <b>Radio buttons without forceId/forceIdIndex</b>
        <br/>
                
        <h:selectOneRadio id="something" value="#{forceIdBean.currentChoice}">
            <f:selectItem itemValue="0" itemLabel="foo"/>
            <f:selectItem itemValue="1" itemLabel="bar"/>
            <f:selectItem itemValue="2" itemLabel="buzz"/>            
        </h:selectOneRadio>
        
        <b>Radio buttons with forceId/forceIdIndex</b>
        <br/>
        
        <t:selectOneRadio id="something" forceId="true" forceIdIndex="true" value="#{forceIdBean.currentChoice}">
            <f:selectItem itemValue="0" itemLabel="foo"/>
            <f:selectItem itemValue="1" itemLabel="bar"/>
            <f:selectItem itemValue="2" itemLabel="buzz"/>            
        </t:selectOneRadio>        
--%>        
    </h:form>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
