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

<%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core/" prefix="f" %>

<BODY BGCOLOR="white">

<f:usefaces>
<h:form id="carStoreForm" formName="carStoreForm" modelReference="CurrentOptionServer" >

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
								<h:graphic_image id="cardemo" url="/cardemo.jpg" /></TD>
							</TR>
							<TR>
								<TD WIDTH="100%">
								<h:graphic_image id="current_car" modelReference="CurrentOptionServer.carImage" /><B>
								<FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
									<h:output_text id="carDetails"  
											modelReference="CurrentOptionServer.carTitle" 
											key="carDetailsLabel" />
                                                                </FONT></B>
									<FONT FACE="Arial, Helvetica"><BR>
                                                                        <BR>
                                                                        <h:output_text 	id="currentCarDesc"  
											modelReference="CurrentOptionServer.carDesc" 
											key="curCarDescLabel" /> <BR>
                                                                        <BR>
                                                                        </FONT><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                                                                        <h:output_text 	id="basePrice"  
											value="Base Price"
											key="basePriceLabel"/>
                                                                        </FONT></B><FONT FACE="Arial, Helvetica"> 
                                                                        <h:output_text 	id="currentCarBasePrice"  
											modelReference="CurrentOptionServer.carBasePrice"
											key="currentCarBasePriceLabel"/>
									<BR>
									</FONT><B>
									<FONT COLOR="#93B629" FACE="Arial, Helvetica">
									<h:output_text 	id="yourPrice"  
											key="yourPriceLabel"
											value="Your Price" />
									</FONT></B>
									<FONT FACE="Arial, Helvetica">
									<h:output_text 	id="currentCarPrice"  
											modelReference="CurrentOptionServer.carCurrentPrice"
											key="currentCarPriceLabel"/><BR>
                                                                        <BR>
                                                                        <h:command_button id="buy1" commandName="buy" label="Buy" />
                                                                        </TD></FONT></TD>
							</TR>
							<TR>
								<TD WIDTH="100%" BGCOLOR="white"><B><FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
                                                                <h:output_text id="opsNPax"  value="Options and Packages" /></FONT></B></TD>
							</TR>
							<TR>
								<TD WIDTH="100%">
									<BLOCKQUOTE>
										<TABLE><TR><TD>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                                                                                <h:output_text id="engine" key="Engine" value="Engine" /></FONT></B></P>
										<BLOCKQUOTE>
                                                                                <P><FONT FACE="Arial, Helvetica">
                                                                                <h:selectone_menu id="engineOption" modelReference="CurrentOptionServer.currentEngineOption">
                                                                                    <h:selectitems id="engineOptionsList" modelReference="CurrentOptionServer.engineOption"/>
                                                                                </h:selectone_menu>    
                                                                                </FONT></P>   
										</BLOCKQUOTE></TD>
										<TD>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
										<h:output_text id="brakes"  value="Brakes"key="Brakes"  />
										</FONT></B>
										<FONT FACE="Arial, Helvetica"></FONT></P>
										<BLOCKQUOTE>
											<P><FONT FACE="Arial, Helvetica">
                                                                                <h:selectone_radio id="brakeOption" modelReference="CurrentOptionServer.currentBrakeOption">
                                                                                    <h:selectitems id="brakeOptionsList" modelReference="CurrentOptionServer.brakeOption"/>
                                                                                </h:selectone_radio>
                                                                                </FONT></P> 
										</BLOCKQUOTE>
										</TD></TR>
										<TR>
										<TD>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
										<h:output_text id="suspension" key="Suspension" value="Suspension" /></FONT></B>
										<FONT FACE="Arial, Helvetica"></FONT></P>
										<BLOCKQUOTE>
											<P><FONT FACE="Arial, Helvetica">
                                                                                <h:selectone_menu id="suspensionOption" modelReference="CurrentOptionServer.currentSuspensionOption">
                                                                                    <h:selectitems id="suspensionOptionsList" modelReference="CurrentOptionServer.suspensionOption"/>
                                                                                </h:selectone_menu>
                                                                                </FONT></P> 
										</BLOCKQUOTE>
										</TD>
										<TD>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
										<h:output_text id="speakers" key="Speakers" value="Speakers"  /></FONT></B>
										<FONT FACE="Arial, Helvetica"></FONT></P>
										<BLOCKQUOTE>
											<P><FONT FACE="Arial, Helvetica">
                                                                                <h:selectone_radio id="speakerOption" modelReference="CurrentOptionServer.currentSpeakerOption" >
                                                                                    <h:selectitems id="speakerOptionsList" modelReference="CurrentOptionServer.speakerOption"/>
                                                                                </h:selectone_radio>
                                                                                </FONT></P> 
										</BLOCKQUOTE>
										</TD></TR>
										<TR><TD>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
										<h:output_text id="audio" key="Audio" value="Audio"   /></FONT></B>
										<FONT FACE="Arial, Helvetica"></FONT></P>
										<BLOCKQUOTE>
											<P><FONT FACE="Arial, Helvetica">
                                                                                <h:selectone_radio id="audioOption" modelReference="CurrentOptionServer.currentAudioOption">
                                                                                    <h:selectitems id="audioOptionsList" modelReference="CurrentOptionServer.audioOption"/>
                                                                                </h:selectone_radio>
                                                                                </FONT></P> 
										</BLOCKQUOTE>
										</TD>
										<TD>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
										<h:output_text id="transmission" key="Transmission" value="Transmission" /></FONT></B>
										<FONT FACE="Arial, Helvetica"></FONT></P>
										<BLOCKQUOTE>
											<P><FONT FACE="Arial, Helvetica">
                                                                                <h:selectone_menu id="transmissionOption" modelReference="CurrentOptionServer.currentTransmissionOption">
                                                                                    <h:selectitems id="transmissionOptionsList" modelReference="CurrentOptionServer.transmissionOption"/>
                                                                                </h:selectone_menu>
                                                                                </FONT></P> 
										</BLOCKQUOTE>
										</TD></TR>
										<TR><TD>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
										<h:output_text id="other" key="OtherOptions" value="Other Options"   /></FONT></B>
										<FONT FACE="Arial, Helvetica"></FONT></P>
										<BLOCKQUOTE>
										
                                                                                </FONT></P> 
										</BLOCKQUOTE>
										</TD></TR></TABLE>

                                                                                <TABLE width="100%">
										<P><FONT FACE="Arial, Helvetica">
                                                                                <TR><TD>                                                                               										
										<h:selectboolean_checkbox   id="sunRoof"
                                                								title="Sunroof" 
                                                                                                                alt="Sunroof"
                                                                                                                modelReference="CurrentOptionServer.sunRoof"
                                                								key="sunroofLabel" 
                                                								/>
										</TD>																		
										<TD><h:output_label id="srLabel" for="sunRoof" >
										Sun Roof</h:output_label> </TD>
										<TD><h:selectboolean_checkbox   id="cruiseControl"
                                                								title="Cruise Control"  
                                                                                                                modelReference="CurrentOptionServer.cruiseControl"
                                                								key="cruiseLabel" 
                                                								/>
										</TD>
						       				<TD><h:output_label id="ccLabel" for="cruiseControl" >
										Cruise Control</h:output_label> </TD>
										<TD><h:selectboolean_checkbox   id="keylessEntry"
                                                								title="Keyless Entry"  
                                                                                                                alt="Keyless Entry"
                                                                                                                modelReference="CurrentOptionServer.keylessEntry"
                                                								key="keylessLabel" 
                                                								/>
										</TD>
										<TD>
										<h:output_label id="keLabel" for="keylessEntry" >
										Keyless Entry</h:output_label> </TD>
										</TR>
										<TR><TD>									
									       	<h:selectboolean_checkbox   id="securitySystem"
                                                								title="Security System"  
                                                                                                                alt="Security System"
                                                                                                                modelReference="CurrentOptionServer.securitySystem"
                                                								key="securityLabel" 
                                                								/>
										</TD><TD>
										<h:output_label id="ssLabel" for="securitySystem" >
										Security System</h:output_label> </TD>
										<TD>
							   			<h:selectboolean_checkbox   id="skiRack"
                                                								title="Ski Rack"  
                                                                                                                alt="Ski Rack"
                                                                                                                modelReference="CurrentOptionServer.skiRack"
                                                								key="skiRackLabel" 
                                                								/>
    				       						</TD>
										<TD><h:output_label id="srLabel" for="skiRack" >
										Ski Rack</h:output_label> </TD>
										<TD><h:selectboolean_checkbox   id="towPackage"
                                                								title="Tow Package"  
                                                                                                                alt="Tow Package"
                                                                                                                modelReference="CurrentOptionServer.towPackage"
                                                								key="towPkgLabel" 
                                                								/>
								       		</TD>
										<TD>
										<h:output_label id="tpLabel" for="towPackage" >
										Tow Package</h:output_label> </TD>
										<TD>
										<h:selectboolean_checkbox   id="gps"
                                                								title="GPS"  
                                                                                                                alt="GPS"
                                                                                                                modelReference="CurrentOptionServer.gps"
                                                								key="gpsLabel" 
                                                								/>
                                                                                                                </FONT>
										</TD>
										<TD>
										<h:output_label id="gpsLabel" for="gps" >
										GPS</h:output_label> </TD>
										</TR></TABLE>

										
									</BLOCKQUOTE>
									<TABLE ALIGN=RIGHT>
									<TR><TD>
									

									</TD><TD>
									<P>
									<h:command_button 	id="buy2"
												label="Buy" 
									 			key="buy"
                                								commandName="buy"/>


									</TD></TR></TABLE>
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
</f:usefaces>
</BODY>

</HTML>
