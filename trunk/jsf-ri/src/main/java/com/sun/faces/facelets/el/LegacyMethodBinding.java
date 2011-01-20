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

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import java.io.Serializable;

/**
 * For legacy ActionSources
 * 
 * @author Jacob Hookom
 * @version $Id$
 * @deprecated
 */
public final class LegacyMethodBinding extends
        MethodBinding implements Serializable {

    private static final long serialVersionUID = 1L;

    private final MethodExpression m;

    public LegacyMethodBinding(MethodExpression m) {
        this.m = m;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.MethodBinding#getType(javax.faces.context.FacesContext)
     */
    public Class getType(FacesContext context)
            throws MethodNotFoundException {
        try {
            return m.getMethodInfo(context.getELContext()).getReturnType();
        } catch (javax.el.MethodNotFoundException e) {
            throw new MethodNotFoundException(e.getMessage(), e.getCause());
        } catch (ELException e) {
            throw new EvaluationException(e.getMessage(), e.getCause());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.el.MethodBinding#invoke(javax.faces.context.FacesContext,
     *      java.lang.Object[])
     */
    public Object invoke(FacesContext context, Object[] params)
            throws EvaluationException, MethodNotFoundException {
        try {
            return m.invoke(context.getELContext(), params);
        } catch (javax.el.MethodNotFoundException e) {
            throw new MethodNotFoundException(e.getMessage(), e.getCause());
        } catch (ELException e) {
            throw new EvaluationException(e.getMessage(), e.getCause());
        }
    }

    public String getExpressionString() {
        return m.getExpressionString();
    }
}
