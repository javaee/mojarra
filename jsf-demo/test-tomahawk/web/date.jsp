<%@ page import="java.util.Random"%>
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

<f:view>

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

    <h:panelGroup id="body">

        <h:messages id="messageList" showSummary="true" showDetail="true" />

        <f:verbatim>
            <h:outputText escape="false" value="<h4> #{example_messages['date_comp_header']}</h4>"/>
        </f:verbatim>

            <h:form>
                <f:verbatim><p></f:verbatim>
                        <h:outputText value="#{example_messages['date_comp_text1']}"/> <h:message for="date1"/>
                        <t:inputDate id="date1" value="#{date1}" popupCalendar="true"/>
                        <f:verbatim><br></f:verbatim>
                        <h:outputText value="#{example_messages['date_comp_text2']} #{date1}"/> <%-- TODO : print date part only --%>
                <f:verbatim></p></f:verbatim>

                <h:outputText value="#{example_messages['date_comp_text3']}"/>
                        <t:inputDate id="date2" value="#{date2}" type="time"/> <h:message for="date2"/>
                        <f:verbatim><br></f:verbatim>
                        <h:outputText value="#{example_messages['date_comp_text4']} #{date2}"/> <%-- TODO : print time part only --%>
                <f:verbatim></p></f:verbatim>

                <h:outputText value="#{example_messages['date_comp_text5']}"/>
                        <t:inputDate id="date3" value="#{date3}" type="both"/> <h:message for="date3"/>
                        <f:verbatim><br></f:verbatim>
                        <h:outputText value="#{example_messages['date_comp_text6']} #{date3}"/>
                <f:verbatim></p></f:verbatim>

                <h:commandButton value="#{example_messages['date_comp_button']}"/>
            </h:form>

    </h:panelGroup>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
