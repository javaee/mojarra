<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<HTML>

<HEAD>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
	<TITLE>CarDemo</TITLE>
</HEAD>

<%@ page extends="com.sun.faces.Page" %>
<%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

<BODY BGCOLOR="white">

<faces:DeclareBean scope="session" id="CarServer" 
    className="cardemo.CarServer"/>
<faces:DeclareBean scope="session" id="CurrentOptionServer" 
    className="cardemo.CurrentOptionServer"/>

<faces:NavigationMap id="navMap" scope="session" >
    <faces:outcome commandName="buyCar1" outcome="success" targetAction="forward"
        targetPath = "more.jsp" />
    <faces:outcome commandName="buyCar1" outcome="failure" targetAction="forward"
        targetPath = "error.jsp" />

    <faces:outcome commandName="buyCar2" outcome="success" targetAction="forward"
        targetPath = "more.jsp" />
    <faces:outcome commandName="buyCar2" outcome="failure" targetAction="forward"
        targetPath = "error.jsp" />

    <faces:outcome commandName="buyCar3" outcome="success" targetAction="forward"
        targetPath = "more.jsp" />
    <faces:outcome commandName="buyCar3" outcome="failure" targetAction="forward"
        targetPath = "error.jsp" />

    <faces:outcome commandName="buyCar4" outcome="success" targetAction="forward"
        targetPath = "more.jsp" />
    <faces:outcome commandName="buyCar4" outcome="failure" targetAction="forward"
        targetPath = "error.jsp" />

</faces:NavigationMap>

<faces:Form id="carStoreFrontForm" modelReference="CurrentOptionServer" navigationMapId="navMap" >
    <faces:Listener id="carListener" scope="session" className="cardemo.CarListener" />
  
<P>
<TABLE BORDER="0" WIDTH="660" BGCOLOR="#E2F7DD">
	<TR>
		<TD WIDTH="828">
			<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
				<TR>
					<TD WIDTH="820"><IMG SRC="pictures/cardemo.jpg" WIDTH="660" HEIGHT="60" ALIGN="BOTTOM" BORDER="0"><BR>
						
						<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="6" WIDTH="660" BGCOLOR="white">
							<TR>
								<TD WIDTH="50%" VALIGN="TOP"><IMG SRC="pictures/crop_honda_civic_small.gif" ALIGN="LEFT" BORDER="0"><B><FONT
									SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
                                                                        <faces:Output_Text id="car1Title"  modelReference="$CarServer.car1Title" />
                                                                        </FONT></B><FONT FACE="Arial, Helvetica"><BR>
									<BR>
									<faces:Output_Text id="car1Desc"  modelReference="$CarServer.car1Desc" /> <BR>
									</FONT>
                                                                        <faces:Command_Button id="buyCar1" label="More" commandName="buyCar1" commandListener="carListener"/>
                                                                        </TD>
								<TD WIDTH="50%" VALIGN="TOP" BGCOLOR="white"><IMG SRC="pictures/crop_acura_sport_small.gif" ALIGN="LEFT" BORDER="0"><B><FONT SIZE="4" COLOR="#330066"
									FACE="Arial, Helvetica"><faces:Output_Text id="car2Title"  modelReference="$CarServer.car2Title" /></FONT></B><FONT FACE="Arial, Helvetica"><BR>
									<BR>
									<faces:Output_Text id="car2Desc"  modelReference="$CarServer.car2Desc" /><BR>
									<faces:Command_Button id="buyCar2" label="More" commandListener="carListener"/>
                                                                        </TD>
							</TR>
							<TR>
								<TD WIDTH="50%" BGCOLOR="white">&nbsp;</TD>
								<TD WIDTH="50%" BGCOLOR="white">&nbsp;</TD>
							</TR>
							<TR>
								<TD WIDTH="50%" VALIGN="TOP" BGCOLOR="white"><IMG SRC="pictures/crop_lexus_small.gif" ALIGN="LEFT" BORDER="0"><B><FONT SIZE="4" COLOR="#330066"
									FACE="Arial, Helvetica"><faces:Output_Text id="car3Title"  modelReference="$CarServer.car3Title" /></FONT></B><FONT FACE="Arial, Helvetica"><BR>
									<BR>
									<faces:Output_Text id="car3Desc"  modelReference="$CarServer.car3Desc" /><BR>
									<faces:Command_Button id="buyCar3" label="More" commandListener="carListener"/></TD>
								<TD WIDTH="50%" VALIGN="TOP" BGCOLOR="white"><IMG SRC="pictures/crop_ford_explorer_small.gif" ALIGN="LEFT" BORDER="0"><B><FONT SIZE="4" COLOR="#330066"
									FACE="Arial, Helvetica"><faces:Output_Text id="car4Title"  modelReference="$CarServer.car4Title" /></FONT></B><FONT FACE="Arial, Helvetica"><BR>
									<BR>
									<faces:Output_Text id="car4Desc"  modelReference="$CarServer.car4Desc" /> <BR>
									<faces:Command_Button id="buyCar4" label="More" commandListener="carListener"/></TD>
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
