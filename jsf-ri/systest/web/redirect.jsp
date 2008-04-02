<HTML>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

       <f:view>  
         <h:form id="redirect">
	   <h:output_text value="Label" /> 
	   <p>
	   <h:command_button id="submit" action="success" value="submit"/>
         </h:form>
       </f:view>

</HTML>
