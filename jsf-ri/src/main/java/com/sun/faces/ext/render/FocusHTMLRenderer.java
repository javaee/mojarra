/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.ext.render;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

/**
 * Renderer class that emits HTML and JavaScript to set the focus to a given field.
 * 
 * @author driscoll
 */
public class FocusHTMLRenderer extends Renderer {
    
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        String forID = (String) component.getAttributes().get("for");
        ResponseWriter writer = context.getResponseWriter();
        // XXX - I'd still like to get the parentID, but need to add a check if it's a form or not...
        //UIComponent parentComponent = component.getParent();
        //String parentID = parentComponent.getClientId(context);
        //String targetID = parentID+":"+forID;
        String targetID = forID;
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeText("setFocus('",null);
        writer.writeText(targetID, null);
        writer.writeText("');\n",null);
        writer.writeText("function setFocus(elementId) { var element = " +
                "document.getElementById(elementId); if (element && element.focus) " +
                "{ element.focus(); } }",null);
        writer.endElement("script");        
    }

}
