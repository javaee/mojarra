/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.

 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.

 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.

 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.

 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"

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
package com.sun.faces.systest.component;

import com.sun.faces.event.UIAddComponent;
import java.util.HashMap;
import java.util.Map;
import javax.faces.FactoryFinder;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.view.facelets.FaceletFactory;

import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class Issue599Bean {
    
    public String getResult() {
        FaceletFactory faceletFactory = (FaceletFactory)
                FactoryFinder.getFactory(FactoryFinder.FACELET_FACTORY);
        FacesContext context = FacesContext.getCurrentInstance();
        
        // Can I create a simple h:form with prependId="false"?
        Map<String,Object> attrs = new HashMap<String, Object>();
        attrs.put("prependId", "false");
        UIForm form = (UIForm) faceletFactory.createComponent(context, "http://java.sun.com/jsf/html",
                "form", attrs);
        
        if (form.isPrependId()) {
            throw new IllegalStateException("I asked for a form to be created" +
                    " with prependId false, but that attr is not set.");
        }
        
        attrs.clear();
        
        // Can I create a composite component in the default ResourceLibrary?
        UINamingContainer cc = (UINamingContainer) faceletFactory.
                createComponent(context, "http://java.sun.com/jsf/composite/i_spec_599_composite", 
                "i_spec_599_composite", attrs);
        attrs = cc.getAttributes();
        if (!attrs.containsKey("customAttr")) {
            throw new IllegalArgumentException("I asked for a composite component" +
                    " with a known default attribute, but that attr is not set.");
        }
        
        if (!"customAttrValue".equals(attrs.get("customAttr"))) {
            throw new IllegalArgumentException("I asked for a composite component" +
                    " with a known default attribute" + 
                    " but the value of that attr is not as expected.");
        }
        
        // Can I create a component coming from a custom taglib?
        attrs = new HashMap<String, Object>();
        UIAddComponent ac = (UIAddComponent) faceletFactory.
                createComponent(context, "http://testcomponent", "addcomponent", attrs);
        if (!"com.sun.faces.event".equals(ac.getFamily())) {
            throw new IllegalArgumentException("I asked for a component" +
                    " with a known family" + 
                    " but the value of that family is not as expected.");
            
        }
        
        // Can I create a composite component coming from a custom
        // taglib?
        cc = (UINamingContainer) faceletFactory.createComponent(context, "i_spec_599_composite_taglib", 
                "i_spec_599_composite_taglib", attrs);
        attrs = cc.getAttributes();
        if (!attrs.containsKey("customAttr2")) {
            throw new IllegalArgumentException("I asked for a composite component" +
                    " with a known default attribute, but that attr is not set.");
        }
        
        if (!"customAttrValue2".equals(attrs.get("customAttr2"))) {
            throw new IllegalArgumentException("I asked for a composite component" +
                    " with a known default attribute" + 
                    " but the value of that attr is not as expected.");
        }
        
        
        return "success";
    }
    
}
