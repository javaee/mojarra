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

package com.sun.faces.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.el.ELResolver;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.CompositeComponentExpressionHolder;

import com.sun.faces.util.Util;
import com.sun.faces.component.CompositeComponentStackManager;
import static com.sun.faces.component.CompositeComponentStackManager.StackType.TreeCreation;
import static com.sun.faces.component.CompositeComponentStackManager.StackType.Evaluation;
import com.sun.faces.util.FacesLogger;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;

/**
 * <p>
 * This {@link ELResolver} will handle the resolution of <code>attrs</code>
 * when processing a composite component instance.
 * </p>
 */
public class CompositeComponentAttributesELResolver extends ELResolver {
    
    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.CONTEXT.getLogger();

    /**
     * Implicit object related only to the cc implicitObject.
     */
    private static final String COMPOSITE_COMPONENT_ATTRIBUTES_NAME = "attrs";

    /**
     * Implicit object related only to the cc implicit object
     * and refers to the composite component parent (if any).
     */
    private static final String COMPOSITE_COMPONENT_PARENT_NAME = "parent";

    /**
     * Key to which we store the mappings between composite component instances
     * and their ExpressionEvalMap.
     */
    private static final String EVAL_MAP_KEY =
          CompositeComponentAttributesELResolver.class.getName() + "_EVAL_MAP";


    // ------------------------------------------------- Methods from ELResolver


    /**
     * <p>
     * If <code>base</code> is a composite component and <code>property</code>
     * is <code>attrs</code>, return a new <code>ExpressionEvalMap</code>
     * which wraps the composite component's attributes map.
     * </p>
     *
     * <p>
     * The <code>ExpressionEvalMap</code> simple evaluates any {@link ValueExpression}
     * instances stored in the composite component's attribute map and returns
     * the result.
     * </p>
     *
     * <p>
     * If <code>base</code> is a composite component and <code>property</code>
     * is <code>parent</code> attempt to resolve the composite componet parent
     * of the current composite component by calling
     * {@link UIComponent#getCompositeComponentParent(javax.faces.component.UIComponent)})
     * and returning that value.
     * </p>
     *
     * @see javax.el.ELResolver#getValue(javax.el.ELContext, Object, Object)
     * @see com.sun.faces.el.CompositeComponentAttributesELResolver.ExpressionEvalMap
     */
    public Object getValue(ELContext context, Object base, Object property) {

        Util.notNull("context", context);

        if (base != null
            && (base instanceof UIComponent)
            && UIComponent.isCompositeComponent((UIComponent) base)
            && property != null) {

            String propertyName = property.toString();
            if (COMPOSITE_COMPONENT_ATTRIBUTES_NAME.equals(propertyName)) {
                UIComponent c = (UIComponent) base;
                context.setPropertyResolved(true);
                FacesContext ctx = (FacesContext)
                      context.getContext(FacesContext.class);
                return getEvalMapFor(c, ctx);
            }

            if (COMPOSITE_COMPONENT_PARENT_NAME.equals(propertyName)) {
                UIComponent c = (UIComponent) base;
                context.setPropertyResolved(true);
                FacesContext ctx = (FacesContext)
                      context.getContext(FacesContext.class);
                CompositeComponentStackManager m =
                      CompositeComponentStackManager.getManager(ctx);
                UIComponent ccp = m.getParentCompositeComponent(TreeCreation,
                                                                ctx,
                                                                c);
                if (ccp == null) {
                    ccp = m.getParentCompositeComponent(Evaluation,
                                                        ctx,
                                                        c);
                }
                return ccp;
            }
        }

        return null;

    }


