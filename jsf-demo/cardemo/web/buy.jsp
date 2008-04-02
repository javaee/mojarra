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

<body bgcolor="#FFFFFF">
<f:use_faces>  

<h:form  formName="carStoreForm" >

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
<h:output_text  valueRef="CurrentOptionServer.carTitle" />
</FONT></B>


<h:panel_grid id="choicesPanel" columns="2" footerClass="subtitle"
   headerClass="subtitlebig" panelClass="medium" columnClasses="subtitle,medium">

<f:facet name="header">
    <h:panel_group>
        <h:output_text  key="buyTitle" bundle="carDemoBundle"/>
    </h:panel_group>
 </f:facet>

    <h:output_text key="Engine" bundle="carDemoBundle" />

    <h:output_text valueRef="CarServer.currentEngineOption"  />

    <h:output_text key="Brakes" bundle="carDemoBundle" />

    <h:output_text  valueRef="CarServer.currentBrakeOption" />

    <h:output_text  key="Suspension" bundle="carDemoBundle" />

    <h:output_text  valueRef="CarServer.currentSuspensionOption" />

    <h:output_text  key="Speakers" bundle="carDemoBundle" />

    <h:output_text  valueRef="CarServer.currentSpeakerOption" />

    <h:output_text  key="Audio" bundle="carDemoBundle" />

    <h:output_text  valueRef="CarServer.currentAudioOption" />

    <h:output_text  key="Transmission" bundle="carDemoBundle" />

    <h:output_text  valueRef="CarServer.currentTransmissionOption" />

    <h:output_text  key="sunroofLabel" bundle="carDemoBundle"  />

    <h:output_text  valueRef="CarServer.currentPackage.sunRoofSelected" />

    <h:output_text  key="cruiseLabel" bundle="carDemoBundle"  />

    <h:output_text  valueRef="CarServer.currentPackage.cruiseControlSelected" />

    <h:output_text key="keylessLabel" bundle="carDemoBundle"  />

    <h:output_text  valueRef="CarServer.currentPackage.keylessEntrySelected" />

    <h:output_text  key="securityLabel" bundle="carDemoBundle"  />

    <h:output_text  valueRef="CarServer.currentPackage.securitySystemSelected" />

    <h:output_text  key="skiRackLabel" bundle="carDemoBundle"  />

    <h:output_text  valueRef="CarServer.currentPackage.skiRackSelected" />

    <h:output_text  key="towPkgLabel" bundle="carDemoBundle"  />

    <h:output_text  valueRef="CarServer.currentPackage.towPackageSelected" />

    <h:output_text  key="gpsLabel" bundle="carDemoBundle"  />

    <h:output_text  valueRef="CarServer.currentPackage.gpsSelected" />
    
  <f:facet name="footer">
     <h:panel_group>
        <h:output_text  key="yourPriceLabel" bundle="carDemoBundle"  />
        &nbsp;
        <h:output_text  valueRef="CarServer.carCurrentPrice" />
     </h:panel_group>
  </f:facet>

</h:panel_grid>

<TABLE ALIGN=RIGHT>
<TR>
<TD>
<P>
<h:command_button key="buy" bundle="carDemoBundle"
                  commandName="customer" action="success" />

</TD></TR></TABLE>
					

<tr>
</TD></TR>
</TABLE>
</blockquote>
</td>
</tr>
</table>
</h:form>
</f:use_faces>
</body>
</html>
