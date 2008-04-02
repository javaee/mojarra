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

<HTML>

<HEAD>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
	<TITLE>Welcome to CarStore</TITLE>
        <link rel="stylesheet" type="text/css"
            href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</HEAD>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<BODY BGCOLOR="white">

       <f:loadBundle
	    basename="carstore.Resources" var="bundle"/>

<f:view>  

    <h:form>

        <h:panel_grid columns="2" 
                      footerClass="form-footer"
                      headerClass="form-header"
                      styleClass="main-background"
                      columnClasses="single-column"
                      summary="#{bundle.chooseCar}" 
                      title="#{bundle.chooseCar}" >

            <h:panel_grid columns="2"
                          styleClass="storeFrontCar">

                <!-- car 1 -->
                <h:graphic_image url="/150x126_Jalopy.jpg" />
                <h:output_text value="#{bundle.car1Desc}" /> 
                <h:output_text value="#{bundle.car1Title}" />
                <h:command_button action="more1" 
                                  value="#{bundle.moreButton}" >
                </h:command_button>

                <!-- car 2 -->
                <h:graphic_image url="/150x126_Roadster.jpg" />
                <h:output_text value="#{bundle.car2Desc}" /> 
                <h:output_text value="#{bundle.car2Title}" />
                <h:command_button action="more2" 
                                  value="#{bundle.moreButton}" >
                </h:command_button>

            </h:panel_grid>

            <h:panel_grid columns="2"
                          styleClass="storeFrontCar">

                <!-- car 3 -->
                <h:graphic_image url="/150x126_Luxury.jpg" />
                <h:output_text value="#{bundle.car3Desc}" /> 
                <h:output_text value="#{bundle.car3Title}" />
                <h:command_button action="more3" 
                                  value="#{bundle.moreButton}" >
                </h:command_button>

                <!-- car 4 -->
                <h:graphic_image url="/150x126_SUV.jpg" />
                <h:output_text value="#{bundle.car4Desc}" /> 
                <h:output_text value="#{bundle.car4Title}" />
                <h:command_button action="more3" 
                                  value="#{bundle.moreButton}" >
                </h:command_button>

            </h:panel_grid>

        </h:panel_grid>

    </h:form>

</f:view>

</BODY>

</HTML>
