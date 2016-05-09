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

<%@ page contentType="text/html"
%><%@ page import="java.util.Iterator"
%><%@ page import="java.util.Map"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.render.RenderKit"
%><%@ page import="javax.faces.render.RenderKitFactory"
%><%@ page import="javax.faces.render.Renderer"
%><%

// This test goes through the config system to test the loading of 
// the default renderkit information as well as a custom renderkit
// consisting of one renderer.

    // Initialize list of Renderer types
    //
    String families[] = {
      "javax.faces.Command",
      "javax.faces.Command",
      "javax.faces.Data",
      "javax.faces.Form",
      "javax.faces.Graphic",
      "javax.faces.Input",
      "javax.faces.Input",
      "javax.faces.Input",
      "javax.faces.Input",
      "javax.faces.Message",
      "javax.faces.Messages",
      "javax.faces.Output",
      "javax.faces.Output",
      "javax.faces.Output",
      "javax.faces.Output",
      "javax.faces.Panel",
      "javax.faces.Panel",
      "javax.faces.SelectBoolean",
      "javax.faces.SelectMany",
      "javax.faces.SelectMany",
      "javax.faces.SelectMany",
      "javax.faces.SelectOne",
      "javax.faces.SelectOne",
      "javax.faces.SelectOne"
    };

    String defaultList[] = {
      "javax.faces.Button",
      "javax.faces.Link",
      "javax.faces.Table",
      "javax.faces.Form",
      "javax.faces.Image",
      "javax.faces.Hidden",
      "javax.faces.Secret",
      "javax.faces.Text",
      "javax.faces.Textarea",
      "javax.faces.Message",
      "javax.faces.Messages",
      "javax.faces.Format",
      "javax.faces.Label",
      "javax.faces.Link",
      "javax.faces.Text",
      "javax.faces.Grid",
      "javax.faces.Group",
      "javax.faces.Checkbox",
      "javax.faces.Checkbox",
      "javax.faces.Listbox",
      "javax.faces.Menu",
      "javax.faces.Listbox",
      "javax.faces.Menu",
      "javax.faces.Radio"
      };

    String customFamilies[] = {"SysTest"};
    String customList[] = {"Text"};

    // Acquire RenderKits and check RenderKitId(s)
    //
    RenderKitFactory renderKitFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    Iterator renderKitIds = renderKitFactory.getRenderKitIds();

    boolean foundDefault = false; 
    boolean foundCustom= false; 
    while (renderKitIds.hasNext()) {
        String renderKitId = (String)renderKitIds.next();
        if (renderKitId.equals(RenderKitFactory.HTML_BASIC_RENDER_KIT)) {
            foundDefault = true;
        } else if (renderKitId.equals("CUSTOM")) {
            foundCustom = true;
        }
    }
    if (!foundDefault || !foundCustom) {
        out.println("/renderkit01.jsp FAILED - all renderkit ids not found");
	return;
    }

    // Check Renderers For Each RenderKit
    //
    while (renderKitIds.hasNext()) {
        String renderKitId = (String)renderKitIds.next();
	RenderKit rKit = renderKitFactory.getRenderKit(null, renderKitId);
	if (rKit == null) {
	    out.println("/renderkit01.jsp FAILED - renderkit not found");
	    return;
	}
	Renderer renderer = null;
	if (renderKitId.equals(RenderKitFactory.HTML_BASIC_RENDER_KIT)) {
	    for (int i=0; i<defaultList.length; i++) {
	        try {
	            renderer = rKit.getRenderer(families[i], defaultList[i]);
	        } catch (IllegalArgumentException ia) {
	            out.println("/renderkit01.jsp FAILED - renderer not found for type:"+
		        defaultList[i]+" in renderkit 'DEFAULT'");
		    return;
	        }
	    }
	} else if (renderKitId.equals("CUSTOM")) {
	    for (int i=0; i<customList.length; i++) {
	        try {
	            renderer = rKit.getRenderer(customFamilies[i],
                                                customList[i]);
	        } catch (IllegalArgumentException ia) {
	            out.println("/renderkit01.jsp FAILED - renderer not found for type:"+
		        customList[i]+" in renderkit 'CUSTOM'");
		    return;
	        }
	    }
	}
    }

    // All tests passed
    //
    out.println("/renderkit01.jsp PASSED");
%>
