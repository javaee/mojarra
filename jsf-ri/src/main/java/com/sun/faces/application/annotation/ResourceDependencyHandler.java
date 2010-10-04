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

package com.sun.faces.application.annotation;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependency;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.sun.faces.el.ELUtils;
import com.sun.faces.util.RequestStateManager;

/**
 * {@link RuntimeAnnotationHandler} responsible for processing {@link ResourceDependency} annotations.
 */
class ResourceDependencyHandler implements RuntimeAnnotationHandler {

    private ResourceDependency[] dependencies;
    private Map<ResourceDependency,Expressions> expressionsMap;


    // ------------------------------------------------------------ Constructors


    public ResourceDependencyHandler(ResourceDependency[] dependencies) {

        this.dependencies = dependencies;
        Map<Object, Object> attrs = FacesContext.getCurrentInstance().getAttributes();
        expressionsMap = new HashMap<ResourceDependency,Expressions>(dependencies.length, 1.0f);
        for (ResourceDependency dep : dependencies) {
            Expressions exprs = new Expressions();
            exprs.name = dep.name();
            String lib = dep.library();
            if (lib.length() > 0) {
                // Take special action to resolve the "this" library name
                if ("this".equals(lib)) {
                    String thisLibrary = (String)
                            attrs.get(com.sun.faces.application.ApplicationImpl.THIS_LIBRARY);
                    assert(null != thisLibrary);
                    lib = thisLibrary;
                }

                exprs.library = lib;
            }
            String tgt = dep.target();
            if (tgt.length() > 0) {
                exprs.target = tgt;
            }
            expressionsMap.put(dep, exprs);
        }

    }


    // ----------------------------------- Methods from RuntimeAnnotationHandler
    

    @SuppressWarnings({"UnusedDeclaration"})
    public void apply(FacesContext ctx, Object... params) {

        for (ResourceDependency dep : dependencies) {
            if (!hasBeenProcessed(ctx, dep)) {
                pushResourceToRoot(ctx, createComponentResource(ctx, dep));
                markProcssed(ctx, dep);
            }
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * Adds the specified {@link UIComponent} as a component resource to the
     * {@link javax.faces.component.UIViewRoot}
     * @param ctx the {@link FacesContext} for the current request
     * @param c the component resource
     */
    private void pushResourceToRoot(FacesContext ctx, UIComponent c) {

        ctx.getViewRoot().addComponentResource(ctx, c, (String) c .getAttributes().get("target"));

    }


    /**
     * Determines of the specified {@link ResourceDependency} has already been
     * previously processed.
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param dep the {@link ResourceDependency} in question
     * @return <code>true</code> if the {@link ResourceDependency} has been
     *  processed, otherwise <code>false</code>
     */
    @SuppressWarnings({"unchecked"})
    private boolean hasBeenProcessed(FacesContext ctx, ResourceDependency dep) {

        Set<ResourceDependency> dependencies = (Set<ResourceDependency>)
              RequestStateManager.get(ctx, RequestStateManager.PROCESSED_RESOURCE_DEPENDENCIES);
        return ((dependencies != null) && dependencies.contains(dep));

    }


    /**
     * Construct a new component resource based off the provided {@link ValueExpression}s.
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param dep the ResourceDependency that the component resource will be
     *  constructed from
     * @return a new component resource based of the provided annotation
     */
    private UIComponent createComponentResource(FacesContext ctx, ResourceDependency dep) {

        Expressions exprs = expressionsMap.get(dep);
        Application app = ctx.getApplication();
        String resname = exprs.getName(ctx);
        UIComponent c = ctx.getApplication().createComponent("javax.faces.Output");
        c.setRendererType(app.getResourceHandler().getRendererTypeForResourceName(resname));
        Map<String,Object> attrs = c.getAttributes();
        attrs.put("name", resname);
        if (exprs.library != null) {
            attrs.put("library", exprs.getLibrary(ctx));
        }
        if (exprs.target != null) {
            attrs.put("target", exprs.getTarget(ctx));
        }
        return c;

    }


    /**
     * Indicates that the specified ResourceDependency has been processed.
     * @param ctx the {@link FacesContext} for the current request
     * @param dep the {@link ResourceDependency}
     */
    @SuppressWarnings({"unchecked"})
    private void markProcssed(FacesContext ctx, ResourceDependency dep) {

        Set<ResourceDependency> dependencies = (Set<ResourceDependency>)
              RequestStateManager.get(ctx, RequestStateManager.PROCESSED_RESOURCE_DEPENDENCIES);
        if (dependencies == null) {
            dependencies = new HashSet<ResourceDependency>(6);
            RequestStateManager.set(ctx, RequestStateManager.PROCESSED_RESOURCE_DEPENDENCIES, dependencies);
        }
        dependencies.add(dep);
        
    }


    // ----------------------------------------------------------- Inner Classes


    /**
     * This helper class hides expression evaluation complexity.
     */
    private static final class Expressions {

        ValueExpression nameExpression;
        ValueExpression libraryExpression;
        ValueExpression targetExpression;
        String name;
        String library;
        String target;

        String getName(FacesContext ctx) {
            if (nameExpression == null) {
                nameExpression = ELUtils.createValueExpression(name, String.class);
            }
            return (String) nameExpression.getValue(ctx.getELContext());
        }

        String getLibrary(FacesContext ctx) {
            if (library != null) {
                if (libraryExpression == null) {
                    libraryExpression = ELUtils.createValueExpression(library, String.class);
                }
                return (String) libraryExpression.getValue(ctx.getELContext());
            }
            return null;
        }

        String getTarget(FacesContext ctx) {
            if (target != null) {
                if (targetExpression == null) {
                    targetExpression = ELUtils.createValueExpression(target, String.class);
                }
                return (String) targetExpression.getValue(ctx.getELContext());
            }
            return null;
        }


    }


}
