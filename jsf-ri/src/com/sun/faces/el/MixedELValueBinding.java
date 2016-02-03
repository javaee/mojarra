/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.faces.el;

import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.Expression;
import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;

import java.util.Iterator;
import java.util.List;

/**
 * PENDING (hans) The main reason for extending ValueBindingImpl
 * instead of ValueBinding is that the ManagedBeanFactory class
 * casts to ValueBindingImpl and calls getScope(), see the
 * comment about this method in ValueBindingImpl for details.
 */
public class MixedELValueBinding extends ValueBindingImpl {

    private static final Log log =
        LogFactory.getLog(MixedELValueBinding.class);
   
    private List expressions = null;
    
    public MixedELValueBinding() { }

    public MixedELValueBinding(Application application) {
        super(application);
        exprInfo.setExpectedType(String.class);        
    }


    public void setRef(String ref) {
        Util.parameterNonEmpty(ref);
        this.ref = ref;
        expressions = null;
    }


    public Object getValue(FacesContext context)
        throws EvaluationException, PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        if (expressions == null) {
            MixedELValueParser parser = new MixedELValueParser();
            try {
                expressions = parser.parse(context, ref);
            } catch (ElException e) {
                Throwable t = e;
                Throwable cause = e.getCause();
                if (cause != null) {
                    t = cause;
                }
                if (log.isDebugEnabled()) {
                    log.debug("getValue Evaluation threw exception:", t);
                }
                throw new EvaluationException(t);
            }
        }

        StringBuffer sb = new StringBuffer();
        Iterator i = expressions.iterator();                       
        exprInfo.setFacesContext(context);        
        while (i.hasNext()) {
            Object o = i.next();
            if (o instanceof Expression) {
                try {
                    Object value = ((Expression) o).evaluate(exprInfo);
                    if (value != null) {
                        sb.append(value);
                    } // null values are effectively zero length strings
                } catch (ElException e) {
                    Throwable t = e;
                    Throwable cause = e.getCause();
                    if (cause != null) {
                        t = cause;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("getValue Evaluation threw exception:", t);
                    }
                    throw new EvaluationException(t);
                } 
            } else {
                sb.append(o);
            }
        }
        return sb.toString();
    }


    public void setValue(FacesContext context, Object value)
        throws EvaluationException, PropertyNotFoundException {
        Object[] params = {ref};
        throw new PropertyNotFoundException(Util.getExceptionMessageString(
            Util.ILLEGAL_MODEL_REFERENCE_ID, params));
    }


    public boolean isReadOnly(FacesContext context)
        throws PropertyNotFoundException {
        return true;
    }


    public Class getType(FacesContext context)
        throws PropertyNotFoundException {
        return String.class;
    }


    public String getExpressionString() {
        return ref;
    }


    public String getScope(String valueBinding) {
        return null;
    }

    // StateHolder methods

    public Object saveState(FacesContext context) {
        return ref;
    }


    public void restoreState(FacesContext context, Object state) {
        super.restoreState(context, state);
        exprInfo.setExpectedType(String.class);
    }


    private boolean isTransient = false;


    public boolean isTransient() {
        return isTransient;
    }


    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }
}
