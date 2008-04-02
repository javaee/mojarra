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

    <f:loadBundle basename="org.apache.myfaces.examples.resource.example_messages" var="example_messages"/>
     
    <p>
        This component allows you to render a part of the page to a buffer, and then use the buffer later.
    </p>
    <p>
        One use of this is the dataScroller. If you display a table with x:dataTable,
        you can't have dataScroller BEFORE the table, as it can't find the not yet rendered table.<br/>
        So, the solution is that you first render the table to a buffer, then you render the dataScroller,
        and then you render the buffer's content.<br/>
        That way, the dataScroller will appear before the table even though it was rendered after it.
    </p>
    <p>
        <b>
            Note : the generated links within the buffer are only supported with the MyFaces implementation
            because of the lack of standard dummy form parameters support.
        </b>
    </p>

    <t:buffer into="#{buffer1}">
        <t:div><h:outputText value="Buffer One"/></t:div>
    </t:buffer>

    <t:buffer into="#{buffer2}">
        <t:div><h:outputText value="Buffer Two"/></t:div>
    </t:buffer>

    <h:outputText value="#{buffer2}" escape="false"/>
    <h:outputText value="#{buffer1}" escape="false"/>
    <h:outputText value="#{buffer2}" escape="false"/>

    <h2>dataTable Example</h2>

    <t:buffer into="#{table}">
        <t:dataTable id="data"
                styleClass="scrollerTable"
                headerClass="standardTable_Header"
                footerClass="standardTable_Header"
                rowClasses="standardTable_Row1,standardTable_Row2"
                columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                var="car"
                value="#{scrollerList.list}"
                preserveDataModel="false"
                rows="10"
           >
           <h:column>
               <f:facet name="header">
               </f:facet>
               <h:outputText value="#{car.id}" />
           </h:column>

           <h:column>
               <f:facet name="header">
                  <h:outputText value="#{example_messages['label_cars']}" />
               </f:facet>
               <h:outputText value="#{car.type}" />
           </h:column>

           <h:column>
               <f:facet name="header">
                  <h:outputText value="#{example_messages['label_color']}" />
               </f:facet>
               <h:outputText value="#{car.color}" />
           </h:column>

        </t:dataTable>
    </t:buffer>

    <t:buffer into="#{tableScroller}">
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
                    >
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
                <t:dataScroller id="scroll_2"
                        for="data"
                        rowsCountVar="rowsCount"
                        displayedRowsCountVar="displayedRowsCountVar"
                        firstRowIndexVar="firstRowIndex"
                        lastRowIndexVar="lastRowIndex"
                        pageCountVar="pageCount"
                        pageIndexVar="pageIndex"
                        >
                        <h:outputFormat value="#{example_messages['dataScroller_pages']}" styleClass="standard" >
                            <f:param value="#{rowsCount}" />
                            <f:param value="#{displayedRowsCountVar}" />
                            <f:param value="#{firstRowIndex}" />
                            <f:param value="#{lastRowIndex}" />
                            <f:param value="#{pageIndex}" />
                            <f:param value="#{pageCount}" />
                        </h:outputFormat>
                </t:dataScroller>
        </h:panelGrid>
    </t:buffer>

    <h:outputText value="#{tableScroller}" escape="false"/>
    <h:outputText value="#{table}" escape="false"/>
    <h:outputText value="#{tableScroller}" escape="false"/>

</f:view>

<%@include file="inc/page_footer.jsp" %>

</body>

</html>
