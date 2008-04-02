<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <title>CarDemo</title>
</head>

<%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core/" prefix="f" %>

<body bgcolor="#FFFFFF">
<f:usefaces>  

<h:form id="carStoreForm" formName="carStoreForm" modelReference="CurrentOptionServer" >

<TABLE BORDER="0" WIDTH="660" BGCOLOR="#4F4F72">
<tr NOSAVE>
<td WIDTH="828" NOSAVE>
<table BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH="100%" >
<tr>
<td WIDTH="820">
<table BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH="100%" BGCOLOR="#FFFFFF" >
<tr>
<td VALIGN=TOP WIDTH="100%">
<h:graphic_image id="cardemo_img" url="/cardemo.jpg" /></td>
</tr>

<tr>
<td WIDTH="100%"><b><font face="Arial, Helvetica"><FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
<h:output_text 	id="carTitle"  
			modelReference="CurrentOptionServer.carTitle"
			key="carTitle"
			 /></FONT></B>
<FONT FACE="Arial, Helvetica"><BR>
<h:output_text 	id="yourOpts" 
			value="You have chosen the following options: " 
			key="yourOpts"
			 /> <BR>

</FONT>
<TABLE><TR><TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text id="engine"  value= "Engine" key="Engine"  /></FONT></B></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text 	id="currentEngineOption"
			modelReference="CurrentOptionServer.currentEngineOption"  
			key="Engine"  /></FONT></P>
</BLOCKQUOTE>
<TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text id="brakes"  value="Brakes" key="Brakes"  /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text id="brakeOption" modelReference="CurrentOptionServer.currentBrakeOption" />
</FONT></P> 
</BLOCKQUOTE>
</TD></TR>
<TR>
<TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text id="suspension"  value="Suspension" key="Suspension" /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text id="suspensionOption" modelReference="CurrentOptionServer.currentSuspensionOption" />
</FONT></P> 
</BLOCKQUOTE>
</TD>
<TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text id="speakers" value="Speakers" key="Speakers"  /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text id="speakerOption" modelReference="CurrentOptionServer.currentSpeakerOption" /></FONT></P> 
</BLOCKQUOTE>
</TD></TR>
<TR><TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text id="audio" value="Audio" key="Audio"  /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text id="audioOption" modelReference="CurrentOptionServer.currentAudioOption" /></FONT></P> 
</BLOCKQUOTE>
</TD>
<TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text id="transmission" value="Transmission" key="Transmission"  /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
<P><FONT FACE="Arial, Helvetica">
<h:output_text id="transmissionOption" modelReference="CurrentOptionServer.currentTransmissionOption" /></FONT></P> 
</BLOCKQUOTE>
</TD></TR>
<TR><TD>
<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
<h:output_text id="other" value="Other Options"  key="OtherOptions"   /></FONT></B>
<FONT FACE="Arial, Helvetica"></FONT></P>
<BLOCKQUOTE>
</TD></TR></TABLE>
<TABLE>
<P><FONT FACE="Arial, Helvetica">
<TR><TD>
<h:output_text id="sunroof"  value="Sunroof" key="sunroofLabel"   />
</TD><TD>
<h:output_text id="sunroof_choice" modelReference="CurrentOptionServer.sunRoof" /></FONT></P></TD></TR>
<TR><TD>
<h:output_text id="cruise" value="Cruise" key="cruiseLabel"   />
</TD><TD>
<h:output_text id="cruise_choice" modelReference="CurrentOptionServer.cruiseControl" /></FONT></P></TD></TR>
<TR><TD>
<h:output_text id="keyless" value="Keyless" key="keylessLabel"   />
</TD><TD>
<h:output_text id="keyless_choice" modelReference="CurrentOptionServer.keylessEntry" /></FONT></P></TD></TR>
<TR><TD>
<h:output_text id="security"  value="Security" key="securityLabel"   />
</TD><TD>
<h:output_text id="security_choice" modelReference="CurrentOptionServer.securitySystem" /></FONT></P></TD></TR>
<TR><TD>
<h:output_text id="skiRack" value="Ski Rack" key="skiRackLabel"   />
</TD><TD>
<h:output_text id="skiRack_choice" modelReference="CurrentOptionServer.skiRack" /></FONT></P></TD></TR>
<TR><TD>
<h:output_text id="towPkg" value="Tow Package" key="towPkgLabel"   />
</TD><TD>
<h:output_text id="towPkg_choice" modelReference="CurrentOptionServer.towPackage" /></FONT></P></TD></TR>
<TR><TD>
<h:output_text id="gps"  value="GPS" key="gpsLabel"   />
</TD><TD>
<h:output_text id="gps_choice" modelReference="CurrentOptionServer.gps" /></FONT></P></TD></TR>
</TD></TR></TABLE>
</FONT></P> 
</BLOCKQUOTE>
<TABLE ALIGN=RIGHT>
<TR><TD>
</TD><TD>
<P>
<h:command_button id="configure" label="Reconfigure"
 				     key="configureButton"
				     commandName="more"/>
<h:command_button 	id="customer"
			label="Buy" 
			key="buyButton"
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
</f:usefaces>
</body>
</html>
