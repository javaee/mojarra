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
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <title>CarDemo</title>
   <link rel="stylesheet" type="text/css"
            href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="cardemo.Resources" var="carDemoBundle"/>
<body bgcolor="#FFFFFF">
<f:view>  

<h:form  >

<TABLE BORDER="0" WIDTH="660" BGCOLOR="#4F4F72">
<tr NOSAVE>
<td WIDTH="828" NOSAVE>
<table BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH="100%" >
<tr>
<td WIDTH="820">
<table BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH="100%" BGCOLOR="#FFFFFF" >
<tr>
<td VALIGN=TOP WIDTH="100%">
<h:graphic_image  url="/cardemo.jpg" /></td>
</tr>

<tr>
<td WIDTH="100%"><b><font face="Arial, Helvetica"><FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
<h:output_text  value="#{CarServer.carTitle}" />
</FONT></B>


<h:panel_grid id="choicesPanel" columns="2" footerClass="subtitle"
   headerClass="subtitlebig" styleClass="medium" columnClasses="subtitle,medium">

<f:facet name="header">
    <h:panel_group>
        <h:output_text  value="#{carDemoBundle.buyTitle}"/>
    </h:panel_group>
 </f:facet>

    <h:output_text value="#{carDemoBundle.Engine}" />

    <h:output_text value="#{CarServer.currentEngineOption}"  />

    <h:output_text value="#{carDemoBundle.Brakes}" />

    <h:output_text  value="#{CarServer.currentBrakeOption}" />

    <h:output_text  value="#{carDemoBundle.Suspension}" />

    <h:output_text  value="#{CarServer.currentSuspensionOption}" />

    <h:output_text  value="#{carDemoBundle.Speakers}" />

    <h:output_text  value="#{CarServer.currentSpeakerOption}" />

    <h:output_text  value="#{carDemoBundle.Audio}" />

    <h:output_text  value="#{CarServer.currentAudioOption}" />

    <h:output_text  value="#{carDemoBundle.Transmission}" />

    <h:output_text  value="#{CarServer.currentTransmissionOption}" />

    <h:output_text  value="#{carDemoBundle.sunroofLabel}"  />

    <h:output_text  value="#{CarServer.currentPackage.sunRoofSelected}" />

    <h:output_text  value="#{carDemoBundle.cruiseLabel}"  />

    <h:output_text  value="#{CarServer.currentPackage.cruiseControlSelected}" />

    <h:output_text value="#{carDemoBundle.keylessLabel}"  />

    <h:output_text  value="#{CarServer.currentPackage.keylessEntrySelected}" />

    <h:output_text  value="#{carDemoBundle.securityLabel}"  />

    <h:output_text  value="#{CarServer.currentPackage.securitySystemSelected}" />

    <h:output_text  value="#{carDemoBundle.skiRackLabel}"  />

    <h:output_text  value="#{CarServer.currentPackage.skiRackSelected}" />

    <h:output_text  value="#{carDemoBundle.towPkgLabel}"  />

    <h:output_text  value="#{CarServer.currentPackage.towPackageSelected}" />

    <h:output_text  value="#{carDemoBundle.gpsLabel}"  />

    <h:output_text  value="#{CarServer.currentPackage.gpsSelected}" />
    
  <f:facet name="footer">
     <h:panel_group>
        <h:output_text  value="#{carDemoBundle.yourPriceLabel}"  />
        &nbsp;
        <h:output_text  value="#{CarServer.carCurrentPrice}" />
     </h:panel_group>
  </f:facet>

</h:panel_grid>

<TABLE ALIGN=RIGHT>
<TR>
<TD>
<P>
<h:command_button value="#{carDemoBundle.buy}"
                  action="success" />

</TD></TR></TABLE>
					

<tr>
</TD></TR>
</TABLE>
</blockquote>
</td>
</tr>
</table>
</h:form>
</f:view>
</body>
</html>
