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

package com.sun.faces.test.agnostic.resource;

import java.io.IOException;
import java.io.InputStream;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class ResourceBean {

    private static final String LIBRARY_NAME = "css";
    private static final String RESOURCE_NAME = "images/background.png";
    private static final String RESOURCE_TYPE = "images/png";
    private static final String COMBINED_NAME = LIBRARY_NAME + "/" + RESOURCE_NAME;

    public ResourceBean() {
    }

    public String getResourceWithLibrary() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ResourceHandler handler = fc.getApplication().getResourceHandler();
        Resource resource = handler.createResource(RESOURCE_NAME, LIBRARY_NAME, RESOURCE_TYPE);
        String resourceAsString = null;
        try {
            resourceAsString = resource.toString();
        } catch (Exception e) {
            resourceAsString = "** could not create resource " + RESOURCE_NAME + " in library " + LIBRARY_NAME + " **";
        }
        return resourceAsString;
    }

    public void setResourceWithLibrary(String resourceWithLibrary) {
        //noop
    }

    public String getResourceWithoutLibrary() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ResourceHandler handler = fc.getApplication().getResourceHandler();
        Resource resource = handler.createResource(COMBINED_NAME);
        String resourceAsString = null;
        try {
            resourceAsString = resource.toString();
        } catch (Exception e) {
            resourceAsString = "** could not create resource " + COMBINED_NAME + " **";
        }
        return resourceAsString;
    }

    public void setResourceWithoutLibrary(String resourceWithoutLibrary) {
        //noop
    }
    
    
    public String getResourceWithTrailingUnderscore() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ResourceHandler handler = fc.getApplication().getResourceHandler();

        Resource resource = handler.createResource("trailing.css", "styles");
        if (null != resource) {
            try {
                InputStream is = resource.getInputStream();
                while (-1 != is.read()) {
                    
                }
            } catch (IOException ex) {
                return "FAILURE";
            }
        }
        return "SUCCESS";
        
    }
    
    public String getResourceWithLeadingUnderscore() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ResourceHandler handler = fc.getApplication().getResourceHandler();

        Resource resource = handler.createResource("leading.css", "styles");
        if (null != resource) {
            try {
                InputStream is = resource.getInputStream();
                while (-1 != is.read()) {
                    
                }
            } catch (IOException ex) {
                return "FAILURE";
            }
        }
        return "SUCCESS";
        
    }

    public String getResourceWithInvalidVersion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ResourceHandler handler = fc.getApplication().getResourceHandler();

        Resource resource = handler.createResource("noUnderscore.css", "styles");
        if (null != resource) {
            try {
                InputStream is = resource.getInputStream();
                while (-1 != is.read()) {
                    
                }
            } catch (IOException ex) {
                return "FAILURE";
            }
        }
        return "SUCCESS";
        
    }
    
    public String getResourceWithValidVersion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ResourceHandler handler = fc.getApplication().getResourceHandler();

        Resource resource = handler.createResource("foreground.css", "styles");
        if (null != resource) {
            try {
                InputStream is = resource.getInputStream();
                if (-1 == is.read()) {
                    return "FAILURE";
                }
                is.close();
            } catch (IOException ex) {
                return "FAILURE";
            }
        }
        return "SUCCESS";
        
    }
    
    
}
