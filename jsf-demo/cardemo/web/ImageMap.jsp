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
   <link rel="stylesheet" type="text/css"
        href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</head>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>

     <fmt:setBundle
	    basename="cardemo.Resources"
	    scope="session" var="carDemoBundle"/>

<f:use_faces>
<h:form  formName="imageMapForm" >
    <table border="0" width="660" bgcolor="#4f4f72">
    <tbody>
      <tr> 
        <td width="828"> <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tbody>
              <tr> 
                <td width="820"> <h:graphic_image  url="/cardemo.jpg" /> 
    <table border="0" cellpadding="0" cellspacing="6" width="660" bgcolor="white">
                    <tbody>
                      <tr> 
                        <td width="50%" valign="top"><P align="center">
                            <h:output_text id="chooseLocale" key="chooseLocale" bundle="carDemoBundle" /> </p>
    </P>
                          <TABLE>
                            <TBODY>
                              <TR> 
                                <TD>
          <h:graphic_image id="mapImage" url="/world.jpg" usemap="#worldMap" />
                <d:map id="worldMap" currentArea="NAmericas" >
                    <f:action_listener type="cardemo.ImageMapEventHandler"/> 
                        <d:area id="NAmericas" valueRef="NA" onmouseover="/world_namer.jpg" onmouseout="/world.jpg" />
                        <d:area id="SAmericas" valueRef="SA" onmouseover="/world_samer.jpg" onmouseout="/world.jpg" />
                        <d:area id="Germany" valueRef="gerA" onmouseover="/world_germany.jpg" onmouseout="/world.jpg" />
                        <d:area id="France" valueRef="fraA" onmouseover="/world_france.jpg" onmouseout="/world.jpg" />
                </d:map>
     </TD>
                              </TR>
                            </TBODY>
                          </TABLE>

</h:form>
</f:use_faces>
</html>
