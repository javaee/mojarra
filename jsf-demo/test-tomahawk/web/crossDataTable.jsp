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

	<f:loadBundle
		basename="org.apache.myfaces.examples.resource.example_messages"
		var="example_messages" />

    <h:form>
    <h:panelGroup id="body">

        <h:panelGrid columns="1">
            <h:commandLink rendered="#{!crossDataTable.editValues}" action="#{crossDataTable.editValues}"
                immediate="true">
                <h:outputText value="#{example_messages['country_edit_table']}"
                    styleClass="standard" />
            </h:commandLink>
            <h:panelGrid rendered="#{!crossDataTable.editValues}" columns="3">
                <h:outputLabel for="columnLabel" value="#{example_messages['crosstable_field_column']}"/>
                <h:inputText id="columnLabel" value="#{crossDataTable.columnLabel}" />
                <h:commandLink action="#{crossDataTable.addColumn}">
                    <h:outputText value="#{example_messages['crosstable_add_column']}"
                        styleClass="standard" />
                </h:commandLink>
            </h:panelGrid>
            <h:commandLink rendered="#{crossDataTable.editValues}" action="#{crossDataTable.saveValues}">
                <h:outputText value="#{example_messages['crosstable_save_values']}"
                    styleClass="standard" />
            </h:commandLink>
        </h:panelGrid>
        <f:verbatim>
            <br>
        </f:verbatim>

        <t:dataTable id="data" styleClass="standardTable"
            headerClass="standardTable_Header"
            footerClass="standardTable_Header"
            rowClasses="standardTable_Row1,standardTable_Row2"
            columnClasses="standardTable_Column" var="country"
            value="#{crossDataTable.countryDataModel}" preserveDataModel="false">
            <h:column>
                <f:facet name="header">
                    <h:outputText value="#{example_messages['label_country_name']}" />
                </f:facet>
                <h:outputText value="#{country.name}" />
            </h:column>

            <t:columns value="#{crossDataTable.columnDataModel}" var="column">
                <f:facet name="header">
                    <h:panelGroup>
                        <h:outputText value="#{column} " />
                        <h:commandLink action="#{crossDataTable.removeColumn}">
                            <h:outputText value="-" title="#{example_messages['crosstable_remove_column']}" />
                        </h:commandLink>
                    </h:panelGroup>
                </f:facet>
                <h:outputText rendered="#{!crossDataTable.editValues}"
                    value="#{crossDataTable.columnValue}" />
                <h:inputText rendered="#{crossDataTable.editValues}"
                    value="#{crossDataTable.columnValue}" />
            </t:columns>

        </t:dataTable>

        <f:verbatim>
            <br>
        </f:verbatim>

    </h:panelGroup>
    </h:form>

</f:view>

<%@include file="inc/page_footer.jsp"%>

</body>

</html>
