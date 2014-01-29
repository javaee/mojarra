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

import java.io.IOException;
import java.net.URL;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">Context representative of a single
 * request from a Facelet.  This instance is passed to nearly every
 * method call in this API.</p>
 *
 * @since 2.0
 */
public abstract class FaceletContext extends ELContext {
    
    // The key in the FacesContext attribute map
    // for the FaceletContext instance.
    public static final String FACELET_CONTEXT_KEY = 
            "javax.faces.FACELET_CONTEXT";

    /**
     * <p class="changed_added_2_0">The current FacesContext bound to
     * this "request".  Must not be <code>null</code>.</p>
     * 
     * @since 2.0
     */
    public abstract FacesContext getFacesContext();

    /**
     * <p class="changed_added_2_0">Generate a unique ID for the passed
     * String</p>
     * 
     * @param base the string from which to generate the ID.
     *
     * @since 2.0
     */
    public abstract String generateUniqueId(String base);

    /**
     * <p class="changed_added_2_0">The ExpressionFactory to use within
     * the Facelet this context is executing upon.  Must not be
     * <code>null</code>.</p>
     * 
     * @since 2.0
     */

    public abstract ExpressionFactory getExpressionFactory();

    /**
     * <p class="changed_added_2_0">Set the VariableMapper to use in EL
     * evaluation/creation.</p>
     * 
     * @param varMapper the new <code>VariableMapper</code>
     *
     * @since 2.0
     */
    public abstract void setVariableMapper(VariableMapper varMapper);

    /**
     * <p class="changed_added_2_0">Set the FunctionMapper to use in EL
     * evaluation/creation.</p>
     * 
     * @param fnMapper the new <code>FunctionMapper</code>
     *
     * @since 2.0
     */
    public abstract void setFunctionMapper(FunctionMapper fnMapper);

    /**
     * <p class="changed_added_2_0">Support method which is backed by
     * the current VariableMapper.</p>
     * 
     * @param name the name of the attribute
     * @param value the value of the attribute
     *
     * @since 2.0
     */
    public abstract void setAttribute(String name, Object value);

    /**
     * <p class="changed_added_2_0">Return an attribute set by a
     * previous call to {@link #setAttribute}.  Support method which is
     * backed by the current VariableMapper</p>
     * 
     * @param name the name of the attribute to return.
     * @since 2.0
     */
    public abstract Object getAttribute(String name);

    /**
     * <p class="changed_added_2_0">Include another Facelet defined at
     * some path, relative to the executing context, not the current
     * Facelet (same as include directive in JSP)</p>
     * 
     * @param parent the <code>UIComponent</code> that will be the
     * parent of any components in the included facelet.
     * @param relativePath the path of the resource containing the
     * facelet markup, relative to the current markup

     * @throws IOException if unable to load <code>relativePath</code>

     * @throws FaceletException if unable to parse the markup loaded from <code>relativePath</code>

     * @throws FacesException if unable to create child <code>UIComponent</code> instances

     * @throws ELException if any of the expressions in the markup
     * loaded from <code>relativePath</code> fail
     *
     * @since 2.0
     */
    public abstract void includeFacelet(UIComponent parent, String relativePath)
    throws IOException;

    /**
     * <p class="changed_added_2_0">Include another Facelet defined at
     * some path, absolute to this ClassLoader/OS</p>
     * 
     * @param parent the <code>UIComponent</code> that will be the
     * parent of any components in the included facelet.

     * @param absolutePath the absolute path to the resource containing
     * the facelet markup

     * @throws IOException if unable to load <code>relativePath</code>

     * @throws FaceletException if unable to parse the markup loaded from <code>relativePath</code>

     * @throws FacesException if unable to create child <code>UIComponent</code> instances

     * @throws ELException if any of the expressions in the markup
     * loaded from <code>relativePath</code> fail

     */
    public abstract void includeFacelet(UIComponent parent, URL absolutePath)
    throws IOException;

}
