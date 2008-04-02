<%@ include file="header.inc" %>
<f:view> 
    <h:form>
        <risb:calendar value="#{testBean.date}"></risb:calendar>
        <h:commandButton value="Submit"></h:commandButton>
    </h:form>
    <h:outputText value="#{testBean.date}" rendered="#{!empty testBean.date}"/>
</f:view>
<%@ include file="footer.inc" %>
