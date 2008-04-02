<%@ include file="header.inc" %>
<f:view> 
    <h3>Input field</h3>
    <h:form>
        <risb:calendar value="#{testBean.date}"></risb:calendar>
        <h:commandButton value="Submit"></h:commandButton>
    </h:form>
    <h:outputText value="#{testBean.date}" rendered="#{!empty testBean.date}"/>
    <h3>Selects</h3>
    <h:form>
        <risb:calendar value="#{testBean.date2}" showSelects="true"></risb:calendar>
        <h:commandButton value="Submit"></h:commandButton>
    </h:form>
    <h:outputText value="#{testBean.date2}" rendered="#{!empty testBean.date2}"/>
</f:view>
<%@ include file="footer.inc" %>
