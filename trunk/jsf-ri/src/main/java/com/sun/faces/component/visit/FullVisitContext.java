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

package com.sun.faces.component.visit;

import javax.faces.component.visit.*;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * <p class="changed_added_2_0">A VisitContext implementation that is 
 * used when performing a full component tree visit.</p>
 *
 * RELEASE_PENDING
 *
 * @since 2.0
 */
public class FullVisitContext extends VisitContext {

    /**
     * Creates a FullVisitorContext instance.
     * @param facesContext the FacesContext for the current request
     * @throws NullPointerException  if {@code facesContext}
     *                               is {@code null}
     */    
    public FullVisitContext(FacesContext facesContext) {
        this(facesContext, null);
    }

    /**
     * Creates a FullVisitorContext instance with the specified
     * hints.
     *
     * @param facesContext the FacesContext for the current request
     * @param hints a the VisitHints for this visit
     * @throws NullPointerException  if {@code facesContext}
     *                               is {@code null}
     */    
    public FullVisitContext(FacesContext facesContext,
                            Set<VisitHint> hints) {

        if (facesContext == null) {
            throw new NullPointerException();
        }

        this.facesContext = facesContext;

        // Copy and store hints - ensure unmodifiable and non-empty
        EnumSet<VisitHint> hintsEnumSet = ((hints == null) || (hints.isEmpty()))
                                          ? EnumSet.noneOf(VisitHint.class)
                                          : EnumSet.copyOf(hints);

        this.hints = Collections.unmodifiableSet(hintsEnumSet);
    }

    /**
     * @see VisitContext#getFacesContext VisitContext.getFacesContext()
     */
    @Override
    public FacesContext getFacesContext() {
        return facesContext;
    }

    /**
     * @see VisitContext#getIdsToVisit VisitContext.getIdsToVisit()
     */
    @Override
    public Collection<String> getIdsToVisit() {

        // We always visits all ids
        return ALL_IDS;
    }

    /**
     * @see VisitContext#getSubtreeIdsToVisit VisitContext.getSubtreeIdsToVisit()
     */
    @Override
    public Collection<String> getSubtreeIdsToVisit(UIComponent component) {

        // Make sure component is a NamingContainer
        if (!(component instanceof NamingContainer)) {
            throw new IllegalArgumentException("Component is not a NamingContainer: " + component);
        }

        // We always visits all ids
        return ALL_IDS;
    }

    /**
     * @see VisitContext#getHints VisitContext.getHints
     */
    @Override
    public Set<VisitHint> getHints() {
        return hints;
    }

    /**
     * @see VisitContext#invokeVisitCallback VisitContext.invokeVisitCallback()
     */
    @Override
    public VisitResult invokeVisitCallback(UIComponent component, 
                                           VisitCallback callback) {

        // Nothing interesting here - just invoke the callback.
        // (PartialVisitContext.invokeVisitCallback() does all of the 
        // interesting work.)
        return callback.visit(this, component);
    }

    // The FacesContext for this request
    private FacesContext facesContext;

    // Our visit hints
    private Set<VisitHint> hints;
}
