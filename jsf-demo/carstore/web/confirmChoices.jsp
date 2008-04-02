<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
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

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<HTML>

<HEAD>
	<TITLE>Welcome to CarStore</TITLE>
        <link rel="stylesheet" type="text/css"
            href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</HEAD>

<BODY BGCOLOR="white">

<f:loadBundle basename="carstore.bundles.Resources" var="bundle"/>
<f:view>  
<h:form>
 
<h:panelGrid id="mainPanel" columns="1" footerClass="subtitle"
   styleClass="medium" columnClasses="medium">

  <h:graphicImage  url="/images/cardemo.jpg" />
  <h:outputText binding="#{carstore.currentModel.components.title}" />
    
<h:panelGrid columns="2"  footerClass="subtitle"
   headerClass="subtitlebig" styleClass="medium" columnClasses="subtitle,medium">
    
    <f:facet name="header">
      <h:outputText  value="#{bundle.buyTitle}" />
    </f:facet>

    <h:outputText value="#{bundle.Engine}" />

    <h:outputText value="#{carstore.currentModel.attributes.engine}"  />

    <h:outputText value="#{bundle.Brakes}" />

    <h:outputText  value="#{carstore.currentModel.attributes.brake}" />

    <h:outputText  value="#{bundle.Suspension}" />

    <h:outputText  value="#{carstore.currentModel.attributes.suspension}" />

    <h:outputText  value="#{bundle.Speakers}" />

    <h:outputText  value="#{carstore.currentModel.attributes.speaker}" />

    <h:outputText  value="#{bundle.Audio}" />

    <h:outputText  value="#{carstore.currentModel.attributes.audio}" />

    <h:outputText  value="#{bundle.Transmission}" />

    <h:outputText  value="#{carstore.currentModel.attributes.transmission}" />

    <h:outputText  value="#{bundle.sunroofLabel}"  />

    <h:outputText  value="#{carstore.currentModel.attributes.sunroof}" />

    <h:outputText  value="#{bundle.cruiseLabel}"  />

    <h:outputText  value="#{carstore.currentModel.attributes.cruisecontrol}" />

    <h:outputText value="#{bundle.keylessLabel}"  />

    <h:outputText  value="#{carstore.currentModel.attributes.keylessentry}" />

    <h:outputText  value="#{bundle.securityLabel}"  />

    <h:outputText  value="#{carstore.currentModel.attributes.securitySystem}" />

    <h:outputText  value="#{bundle.skiRackLabel}"  />

    <h:outputText  value="#{carstore.currentModel.attributes.skiRack}" />

    <h:outputText  value="#{bundle.towPkgLabel}"  />

    <h:outputText  value="#{carstore.currentModel.attributes.towPackage}" />

    <h:outputText  value="#{bundle.gpsLabel}"  />

    <h:outputText  value="#{carstore.currentModel.attributes.gps}" />
    
  <f:facet name="footer">
     <h:panelGroup>
        <h:outputText  value="#{bundle.yourPriceLabel}"  />
        &nbsp;
        <h:outputText  value="#{carstore.currentModel.currentPrice}" />
     </h:panelGroup>
  </f:facet>

</h:panelGrid>

<h:panelGroup>
<h:commandButton value="#{bundle.buy}" action="customerInfo" title="#{bundle.buy}" />
<h:commandButton value="#{bundle.back}" action="carDetail" title="#{bundle.back}"/>
</h:panelGroup>

</h:panelGrid>
</h:form>
<jsp:include page="bottomMatter.jsp"/>
</f:view>

</BODY>
</HTML>

