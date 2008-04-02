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
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

     <fmt:setBundle
	    basename="cardemo.Resources"
	    scope="session" var="carDemoBundle"/>

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
                <BR>
            <B> <FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text  
                modelReference="CurrentOptionServer.carTitle" 
                 />
            </FONT></B>
            <FONT FACE="Arial, Helvetica"><BR> <BR>
            <h:output_text  
                modelReference="CurrentOptionServer.carDesc" />
            <BR> <BR>
            </FONT><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
            <h:output_text key="basePriceLabel" bundle="carDemoBundle" />

            </FONT></B><FONT FACE="Arial, Helvetica"> 
            <h:output_text   
                modelReference="CurrentOptionServer.carBasePrice" />

            <BR> </FONT><B>
            <FONT COLOR="#93B629" FACE="Arial, Helvetica">
            <h:output_text key="yourPriceLabel" bundle="carDemoBundle" />

            </FONT></B>
            <FONT FACE="Arial, Helvetica">
            <h:output_text   
                modelReference="CurrentOptionServer.carCurrentPrice" />

            <BR> <BR>
            <h:command_button commandName="buy" key="buy" bundle="carDemoBundle">
                <f:action_listener type="cardemo.CarActionListener"/>
            </h:command_button>
            <BR> <BR>
            </TD></FONT></TD>
        </TR>
        <TR>
            <TD WIDTH="100%" BGCOLOR="white"><B><FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text key="OptionsPackages" bundle="carDemoBundle" />
            </FONT></B></TD>
        </TR>
        <TR>
            <TD WIDTH="100%" BGCOLOR="white"><B><FONT SIZE="3" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text key="Package" bundle="carDemoBundle" />
            </FONT></B>
            <BR>
            <h:command_button id="custom" commandName="custom"
                commandClass="package-selected"
                key="Custom" bundle="carDemoBundle">
                <f:action_listener type="cardemo.CarActionListener" />
            </h:command_button>
            <h:command_button id="standard" commandName="standard"
                key="Standard" bundle="carDemoBundle">
                <f:action_listener type="cardemo.CarActionListener" />
            </h:command_button>
            <h:command_button id="performance" commandName="performance"
                key="Performance" bundle="carDemoBundle">
                <f:action_listener type="cardemo.CarActionListener" />
            </h:command_button>
            <h:command_button id="deluxe" commandName="deluxe"
                key="Deluxe" bundle="carDemoBundle">
                <f:action_listener type="cardemo.CarActionListener" />
            </h:command_button>
            <BR> <BR>
            </TD>
        </TR>
        <TR>
            <TD WIDTH="100%"><BLOCKQUOTE>
            <TABLE>
            <TR>
                <TD><P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text key="Engine" bundle="carDemoBundle" />
                </FONT></B></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_menu  id="currentEngine"
                     modelReference="CurrentOptionServer.currentEngineOption">
                     <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                    <h:selectitems  
                        modelReference="CurrentOptionServer.engineOption"/>
                </h:selectone_menu>    
                </FONT></P></BLOCKQUOTE>
                </TD>
                <TD><P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                 <h:output_text key="Brakes" bundle="carDemoBundle"  />
                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_radio  id="currentBrake"
                    modelReference="CurrentOptionServer.currentBrakeOption">
                    <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                    <h:selectitems  
                        modelReference="CurrentOptionServer.brakeOption"/>
                </h:selectone_radio>
                </FONT></P></BLOCKQUOTE>
                </TD>
            </TR>
            <TR>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text key="Suspension" bundle="carDemoBundle" />

                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_menu  id="currentSuspension"
                    modelReference="CurrentOptionServer.currentSuspensionOption">
                     <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                    <h:selectitems  
                        modelReference="CurrentOptionServer.suspensionOption"/>
                </h:selectone_menu>
                </FONT></P> </BLOCKQUOTE>
                </TD>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text key="Speakers" bundle="carDemoBundle" />

                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE><P><FONT FACE="Arial, Helvetica">
                <h:selectone_radio  id="currentSpeaker"
                    modelReference="CurrentOptionServer.currentSpeakerOption" >
                    <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                   <h:selectitems  
                        modelReference="CurrentOptionServer.speakerOption"/>
                </h:selectone_radio>
                </FONT></P> </BLOCKQUOTE>
                </TD>
            </TR>
            <TR>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text key="Audio" bundle="carDemoBundle" />
                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE> <P><FONT FACE="Arial, Helvetica">
                <h:selectone_radio  id="currentAudio"
                    modelReference="CurrentOptionServer.currentAudioOption">
                     <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                    <h:selectitems  
                        modelReference="CurrentOptionServer.audioOption"/>
                </h:selectone_radio>
                </FONT></P> 
                </BLOCKQUOTE>
                </TD>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text key="Transmission" bundle="carDemoBundle" />

                </FONT></B>
                <FONT FACE="Arial, Helvetica"></FONT></P>
                <BLOCKQUOTE> <P><FONT FACE="Arial, Helvetica">
                <h:selectone_menu  id="currentTransmission"
                    modelReference="CurrentOptionServer.currentTransmissionOption">
                     <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                    <h:selectitems  
                        modelReference="CurrentOptionServer.transmissionOption"/>
                </h:selectone_menu>
                </FONT></P> </BLOCKQUOTE>
                </TD>
            </TR>
            <TR>
                <TD>
                <P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                <h:output_text key="OtherOptions" bundle="carDemoBundle" />

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
                    alt="Sunroof" modelReference="CurrentOptionServer.sunRoofSelected">
                     <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>    
                </TD>																		
                <TD>
                <h:output_text key="sunroofLabel" bundle="carDemoBundle" /> 
                </TD> 
                <TD>
                <h:selectboolean_checkbox id="cruisecontrol" 
                    title="Cruise Control"  
                    modelReference="CurrentOptionServer.cruiseControlSelected" >
                     <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>

                </TD>
                <TD>
                <h:output_text key="cruiseLabel" bundle="carDemoBundle" /> 
                </TD>
                <TD>
                <h:selectboolean_checkbox id="keylessentry" 
                    title="Keyless Entry"  alt="Keyless Entry"
                    modelReference="CurrentOptionServer.keylessEntrySelected" >
                     <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>

                </TD>
                <TD>
                <h:output_text key="keylessLabel" bundle="carDemoBundle" /> 
                </TD>
            </TR>
            <TR>
                <TD> 
                <h:selectboolean_checkbox id="securitySystem"
                    title="Security System"  alt="Security System"
                    modelReference="CurrentOptionServer.securitySystemSelected" >
                     <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>

                </TD>
                <TD>
                <h:output_text key="securityLabel" bundle="carDemoBundle" />  
                </TD>
                <TD>
                <h:selectboolean_checkbox id="skirack" title="Ski Rack"  
                    alt="Ski Rack" modelReference="CurrentOptionServer.skiRackSelected"
                     >
                     <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>
                </TD>
                <TD>
                <h:output_text key="skiRackLabel" bundle="carDemoBundle" /> 
                </TD>
                <TD>
                <h:selectboolean_checkbox id="towPackage" title="Tow Package"  
                    alt="Tow Package" 
                    modelReference="CurrentOptionServer.towPackageSelected" >
                     <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>

                </TD>
                <TD>
                <h:output_text key="towPkgLabel" bundle="carDemoBundle" /> 
                </TD>
            </TR>
            <TR>
                <TD>
                <h:selectboolean_checkbox id="gps" title="GPS" alt="GPS"
                    modelReference="CurrentOptionServer.gpsSelected"  >
                     <f:valuechanged_listener type="cardemo.PackageValueChanged" />
                 </h:selectboolean_checkbox>
                </FONT> 
                </TD>
                <TD>
                <h:output_text key="gpsLabel" bundle="carDemoBundle" /> 
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
                <h:command_button id="recalculate" key="recalculate" 
                bundle="carDemoBundle" commandName="recalculate">
                    <f:action_listener type="cardemo.CarActionListener" />
                </h:command_button>
                <h:command_button id="buy2" key="buy" bundle="carDemoBundle" 
                commandName="buy">
                    <f:action_listener type="cardemo.CarActionListener"/>
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
</f:use_faces>
</BODY>

</HTML>
