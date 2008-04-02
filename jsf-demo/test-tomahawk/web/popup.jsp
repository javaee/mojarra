<%@ page import="java.math.BigDecimal,
                 java.util.Date"%>
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
    <t:dataTable id="data"
            styleClass="standardTable"
            headerClass="standardTable_Header"
            rowClasses="standardTable_Row1,standardTable_Row2"
            columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
            var="country"
            value="#{countryList.countries}"
            preserveDataModel="true">
       <h:column>
        <h:panelGrid columns="1">
            <t:popup id="x"
                styleClass="popup"
                closePopupOnExitingElement="true"
                closePopupOnExitingPopup="true"
                displayAtDistanceX="10"
                displayAtDistanceY="10" >
                <h:outputText id="ox" value="This is the first textual text situation."/>
                <f:facet name="popup">
                    <h:panelGroup>
                        <h:panelGrid columns="1" >
                        <h:outputText value="Popup Text 1"/>
                        </h:panelGrid>
                    </h:panelGroup>
                </f:facet>
            </t:popup>

            <t:popup
                styleClass="popup"
                closePopupOnExitingElement="true"
                closePopupOnExitingPopup="true"
                displayAtDistanceX="10"
                displayAtDistanceY="10" >
                <h:outputText value="This is the second textual text situation."/>
                <f:facet name="popup">
                    <h:panelGroup>
                        <h:panelGrid columns="1" >
                        <h:outputText value="Popup Text 2"/>
                        </h:panelGrid>
                    </h:panelGroup>
                </f:facet>
            </t:popup>

            <t:popup styleClass="popup">
                <h:outputText value="This is the third textual text situation."/>
                <f:facet name="popup">
                    <h:panelGroup>
                        <h:panelGrid columns="1" >
                        <h:outputText value="Popup Text 3"/>
                        <h:outputLink value="http://myfaces.apache.org" >
                            <h:outputText value="MyFaces Homepage"/>
                        </h:outputLink>
                        <h:outputLink value="http://myfaces.apache.org" >
                            <h:outputText value="MyFaces Homepage"/>
                        </h:outputLink>
                        <h:outputLink value="http://myfaces.apache.org" >
                            <h:outputText value="MyFaces Homepage"/>
                        </h:outputLink>
                        <h:outputLink value="http://myfaces.apache.org" >
                            <h:outputText value="MyFaces Homepage"/>
                        </h:outputLink>
                        <h:outputLink value="http://myfaces.apache.org" >
                            <h:outputText value="MyFaces Homepage"/>
                        </h:outputLink>
                        </h:panelGrid>
                    </h:panelGroup>
                </f:facet>
            </t:popup>
        </h:panelGrid>
      </h:column>
   </t:dataTable>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
