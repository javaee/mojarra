/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.flow;

import com.sun.faces.facelets.flow.FlowNavigationCase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;
import javax.faces.flow.SwitchNode;

public class SwitchNodeImpl extends SwitchNode {
        
    private final String id;
    private final NavigationCase defaultCase;
    private List<NavigationCase> cases;

    public SwitchNodeImpl(String id, FlowNavigationCase defaultCase) {
        this.id = id;
        FacesContext context = FacesContext.getCurrentInstance();
        
        this.defaultCase = defaultCase;
        cases = Collections.synchronizedList(new ArrayList<NavigationCase>());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SwitchNodeImpl other = (SwitchNodeImpl) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if (this.defaultCase != other.defaultCase && (this.defaultCase == null || !this.defaultCase.equals(other.defaultCase))) {
            return false;
        }
        if (this.cases != other.cases && (this.cases == null || !this.cases.equals(other.cases))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 47 * hash + (this.defaultCase != null ? this.defaultCase.hashCode() : 0);
        hash = 47 * hash + (this.cases != null ? this.cases.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String getId() {
        return id;
    }
        
    @Override
    public List<NavigationCase> getCases() {
        return cases;
    }

    @Override
    public NavigationCase getDefaultCase() {
        return defaultCase;
    }
    
    

    
}
