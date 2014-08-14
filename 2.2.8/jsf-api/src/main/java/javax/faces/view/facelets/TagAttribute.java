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

package javax.faces.view.facelets;

import javax.faces.view.Location;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * <p class="changed_added_2_0"><span class="changed_modified_2_2">Representation</span>
 * of an XML attribute name=value pair on an XML element in a Facelet file.</p>
 * 
 * @since 2.0
 */
public abstract class TagAttribute {

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
    public abstract boolean getBoolean(FaceletContext ctx);

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
    public abstract int getInt(FaceletContext ctx);

    /**
     * Local name of this attribute
     * 
     * @return local name of this attribute
     */
    public abstract String getLocalName();

    /**
     * The location of this attribute in the FaceletContext
     * 
     * @return the TagAttribute's location
     */
    public abstract Location getLocation();

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
    public abstract MethodExpression getMethodExpression(FaceletContext ctx, Class type,
            Class[] paramTypes);
    
    /**
     * The resolved Namespace for this attribute
     * 
     * @return resolved Namespace
     */
    public abstract String getNamespace();

    /**
     * Delegates to getObject with Object.class as a param
     * 
     * @see #getObject(FaceletContext, Class)
     * @param ctx
     *            FaceletContext to use
     * @return Object representation of this attribute's value
     */
    public abstract Object getObject(FaceletContext ctx);

    /**
     * The qualified name for this attribute
     * 
     * @return the qualified name for this attribute
     */
    public abstract String getQName();

    /**
     * Return the literal value of this attribute
     * 
     * @return literal value
     */
    public abstract String getValue();

    /**
     * If literal, then return our value, otherwise delegate to getObject,
     * passing String.class.
     * 
     * @see #getObject(FaceletContext, Class)
     * @param ctx
     *            FaceletContext to use
     * @return String value of this attribute
     */
    public abstract String getValue(FaceletContext ctx);

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
    public abstract Object getObject(FaceletContext ctx, Class type);

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
    public abstract ValueExpression getValueExpression(FaceletContext ctx, Class type);

    /**
     * If this TagAttribute is literal (not #{..} or ${..})
     * 
     * @return true if this attribute is literal
     */
    public abstract boolean isLiteral();
    
    /**
     * <p class="changed_added_2_2">A reference to the Tag for which this class
     * represents the attributes.  For compatibility with previous implementations,
     * an implementation is provided that returns {@code null}.</p>
     * 
     * @since 2.2
     * 
     * @return the {@link Tag} for which this class represents the attributes.
     */
    
    public Tag getTag() {
        return null;
    }

    /**
     * <p class="changed_added_2_2">Set a reference to the Tag for which
     * this class represents the attributes.  The VDL runtime must
     * ensure that this method is called before any {@link
     * FaceletHandler}s for this element are instantiated. For
     * compatibility with previous implementations, a no-op
     * implementation is provided.</p>
     * 
     * @since 2.2
     * 
     */
    public void setTag(Tag tag) {
        
    }
}
