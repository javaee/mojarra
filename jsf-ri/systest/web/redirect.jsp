<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>

    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

       <f:view>  
         <h:form id="redirect">
	   <h:outputText value="Label" /> 
	   <p>
	   <h:commandButton id="submit" action="success" value="submit"/>
         </h:form>
       </f:view>

</HTML>
