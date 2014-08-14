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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.impl;

import javax.faces.view.facelets.Facelet;
import com.sun.faces.facelets.FaceletContextImplBase;
import com.sun.faces.facelets.TemplateClient;
import com.sun.faces.facelets.el.DefaultVariableMapper;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Default FaceletContext implementation.
 * 
 * A single FaceletContext is used for all Facelets involved in an invocation of
 * {@link com.sun.faces.facelets.Facelet#apply(FacesContext, UIComponent) Facelet#apply(FacesContext, UIComponent)}.
 * This means that included Facelets are treated the same as the JSP include
 * directive.
 * 
 * @author Jacob Hookom
 * @version $Id: DefaultFaceletContext.java,v 1.4.4.3 2006/03/25 01:01:53 jhook
 *          Exp $
 */
final class DefaultFaceletContext extends FaceletContextImplBase {

    private final FacesContext faces;

    private final ELContext ctx;

    private final DefaultFacelet facelet;
    private final List<Facelet> faceletHierarchy;

    private VariableMapper varMapper;

    private FunctionMapper fnMapper;

    private final Map<String,Integer> ids;
    private final Map<Integer,Integer> prefixes;
    private String prefix;
    private final StringBuilder uniqueIdBuilder=new StringBuilder(30);


    public DefaultFaceletContext(DefaultFaceletContext ctx,
            DefaultFacelet facelet) {
        this.ctx = ctx.ctx;
        this.clients = ctx.clients;
        this.faces = ctx.faces;
        this.fnMapper = ctx.fnMapper;
        this.ids = ctx.ids;
        this.prefixes = ctx.prefixes;
        this.varMapper = ctx.varMapper;
        this.faceletHierarchy = new ArrayList<Facelet>(ctx.faceletHierarchy.size()+1);
        this.faceletHierarchy.addAll(ctx.faceletHierarchy);
        this.faceletHierarchy.add(facelet);
        this.facelet=facelet;
        this.faces.getAttributes().put(FaceletContext.FACELET_CONTEXT_KEY,
                this);
    }

    public DefaultFaceletContext(FacesContext faces, DefaultFacelet facelet) {
        this.ctx = faces.getELContext();
        this.ids = new HashMap<String,Integer>();
        this.prefixes = new HashMap<Integer,Integer>();
        this.clients = new ArrayList<TemplateManager>(5);
        this.faces = faces;
        this.faceletHierarchy = new ArrayList<Facelet>(1);
        this.faceletHierarchy.add(facelet);
        this.facelet = facelet;
        this.varMapper = this.ctx.getVariableMapper();
        if (this.varMapper == null) {
            this.varMapper = new DefaultVariableMapper();
        }
        this.fnMapper = this.ctx.getFunctionMapper();
        this.faces.getAttributes().put(FaceletContext.FACELET_CONTEXT_KEY,
                this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.view.facelets.FaceletContext#getFacesContext()
     */
    public FacesContext getFacesContext() {
        return this.faces;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.view.facelets.FaceletContext#getExpressionFactory()
     */
    public ExpressionFactory getExpressionFactory() {
        return this.facelet.getExpressionFactory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.view.facelets.FaceletContext#setVariableMapper(javax.el.VariableMapper)
     */
    public void setVariableMapper(VariableMapper varMapper) {
        // Assert.param("varMapper", varMapper);
        this.varMapper = varMapper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.view.facelets.FaceletContext#setFunctionMapper(javax.el.FunctionMapper)
     */
    public void setFunctionMapper(FunctionMapper fnMapper) {
        // Assert.param("fnMapper", fnMapper);
        this.fnMapper = fnMapper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.view.facelets.FaceletContext#includeFacelet(javax.faces.component.UIComponent,
     *      java.lang.String)
     */
    public void includeFacelet(UIComponent parent, String relativePath)
            throws IOException, FacesException, ELException {
        this.facelet.include(this, parent, relativePath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.el.ELContext#getFunctionMapper()
     */
    public FunctionMapper getFunctionMapper() {
        return this.fnMapper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.el.ELContext#getVariableMapper()
     */
    public VariableMapper getVariableMapper() {
        return this.varMapper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.el.ELContext#getContext(java.lang.Class)
     */
    public Object getContext(Class key) {
        return this.ctx.getContext(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.el.ELContext#putContext(java.lang.Class, java.lang.Object)
     */
    public void putContext(Class key, Object contextObject) {
        this.ctx.putContext(key, contextObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.view.facelets.FaceletContext#generateUniqueId(java.lang.String)
     */
    public String generateUniqueId(String base) {

        if(prefix==null) {
            StringBuilder builder = new StringBuilder(faceletHierarchy.size()*30);
            for(int i=0; i< faceletHierarchy.size(); i++) {
                DefaultFacelet facelet = (DefaultFacelet) faceletHierarchy.get(i);
                builder.append(facelet.getAlias());
            }
            Integer prefixInt = builder.toString().hashCode();

            Integer cnt = prefixes.get(prefixInt);
            if(cnt==null) {
                this.prefixes.put(prefixInt, 0);
                prefix = prefixInt.toString();
            } else {
                int i=cnt.intValue()+1;
                this.prefixes.put(prefixInt, i);
                prefix = prefixInt + "_" +i;
            }
        }

        Integer cnt = this.ids.get(base);
        if (cnt == null) {
            this.ids.put(base, 0);
            uniqueIdBuilder.delete(0,uniqueIdBuilder.length());
            uniqueIdBuilder.append(prefix);
            uniqueIdBuilder.append("_");
            uniqueIdBuilder.append(base);
            return uniqueIdBuilder.toString();
        } else {
            int i = cnt.intValue() + 1;
            this.ids.put(base, i);
            uniqueIdBuilder.delete(0,uniqueIdBuilder.length());
            uniqueIdBuilder.append(prefix);
            uniqueIdBuilder.append("_");
            uniqueIdBuilder.append(base);
            uniqueIdBuilder.append("_");            
            uniqueIdBuilder.append(i);
            return uniqueIdBuilder.toString();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.view.facelets.FaceletContext#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name) {
        if (this.varMapper != null) {
            ValueExpression ve = this.varMapper.resolveVariable(name);
            if (ve != null) {
                return ve.getValue(this);
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.view.facelets.FaceletContext#setAttribute(java.lang.String,
     *      java.lang.Object)
     */
    public void setAttribute(String name, Object value) {
        if (this.varMapper != null) {
            if (value == null) {
                this.varMapper.setVariable(name, null);
            } else {
                this.varMapper.setVariable(name, this.facelet
                        .getExpressionFactory().createValueExpression(value,
                                Object.class));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.view.facelets.FaceletContext#includeFacelet(javax.faces.component.UIComponent,
     *      java.net.URL)
     */
    public void includeFacelet(UIComponent parent, URL absolutePath)
            throws IOException, FacesException, ELException {
        this.facelet.include(this, parent, absolutePath);
    }

    public ELResolver getELResolver() {
        return this.ctx.getELResolver();
    }

    private final List<TemplateManager> clients;

    public void popClient(TemplateClient client) {
        if (!this.clients.isEmpty()) {
            Iterator itr = this.clients.iterator();
            while (itr.hasNext()) {
                if (itr.next().equals(client)) {
                    itr.remove();
                    return;
                }
            }
        }
        throw new IllegalStateException(client + " not found");
    }

    public void pushClient(final TemplateClient client) {
        this.clients.add(0, new TemplateManager(this.facelet, client, true));
    }

    public void extendClient(final TemplateClient client) {
        this.clients.add(new TemplateManager(this.facelet, client, false));
    }

    public boolean includeDefinition(UIComponent parent, String name)
    throws IOException {
        boolean found = false;
        TemplateManager client;

        for (int i = 0, size = this.clients.size(); i < size && !found; i++) {
            client = this.clients.get(i);
            //noinspection EqualsBetweenInconvertibleTypes
            if (client.equals(this.facelet))
                continue;            
            found = client.apply(this, parent, name);            
        }

        return found;
    }

    private final static class TemplateManager implements TemplateClient {
        private final DefaultFacelet owner;

        private final TemplateClient target;
        
        private final boolean root;

        private final Set<String> names = new HashSet<String>();

        public TemplateManager(DefaultFacelet owner, TemplateClient target, boolean root) {
            this.owner = owner;
            this.target = target;
            this.root = root;
        }

        public boolean apply(FaceletContext ctx, UIComponent parent, String name)
        throws IOException {

            String testName = (name != null) ? name : "facelets._NULL_DEF_";
            if (this.names.contains(testName)) {
                return false;
            } else {
                this.names.add(testName);
                boolean found = this.target.apply(new DefaultFaceletContext(
                        (DefaultFaceletContext) ctx, this.owner), parent, name);
                this.names.remove(testName);
                return found;
            }
        }

        
        @Override
        public boolean equals(Object o) {
            // System.out.println(this.owner.getAlias() + " == " +
            // ((DefaultFacelet) o).getAlias());
            return this.owner == o || this.target == o;
        }

        public boolean isRoot() {
            return this.root;
        }
    }


    public boolean isPropertyResolved() {
        return this.ctx.isPropertyResolved();
    }

    public void setPropertyResolved(boolean resolved) {
        this.ctx.setPropertyResolved(resolved);
    }
}
