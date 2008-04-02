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
    <h:messages />
        <h:form>
            <t:dataTable id="data"
                         styleClass="scrollerTableNoWidth"
                         headerClass="standardTable_Header"
                         footerClass="standardTable_Header"
                         rowClasses="standardTable_Row1,standardTable_Row2"
                         columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                         rowOnMouseOver="this.style.backgroundColor='#A5CBFF'"
                         rowOnMouseOut="this.style.backgroundColor='#FFFFE0'"
                         rowOnClick="this.style.backgroundColor='#FFE0E0'"
                         rowOnDblClick="this.style.backgroundColor='#E0E0E0'"
                         var="row"
                         value="#{openDataList.data}"
                         preserveDataModel="false"
                         rows="10"
                         sortColumn="#{openDataList.sort}"
                         sortAscending="#{openDataList.ascending}"
                         preserveSort="true">
                <t:columns id="columns" value="#{openDataList.columnHeaders}" var="columnHeader" style="width:#{openDataList.columnWidth}px">
                    <f:facet name="header">
                        <t:commandSortHeader columnName="#{columnHeader.label}" arrow="false" immediate="false">
                            <f:facet name="ascending">
                                <t:graphicImage value="images/ascending-arrow.gif" rendered="true" border="0"/>
                            </f:facet>
                            <f:facet name="descending">
                                <t:graphicImage value="images/descending-arrow.gif" rendered="true" border="0"/>
                            </f:facet>
                            <h:outputText value="#{columnHeader.label}" />
                        </t:commandSortHeader>
                    </f:facet>
                    <!-- row is also available -->
                    <h:inputText rendered="#{openDataList.valueModifiable}" value="#{openDataList.columnValue}" />
                    <h:outputText rendered="#{!openDataList.valueModifiable}" value="#{openDataList.columnValue}" />
                </t:columns>
            </t:dataTable>

            <h:panelGrid columns="1" styleClass="scrollerTable2" columnClasses="standardTable_ColumnCentered" >
                <t:dataScroller id="scroll_1"
                                for="data"
                                fastStep="10"
                                pageCountVar="pageCount"
                                pageIndexVar="pageIndex"
                                styleClass="scroller"
                                paginator="true"
                                paginatorMaxPages="9"
                                paginatorTableClass="paginator"
                                paginatorActiveColumnStyle="font-weight:bold;"
                                immediate="false">
                    <f:facet name="first" >
                        <t:graphicImage url="images/arrow-first.gif" border="1" />
                    </f:facet>
                    <f:facet name="last">
                        <t:graphicImage url="images/arrow-last.gif" border="1" />
                    </f:facet>
                    <f:facet name="previous">
                        <t:graphicImage url="images/arrow-previous.gif" border="1" />
                    </f:facet>
                    <f:facet name="next">
                        <t:graphicImage url="images/arrow-next.gif" border="1" />
                    </f:facet>
                    <f:facet name="fastforward">
                        <t:graphicImage url="images/arrow-ff.gif" border="1" />
                    </f:facet>
                    <f:facet name="fastrewind">
                        <t:graphicImage url="images/arrow-fr.gif" border="1" />
                    </f:facet>
                </t:dataScroller>

            </h:panelGrid>
        </h:form>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
