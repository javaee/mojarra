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
    countryList
-->

<f:view>

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>

    <t:panelLayout id="page" layout="#{globalOptions.pageLayout}"
            styleClass="pageLayout"
            headerClass="pageHeader"
            navigationClass="pageNavigation"
            bodyClass="pageBody"
            footerClass="pageFooter" >

        <f:facet name="header">
            <f:subview id="header">
                <jsp:include page="inc/page_header.jsp" />
            </f:subview>
        </f:facet>

        <f:facet name="navigation">
            <f:subview id="menu" >
                <jsp:include page="inc/navigation.jsp" />
            </f:subview>
        </f:facet>

        <f:facet name="body">
            <h:panelGroup id="body">

               <h:panelGrid columns="1">
                   <h:commandLink action="go_country" immediate="true">
                        <h:outputText value="#{example_messages['new_country']}" styleClass="standard" />
                   </h:commandLink>
                   <h:commandLink action="go_edit_table" immediate="true">
                        <h:outputText value="#{example_messages['country_edit_table']}" styleClass="standard" />
                   </h:commandLink>
               </h:panelGrid>
               <f:verbatim><br></f:verbatim>

                <t:dataTable id="data"
                        styleClass="standardTable"
                        headerClass="standardTable_Header"
                        footerClass="standardTable_Header"
                        rowClasses="standardTable_Row1,standardTable_Row2"
                        columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                        var="country"
                        value="#{countryList.countries}"
                        preserveDataModel="true" >
                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_country_name']}" />
                       </f:facet>
                       <t:commandLink action="go_country" immediate="true" >
                            <h:outputText value="#{country.name}" />
                            <!-- for convenience: MyFaces extension. sets id of current row in countryForm -->
                            <!-- you don't have to implement a custom action! -->
                            <t:updateActionListener property="#{countryForm.id}" value="#{country.id}" />
                       </t:commandLink>
                   </h:column>

                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_country_iso']}" />
                       </f:facet>
                       <h:outputText value="#{country.isoCode}" />
                   </h:column>

                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_country_cities']}" />
                       </f:facet>
                        <t:dataTable id="cities"
                                styleClass="standardTable_Column"
                                var="city"
                                value="#{country.cities}"
                                preserveDataModel="false">
                           <h:column>
                               <h:outputText value="#{city}" style="font-size: 11px" />
                           </h:column>
                        </t:dataTable>
                   </h:column>

                </t:dataTable>

                <f:verbatim><br></f:verbatim>

            </h:panelGroup>
        </f:facet>

        <%@include file="inc/page_footer.jsp" %>

    </t:panelLayout>

</f:view>

</body>

</html>