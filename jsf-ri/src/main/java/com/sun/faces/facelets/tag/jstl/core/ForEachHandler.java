/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.facelets.tag.jstl.core;

import com.sun.faces.facelets.tag.TagHandlerImpl;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;

/**
 * @author Jacob Hookom
 * @author Andrew Robinson
 */
public final class ForEachHandler extends TagHandlerImpl {

    private static class ArrayIterator implements Iterator {

        protected final Object array;

        protected int i;

        protected final int len;

        public ArrayIterator(Object src) {
            this.i = 0;
            this.array = src;
            this.len = Array.getLength(src);
        }

        public boolean hasNext() {
            return this.i < this.len;
        }

        public Object next() {
            try {
                return Array.get(this.array, this.i++);
            } catch (ArrayIndexOutOfBoundsException ioob) {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private final TagAttribute begin;

    private final TagAttribute end;

    private final TagAttribute items;

    private final TagAttribute step;

    private final TagAttribute tranzient;

    private final TagAttribute var;

    private final TagAttribute varStatus;

    /**
     * @param config
     */
    public ForEachHandler(TagConfig config) {
        super(config);
        this.items = this.getAttribute("items");
        this.var = this.getAttribute("var");
        this.begin = this.getAttribute("begin");
        this.end = this.getAttribute("end");
        this.step = this.getAttribute("step");
        this.varStatus = this.getAttribute("varStatus");
        this.tranzient = this.getAttribute("transient");

        if (this.items == null && this.begin != null && this.end == null) {
            throw new TagAttributeException(
                    this.tag,
                    this.begin,
                    "If the 'items' attribute is not specified, but the 'begin' attribute is, then the 'end' attribute is required");
        }
    }

    public void apply(FaceletContext ctx, UIComponent parent)
            throws IOException {

        String prefix = ctx.generateUniqueId(tagId);

        int s = this.getBegin(ctx);
        int e = this.getEnd(ctx);
        int m = this.getStep(ctx);
        Integer sO = this.begin != null ? s : null;
        Integer eO = this.end != null ? e : null;
        Integer mO = this.step != null ? m : null;

        boolean t = this.getTransient(ctx);
        Object src;
        ValueExpression srcVE = null;
        if (this.items != null) {
            srcVE = this.items.getValueExpression(ctx, Object.class);
            src = srcVE.getValue(ctx);
        } else {
            byte[] b = new byte[e + 1];
            for (int i = 0; i < b.length; i++) {
                b[i] = (byte) i;
            }
            src = b;
        }
        if (src != null) {
            Iterator itr = this.toIterator(src);
            if (itr != null) {
                int i = 0;

                // move to start
                while (i < s && itr.hasNext()) {
                    itr.next();
                    i++;
                }

                String v = this.getVarName(ctx);
                String vs = this.getVarStatusName(ctx);
                VariableMapper vars = ctx.getVariableMapper();
                ValueExpression ve;
                ValueExpression vO = this.capture(v, vars);
                ValueExpression vsO = this.capture(vs, vars);
                int mi;
                Object value;
                int count = 0;
                try {
                    boolean first = true;
                    while (i <= e && itr.hasNext()) {
                        count++;
                        value = itr.next();

                        // set the var
                        if (v != null) {
                            if (t || srcVE == null) {
                                ctx.setAttribute(v, value);
                            } else {
                                ve = this.getVarExpr(srcVE, src, value, i);
                                vars.setVariable(v, ve);
                            }
                        }

                        // set the varStatus
                        if (vs != null) {
                            JstlIterationStatus itrS = new JstlIterationStatus(first, !itr.hasNext(), i, sO, eO, mO, value, count);
                            if (t || srcVE == null) {
                                ctx.setAttribute(vs, itrS);
                            } else {
                                ve = new IterationStatusExpression(itrS);
                                vars.setVariable(vs, ve);
                            }
                        }

                        // execute body
                        ForEachFaceletContext faceletContext = new ForEachFaceletContext(ctx, prefix, i);
                        this.nextHandler.apply(faceletContext, parent);

                        // increment steps
                        mi = 1;
                        while (mi < m && itr.hasNext()) {
                            itr.next();
                            mi++;
                            i++;
                        }
                        i++;

                        first = false;
                    }
                } finally {
                    if (v != null) {
                        vars.setVariable(v, vO);
                    }
                    if (vs != null) {
                        vars.setVariable(vs, vsO);
                    }
                }
            }
        }
    }

    private ValueExpression capture(String name, VariableMapper vars) {
        if (name != null) {
            return vars.setVariable(name, null);
        }
        return null;
    }

    private int getBegin(FaceletContext ctx) {
        if (this.begin != null) {
            return this.begin.getInt(ctx);
        }
        return 0;
    }

    private int getEnd(FaceletContext ctx) {
        if (this.end != null) {
            return this.end.getInt(ctx);
        }
        return Integer.MAX_VALUE - 1; //hotspot bug in the JVM
    }

    private int getStep(FaceletContext ctx) {
        if (this.step != null) {
            return this.step.getInt(ctx);
        }
        return 1;
    }

    private boolean getTransient(FaceletContext ctx) {
        if (this.tranzient != null) {
            return this.tranzient.getBoolean(ctx);
        }
        return false;
    }

    private ValueExpression getVarExpr(ValueExpression ve, Object src,
            Object value, int i) {
        if (src instanceof List || src.getClass().isArray()) {
            return new IndexedValueExpression(ve, i);
        } else if (src instanceof Map && value instanceof Map.Entry) {
            return new MappedValueExpression(ve, (Map.Entry) value);
        } else if (src instanceof Collection) {
            return new IteratedValueExpression(ve, value);
        }
        throw new IllegalStateException("Cannot create VE for: " + src);
    }

    private String getVarName(FaceletContext ctx) {
        if (this.var != null) {
            return this.var.getValue(ctx);
        }
        return null;
    }

    private String getVarStatusName(FaceletContext ctx) {
        if (this.varStatus != null) {
            return this.varStatus.getValue(ctx);
        }
        return null;
    }

    private Iterator toIterator(Object src) {
        if (src == null) {
            return null;
        } else if (src instanceof Collection) {
            return ((Collection) src).iterator();
        } else if (src instanceof Map) {
            return ((Map) src).entrySet().iterator();
        } else if (src.getClass().isArray()) {
            return new ArrayIterator(src);
        } else {
            throw new TagAttributeException(this.tag, this.items,
                    "Must evaluate to a Collection, Map, Array, or null.");
        }
    }

    private static class ForEachFaceletContext extends FaceletContext {

        private String prefix;
        private int index;
        private FaceletContext faceletContext;
        private final Map<String, Integer> ids;

        private ForEachFaceletContext(FaceletContext faceletContext, String prefix, int index) {
            this.faceletContext = faceletContext;
            this.prefix = prefix;
            this.index = index;
            this.ids = new HashMap<String, Integer>();
        }

        @Override
        public FacesContext getFacesContext() {
            return faceletContext.getFacesContext();
        }

        @Override
        public String generateUniqueId(String base) {
            StringBuilder uniqueIdBuilder = new StringBuilder();
            Integer cnt = ids.get(base);

            if (cnt == null) {
                ids.put(base, 0);
                uniqueIdBuilder.delete(0, uniqueIdBuilder.length());
                uniqueIdBuilder.append(prefix);
                uniqueIdBuilder.append("_");
                uniqueIdBuilder.append(index);
                uniqueIdBuilder.append("_");
                uniqueIdBuilder.append(base);
            } else {
                int i = cnt.intValue() + 1;
                ids.put(base, i);
                uniqueIdBuilder.delete(0, uniqueIdBuilder.length());
                uniqueIdBuilder.append(prefix);
                uniqueIdBuilder.append("_");
                uniqueIdBuilder.append(index);
                uniqueIdBuilder.append("_");
                uniqueIdBuilder.append(base);
                uniqueIdBuilder.append("_");
                uniqueIdBuilder.append(i);
            }

            return uniqueIdBuilder.toString();
        }

        @Override
        public ExpressionFactory getExpressionFactory() {
            return faceletContext.getExpressionFactory();
        }

        @Override
        public void setVariableMapper(VariableMapper varMapper) {
            faceletContext.setVariableMapper(varMapper);
        }

        @Override
        public void setFunctionMapper(FunctionMapper fnMapper) {
            faceletContext.setFunctionMapper(fnMapper);
        }

        @Override
        public void setAttribute(String name, Object value) {
            faceletContext.setAttribute(name, value);
        }

        @Override
        public Object getAttribute(String name) {
            return faceletContext.getAttribute(name);
        }

        @Override
        public void includeFacelet(UIComponent parent, String relativePath) throws IOException {
            faceletContext.includeFacelet(parent, relativePath);
        }

        @Override
        public void includeFacelet(UIComponent parent, URL absolutePath) throws IOException {
            faceletContext.includeFacelet(parent, absolutePath);
        }

        @Override
        public ELResolver getELResolver() {
            return faceletContext.getELResolver();
        }

        @Override
        public FunctionMapper getFunctionMapper() {
            return faceletContext.getFunctionMapper();
        }

        @Override
        public VariableMapper getVariableMapper() {
            return faceletContext.getVariableMapper();
        }

        @Override
        public Object getContext(Class key) {
            return faceletContext.getContext(key);
        }

        @Override
        public Locale getLocale() {
            return faceletContext.getLocale();
        }

        public int getIndex() {
            return index;
        }

        public String getPrefix() {
            return prefix;
        }

        @Override
        public boolean isPropertyResolved() {
            return faceletContext.isPropertyResolved();
        }

        @Override
        public void putContext(Class key, Object contextObject) {
            faceletContext.putContext(key, contextObject);
        }

        @Override
        public void setLocale(Locale locale) {
            faceletContext.setLocale(locale);
        }

        @Override
        public void setPropertyResolved(boolean resolved) {
            faceletContext.setPropertyResolved(resolved);
        }
    }
}
