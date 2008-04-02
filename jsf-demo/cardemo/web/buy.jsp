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
</head>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<body bgcolor="#FFFFFF">
<f:use_faces>  

<h:form  formName="carStoreForm" modelReference="CurrentOptionServer" >

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
<h:output_text  modelReference="CurrentOptionServer.carTitle" />
</FONT></B>


<FONT FACE="Arial, Helvetica"><BR>
<h:output_text  key="buyTitle" bundle="carDemoBundle"/> <BR>




</FONT>
<TABLE><TR><TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text key="Engine" bundle="carDemoBundle" /></FONT></B></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text 	
			modelReference="CurrentOptionServer.currentEngineOption"  />

</BLOCKQUOTE>
<TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text key="Brakes" bundle="carDemoBundle" /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text  modelReference="CurrentOptionServer.currentBrakeOption" />
</FONT></P> 
</BLOCKQUOTE>
</TD></TR>
<TR>
<TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text  key="Suspension" bundle="carDemoBundle" /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text  modelReference="CurrentOptionServer.currentSuspensionOption" />
</FONT></P> 
</BLOCKQUOTE>
</TD>
<TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text  key="Speakers" bundle="carDemoBundle" /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text  modelReference="CurrentOptionServer.currentSpeakerOption" /></FONT></P> 
</BLOCKQUOTE>
</TD></TR>
<TR><TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text  key="Audio" bundle="carDemoBundle" /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text  modelReference="CurrentOptionServer.currentAudioOption" /></FONT></P> 
</BLOCKQUOTE>
</TD>
<TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text  key="Transmission" bundle="carDemoBundle" /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text  modelReference="CurrentOptionServer.currentTransmissionOption" /></FONT></P> 
</BLOCKQUOTE>
</TD></TR>
<TR><TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text key="OtherOptions" bundle="carDemoBundle"  /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
</TD></TR></TABLE>
<TABLE>
<P><FONT FACE="Arial, Helvetica">
<TR><TD>
<h:output_text  key="sunroofLabel" bundle="carDemoBundle"  />
</TD><TD>
<h:output_text  modelReference="CurrentOptionServer.sunRoof" /></FONT></P></TD></TR>
<TR><TD>
<h:output_text  key="cruiseLabel" bundle="carDemoBundle"  />
</TD><TD>
<h:output_text  modelReference="CurrentOptionServer.cruiseControl" /></FONT></P></TD></TR>
<TR><TD>
<h:output_text key="keylessLabel" bundle="carDemoBundle"  />
</TD><TD>
<h:output_text  modelReference="CurrentOptionServer.keylessEntry" /></FONT></P></TD></TR>
<TR><TD><h:output_text  key="securityLabel" bundle="carDemoBundle"  />
</TD><TD>
<h:output_text  modelReference="CurrentOptionServer.securitySystem" /></FONT></P></TD></TR>
<TR><TD>
<h:output_text  key="skiRackLabel" bundle="carDemoBundle"  />
</TD><TD>
<h:output_text  modelReference="CurrentOptionServer.skiRack" /></FONT></P></TD></TR>
<TR><TD>
<h:output_text  key="towPkgLabel" bundle="carDemoBundle"  />
</TD><TD>
<h:output_text  modelReference="CurrentOptionServer.towPackage" /></FONT></P></TD></TR>
<TR><TD>
<h:output_text  key="gpsLabel" bundle="carDemoBundle"  />
</TD><TD>
<h:output_text  modelReference="CurrentOptionServer.gps" /></FONT></P></TD></TR>
</TD></TR></TABLE>
</FONT></P> 
</BLOCKQUOTE>
<TABLE ALIGN=RIGHT>
<TR><TD>
</TD><TD>
<P>
<h:command_button  key="configureButton" bundle="carDemoBundle"
				     commandName="more" />
<h:command_button key="buy" bundle="carDemoBundle"
                  commandName="customer"/>

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
