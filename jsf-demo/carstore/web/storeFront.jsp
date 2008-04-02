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
	<TITLE>Welcome to CarStore</TITLE>
        <link rel="stylesheet" type="text/css"
            href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</HEAD>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<BODY BGCOLOR="white">

       <f:loadBundle basename="carstore.bundles.Resources" var="bundle"/>

<f:view>  

    <h:form>

        <h:graphicImage  url="/images/cardemo.jpg" /> 

        <h:panelGrid columns="2" 
                      footerClass="form-footer"
                      headerClass="form-header"
                      styleClass="top-table"
                      columnClasses="single-column"
                      summary="#{bundle.chooseCar}" 
                      title="#{bundle.chooseCar}" >

            <h:panelGrid columns="2"
                          styleClass="storeFrontCar">

                <!-- Jalopy -->
                <h:graphicImage binding="#{carstore.models.Jalopy.components.imageSmall}" />
                <h:outputText styleClass="subtitlebig"
                  value="#{carstore.models.Jalopy.attributes.title}" />
                <h:outputText 
            value="#{carstore.models.Jalopy.attributes.description}"/> 
                <h:commandButton 
                           action="#{carstore.storeFrontJalopyPressed}" 
                           value="#{bundle.moreButton}" >
                </h:commandButton>

                <!-- Roadster -->
                <h:graphicImage binding="#{carstore.models.Roadster.components.imageSmall}" />
                <h:outputText styleClass="subtitlebig"
               value="#{carstore.models.Roadster.attributes.title}" />
                <h:outputText 
         value="#{carstore.models.Roadster.attributes.description}" /> 
                <h:commandButton 
                           action="#{carstore.storeFrontRoadsterPressed}" 
                           value="#{bundle.moreButton}" >
                </h:commandButton>

            </h:panelGrid>

            <h:panelGrid columns="2"
                          styleClass="storeFrontCar">

                <!-- Luxury -->
                <h:graphicImage binding="#{carstore.models.Luxury.components.imageSmall}" />
                <h:outputText styleClass="subtitlebig"
                  value="#{carstore.models.Luxury.attributes.title}" />
                <h:outputText 
           value="#{carstore.models.Luxury.attributes.description}" /> 
                <h:commandButton 
                           action="#{carstore.storeFrontLuxuryPressed}" 
                           value="#{bundle.moreButton}" >
                </h:commandButton>

                <!-- SUV -->
                <h:graphicImage binding="#{carstore.models.SUV.components.imageSmall}" />
                <h:outputText styleClass="subtitlebig"
                  value="#{carstore.models.SUV.attributes.title}" />
                <h:outputText 
              value="#{carstore.models.SUV.attributes.description}" /> 
                <h:commandButton action="#{carstore.storeFrontSUVPressed}" 
                                  value="#{bundle.moreButton}" >
                </h:commandButton>

            </h:panelGrid>

        </h:panelGrid>

    </h:form>

 <jsp:include page="bottomMatter.jsp"/>

</f:view>

</BODY>

</HTML>
