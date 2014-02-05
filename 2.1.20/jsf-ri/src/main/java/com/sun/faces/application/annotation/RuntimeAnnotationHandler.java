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

import javax.faces.context.FacesContext;

/**
 * Implementations of this class provide basic caching and processing of
 * of {@link java.lang.annotation.Annotation} instances.
 */
interface RuntimeAnnotationHandler {

    /**
     * <p>Apply the {@link java.lang.annotation.Annotation}(s). The act
     * of doing so should affect the JSF runtime in some fashion (see the spec
     * for the specific annotation types).</p>
     *
     * <p>
     * <em>NOTE</em>: when adding new types of components that can be annotated,
     * the fact that we expose varargs here should be hidden.  Type-safe methods
     * should be added to {@link AnnotationManager} to clarify the contract.
     * </p>
     *
     * @param ctx the {@link javax.faces.context.FacesContext} for the current
     *  request
     * @param params one or more arguments to the handler instance.  The type
     *  and number may vary.
     */
    public void apply(FacesContext ctx, Object... params);
    
}
