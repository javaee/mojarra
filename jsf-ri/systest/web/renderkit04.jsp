<HTML>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<jsp:useBean id="TestBean" class="com.sun.faces.systest.model.TestBean"
             scope="session"/>
<f:view renderKitId="HTML_BASIC">
    <h:form id="form">
        <table>
            <tr>
                <td><h:outputText value="RenderKit:"/></td>
                <td><h:outputText value="#{TestBean.renderKitInfo}"/></td>
            </tr>
            <tr>
                <td><h:outputText value="ResponseWriter:"/></td>
                <td><h:outputText value="#{TestBean.responseWriterInfo}"/></td>
            </tr>
        </table>
        <h:commandButton id="submit" action="success" value="submit"/>
    </h:form>
</f:view>

</HTML>
