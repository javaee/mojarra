<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<%-- Confirm duplicate ID's are found --%>
<% try { %>
<f:view>
    <h:outputText id="duplicate1"/>
    <h:outputText id="output2"/>
    <h:outputText id="duplicate1"/>
</f:view>
<% 
  } catch (JspException je) {
       je.printStackTrace();
       if (!(je.getRootCause() instanceof IllegalStateException)) {
           throw je;
       }
   }
%>
