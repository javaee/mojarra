<?xml version="1.0"?>

<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN"
         "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">

<!--
 Copyright 2005 Sun Microsystems, Inc. All Rights Reserved.
                                                                                                               
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

<%@ page contentType="image/svg+xml"%>
<%@ taglib uri="http://java.sun.com/jsf/svg" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
                                                                                       
<svg xmlns="http://www.w3.org/2000/svg"
     xmlns:xlink="http://www.w3.org/1999/xlink">
    <style type="text/css">
      rect:hover {fill-opacity:0.3;}
    </style>

    <script xlink:href="../src/script/http-svg.es"/>
    <script xlink:href="../src/script/lifecycle.es"/>
    <f:view renderKitId="SVG" >  
        <g:form id="form">
           <g:outputText x="100" y="50" textAnchor="middle" value="JSF Request Processing Lifecycle"
                         style="stroke:black; stroke-width:0.5; fill:none; font-size:32pt;" />

           <!-- Restore View Graphic -->

           <g:line id="toRestore" x1="25" y1="125" x2="100" y2="125" style="stroke:black; fill:none;" />
           <g:commandButton id="restore" width="120" height="50" x="100" y="100" type="submit"
              action="xul-restore" style="stroke:black; fill:#8470ff;" >
              <g:outputText x="130" y="120" textAnchor="middle" value="Restore" />
              <g:outputText x="135" y="140" textAnchor="middle" value="View" />
           </g:commandButton>

           <!-- Apply Request Values Graphic -->

           <g:line id="toApply" x1="220" y1="125" x2="350" y2="125" style="stroke:black; fill:none;" />
           <g:commandButton id="apply" width="120" height="50" x="350" y="100" type="submit"
              action="xul-apply" style="stroke:black; fill:#8470ff;" >
              <g:outputText x="360" y="120" textAnchor="middle" value="Apply Request" />
              <g:outputText x="385" y="140" textAnchor="middle" value="Values" />
           </g:commandButton>

           <!-- Process Validation Graphic -->

           <g:line id="toVal" x1="470" y1="125" x2="600" y2="125" style="stroke: black; fill:none;" />
           <g:commandButton id="validation" width="120" height="50" x="600" y="100" type="submit"
              action="xul-valid" style="stroke:black; fill:#8470ff;" >
              <g:outputText x="630" y="120" textAnchor="middle" value="Process" />
              <g:outputText x="625" y="140" textAnchor="middle" value="Validations" />
           </g:commandButton>

           <!-- Update Model Values Graphic -->

           <g:line id="toUpdate1" x1="720" y1="125" x2="785" y2="125" style="stroke: black; fill: none;" />
           <g:line id="toUpdate2" x1="785" y1="125" x2="785" y2="275" style="stroke: black; fill: none;" />
           <g:line id="toUpdate3" x1="785" y1="275" x2="720" y2="275" style="stroke: black; fill: none;" />
           <g:commandButton id="update" width="120" height="50" x="600" y="250" type="submit"
              action="xul-update" style="stroke:black; fill:#8470ff;" >
              <g:outputText x="610" y="270" textAnchor="middle" value="Update Model" />
              <g:outputText x="635" y="290" textAnchor="middle" value="Values" />
           </g:commandButton>

           <!-- Invoke Applications Graphic -->

           <g:line id="toApp" x1="600" y1="275" x2="470" y2="275" style="stroke: black; fill: none;" />
           <g:commandButton id="invoke" width="120" height="50" x="350" y="250" type="submit"
              action="xul-invoke" style="stroke:black; fill:#8470ff;" >
              <g:outputText x="380" y="270" textAnchor="middle" value="Invoke" />
              <g:outputText x="370" y="290" textAnchor="middle" value="Applications" />
           </g:commandButton>

           <!-- Render Response Graphic -->

           <g:line id="toRender" x1="350" y1="275" x2="220" y2="275" style="stroke: black; fill: none;" />
           <g:commandButton id="render" width="120" height="50" x="100" y="250" type="submit"
              action="xul-render" style="stroke:black; fill:#8470ff;" >
              <g:outputText x="130" y="270" textAnchor="middle" value="Render" />
              <g:outputText x="125" y="290" textAnchor="middle" value="Response" />
           </g:commandButton>

           <g:line id="toResponse" x1="100" y1="275" x2="25" y2="275" style="stroke: black; fill: none;" />
           <g:line id="toRender1" x1="160" y1="150" x2="160" y2="250" style="stroke: black; fill: none;" />
           <g:line id="convalError1" x1="660" y1="150" x2="660" y2="200" 
                   style="stroke:red; fill: none; stroke-dasharray:9,5; visibility:hidden" />
           <g:line id="convalError2" x1="660" y1="200" x2="170" y2="200" 
                   style="stroke:red; fill: none; stroke-dasharray:9,5; visibility:hidden" />
           <g:line id="convalError3" x1="170" y1="200" x2="170" y2="250" 
                   style="stroke:red; fill: none; stroke-dasharray:9,5; visibility:hidden" />

           <!-- The small "request" symbol -->

           <g:rectangle id="request" x="25" y="120" rx="5" ry="5" width="15" height="10" style="fill: #8470ff;"/>

           <!-- Buttons -->

           <g:rectangle id="controlPanel" x="25" y="340" width="310" height="170" style="stroke:black;fill:#778889;" />

           <g:commandButton id="initial" width="140" height="30" x="35" y="350" rx="5" ry="5" dx="68" dy="12"
                     style="stroke:black; fill:silver;" value="Initial Request" onclick="initialMove()"/>

           <g:commandButton id="postback" width="140" height="30" x="35" y="390" rx="5" ry="5" dx="70" dy="9"
                     style="stroke:black; fill:silver;" value="Postback" onclick="postbackMove()"/>

           <g:commandButton id="postbackVal" width="140" height="30" x="185" y="350" rx="5" ry="5" dx="70" dy="9"
                     style="stroke:black; fill:silver;" value="Validation Error" 
                     onclick="postbackConValMove()"/>

           <g:commandButton id="postbackUpd" width="140" height="30" x="185" y="390" rx="5" ry="5" dx="70" dy="9"
                     style="stroke:black; fill:silver;" value="Conversion Error" onclick="postbackConValMove()"/>

           <g:commandButton id="stop" width="140" height="30" x="35" y="430" rx="5" ry="5" dx="68" dy="10"
                     style="stroke:black; fill:#ff4500;" value="Stop/Resume" onclick="stopMovement()"/>

           <g:commandButton id="reset" width="140" height="30" x="185" y="430" rx="5" ry="5" dx="68" dy="10"
                     style="stroke:black; fill:#ff4500;" value="Reset Demo" onclick="resetDemo()"/>

           <g:commandButton id="back" width="140" height="30" x="105" y="470" rx="5" ry="5" dx="68" dy="10"
                     style="stroke:black; fill:silver;" value="Main" type="submit" action="success" />

           <g:outputText id="msg1" x="400" y="350" value="marker" style="visibility:hidden" />
           <g:outputText id="msg2" x="410" y="370" value="marker" style="visibility:hidden" />
           <g:outputText id="msg3" x="410" y="390" value="marker" style="visibility:hidden" />
       </g:form>
    </f:view>
</svg>
