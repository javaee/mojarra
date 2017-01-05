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
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * <p><span class="changed_modified_2_2">The</span> parent or root object
 * in a FaceletHandler composition. The Facelet will take care of
 * populating the passed UIComponent parent in relation to the
 * create/restore lifecycle of JSF.</p>
 * 
 */
public abstract class Facelet {

    /**
     * <p><span class="changed_modified_2_2">The</span> passed
     * UIComponent parent will be populated/restored in accordance with
     * the <span class="changed_modified_2_2">Facelets chapter in the
     * spec prose document.</span></p>
     * 
     * @param facesContext
     *            The current FacesContext (Should be the same as
     *            FacesContext.getInstance())
     * @param parent
     *            The UIComponent to populate in a compositional fashion. In
     *            most cases a Facelet will be base a UIViewRoot.
     * 
     * @throws IOException if unable to load a file necessary to apply this {@code Facelet}

     * @throws FaceletException if unable to parse the markup loaded in applying this {@code Facelet}

     * @throws javax.faces.FacesException if unable to create child <code>UIComponent</code> instances

     * @throws javax.el.ELException if any of the expressions in the markup
     * loaded during the apply fail

     */
    public abstract void apply(FacesContext facesContext, UIComponent parent)
    throws IOException;
}
