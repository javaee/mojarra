<%--
    Added for issue 212.  
    Ensure transient components do not cause issues
    with state restoration (verbatim is treated as transient)
--%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
    <h:form>
        <f:verbatim>
            Test Verbatim
        </f:verbatim>
        <h:outputText value="Test Output Text"/>       
        <h:commandButton id="submit" value="Groovy Button"/>
    </h:form>
</f:view>