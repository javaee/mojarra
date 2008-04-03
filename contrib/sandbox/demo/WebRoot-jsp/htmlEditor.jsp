<%@ include file="header.inc" %>
<f:view> 
    <h:form>
        <h3>Normal editor</h3>
        <risb:htmlEditor rows="10" cols="85" 
            value="#{testBean.editorValue}"/>
        <h3>Simplified editor</h3>
        <risb:htmlEditor rows="10" cols="85" 
            value="#{testBean.editorValue}"
            themeStyle="simplified"/>
        <h3>Full editor</h3>
        <risb:htmlEditor rows="10" cols="85" 
            value="#{testBean.editorValue}"
            themeStyle="full"/>
    </h:form>
</f:view>
<%@ include file="footer.inc" %>