    /**
     * <p>
     * Readonly, so return <code>null</code>.
     * </p>
     *
     * @see ELResolver#getType(javax.el.ELContext, Object, Object)
     */
    public Class<?> getType(ELContext context, Object base, Object property) {
        Util.notNull("context", context);
        if (!(base instanceof ExpressionEvalMap && property instanceof String)) {
            return null;
        }
        Class<?> exprType = null;
        Class<?> metaType = null;

        ExpressionEvalMap evalMap = (ExpressionEvalMap) base;
        ValueExpression ve = evalMap.getExpression((String) property);
        if (ve != null) {
            exprType = ve.getType(context);
        }

        if (!"".equals(property)) {
            FacesContext facesContext = (FacesContext) context.getContext(FacesContext.class);
            UIComponent cc = UIComponent.getCurrentCompositeComponent(facesContext);
            BeanInfo metadata = (BeanInfo) cc.getAttributes().get(UIComponent.BEANINFO_KEY);
            assert(null != metadata);
            PropertyDescriptor[] attributes = metadata.getPropertyDescriptors();
            if (null != attributes) {
                for (PropertyDescriptor cur : attributes) {
                    if (property.equals(cur.getName())) {
                        Object type = cur.getValue("type");
                        if (null != type) {
                            assert(type instanceof Class);
                            metaType = (Class) type;
                            break;
                        }
                    }
                }
            }
        }
        if (metaType != null) {
            // override exprType only if metaType is narrower:
            if (exprType == null || exprType.isAssignableFrom(metaType)) {
                context.setPropertyResolved(true);
                return metaType;
            }
        }
        return exprType;
    }
    
    /**
     * <p>
     * This is a no-op.
     * </p>
     *
     * @see ELResolver#setValue(javax.el.ELContext, Object, Object, Object)
     */
    public void setValue(ELContext context,
                         Object base,
                         Object property,
                         Object value) {

        Util.notNull("context", context);

    }


    /**
     * <p>
     * Readonly, so return <code>true</code>
     * </p>
     *
     * @see javax.el.ELResolver#isReadOnly(javax.el.ELContext, Object, Object)
     */
    public boolean isReadOnly(ELContext context, Object base, Object property) {

        Util.notNull("context", context);
        return true;

    }


