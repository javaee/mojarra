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

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 * <p>
 * This <code>Renderer</code> is responsible for rendering the children
 * defined within the composite implementation section of a composite component
 * template.
 * </p>
 */
public class CompositeRenderer extends Renderer {


    // --------------------------------------------------- Methods from Renderer


    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {

        Util.notNull("context", context);
        Util.notNull("component", component);

        Map<String,UIComponent> facets = component.getFacets();
        UIComponent compositeRoot = facets.get(UIComponent.COMPOSITE_FACET_NAME);
        if (null == compositeRoot) {
            throw new IOException("PENDING_I18N: Unable to find composite " + 
                    " component root for composite component with id " + 
                    component.getId() + " and class " + 
                    component.getClass().getName());
        }
        compositeRoot.encodeAll(context);

    }


    @Override
    public boolean getRendersChildren() {
        return true;
    }


}
