<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<%-- Confirm duplicate ID's are found --%>
<% request.setAttribute("duplicate", "output1"); %>
<% try { %>
<f:view>
    <h:output_text id="#{duplicate}"/>
    <h:output_text id="output2"/>
    <h:output_text id="#{duplicate}"/>
</f:view>
<% 
  } catch (JspException je) {
       je.printStackTrace();
       if (!(je.getRootCause() instanceof IllegalStateException)) {
           throw je;
       }
   }
%>
