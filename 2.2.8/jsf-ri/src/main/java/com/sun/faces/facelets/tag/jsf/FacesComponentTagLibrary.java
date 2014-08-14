/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.facelets.tag.jsf;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.annotation.FacesComponentUsage;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

public class FacesComponentTagLibrary extends LazyTagLibrary {
    
    private static final Logger LOGGER = FacesLogger.FACELETS_COMPONENT.getLogger();

    private ApplicationAssociate appAss;
    
    
    public FacesComponentTagLibrary(String ns) {
        super(ns);
        if (null == ns) {
            throw new NullPointerException();
        }
        appAss = ApplicationAssociate.getCurrentInstance();
    }


    @Override
    public boolean containsTagHandler(String ns, String localName) {
        Util.notNull("namespace", ns);
        Util.notNull("tagName", localName);
        
        if (!ns.equals(this.getNamespace())) {
            return false;
        }
        
        // Check the cache maintained by our superclass...
        boolean containsTagHandler = super.containsTagHandler(ns, localName);
        if (!containsTagHandler) {
            FacesComponentUsage matchingFacesComponentUsage = 
                    findFacesComponentUsageForLocalName(ns, localName);
            containsTagHandler = null != matchingFacesComponentUsage;
            
        }
        return containsTagHandler;
    }
    
    private FacesComponentUsage findFacesComponentUsageForLocalName(String ns, String localName) {
        FacesComponentUsage result = null;
        
        Util.notNull("namespace", ns);
        Util.notNull("tagName", localName);
        
        if (!ns.equals(this.getNamespace())) {
            return result;
        }
        List<FacesComponentUsage> componentsForNamespace = appAss.getComponentsForNamespace(ns);
        String tagName;
        for (FacesComponentUsage cur: componentsForNamespace) {
            FacesComponent curFacesComponent = cur.getAnnotation();
            tagName = curFacesComponent.tagName();
            // if the current entry has an explicitly declared tagName...
            if (null != tagName && 0 < tagName.length()) {
                // compare it to the argument tagName
                if (localName.equals(tagName)) {
                    result = cur;
                    break;
                }
            } else if (null != tagName) {
                tagName = cur.getTarget().getSimpleName();
                tagName = tagName.substring(0, 1).toLowerCase() + tagName.substring(1);
                if (localName.equals(tagName)) {
                    result = cur;
                    break;
                }
            }
        }
        
        return result;
    }

    @Override
    public TagHandler createTagHandler(String ns, String localName, TagConfig tag) throws FacesException {
        assert(containsTagHandler(ns, localName));
        TagHandler result = super.createTagHandler(ns, localName, tag);
        if (null == result) {
            FacesComponentUsage facesComponentUsage = 
                    findFacesComponentUsageForLocalName(ns, localName);
            String componentType = facesComponentUsage.getAnnotation().value();

            if (null == componentType || 0 == componentType.length()) {
                componentType = facesComponentUsage.getTarget().getSimpleName();
                componentType = Character.toLowerCase(componentType.charAt(0)) + 
                        componentType.substring(1);
            }
            
            UIComponent throwAwayComponent = FacesContext.getCurrentInstance().
                    getApplication().createComponent(componentType);
            String rendererType = throwAwayComponent.getRendererType();
            super.addComponent(localName, componentType, rendererType);
            result = super.createTagHandler(ns, localName, tag);
        }
        return result;
    }

    @Override
    public boolean tagLibraryForNSExists(String ns) {
        boolean result = false;
        List<FacesComponentUsage> componentsForNamespace = appAss.getComponentsForNamespace(ns);
        
        result = !componentsForNamespace.isEmpty();
        
        return result;
    }

    
}
