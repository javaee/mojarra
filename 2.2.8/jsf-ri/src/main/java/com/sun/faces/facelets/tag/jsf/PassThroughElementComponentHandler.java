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
 *
 */

package com.sun.faces.facelets.tag.jsf;

import com.sun.faces.util.Util;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.render.Renderer;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagException;

public class PassThroughElementComponentHandler extends ComponentHandler {
    
    private final TagAttribute elementName;
    
    protected final TagAttribute getRequiredPassthroughAttribute(String localName)
            throws TagException {
        TagAttribute attr = this.tag.getAttributes().get(PassThroughAttributeLibrary.Namespace, localName);
        if (attr == null) {
            throw new TagException(this.tag, "Attribute '" + localName
                    + "' is required");
        }
        return attr;
    }
    
    

    public PassThroughElementComponentHandler(ComponentConfig config) {
        super(config);
        
        elementName = this.getRequiredPassthroughAttribute(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY);
    }
    
    @Override
    public UIComponent createComponent(FaceletContext ctx) {
        UIComponent result = null;
        try {
            Class clazz = Util.loadClass("com.sun.faces.component.PassthroughElement", this);
            result = (UIComponent)clazz.newInstance();
        } catch (ClassNotFoundException cnfe) {
            throw new FacesException(cnfe);
        } catch (IllegalAccessException iae) {
            throw new FacesException(iae);
        } catch (InstantiationException ie) {
            throw new FacesException(ie);
        }
        
        return result;
    }

    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        if (parent.getParent() == null) {
            Map<String,Object> passThroughAttrs = c.getPassThroughAttributes(true);
            Object attrValue;
            attrValue = (this.elementName.isLiteral()) ? this.elementName.getValue(ctx) : this.elementName.getValueExpression(ctx, Object.class);
            passThroughAttrs.put(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY, attrValue);
        }
        
    }
    
    
    
}
