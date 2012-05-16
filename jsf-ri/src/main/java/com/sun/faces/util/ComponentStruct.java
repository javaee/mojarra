/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

/**
 * Utility class to enable partial state saving of components that have been
 * dynamically added to the view.
 */
public class ComponentStruct implements StateHolder {    
    /**
     * Marker that specifies this is an ADD.
     */
    public static final String ADD = "ADD";
    /**
     * Marker that specifies this is a REMOVE.
     */
    public static final String REMOVE = "REMOVE";

    public String action;
    public String parentClientId;
    public String id;
    public String clientId;
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
        this.action = (String) s[0];
        this.parentClientId = (String) s[1];
        this.clientId = (String) s[2];
        this.id = (String) s[3];
        this.facetName = (String) s[4];
    }

    public Object saveState(FacesContext ctx) {
        if (ctx == null) {
            throw new NullPointerException();
        }
        Object state[] = new Object[5];
        state[0] = this.action;
        state[1] = this.parentClientId;
        state[2] = this.clientId;
        state[3] = this.id;
        state[4] = this.facetName;
        return state;
    }

    public void setTransient(boolean trans) {
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        
        if (obj instanceof ComponentStruct) {
            ComponentStruct struct = (ComponentStruct) obj;
            result = struct.clientId.equals(this.clientId);
        }
        
        return result;
    }

    /**
     * Hash code.
     * 
     * @return the hashcode.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.clientId != null ? this.clientId.hashCode() : 0);
        return hash;
    }


    
} // END ComponentStruct
