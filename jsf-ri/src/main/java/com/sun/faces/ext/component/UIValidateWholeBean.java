/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
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
 */

package com.sun.faces.ext.component;

import com.sun.faces.util.copier.CloneCopier;
import com.sun.faces.util.copier.CopyCtorCopier;
import com.sun.faces.util.copier.NewInstanceCopier;
import com.sun.faces.util.copier.SerializationCopier;
import java.io.Serializable;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class UIValidateWholeBean extends UIComponentBase {
    
    public static final String FAMILY = "com.sun.faces.ext.validateWholeBean";
            
    @Override
    public String getFamily() {
        return FAMILY;
    }

    @Override
    public void processValidators(FacesContext context) {
        ValueExpression beanVE = this.getValueExpression("bean");
        ValueExpression propertiesVE = this.getValueExpression("properties");
        if (null == beanVE || null == propertiesVE) {
            throw new FacesException("Both the \"bean\" and the \"properties\" attributes are required");
        }
        Object valCopy = null;
        System.out.println(beanVE.getExpressionString());
        Object val = beanVE.getValue(context.getELContext());
        System.out.println(val);
        
        if (val instanceof Cloneable) {
            try {
                CloneCopier cc = new CloneCopier();
                valCopy = cc.copy(val);
            } catch (IllegalStateException ise) { }
        } else if (val instanceof Serializable) {
            try {
                SerializationCopier sc = new SerializationCopier();
                valCopy = sc.copy(val);
            } catch (IllegalStateException ise) { }
        } else {
            try {
                CopyCtorCopier ccc = new CopyCtorCopier();
                valCopy = ccc.copy(val);
            } catch (IllegalStateException ise) {
                try {
                    NewInstanceCopier nic = new NewInstanceCopier();
                    valCopy = nic.copy(val);
                } catch (IllegalStateException ise2) { }
            }
        }
        if (null == valCopy) {
            throw new FacesException("Unable to copy value from " + beanVE.getExpressionString());
        }
        
        String propertiesValue = (String) propertiesVE.getValue(context.getELContext());
        
        
    }
    
    
    
}  
