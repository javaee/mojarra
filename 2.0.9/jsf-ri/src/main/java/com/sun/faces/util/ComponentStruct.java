/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.util;

import java.util.Map;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Utility class to enable partial state saving of components that have been
 * dynamically added to the view.
 */
public class ComponentStruct implements StateHolder {

    public String parentClientId;
    public String clientId;
    public int indexOfChildInParent = -1;
    public String facetName;

    public boolean isTransient() {
        return false;
    }

    public void restoreState(FacesContext ctx, Object state) {
        if (ctx == null) {
            throw new NullPointerException();
        }
        if (state == null) {
            return;
        }
        Object s[] = (Object[]) state;
        this.parentClientId = s[0].toString();
        this.clientId = s[1].toString();
        this.indexOfChildInParent = (Integer) s[2];
        this.facetName = (String) s[3];
    }

    public Object saveState(FacesContext ctx) {
        if (ctx == null) {
            throw new NullPointerException();
        }
        Object state[] = new Object[4];
        state[0] = this.parentClientId;
        state[1] = this.clientId;
        state[2] = this.indexOfChildInParent;
        state[3] = this.facetName;
        return state;
    }

    public void setTransient(boolean trans) {
    }

    public void absorbComponent(FacesContext context, UIComponent added) {
        UIComponent parent = added.getParent();
        this.clientId = added.getClientId(context);
        this.parentClientId = parent.getClientId(context);
        // this needs work
        int idx = parent.getChildren().indexOf(added);
        if (idx == -1) {
            // this must be a facet
            for (Map.Entry<String, UIComponent> facet : parent.getFacets().entrySet()) {
                if (facet.getValue() == added) {
                    this.facetName = facet.getKey();
                    break;
                }
            }
        } else {
            this.indexOfChildInParent = parent.getChildren().indexOf(added);
        }

    }


} // END ComponentStruct
