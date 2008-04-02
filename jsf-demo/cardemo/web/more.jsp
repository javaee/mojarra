<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">

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
	<TITLE>CarDemo</TITLE>
        <link rel="stylesheet" type="text/css"
            href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</HEAD>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

     <f:loadBundle
	    basename="cardemo.Resources" var="carDemoBundle"/>

<BODY BGCOLOR="white">

<f:view>
<h:form  >

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
                valueRef="CarServer.carImage" />
                <BR>
            <B> <FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text  
                valueRef="CarServer.carTitle" 
                 />
            </FONT></B>
            <FONT FACE="Arial, Helvetica"><BR> <BR>
            <h:output_text  
                valueRef="CarServer.carDesc" />
            <BR> <BR>
            </FONT><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
            <h:output_text value="#{carDemoBundle.basePriceLabel}" />

            </FONT></B><FONT FACE="Arial, Helvetica"> 
            <h:output_text   
                valueRef="CarServer.carBasePrice" />

            <BR> </FONT><B>
            <FONT COLOR="#93B629" FACE="Arial, Helvetica">
            <h:output_text value="#{carDemoBundle.yourPriceLabel}" />

            </FONT></B>
            <FONT FACE="Arial, Helvetica">
            <h:output_text   
                valueRef="CarServer.carCurrentPrice" />

            <BR> <BR>
            <h:command_button value="#{carDemoBundle.buy}" 
                 actionRef="CarServer.carBuyAction" >
                <f:action_listener type="cardemo.CarBuyListener"/>
            </h:command_button>
            <BR> <BR>
            </TD></FONT></TD>
        </TR>
        <TR>
            <TD WIDTH="100%" BGCOLOR="white"><B><FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text value="carDemoBundle.OptionsPackages" />
            </FONT></B></TD>
        </TR>
        <TR>
            <TD WIDTH="100%" BGCOLOR="white"><B><FONT SIZE="3" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text value="#{carDemoBundle.Package}" />
            </FONT></B>
            <BR>
            <h:command_button id="custom" action="custom"
                styleClass="package-selected"
                value="#{carDemoBundle.Custom}">
                <f:action_listener type="cardemo.CarPackageListener" />
            </h:command_button>
            <h:command_button id="standard" action="standard" 
                value="#{carDemoBundle.Standard}">
                <f:action_listener type="cardemo.CarPackageListener" />
            </h:command_button>
            <h:command_button id="performance" action="performance"
                value="#{carDemoBundle.Performance}">
                <f:action_listener type="cardemo.CarPackageListener" />
            </h:command_button>
            <h:command_button id="deluxe" action="deluxe"
                value="#{carDemoBundle.Deluxe}">
                <f:action_listener type="cardemo.CarPackageListener" />
            </h:command_button>
            <BR> <BR>
            </TD>
        </TR>
        <TR>
            <TD WIDTH="100%"><BLOCKQUOTE>
            <TABLE>
            <TR>
                <TD><P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text value="#{carDemoBundle.Engine}" />
                </FONT></B></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_menu  id="currentEngine"
                     valueRef="CarServer.currentEngineOption">
                     <f:valuechange_listener type="cardemo.PackageValueChanged" />
                    <f:selectitems  
                        valueRef="CarServer.engineOption"/>
                </h:selectone_menu>    
                </FONT></P></BLOCKQUOTE>
                </TD>
                <TD><P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                 <h:output_text value="#{carDemoBundle.Brakes}"  />
                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_radio  id="currentBrake"
                    valueRef="CarServer.currentBrakeOption">
                    <f:valuechange_listener type="cardemo.PackageValueChanged" />
                    <f:selectitems  
                        valueRef="CarServer.brakeOption"/>
                </h:selectone_radio>
                </FONT></P></BLOCKQUOTE>
                </TD>
            </TR>
            <TR>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text value="#{carDemoBundle.Suspension}" />

                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_menu  id="currentSuspension"
                    valueRef="CarServer.currentSuspensionOption">
                     <f:valuechange_listener type="cardemo.PackageValueChanged" />
                    <f:selectitems  
                        valueRef="CarServer.suspensionOption"/>
                </h:selectone_menu>
                </FONT></P> </BLOCKQUOTE>
                </TD>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text value="#{carDemoBundle.Speakers}" />

                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_radio  id="currentSpeaker"
                    valueRef="CarServer.currentSpeakerOption" >
                    <f:valuechange_listener type="cardemo.PackageValueChanged" />
                   <f:selectitems  
                        valueRef="CarServer.speakerOption"/>
                </h:selectone_radio>
                </FONT></P> </BLOCKQUOTE>
                </TD>
            </TR>
            <TR>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text value="#{carDemoBundle.Audio}" />
                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE> <P><FONT FACE="Arial, Helvetica">
                <h:selectone_radio  id="currentAudio"
                    valueRef="CarServer.currentAudioOption">
                     <f:valuechange_listener type="cardemo.PackageValueChanged" />
                    <f:selectitems  
                        valueRef="CarServer.audioOption"/>
                </h:selectone_radio>
                </FONT></P> 
                </BLOCKQUOTE>
                </TD>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text value="#{carDemoBundle.Transmission}" />

                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE> <P><FONT FACE="Arial, Helvetica">
                <h:selectone_menu  id="currentTransmission"
                    valueRef="CarServer.currentTransmissionOption">
                     <f:valuechange_listener type="cardemo.PackageValueChanged" />
                    <f:selectitems  
                        valueRef="CarServer.transmissionOption"/>
                </h:selectone_menu>
                </FONT></P> </BLOCKQUOTE>
                </TD>
            </TR>
            <TR>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text value="#{carDemoBundle.OtherOptions}" />

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
                <h:selectboolean_checkbox id="sunroof" title="Sunroof" 
                    alt="Sunroof" valueRef="CarServer.currentPackage.sunRoofSelected">
                     <f:valuechange_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>    
                </TD>																		
                <TD>
                <h:output_text value="#{carDemoBundle.sunroofLabel}" /> 
                </TD> 
                <TD>
                <h:selectboolean_checkbox id="cruisecontrol" 
                    title="Cruise Control"  
                    valueRef="CarServer.currentPackage.cruiseControlSelected" >
                     <f:valuechange_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>

                </TD>
                <TD>
                <h:output_text value="#{carDemoBundle.cruiseLabel}" /> 
                </TD>
                <TD>
                <h:selectboolean_checkbox id="keylessentry" 
                    title="Keyless Entry"  alt="Keyless Entry"
                    valueRef="CarServer.currentPackage.keylessEntrySelected" >
                     <f:valuechange_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>

                </TD>
                <TD>
                <h:output_text value="#{carDemoBundle.keylessLabel}" /> 
                </TD>
            </TR>
            <TR>
                <TD> 
                <h:selectboolean_checkbox id="securitySystem"
                    title="Security System"  alt="Security System"
                    valueRef="CarServer.currentPackage.securitySystemSelected" >
                     <f:valuechange_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>

                </TD>
                <TD>
                <h:output_text value="{carDemoBundle.securityLabel}" />  
                </TD>
                <TD>
                <h:selectboolean_checkbox id="skirack" title="Ski Rack"  
                    alt="Ski Rack" valueRef="CarServer.currentPackage.skiRackSelected"
                     >
                     <f:valuechange_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>
                </TD>
                <TD>
                <h:output_text value="#{carDemoBundle.skiRackLabel}" /> 
                </TD>
                <TD>
                <h:selectboolean_checkbox id="towPackage" title="Tow Package"  
                    alt="Tow Package" 
                    valueRef="CarServer.currentPackage.towPackageSelected" >
                     <f:valuechange_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>

                </TD>
                <TD>
                <h:output_text value="#{carDemoBundle.towPkgLabel}" /> 
                </TD>
            </TR>
            <TR>
                <TD>
                <h:selectboolean_checkbox id="gps" title="GPS" alt="GPS"
                    valueRef="CarServer.currentPackage.gpsSelected"  >
                     <f:valuechange_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>
                </FONT> 
                </TD>
                <TD>
                <h:output_text value="#{carDemoBundle.gpsLabel}" /> 
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
                <h:command_button id="recalculate" 
                    value="#{carDemoBundle.recalculate}">
                    <f:action_listener type="cardemo.CarBuyListener" />
                </h:command_button>
                <h:command_button value="#{carDemoBundle.buy}" 
                actionRef="CarServer.carBuyAction">
                    <f:action_listener type="cardemo.CarBuyListener"/>
                </h:command_button>

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
</f:view>
</BODY>

</HTML>
