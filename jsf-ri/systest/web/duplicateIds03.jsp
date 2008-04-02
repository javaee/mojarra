<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<%-- Confirm duplicate ID's are found --%>

<% try { %>
<f:view>
    <h:outputText id="output1"/>
    <h:outputText />
    <h:outputText>
       <f:facet name="facet1">
           <h:outputText id="output1"/>
       </f:facet>
    </h:outputText>
</f:view>
<% 
  } catch (JspException je) {
       if (!(je.getRootCause() instanceof IllegalStateException)) {
           throw je;
       }
  }
%>
