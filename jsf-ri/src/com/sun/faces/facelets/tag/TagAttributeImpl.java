/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
 * This file incorporates work covered by the following copyright and
 * permission notice:
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

package com.sun.faces.facelets.tag;

import javax.faces.webapp.pdl.facelets.tag.TagAttributeException;
import javax.faces.webapp.pdl.facelets.tag.Location;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.ValueExpression;

import javax.faces.webapp.pdl.facelets.FaceletContext;
import com.sun.faces.facelets.el.ELText;
import com.sun.faces.facelets.el.TagMethodExpression;
import com.sun.faces.facelets.el.TagValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;

/**
 * Representation of a Tag's attribute in a Facelet File
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public final class TagAttributeImpl extends TagAttribute {

    private final boolean literal;

    private final String localName;

    private final Location location;

    private final String namespace;

    private final String qName;

    private final String value;

    private String string;

    public TagAttributeImpl(Location location, String ns, String localName,
            String qName, String value) {
        this.location = location;
        this.namespace = ns;
        this.localName = localName;
        this.qName = qName;
        this.value = value;
        try {
            this.literal = ELText.isLiteral(this.value);
        } catch (ELException e) {
            throw new TagAttributeException(this, e);
        }
    }

    /**
     * If literal, return
     * {@link Boolean#getBoolean(java.lang.String) Boolean.getBoolean(java.lang.String)}
     * passing our value, otherwise call
     * {@link #getObject(FaceletContext, Class) getObject(FaceletContext, Class)}.
     * 
     * @see Boolean#getBoolean(java.lang.String)
     * @see #getObject(FaceletContext, Class)
     * @param ctx
     *            FaceletContext to use
     * @return boolean value
     */
    @Override
    public boolean getBoolean(FaceletContext ctx) {
        if (this.literal) {
            return Boolean.valueOf(this.value).booleanValue();
        } else {
            return ((Boolean) this.getObject(ctx, Boolean.class))
                    .booleanValue();
        }
    }

    /**
     * If literal, call
     * {@link Integer#parseInt(java.lang.String) Integer.parseInt(String)},
     * otherwise call
     * {@link #getObject(FaceletContext, Class) getObject(FaceletContext, Class)}.
     * 
     * @see Integer#parseInt(java.lang.String)
     * @see #getObject(FaceletContext, Class)
     * @param ctx
     *            FaceletContext to use
     * @return int value
     */
    @Override
    public int getInt(FaceletContext ctx) {
        if (this.literal) {
            return Integer.parseInt(this.value);
        } else {
            return ((Number) this.getObject(ctx, Integer.class)).intValue();
        }
    }

    /**
     * Local name of this attribute
     * 
     * @return local name of this attribute
     */
    @Override
    public String getLocalName() {
        return this.localName;
    }

    /**
     * The location of this attribute in the FaceletContext
     * 
     * @return the TagAttributeImpl's location
     */
    @Override
    public Location getLocation() {
        return this.location;
    }

    /**
     * Create a MethodExpression, using this attribute's value as the expression
     * String.
     * 
     * @see ExpressionFactory#createMethodExpression(javax.el.ELContext,
     *      java.lang.String, java.lang.Class, java.lang.Class[])
     * @see MethodExpression
     * @param ctx
     *            FaceletContext to use
     * @param type
     *            expected return type
     * @param paramTypes
     *            parameter type
     * @return a MethodExpression instance
     */
    @Override
    public MethodExpression getMethodExpression(FaceletContext ctx, Class type,
            Class[] paramTypes) {
        MethodExpression result = null;
        final String specialPrefix = "#{compositeComponent.attrs.";
        final int specialPrefixLen = specialPrefix.length();
        int i;
        try {
            ExpressionFactory f = ctx.getExpressionFactory();
            // Determine if this is a composite component attribute lookup.
            // If so, look for a MethodExpression under the attribute key
            if (this.value.startsWith(specialPrefix)) {
                // Make sure this is *only* an attribute lookup
                if (specialPrefixLen < this.value.length() &&
                        -1 == this.value.indexOf(".", specialPrefixLen)) {
                    String attrName = this.value.substring(specialPrefixLen,
                            this.value.length() - 1);
                    result = new AttributeLookupMethodExpression(this.value, attrName);
                }
            }
            if (null == result) {
                result = new TagMethodExpression(this, f.createMethodExpression(ctx,
                        this.value, type, paramTypes));
            }
        } catch (Exception e) {
            throw new TagAttributeException(this, e);
        }
        return result;
    }
    
    private static class AttributeLookupMethodExpression extends MethodExpression implements StateHolder {

        private String attrName = null;
        private String expressionString = null;
        private boolean isTransient = false;
        
        public AttributeLookupMethodExpression(String expressionString,
                String attrName) {
            if (null == expressionString || null == attrName) {
                throw new NullPointerException("null MethodExpression");
            }
            this.expressionString = expressionString;
            this.attrName = attrName;
        }
        
        public AttributeLookupMethodExpression() {}

        public boolean isTransient() {
            return isTransient;
        }
        
        public void setTransient(boolean isTransient) {
            this.isTransient = isTransient;
        }
        
        public void restoreState(FacesContext context, Object stateObj) {
            String [] state = (String []) stateObj;
            this.attrName = state[0];
            this.expressionString = state[1];
        }

        public Object saveState(FacesContext arg0) {
            String [] state = new String[2];
            state[0] = this.attrName;
            state[1] = this.expressionString;
            return state;
        }

        @Override
        public MethodInfo getMethodInfo(ELContext arg0) {
            return null;
        }

        @Override
        public Object invoke(ELContext elContext, Object[] arg1) {
            Object result = null;
            FacesContext context = (FacesContext) elContext.getContext(FacesContext.class);
            // NPE is ok here.
            UIComponent composite = UIComponent.getCurrentCompositeComponent(context);
            MethodExpression me = null;
            me = (MethodExpression) composite.getAttributes().get(attrName);
            result = me.invoke(elContext, arg1);
            return result;
        }

        @Override
        public String getExpressionString() {
            return expressionString;
        }

        @Override
        public boolean equals(Object otherObj) {
            boolean result = false;
            if (otherObj instanceof AttributeLookupMethodExpression) {
                AttributeLookupMethodExpression other = 
                        (AttributeLookupMethodExpression) otherObj;
                result = this.expressionString.equals(other.expressionString);
            }
            return result;
        }

        @Override
        public boolean isLiteralText() {
            boolean result = false;
            
            result = (this.expressionString.startsWith("#{") &&
                          (this.expressionString.endsWith("}")));
            return result;
        }

        @Override
        public int hashCode() {
            return this.expressionString.hashCode();
        }
    }

    /**
     * The resolved Namespace for this attribute
     * 
     * @return resolved Namespace
     */
    @Override
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * Delegates to getObject with Object.class as a param
     * 
     * @see #getObject(FaceletContext, Class)
     * @param ctx
     *            FaceletContext to use
     * @return Object representation of this attribute's value
     */
    @Override
    public Object getObject(FaceletContext ctx) {
        return this.getObject(ctx, Object.class);
    }

    /**
     * The qualified name for this attribute
     * 
     * @return the qualified name for this attribute
     */
    @Override
    public String getQName() {
        return this.qName;
    }

    /**
     * Return the literal value of this attribute
     * 
     * @return literal value
     */
    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * If literal, then return our value, otherwise delegate to getObject,
     * passing String.class.
     * 
     * @see #getObject(FaceletContext, Class)
     * @param ctx
     *            FaceletContext to use
     * @return String value of this attribute
     */
    @Override
    public String getValue(FaceletContext ctx) {
        if (this.literal) {
            return this.value;
        } else {
            return (String) this.getObject(ctx, String.class);
        }
    }

    /**
     * If literal, simply coerce our String literal value using an
     * ExpressionFactory, otherwise create a ValueExpression and evaluate it.
     * 
     * @see ExpressionFactory#coerceToType(java.lang.Object, java.lang.Class)
     * @see ExpressionFactory#createValueExpression(javax.el.ELContext,
     *      java.lang.String, java.lang.Class)
     * @see ValueExpression
     * @param ctx
     *            FaceletContext to use
     * @param type
     *            expected return type
     * @return Object value of this attribute
     */
    @Override
    public Object getObject(FaceletContext ctx, Class type) {
        if (this.literal) {
            if (String.class.equals(type)) {
                return this.value;
            } else {
                try {
                    return ctx.getExpressionFactory().coerceToType(this.value,
                            type);
                } catch (Exception e) {
                    throw new TagAttributeException(this, e);
                }
            }
        } else {
            ValueExpression ve = this.getValueExpression(ctx, type);
            try {
                return ve.getValue(ctx);
            } catch (Exception e) {
                throw new TagAttributeException(this, e);
            }
        }
    }

    /**
     * Create a ValueExpression, using this attribute's literal value and the
     * passed expected type.
     * 
     * @see ExpressionFactory#createValueExpression(javax.el.ELContext,
     *      java.lang.String, java.lang.Class)
     * @see ValueExpression
     * @param ctx
     *            FaceletContext to use
     * @param type
     *            expected return type
     * @return ValueExpression instance
     */
    @Override
    public ValueExpression getValueExpression(FaceletContext ctx, Class type) {
        try {
            ExpressionFactory f = ctx.getExpressionFactory();
            return new TagValueExpression(this, f.createValueExpression(ctx,
                    this.value, type));
        } catch (Exception e) {
            throw new TagAttributeException(this, e);
        }
    }

    /**
     * If this TagAttributeImpl is literal (not #{..} or ${..})
     * 
     * @return true if this attribute is literal
     */
    @Override
    public boolean isLiteral() {
        return this.literal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (this.string == null) {
            this.string = this.location + " " + this.qName + "=\"" + this.value
                    + "\"";
        }
        return this.string;
    }

}
