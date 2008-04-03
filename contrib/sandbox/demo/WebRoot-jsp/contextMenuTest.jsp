<?xml version='1.0' encoding='UTF-8'?>
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/ri/sandbox" prefix="risb"%>
<html>
    <f:view>
        <h:dataTable id="books"
            columnClasses="list-column-center,list-column-right,list-column-center,list-column-right"
            headerClass="list-header" 
            rowClasses="list-row" 
            styleClass="list-background" 
            value="#{BookStore.items}"
            var="store">
            <h:column>
                <f:facet name="header">
                    <h:outputText value="#{msg.storeNameLabel}" />
                </f:facet>
                <h:outputText value="#{store.name}">
                    <risb:contextMenu>
                        <risb:menuItem>
                            <h:commandLink action="#{testBean.edit}" value="Edit">
                                <f:setPropertyActionListener value="#{store.id}" target="#{testBean.id}" />
                            </h:commandLink>
                        </risb:menuItem>
                        <risb:menuItem>
                            <h:commandLink action="#{testBean.delete}" value="Delete">
                                <f:setPropertyActionListener value="#{store.id}" target="#{testBean.id}" />
                            </h:commandLink>
                        </risb:menuItem>
                    </risb:contextMenu>
                </h:outputText>
            </h:column>

            <h:column>
                <f:facet name="header">
                    <Subject
                </f:facet>
                <h:outputText value="#{store.subject}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:outputText value="#{msg.storePriceLabel}" />
                </f:facet>
                <h:outputText value="#{store.price}" />
            </h:column>
        </h:dataTable>
    </f:view>
</html>
