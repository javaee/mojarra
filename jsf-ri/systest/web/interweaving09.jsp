<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>


<f:view>
    <html>
        <body>
            <h:form id="form">
                <h:dataTable value="#{interweaving09.items}" var="item"
binding="#{interweaving09.table}">
                    <h:column>
                        <%-- 
                           Validates CASE 2 in UIComponentClassicTagBase when
                           using a component binding with a lifetime longer
                           than request
                        --%>
                        <h:outputText value="#{item}"/>                     
                        ciao
                        <h:outputText value="#{item}"/>
                    </h:column>
                </h:dataTable>                
            </h:form>
        </body>
    </html>
</f:view>

