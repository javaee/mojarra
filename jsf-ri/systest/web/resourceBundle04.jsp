<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
    Values from Components: <br/>

    exsting resource bundle, but non-existing key: <h:outputText
      value="#{resourceBundle03.bogusKey}"/> <br/>

</f:view>
