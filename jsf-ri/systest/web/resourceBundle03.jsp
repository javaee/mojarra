<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
Values from Components: <br />

resourceBundle01: <h:outputText value="#{resourceBundle01.value1}" /> <br />

non existing resourceBundle: <h:outputText value="#{resourceBundle02.value1}" /> <br />

resourceBundle03: <h:outputText value="#{resourceBundle03.value3}" /> <br />

</f:view>
