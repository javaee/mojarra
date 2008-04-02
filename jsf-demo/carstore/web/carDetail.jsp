<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">

<!--
 Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 
 Redistribution and use in source and binary forms, with or
 without modification, are permitted provided that the following
 conditions are met:
 
 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 
 - Redistribution in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials
   provided with the distribution.
    
 Neither the name of Sun Microsystems, Inc. or the names of
 contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.
  
 This software is provided "AS IS," without a warranty of any
 kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
  
 You acknowledge that this software is not designed, licensed or
 intended for use in the design, construction, operation or
 maintenance of any nuclear facility.
-->

<HTML>

<HEAD>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
	<TITLE>CarStore</TITLE>
        <link rel="stylesheet" type="text/css"
            href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</HEAD>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

     <f:loadBundle basename="carstore.bundles.Resources" var="bundle"/>

<BODY BGCOLOR="white">

<f:view>

    <h:form>


        <!-- non-option details -->

        <h:panelGrid columns="1"
                      summary="#{bundle.carDetails}"
                      title="#{bundle.carDetails}">

            <h:graphicImage  url="/images/cardemo.jpg" /> 

            <h:graphicImage 
               binding="#{carstore.currentModel.components.image}" />

            <h:outputText styleClass="subtitlebig"
               binding="#{carstore.currentModel.components.title}" />
            
            <h:outputText 
               binding="#{carstore.currentModel.components.description}" />

            <h:panelGrid columns="2">

	      <h:outputText styleClass="subtitle"
                             value="#{bundle.basePriceLabel}" />

	      <h:outputText 
		 binding="#{carstore.currentModel.components.basePrice}" />

	      <h:outputText styleClass="subtitle"
                             value="#{bundle.yourPriceLabel}" />

	      <h:outputText value="#{carstore.currentModel.currentPrice}" />

            </h:panelGrid>

            <h:commandButton action="#{carstore.buyCurrentCar}"
                 value="#{bundle.buy}" />

        </h:panelGrid>

        <jsp:include page="optionsPanel.jsp"/>

        <h:commandButton value="#{bundle.recalculate}" 
                      action="#{carstore.currentModel.updatePricing}" />

        <h:commandButton action="#{carstore.buyCurrentCar}"
                 value="#{bundle.buy}" />

</h:form>

 <jsp:include page="bottomMatter.jsp"/>

</f:view>
</BODY>

</HTML>
