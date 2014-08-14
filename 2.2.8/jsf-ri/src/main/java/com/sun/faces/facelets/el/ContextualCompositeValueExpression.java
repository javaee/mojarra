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

package com.sun.faces.facelets.el;

import com.sun.faces.component.CompositeComponentStackManager;

import javax.el.ValueExpression;
import javax.el.ELContext;
import javax.faces.view.Location;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p>
 * This specialized <code>ValueExpression</code> enables the evaluation of
 * composite component expressions.  Instances of this expression will be
 * created when {@link com.sun.faces.facelets.tag.TagAttributeImpl#getValueExpression(javax.faces.view.facelets.FaceletContext, Class)}
 * is invoked and the expression represents a composite component expression (i.e. #{cc.[properties]}).
 * </p>
 *
 * <p>
 * It's important to note that these <code>ValueExpression</code>s are context
 * sensitive in that they leverage the location in which they were referenced
 * in order to push the proper composite component to the evaluation context
 * prior to evaluating the expression itself.
 * </p>
 *
 * Here's an example:
 *
 * <pre>
 * Using Page test.xhtml
 * ---------------------------------
 *    &lt;ez:comp1 greeting="Hello!" /&gt;
 *
 *
 * comp1.xhtml
 * ---------------------------------
 * &lt;composite:interface&gt;
 *    &lt;composite:attribute name="greeting" type="java.lang.String" required="true" /&gt;
 * &lt;/composite:interface&gt;
 * &lt;composite:implementation&gt;
 *    &lt;ez:nesting&gt;
 *       &lt;h:outputText value="#{cc.attrs.greetings}" /&gt;
 *    &lt;/ez:nesting&gt;
 * &lt;/composite:implementation&gt;
 *
 * nesting.xhtml
 * ---------------------------------
 * &lt;composite:interface /&gt;
 * &lt;composite:implementation&gt;
 *    &lt;composite:insertChildren&gt;
 * &lt;/composite:implementation&gt;
 * </pre>
 *
 * <p>
 * In the above example, there will be two composite components available to
 * the runtime: <code>ez:comp1</code> and <code>ez:nesting</code>.
 * </p>
 *
 * <p>
 * When &lt;h:outputText value="#{cc.attrs.greeting}" /&gt;, prior to attempting
 * to evaluate the expression, the {@link Location} object will be used to
 * find the composite component that 'owns' the template in which
 * the expression was defined in by comparing the path of the Location with the
 * name and library of the {@link javax.faces.application.Resource} instance associated
 * with each composite component.  If a matching composite component is found,
 * it will be made available to the EL by calling {@link CompositeComponentStackManager#push(javax.faces.component.UIComponent)}.
 * </p>
 */
public final class ContextualCompositeValueExpression extends ValueExpression {
    
    private static final long serialVersionUID = -2637560875633456679L;

    private ValueExpression originalVE;
    private Location location;


    // ---------------------------------------------------- Constructors


    /* For serialization purposes */
    public ContextualCompositeValueExpression() { }


    public ContextualCompositeValueExpression(Location location,
                                              ValueExpression originalVE) {

        this.originalVE = originalVE;
        this.location = location;

    }


    // ------------------------------------ Methods from ValueExpression


    public Object getValue(ELContext elContext) {

        FacesContext ctx = (FacesContext) elContext.getContext(FacesContext.class);
        boolean pushed = pushCompositeComponent(ctx);
        try {
            return originalVE.getValue(elContext);
        } finally {
            if (pushed) {
                popCompositeComponent(ctx);
            }
        }

    }

    public void setValue(ELContext elContext, Object o) {

        FacesContext ctx = (FacesContext) elContext.getContext(FacesContext.class);
         boolean pushed = pushCompositeComponent(ctx);
        try {
            originalVE.setValue(elContext, o);
        } finally {
            if (pushed) {
                popCompositeComponent(ctx);
            }
        }

    }

    public boolean isReadOnly(ELContext elContext) {

        FacesContext ctx = (FacesContext) elContext.getContext(FacesContext.class);
        boolean pushed = pushCompositeComponent(ctx);
        try {
            return originalVE.isReadOnly(elContext);
        } finally {
            if (pushed) {
                popCompositeComponent(ctx);
            }
        }

    }

    public Class<?> getType(ELContext elContext) {

        FacesContext ctx = (FacesContext) elContext.getContext(FacesContext.class);
        boolean pushed = pushCompositeComponent(ctx);
        try {
            return originalVE.getType(elContext);
        } finally {
            if (pushed) {
                popCompositeComponent(ctx);
            }
        }

    }

    public Class<?> getExpectedType() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        boolean pushed = pushCompositeComponent(ctx);
        try {
            return originalVE.getExpectedType();
        } finally {
            if (pushed) {
                popCompositeComponent(ctx);
            }
        }

    }


    // --------------------------------------------- Methods from Expression


    public String getExpressionString() {
        return originalVE.getExpressionString();
    }

    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
    public boolean equals(Object o) {
        return originalVE.equals(o);
    }

    public int hashCode() {
        return originalVE.hashCode();
    }

    public boolean isLiteralText() {
        return originalVE.isLiteralText();
    }

    @Override
    public String toString() {
        return originalVE.toString();
    }


    // ------------------------------------------------------ Public Methods


    /**
     * @return the {@link Location} of this <code>ValueExpression</code>
     */
    public Location getLocation() {
        return location;
    }


    // ----------------------------------------------------- Private Methods


    private boolean pushCompositeComponent(FacesContext ctx) {

        CompositeComponentStackManager manager =
              CompositeComponentStackManager.getManager(ctx);
        UIComponent cc = manager.findCompositeComponentUsingLocation(ctx, location);
        return manager.push(cc);

    }


    private void popCompositeComponent(FacesContext ctx) {

        CompositeComponentStackManager manager =
              CompositeComponentStackManager.getManager(ctx);
        manager.pop();

    }


} // END ContextualCompositeValueExpression
