<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<!--
 Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 
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
<html>
<head>
   <title>CarStore</title>
   <link rel="stylesheet" type="text/css"
        href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>

     <f:loadBundle basename="carstore.bundles.Resources" var="bundle"/>

<f:view>
    <h:form>

        <h:panel_grid columns="1" 
                      footerClass="form-footer"
                      headerClass="form-header"
                      styleClass="main-background"
                      columnClasses="single-column"
                      summary="#{bundle.chooseLocale}" 
                      title="#{bundle.chooseLocale}" >

            <h:graphic_image  url="/images/cardemo.jpg" /> 

            <h:output_text styleClass="maintitle"
                           value="#{bundle.chooseLocale}" />

            <h:graphic_image id="mapImage" url="/images/world.jpg" 
                             alt="#{bundle.chooseLocale}"
                             usemap="#worldMap" />

            <d:map id="worldMap" current="NAmericas" immediate="true"
                   action="storeFront"
                   actionListener="#{carstore.chooseLocaleFromMap}">
                    <d:area id="NAmerica" value="#{NA}" 
                            onmouseover="/images/world_namer.jpg" 
                            onmouseout="/images/world.jpg" 
                            targetImage="mapImage" />
                    <d:area id="SAmerica" value="#{SA}" 
                            onmouseover="/images/world_samer.jpg"
                            onmouseout="/images/world.jpg" 
                            targetImage="mapImage" />
                    <d:area id="Germany" value="#{gerA}" 
                            onmouseover="/images/world_germany.jpg" 
                            onmouseout="/images/world.jpg" 
                            targetImage="mapImage" />
                    <d:area id="France" value="#{fraA}" 
                            onmouseover="/images/world_france.jpg" 
                            onmouseout="/images/world.jpg" 
                            targetImage="mapImage" />
            </d:map>

        </h:panel_grid>

    </h:form>

    <h:form>

        <!-- For non graphical browsers  -->

        <h:panel_grid id="links" columns="4" 
                      summary="#{bundle.chooseLocale}" 
                      title="#{bundle.chooseLocale}" >

          <h:command_link id="NAmerica" action="storeFront"
                          actionListener="#{carstore.chooseLocaleFromLink}">
  
            <h:output_text value="#{bundle.english}" />

          </h:command_link>

          <h:command_link id="Germany" action="storeFront"
                          actionListener="#{carstore.chooseLocaleFromLink}">

            <h:output_text value="#{bundle.german}" />

          </h:command_link>

          <h:command_link id="France" action="storeFront"
                          actionListener="#{carstore.chooseLocaleFromLink}">

            <h:output_text value="#{bundle.french}" />

          </h:command_link>

          <h:command_link id="SAmerica" action="storeFront"
                          actionListener="#{carstore.chooseLocaleFromLink}">

            <h:output_text value="#{bundle.spanish}" />

          </h:command_link>

        </h:panel_grid>
    
    </h:form>

 <jsp:include page="bottomMatter.jsp"/>

</f:view>

</html>
