
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

package com.sun.faces.facelets.tag.jsp;

import com.sun.faces.util.Util;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;




public class UseBeanHandler extends TagHandler {

    private final TagAttribute id;

    private final TagAttribute scope;

    private final TagAttribute classAttr;

    private final TagAttribute type;

    private final TagAttribute beanName;

    public UseBeanHandler(TagConfig config) {
        super(config);

        this.id = this.getRequiredAttribute("id");
        this.scope = this.getAttribute("scope");
        this.classAttr = this.getAttribute("class");
        this.type = this.getAttribute("type");
        this.beanName = this.getAttribute("beanName");
    }

    public void apply(FaceletContext fc, UIComponent uic) throws IOException {
        FacesContext facesContext = fc.getFacesContext();
        ExternalContext extContext = facesContext.getExternalContext();
        // view scope is defined as equivalent to page scope
        Map<String, Object> scopeMap = facesContext.getViewRoot().getViewMap();
        boolean isRequestScoped = false;

        // Find the correct scope
        String scopeName = (null != this.scope) ? this.scope.getValue(fc) : "";
        if ("session".equals(scopeName)) {
            scopeMap = extContext.getSessionMap();
        } else if ("application".equals(scopeName)) {
            scopeMap = extContext.getApplicationMap();
        } else if ("request".equals(scopeName)) {
            isRequestScoped = true;
            scopeMap = extContext.getRequestMap();
        } else {
            if ((!"".equals(scopeName)) && !("page".equals(scopeName))) {
                throw new FaceletException("Invalid scope name " + scopeName + ".");
            }
        }
        assert(null != scopeMap);
        String idVal = this.id.getValue(fc);
        Object bean = scopeMap.get(idVal);
        boolean instantiatedByThisMetod = null != bean;

        if(!instantiatedByThisMetod) {
            // case 1 we have a class with an optional type
            // case 2 we have a beanName and type
            // case 3 we can have just a type

            if (null == beanName && null == classAttr && null != type) {
                // this is case 3
            } else if ((null != beanName) && (null != type)) {
                // this is case 2
                String beanNameVal = this.beanName.getValue(fc);
                try {
                    bean = java.beans.Beans.instantiate(Util.getCurrentLoader(this), beanNameVal);
                    instantiatedByThisMetod = true;
                } catch (ClassNotFoundException ex) {
                    throw new FaceletException(ex);
                }
            } else if (null != classAttr) {
                // this is case 1
                String className = this.classAttr.getValue(fc);
                try {
                    Class clazz = Util.loadClass(className, this);
                    bean = clazz.newInstance();
                    instantiatedByThisMetod = true;
                } catch (IllegalAccessException ie) {
                    throw new FaceletException(ie);
                } catch (InstantiationException ie) {
                    throw new FaceletException(ie);
                } catch (ClassNotFoundException ex) {
                    throw new FaceletException(ex);
                }
            }
        }

        // The "object reference variable" concept does not exist
        // in Facelets because there is no concept of scriptlets.
        // Therefore, request scope stands in for the object reference variable
        // concept
        if (!isRequestScoped && instantiatedByThisMetod) {
            extContext.getRequestMap().put(idVal, bean);
        }

        if (instantiatedByThisMetod) {
            nextHandler.apply(fc, uic);
        }
    }



}
