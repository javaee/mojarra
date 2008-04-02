<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">

<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>

<HEAD>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
	<TITLE>CarDemo</TITLE>
</HEAD>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<BODY BGCOLOR="white">

<f:use_faces>
<h:form  formName="carStoreForm" modelReference="CurrentOptionServer" >

<P>
<TABLE BORDER="0" WIDTH="660" BGCOLOR="#4F4F72">
<TR>
    <TD WIDTH="828">
    <TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
    <TR>
        <TD WIDTH="820">
        <TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BGCOLOR="white">
        <TR>
            <TD WIDTH="100%" VALIGN="TOP">
            <h:graphic_image  url="/cardemo.jpg" />
            </TD>
        </TR>
        <TR>
            <TD WIDTH="100%">
            <h:graphic_image  
                modelReference="CurrentOptionServer.carImage" />
            <B> <FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text  
                modelReference="CurrentOptionServer.carTitle" 
                key="carDetailsLabel" />
            </FONT></B>
            <FONT FACE="Arial, Helvetica"><BR> <BR>
            <h:output_text  
                modelReference="CurrentOptionServer.carDesc" 
                key="curCarDescLabel" /> 
            <BR> <BR>
            </FONT><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
            <h:output_text  value="Base Price" 
                key="basePriceLabel"/>
            </FONT></B><FONT FACE="Arial, Helvetica"> 
            <h:output_text   
                modelReference="CurrentOptionServer.carBasePrice"
                key="currentCarBasePriceLabel"/>
            <BR> </FONT><B>
            <FONT COLOR="#93B629" FACE="Arial, Helvetica">
            <h:output_text   key="yourPriceLabel"
                value="Your Price" />
            </FONT></B>
            <FONT FACE="Arial, Helvetica">
            <h:output_text   
                modelReference="CurrentOptionServer.carCurrentPrice"
                key="currentCarPriceLabel"/>
            <BR> <BR>
            <h:command_button  commandName="buy" label="Buy" />
            </TD></FONT></TD>
        </TR>
        <TR>
            <TD WIDTH="100%" BGCOLOR="white"><B><FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text   value="Options and Packages" />
            </FONT></B></TD>
        </TR>
        <TR>
            <TD WIDTH="100%"><BLOCKQUOTE>
            <TABLE>
            <TR>
                <TD><P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text  key="Engine" value="Engine" />
                </FONT></B></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_menu  
                     modelReference="CurrentOptionServer.currentEngineOption">
                    <h:selectitems  
                        modelReference="CurrentOptionServer.engineOption"/>
                </h:selectone_menu>    
                </FONT></P></BLOCKQUOTE>
                </TD>
                <TD><P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                 <h:output_text   value="Brakes"key="Brakes"  />
                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_radio  
                    modelReference="CurrentOptionServer.currentBrakeOption">
                    <h:selectitems  
                        modelReference="CurrentOptionServer.brakeOption"/>
                </h:selectone_radio>
                </FONT></P></BLOCKQUOTE>
                </TD>
            </TR>
            <TR>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text  key="Suspension" 
                    value="Suspension" />
                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_menu  
                    modelReference="CurrentOptionServer.currentSuspensionOption">
                    <h:selectitems  
                        modelReference="CurrentOptionServer.suspensionOption"/>
                </h:selectone_menu>
                </FONT></P> </BLOCKQUOTE>
                </TD>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text  key="Speakers" value="Speakers" />
                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_radio  
                    modelReference="CurrentOptionServer.currentSpeakerOption" >
                    <h:selectitems  
                        modelReference="CurrentOptionServer.speakerOption"/>
                </h:selectone_radio>
                </FONT></P> </BLOCKQUOTE>
                </TD>
            </TR>
            <TR>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text  key="Audio" value="Audio" />
                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE> <P><FONT FACE="Arial, Helvetica">
                <h:selectone_radio  
                    modelReference="CurrentOptionServer.currentAudioOption">
                    <h:selectitems  
                        modelReference="CurrentOptionServer.audioOption"/>
                </h:selectone_radio>
                </FONT></P> 
                </BLOCKQUOTE>
                </TD>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text  key="Transmission" 
                    value="Transmission" />
                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE> <P><FONT FACE="Arial, Helvetica">
                <h:selectone_menu  
                    modelReference="CurrentOptionServer.currentTransmissionOption">
                    <h:selectitems  
                        modelReference="CurrentOptionServer.transmissionOption"/>
                </h:selectone_menu>
                </FONT></P> </BLOCKQUOTE>
                </TD>
            </TR>
            <TR>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text  key="OtherOptions" 
                    value="Other Options" />
                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE> </FONT></P> 
                </BLOCKQUOTE>
                </TD>
            </TR>
            </TABLE>

            <TABLE width="100%"> <P><FONT FACE="Arial, Helvetica">
            <TR>
                <TD> 
                <h:selectboolean_checkbox  title="Sunroof" 
                    alt="Sunroof" modelReference="CurrentOptionServer.sunRoof"
                    key="sunroofLabel" />
                </TD>																		
                <TD>
                <h:output_label  for="sunRoof" > Sun Roof</h:output_label> 
                </TD> 
                <TD>
                <h:selectboolean_checkbox  
                    title="Cruise Control"  
                    modelReference="CurrentOptionServer.cruiseControl"
                    key="cruiseLabel" />
                </TD>
                <TD>
                <h:output_label  for="cruiseControl" > Cruise Control</h:output_label> 
                </TD>
                <TD>
                <h:selectboolean_checkbox  
                    title="Keyless Entry"  alt="Keyless Entry"
                    modelReference="CurrentOptionServer.keylessEntry"
                    key="keylessLabel" />
                </TD>
                <TD>
                <h:output_label  for="keylessEntry" > Keyless Entry</h:output_label> 
                </TD>
            </TR>
            <TR>
                <TD> 
                <h:selectboolean_checkbox 
                    title="Security System"  alt="Security System"
                    modelReference="CurrentOptionServer.securitySystem"
                    key="securityLabel" />
                </TD>
                <TD>
                <h:output_label  for="securitySystem" > Security System</h:output_label> 
                </TD>
                <TD>
                <h:selectboolean_checkbox  title="Ski Rack"  
                    alt="Ski Rack" modelReference="CurrentOptionServer.skiRack"
                    key="skiRackLabel" />
                </TD>
                <TD>
                <h:output_label  for="skiRack" >Ski Rack</h:output_label> 
                </TD>
                <TD>
                <h:selectboolean_checkbox  title="Tow Package"  
                    alt="Tow Package" 
                    modelReference="CurrentOptionServer.towPackage"
                    key="towPkgLabel" />
                </TD>
                <TD>
                <h:output_label  for="towPackage" >Tow Package</h:output_label> 
                </TD>
            </TR>
            <TR>
                <TD>
                <h:selectboolean_checkbox  title="GPS" alt="GPS"
                    modelReference="CurrentOptionServer.gps" key="gpsLabel" />
                </FONT> 
                </TD>
                <TD>
                <h:output_label  for="gps" >GPS</h:output_label> 
                </TD>
            </TR>
            </TABLE>
            </BLOCKQUOTE>
            <TABLE ALIGN=RIGHT>
            <TR> 
                <TD>
                </TD>
                <TD>
                <P>
                <h:command_button  label="Buy" key="buy"
                    commandName="buy"/>
                </TD>
            </TR>
            </TABLE>
            </TD>
        </TR>
        </TABLE>
        </TD>
    </TR>
    </TABLE>
    </TD>
</TR>
</TABLE>
</h:form>
</f:use_faces>
</BODY>

</HTML>
