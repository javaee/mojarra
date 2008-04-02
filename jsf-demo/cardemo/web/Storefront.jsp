<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>

<HEAD>
	<META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
	<TITLE>Welcome to CarDemo</TITLE>
</HEAD>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<BODY BGCOLOR="white">

       <fmt:setBundle
	    basename="carDemo.Resources"
	    scope="session" var="carDemoBundle"/>
	<jsp:useBean id="CarServer" class="cardemo.CarServer" scope="session" />
	<jsp:useBean id="CurrentOptionServer" class="cardemo.CurrentOptionServer" scope="session" >
            <jsp:setProperty name="CurrentOptionServer" property="carImage" 
                value="current.gif"/>
	</jsp:useBean>

<f:use_faces>  

<h:form  formName="carStoreForm" modelReference="CurrentOptionServer" >
  
<P>
<TABLE BORDER="0" WIDTH="660" BGCOLOR="#4F4F72">
<TR>
    <TD WIDTH="828">
    <TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
    <TR>
        <TD WIDTH="820">
        <h:graphic_image  url="/cardemo.jpg" />
        <BR>
        <TABLE BORDER="0" CELLPADDING="0" CELLSPACING="6" WIDTH="660" BGCOLOR="white">
        <TR>
            <TD WIDTH="50%" VALIGN="TOP">
            <h:graphic_image  url="/150x126_Jalopy.jpg" />
            <B><FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text  modelReference="CarServer.car1Title"
                key="car1title" /> 
            </FONT></B><FONT FACE="Arial, Helvetica"><BR><BR>
            <h:output_text  modelReference="CarServer.car1Desc"	                key="car1Desc" /> 
            <BR></FONT>
            <h:command_button  label="More" key="moreButton"
                commandName="more1"/>
            </TD>
            <TD WIDTH="50%" VALIGN="TOP" BGCOLOR="white">
            <h:graphic_image  url="/150x126_Roadster.jpg" />
            <B><FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text  modelReference="CarServer.car2Title" 
                key="car2Title"/>
            </FONT></B>
            <FONT FACE="Arial, Helvetica"><BR> <BR>
            <h:output_text  modelReference="CarServer.car2Desc" 
                key="car2Desc"/>
            <BR>
            <h:command_button  label="More" key="moreButton"
                commandName="more2"/>
            </TD>
        </TR>
        <TR>
            <TD WIDTH="50%" BGCOLOR="white">&nbsp;</TD>
            <TD WIDTH="50%" BGCOLOR="white">&nbsp;</TD>
        </TR>
        <TR>
            <TD WIDTH="50%" VALIGN="TOP" BGCOLOR="white">
            <h:graphic_image  url="/150x126_Luxury.jpg" />
            <B> <FONT SIZE="4" COLOR="#330066"FACE="Arial, Helvetica">
            <h:output_text  modelReference="CarServer.car3Title" 
                key="car3Title" />
            </FONT></B>
            <FONT FACE="Arial, Helvetica"><BR> <BR>
            <h:output_text  modelReference="CarServer.car3Desc" 
                key="car3Desc" />
            <BR>
            <h:command_button  label="More" key="moreButton"
                commandName="more3"/>
            </TD>
            <TD WIDTH="50%" VALIGN="TOP" BGCOLOR="white">
            <h:graphic_image  url="/150x126_SUV.jpg" />
            <B> <FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text  modelReference="CarServer.car4Title"
                key="car4Title" />
            </FONT></B>
            <FONT FACE="Arial, Helvetica"><BR> <BR>
            <h:output_text  modelReference="CarServer.car4Desc" 
                key="car4Desc" /> 
            <BR>
            <h:command_button  label="More" key="moreButton"
                commandName="more4"/>
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
