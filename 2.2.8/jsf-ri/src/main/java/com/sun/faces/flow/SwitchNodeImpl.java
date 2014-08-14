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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.flow.SwitchCase;
import javax.faces.flow.SwitchNode;

public class SwitchNodeImpl extends SwitchNode implements Serializable {
    
    private static final long serialVersionUID = -9203493858518714933L;
        
    private final String id;
    private ValueExpression defaultOutcome;
    private CopyOnWriteArrayList<SwitchCase> _cases;
    private List<SwitchCase> cases;

    public SwitchNodeImpl(String id) {
        this.id = id;
        
        this.defaultOutcome = null;
        _cases = new CopyOnWriteArrayList<SwitchCase>();
        cases = Collections.unmodifiableList(_cases);
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
        if (this.defaultOutcome != other.defaultOutcome && (this.defaultOutcome == null || !this.defaultOutcome.equals(other.defaultOutcome))) {
            return false;
        }
        if (this._cases != other._cases && (this._cases == null || !this._cases.equals(other._cases))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 47 * hash + (this.defaultOutcome != null ? this.defaultOutcome.hashCode() : 0);
        hash = 47 * hash + (this._cases != null ? this._cases.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String getId() {
        return id;
    }
        
    @Override
    public List<SwitchCase> getCases() {
        return cases;
    }

    public List<SwitchCase> _getCases() {
        return _cases;
    }

    @Override
    public String getDefaultOutcome(FacesContext context) {
        String result = null;
        
        if (null != defaultOutcome) {
            Object objResult = defaultOutcome.getValue(context.getELContext());
            result = (null != objResult) ? objResult.toString() : null;
        }
        return result;
    }
    
    public void setDefaultOutcome(String defaultOutcome) {
        if (null == defaultOutcome) {
            this.defaultOutcome = null;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory eFactory = context.getApplication().getExpressionFactory();
        this.defaultOutcome = eFactory.createValueExpression(context.getELContext(), 
                defaultOutcome, Object.class);
    }
    
    public void setDefaultOutcome(ValueExpression defaultOutcome) {
        this.defaultOutcome = defaultOutcome;
    }

    
}
