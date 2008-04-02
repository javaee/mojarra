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
    <HEAD> <TITLE> Welcome to JavaServer Faces </TITLE> 
    </HEAD>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="/WEB-INF/imageMapRender.tld" prefix="imageMapRender" %>

     <fmt:setBundle
	    basename="imageMapRender.Resources"
	    scope="session" var="imageMapRenderBundle"/>
      <f:use_faces>
        <h:form id="imageMapRenderForm" formName="imageMapRenderForm" >
	<table> 
             <tr> 
              <td> <h:output_text id="welcomeLabel" key="welcomeLabel" 
                                      bundle="imageMapRenderBundle" /> </td>
	     </tr>
             <tr>
              <TD>
		<imageMapRender:image id="mapImage" url="/world.gif" usemap="#worldMap" />	
		<imageMapRender:map id="worldMap" currentArea="NAmericas" >	
                    <f:action_listener type="imageMapRender.ImageMapEventHandler"/>
       			<imageMapRender:area id="NAmericas" modelReference="NA" onmouseover="/imageMapRender/world_namer.gif" onmouseout="/imageMapRender/world.gif" />
			<imageMapRender:area id="SAmericas" modelReference="SA" onmouseover="/imageMapRender/world_samer.gif" onmouseout="/imageMapRender/world.gif" />
			<imageMapRender:area id="Finland" modelReference="finA" onmouseover="/imageMapRender/world_finland.gif" onmouseout="/imageMapRender/world.gif"  />
			<imageMapRender:area id="Germany" modelReference="gerA" onmouseover="/imageMapRender/world_germany.gif" onmouseout="/imageMapRender/world.gif" />
       			<imageMapRender:area id="France" modelReference="fraA" onmouseover="/imageMapRender/world_france.gif" onmouseout="/imageMapRender/world.gif" />
		</imageMapRender:map>
	     </TD></TR>
	</TABLE>
	</h:form>
       </f:use_faces>

</HTML>
