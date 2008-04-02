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
	<TITLE>Welcome to CarDemo</TITLE>
        <link rel="stylesheet" type="text/css"
            href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</HEAD>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<BODY BGCOLOR="white">

       <fmt:setBundle
	    basename="cardemo.Resources"
	    scope="session" var="carDemoBundle"/>

        <jsp:useBean id="CurrentOptionServer" class="cardemo.CurrentOptionServer" scope="session" >
            <jsp:setProperty name="CurrentOptionServer" property="carImage"
                value="current.gif"/>
        </jsp:useBean>

<f:use_faces>  

<h:form  formName="carStoreForm">
  
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
            <table border="0" align="left">
            <tr>
            <td>
            <h:graphic_image  graphicClass="alignLeft" url="/150x126_Jalopy.jpg" /></tr>
                          </table>
                          <p><br>
            <B><FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text key="car1Title" bundle="carDemoBundle" /> 

            </FONT></B><FONT FACE="Arial, Helvetica"><BR><BR>
            <h:output_text  key="car1Desc" bundle="carDemoBundle"  /> 
            <BR></FONT>
            <h:command_button label="More" key="moreButton" action="success"
                 bundle="carDemoBundle" commandName="more1">
                <f:action_listener type="cardemo.CarActionListener"/>
            </h:command_button>
            </TD>
            <TD WIDTH="50%" VALIGN="TOP" BGCOLOR="white">
            <table border="0" align="left">
            <tr>
            <td>
            <h:graphic_image  url="/150x126_Roadster.jpg" />
            </tr>
                          </table>
                          <p><br>
            <B><FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text  key="car2Title" bundle="carDemoBundle" />

            </FONT></B>
            <FONT FACE="Arial, Helvetica"><BR> <BR>
            <h:output_text  key="car2Desc" bundle="carDemoBundle" />

            <BR>
            <h:command_button  label="More" key="moreButton" action="success"
                 bundle="carDemoBundle" commandName="more2">
                 <f:action_listener type="cardemo.CarActionListener"/>
            </h:command_button>
            </TD>
        </TR>
        <TR>
            <TD WIDTH="50%" BGCOLOR="white">&nbsp;</TD>
            <TD WIDTH="50%" BGCOLOR="white">&nbsp;</TD>
        </TR>
        <TR>
            <TD WIDTH="50%" VALIGN="TOP" BGCOLOR="white">
            <table border="0" align="left">
            <tr>
            <td>
            <h:graphic_image  url="/150x126_Luxury.jpg" />
            </tr>
                          </table>
                          <p><br>
            <B> <FONT SIZE="4" COLOR="#330066"FACE="Arial, Helvetica">
            <h:output_text  key="car3Title" bundle="carDemoBundle"  />

            </FONT></B>
            <FONT FACE="Arial, Helvetica"><BR> <BR>
            <h:output_text  key="car3Desc" bundle="carDemoBundle"  />

            <BR>
            <h:command_button label="More" key="moreButton" action="success"
                 bundle="carDemoBundle" commandName="more3">
                <f:action_listener type="cardemo.CarActionListener"/>
            </h:command_button>
            </TD>
            <TD WIDTH="50%" VALIGN="TOP" BGCOLOR="white">
            <table border="0" align="left">
            <tr>
            <td>
            <h:graphic_image  url="/150x126_SUV.jpg" />
            </tr>
                          </table>
                          <p><br>
            <B> <FONT SIZE="4" COLOR="#330066" FACE="Arial, Helvetica">
            <h:output_text  key="car4Title" bundle="carDemoBundle"  />

            </FONT></B>
            <FONT FACE="Arial, Helvetica"><BR> <BR>
            <h:output_text key="car4Desc" bundle="carDemoBundle"  /> 

            <BR>
            <h:command_button  key="moreButton" action="success" 
                 bundle="carDemoBundle" commandName="more4">
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
</h:form>
</f:use_faces>

</BODY>

</HTML>
