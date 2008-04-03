<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
 Contributor(s):
 
 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
--%>

<?xml version="1.0"?>

<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN"
         "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">

<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<%@ page contentType="image/svg+xml"%>
<%@ taglib uri="http://java.sun.com/jsf/svg" prefix="g" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
                                                                                       
<svg xmlns="http://www.w3.org/2000/svg"
     xmlns:xlink="http://www.w3.org/1999/xlink">
    <style type="text/css">
      rect:hover {fill-opacity:0.3;}
    </style>

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

           <g:rectangle id="messagePanel" x="350" y="340" width="510" height="170" style="stroke:black;fill:#f0e68c; visibility:hidden" />
           <g:outputText id="msg1" x="370" y="370" value="marker" style="visibility:hidden" />
           <g:outputText id="msg2" x="380" y="390" value="marker" style="visibility:hidden" />
           <g:outputText id="msg3" x="390" y="410" value="marker" style="visibility:hidden" />
       </g:form>
    </f:view>
</svg>