    /**
     * <p>
     * This <code>ELResolver</code> currently returns no feature descriptors
     * as we have no way to effectively iterate over the UIComponent
     * attributes Map.
     * </p>
     *
     * @see javax.el.ELResolver#getFeatureDescriptors(javax.el.ELContext, Object)
     */
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
                                                             Object base) {

        Util.notNull("context", context);
        return null;

    }


    /**
     * <p>
     * <code>attrs<code> is considered a <code>String</code> property.
     * </p>
     *
     * @see javax.el.ELResolver#getCommonPropertyType(javax.el.ELContext, Object)
     */
    public Class<?> getCommonPropertyType(ELContext context, Object base) {

        Util.notNull("context", context);
        return String.class;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>
     * Creates (if necessary) and caches an <code>ExpressionEvalMap</code>
     * instance associated with the owning {@link UIComponent}
     * </p>
     *
     * @param c the owning {@link UIComponent}
     * @param ctx the {@link FacesContext} for the current request
     * @return an <code>ExpressionEvalMap</code> for the specified component
     */
    public Map<String,Object> getEvalMapFor(UIComponent c, FacesContext ctx) {

        Map<Object, Object> ctxAttributes = ctx.getAttributes();
        //noinspection unchecked
        Map<UIComponent,Map<String,Object>> topMap =
              (Map<UIComponent,Map<String,Object>>) ctxAttributes.get(EVAL_MAP_KEY);
        Map<String,Object> evalMap = null;
        if (topMap == null) {
            topMap = new HashMap<UIComponent,Map<String,Object>>();
            ctxAttributes.put(EVAL_MAP_KEY, topMap);
            evalMap = new ExpressionEvalMap(ctx, c);
            topMap.put(c, evalMap);
        }
        else {
            evalMap = topMap.get(c);
            if (evalMap == null) {
                evalMap = new ExpressionEvalMap(ctx, c);
                topMap.put(c, evalMap);
            } else {
        	// JAVASERVERFACES-2508 - running as Portlet2 FacesContext must be updated for rendering, or 
        	// ExpressionEvalMap would have to be reconstructed for the second Portlet phase
        	((ExpressionEvalMap)evalMap).updateFacesContext(ctx);
            }
        }
        return evalMap;

    }


    // ---------------------------------------------------------- Nested Classes


    /**
     * Simple Map implementation to evaluate any <code>ValueExpression</code>
     * stored directly within the provided attributes map.
     */
    private static final class ExpressionEvalMap
    implements Map<String,Object>, CompositeComponentExpressionHolder {

        private Map<String,Object> attributesMap;
        private PropertyDescriptor[] declaredAttributes;
        private Map<Object, Object> declaredDefaultValues;
        private FacesContext ctx;
        private UIComponent cc;


        // -------------------------------------------------------- Constructors


        ExpressionEvalMap(FacesContext ctx, UIComponent cc) {

            this.cc = cc;
            this.attributesMap = cc.getAttributes();
            BeanInfo metadata = (BeanInfo) this.attributesMap.get(UIComponent.BEANINFO_KEY);
            if (null != metadata) {
                this.declaredAttributes = metadata.getPropertyDescriptors();
                this.declaredDefaultValues = new HashMap<Object, Object>(5);
            }
            this.ctx = ctx;

        }


        // --------------------- Methods from CompositeComponentExpressionHolder


        public ValueExpression getExpression(String name) {
            Object ve = cc.getValueExpression(name);
            return ((ve instanceof ValueExpression) ? (ValueExpression) ve : null);
        }


        // ---------------------------------------------------- Methods from Map


        public int size() {
            throw new UnsupportedOperationException();
        }

        public boolean isEmpty() {
            throw new UnsupportedOperationException();
        }

        public boolean containsKey(Object key) {
            boolean result = attributesMap.containsKey(key);
            if (!result) {
                result = null != getDeclaredDefaultValue(key);
            }
            return result;
        }

        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException();
        }



        public Object get(Object key) {
            Object v = attributesMap.get(key);
            if (v == null) {
                v = getDeclaredDefaultValue(key);
                if (v != null) {
                    return ((ValueExpression) v).getValue(ctx.getELContext());
                }
            }
            if (v != null && v instanceof MethodExpression) {
                return v;
            }
            return v;
        }

        public Object put(String key, Object value) {
            // Unlinke AttributesMap.get() which will obtain a value from
            // a ValueExpression, AttributesMap.put(), when passed a value,
            // will never call ValueExpression.setValue(), so we have to take
            // matters into our own hands...
            ValueExpression ve = cc.getValueExpression(key);
            if (ve != null) {
                ve.setValue(ctx.getELContext(), value);
            } else {
                attributesMap.put(key, value);
            }
            return null;
        }

        public Object remove(Object key) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map<? extends String,?> t) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public Set<String> keySet() {
            throw new UnsupportedOperationException();
        }

        public Collection<Object> values() {
            throw new UnsupportedOperationException();
        }

        public Set<Map.Entry<String,Object>> entrySet() {
            throw new UnsupportedOperationException();
        }

        private Object getDeclaredDefaultValue(Object key) {
            Object result = null;

            // If it's not in the cache...
            if (!declaredDefaultValues.containsKey(key)) {
                // iterate through the property descriptors...
                boolean found = false;
                for (PropertyDescriptor cur : declaredAttributes) {
                    // and if you find a match...
                    if (cur.getName().equals(key)) {
                        found = true;
                        // put it in the cache, returning the value.
                        declaredDefaultValues.put(key, result = cur.getValue("default"));
                        break;
                    }
                }
                // Otherwise, if no attribute was declared
                if (!found) {
                    // put null into the cache for future lookups.
                    declaredDefaultValues.put(key, null);
                }
            } else {
                // It's in the cache, just return the value.
                result = declaredDefaultValues.get(key);
            }

            return result;
        }
        
        public void updateFacesContext(FacesContext ctx) {
        	if (this.ctx != ctx) {
        		this.ctx = ctx;
        	}
        }
    }
}
