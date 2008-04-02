<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<%-- Confirm duplicate ID's are found --%>
<% try { %>
<f:view>
    <h:output_text id="duplicate1"/>
    <h:output_text id="output2"/>
    <h:output_text id="duplicate1"/>
</f:view>
<% 
  } catch (JspException je) {
       je.printStackTrace();
       if (!(je.getRootCause() instanceof IllegalStateException)) {
           throw je;
       }
   }
%>
