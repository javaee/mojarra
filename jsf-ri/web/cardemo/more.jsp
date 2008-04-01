<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<HTML>

<HEAD>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
	<TITLE>CarDemo</TITLE>
</HEAD>

<%@ page extends="com.sun.faces.Page" %>
<%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

<BODY BGCOLOR="white">

<faces:NavigationMap id="buyNavMap" scope="session" >
    <faces:outcome commandName="checkOut" outcome="success" targetAction="forward"
        targetPath = "buy.jsp" />
    <faces:outcome commandName="checkOut" outcome="failure" targetAction="forward"
        targetPath = "error.jsp" />
</faces:NavigationMap>

<faces:Form id="carBuyForm" modelReference="CurrentOptionServer" navigationMapId="buyNavMap" >
    <faces:Listener id="optionListener" scope="session" className="cardemo.OptionListener" />

<P>
<TABLE BORDER="0" WIDTH="660" BGCOLOR="#E2F7DD">
	<TR>
		<TD WIDTH="828">
			<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
				<TR>
					<TD WIDTH="820">
						<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BGCOLOR="white">
							<TR>
								<TD WIDTH="100%" VALIGN="TOP"><IMG SRC="cardemo.jpg" WIDTH="660" HEIGHT="60" ALIGN="BOTTOM" BORDER="0"></TD>
							</TR>
							<TR>
								<TD WIDTH="100%"><IMG SRC="pictures/current.gif" ALIGN="LEFT" BORDER="0"><B><FONT SIZE="4" COLOR="#330066"
									FACE="Arial, Helvetica"><faces:Output_Text id="carDetails"  modelReference="$CurrentOptionServer.carTitle" /></FONT></B><FONT FACE="Arial, Helvetica"><BR>
                                                                        <BR>
                                                                        <faces:Output_Text id="currentCarDesc"  modelReference="$CurrentOptionServer.carDesc" /><BR>
                                                                        <BR>
                                                                        </FONT><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                                                                        <faces:Output_Text id="basePrice"  value="Base Price: " />
                                                                        </FONT></B><FONT FACE="Arial, Helvetica"> 
                                                                        <faces:Output_Text id="currentCarBasePrice"  modelReference="$CurrentOptionServer.basePrice" />
									<BR>
									</FONT><B><FONT COLOR="#93B629" FACE="Arial, Helvetica"><faces:Output_Text id="yourPrice"  value="Price Including your Options: " /></FONT></B><FONT FACE="Arial, Helvetica"><faces:Output_Text id="currentCarPrice"  modelReference="$CurrentOptionServer.currentPrice" /><BR>
                                                                        <BR>
                                                                        <faces:Command_Button id="checkOut" label="Buy" commandListener="optionListener"/>
                                                                        </TD></FONT></TD>
							</TR>
							<TR>
								<TD WIDTH="100%" BGCOLOR="white"><B><FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica"><faces:Output_Text id="opsNPax"  value="Options and Packages" /></FONT></B></TD>
							</TR>
							<TR>
								<TD WIDTH="100%">
									<BLOCKQUOTE>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica">
                                                                                <faces:Output_Text id="engine"  value="Engine" /></FONT></B></P>
										<BLOCKQUOTE>
                                                                                <P><FONT FACE="Arial, Helvetica">
                                                                                <faces:SelectOne_OptionList id="engineOption" modelReference="$CurrentOptionServer.engineOption" 
                                                                                    selectedModelReference="$CurrentOptionServer.currentEngineOption" valueChangeListener="optionListener" >
                                                                                    <faces:SelectOne_Option value="4 cylinder" selected="true" label="4 cylinder" />
                                                                                    <faces:SelectOne_Option value="V6" label="V6" />
                                                                                    <faces:SelectOne_Option value="V8" label="V8" />
                                                                                </faces:SelectOne_OptionList>    
                                                                                </FONT></P>   
										</BLOCKQUOTE>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica"><faces:Output_Text id="brakes"  value="Brakes" /></FONT></B><FONT FACE="Arial, Helvetica"></FONT></P>
										<BLOCKQUOTE>
											<P><FONT FACE="Arial, Helvetica">
                                                                                <faces:SelectOne_OptionList id="brakeOption" modelReference="$CurrentOptionServer.brakeOption" 
                                                                                    selectedModelReference="$CurrentOptionServer.currentBrakeOption" valueChangeListener="optionListener" >
                                                                                    <faces:SelectOne_Option value="disk" selected="true" label="disk" />
                                                                                    <faces:SelectOne_Option value="drum" label="drum" />
                                                                                </faces:SelectOne_OptionList>
                                                                                </FONT></P> 
										</BLOCKQUOTE>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica"><faces:Output_Text id="suspension"  value="Suspension" /></FONT></B><FONT FACE="Arial, Helvetica"></FONT></P>
										<BLOCKQUOTE>
											<P><FONT FACE="Arial, Helvetica">
                                                                                <faces:SelectOne_OptionList id="suspensionOption" modelReference="$CurrentOptionServer.suspensionOption" 
                                                                                    selectedModelReference="$CurrentOptionServer.currentSuspensionOption" valueChangeListener="optionListener" >
                                                                                    <faces:SelectOne_Option value="regular" selected="true" label="regular" />
                                                                                    <faces:SelectOne_Option value="performance" label="performance" />
                                                                                </faces:SelectOne_OptionList>
                                                                                </FONT></P> 
										</BLOCKQUOTE>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica"><faces:Output_Text id="speakers"  value="Speakers" /></FONT></B><FONT FACE="Arial, Helvetica"></FONT></P>
										<BLOCKQUOTE>
											<P><FONT FACE="Arial, Helvetica">
                                                                                <faces:SelectOne_OptionList id="speakerOption" modelReference="$CurrentOptionServer.speakerOption" 
                                                                                    selectedModelReference="$CurrentOptionServer.currentSpeakerOption" valueChangeListener="optionListener" >
                                                                                    <faces:SelectOne_Option value="4 speakers" selected="true" label="4 speakers" />
                                                                                    <faces:SelectOne_Option value="6 speakers" label="6 speakers" />
                                                                                </faces:SelectOne_OptionList>
                                                                                </FONT></P> 
										</BLOCKQUOTE>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica"><faces:Output_Text id="audio"  value="Audio" /></FONT></B><FONT FACE="Arial, Helvetica"></FONT></P>
										<BLOCKQUOTE>
											<P><FONT FACE="Arial, Helvetica">
                                                                                <faces:SelectOne_OptionList id="audioOption" modelReference="$CurrentOptionServer.audioOption" 
                                                                                    selectedModelReference="$CurrentOptionServer.currentAudioOption" valueChangeListener="optionListener" >
                                                                                    <faces:SelectOne_Option value="standard" selected="true" label="standard" />
                                                                                    <faces:SelectOne_Option value="premium" label="premium" />
                                                                                </faces:SelectOne_OptionList>
                                                                                </FONT></P> 
										</BLOCKQUOTE>
										<P><B><FONT COLOR="#93B629" FACE="Arial, Helvetica"><faces:Output_Text id="transmission"  value="Transmission" /></FONT></B><FONT FACE="Arial, Helvetica"></FONT></P>
										<BLOCKQUOTE>
											<P><FONT FACE="Arial, Helvetica">
                                                                                <faces:SelectOne_OptionList id="transmissionOption" modelReference="$CurrentOptionServer.transmissionOption" 
                                                                                    selectedModelReference="$CurrentOptionServer.currentTransmissionOption" valueChangeListener="optionListener" >
                                                                                    <faces:SelectOne_Option value="auto" selected="true" label="auto" />
                                                                                    <faces:SelectOne_Option value="manual" label="manual" />
                                                                                </faces:SelectOne_OptionList>
                                                                                </FONT></P> 
										</BLOCKQUOTE>
										
									</BLOCKQUOTE>
									<P><faces:Command_Button id="checkOut" label="Buy" commandListener="optionListener"/>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</faces:Form>

</BODY>

</HTML>
