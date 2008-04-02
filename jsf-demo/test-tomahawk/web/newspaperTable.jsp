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

    <t:newspaperTable id="data"
            newspaperColumns="2"
            styleClass="standardTable"
            headerClass="standardTable_Header"
            footerClass="standardTable_Header"
            rowClasses="standardTable_Row1,standardTable_Row2"
            columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
            var="country"
            value="#{countryList.countries}">
           <f:facet name="spacer">
             <f:verbatim> &nbsp; </f:verbatim>
           </f:facet>
       <h:column>
           <f:facet name="header">
              <h:outputText value="#{example_messages['label_country_name']}" />
           </f:facet>
            <h:outputText value="#{country.name}" />
       </h:column>

       <h:column>
           <f:facet name="header">
              <h:outputText value="#{example_messages['label_country_iso']}" />
           </f:facet>
           <h:outputText value="#{country.isoCode}" />
       </h:column>

    </t:newspaperTable>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
