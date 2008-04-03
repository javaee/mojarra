<%@ include file="header.inc" %>
<f:view> 
            <h1>You uploaded some files!</h1>
            <h:dataTable value="#{testBean.fileNames}" var="fileName">
                <h:column><h:outputText value="#{fileName}"/></h:column>
            </h:dataTable>
</f:view>
<%@ include file="footer.inc" %>