<%@ include file="header.inc" %>
<f:view> 
    <h:form>
        <risb:htmlEditor rows="20" cols="120" value="#{testBean.editorValue}"/>
        <br/>
        <h:commandButton value="Save"/>
    </h:form>
</f:view>
<%@ include file="footer.inc" %>