/*
 * $Id: ValueBindingImpl.java,v 1.35.28.3 2007/04/27 21:27:40 ofung Exp $
 */

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

import javax.faces.application.Application;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.Expression;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.util.Util;

public class ValueBindingImpl extends ValueBinding implements StateHolder {

//
// Private/Protected Constants
//     
    // Array of faces implicit objects
    private static final String[] FACES_IMPLICIT_OBJECTS = {
        "applicationScope",
        "sessionScope",
        "requestScope",
        "facesContext",
        "cookies",
        "header",
        "headerValues",
        "initParam",
        "param",
        "paramValues",
        "view"
    };


    static {
        // Sort for binary searching
        Arrays.sort(FACES_IMPLICIT_OBJECTS);
    }
    
//
// Class Variables
//

    private static final Log log = LogFactory.getLog(ValueBindingImpl.class);

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

    protected String ref;
    protected String exprString;
    protected ExpressionInfo exprInfo = new ExpressionInfo();           
   

//
// Constructors and Initializers    
//
    /**
     * <p>Necessary for {@link StateHolder} contract.</p>
     */
    public ValueBindingImpl() {
    }


    public ValueBindingImpl(Application application) {
        Util.parameterNonNull(application);
                       
        exprInfo.setVariableResolver(application.getVariableResolver());
        exprInfo.setPropertyResolver(application.getPropertyResolver());        
        
    }

//
// Class methods
//

//
// General Methods
//
 
    public void setRef(String ref) {        
        Util.parameterNonEmpty(ref);
        this.ref = ref;
        exprString = getExprString(ref);
    }

   
//
// Methods from ValueBinding
//

    public Object getValue(FacesContext context)
        throws EvaluationException, PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        Object result;

        if (log.isDebugEnabled()) {
            log.debug("getValue(ref=" + ref + ")");
        }
        result = getValue(context, ref);
        if (log.isTraceEnabled()) {
            log.trace("-->Returning " + result);
        }
        return result;
    }


    protected Object getValue(FacesContext context, String toEvaluate)
        throws EvaluationException, PropertyNotFoundException {
        Object result;


        try {
            exprInfo.setExpressionString(toEvaluate);
            exprInfo.setExpectedType(Object.class);
            exprInfo.setFacesContext(context);           
            result = Util.getExpressionEvaluator().evaluate(exprInfo);
            if (log.isDebugEnabled()) {
                log.debug("getValue Result:" + result);
            }
        } catch (Throwable e) {
            if (e instanceof ElException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((ElException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("getValue Evaluation threw exception:", l);
                }                
                throw new EvaluationException(e);
            } else if (e instanceof PropertyNotFoundException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((PropertyNotFoundException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("getValue Evaluation threw exception:", l);
                }
                // Just rethrow it to keep detailed message                
                throw (PropertyNotFoundException) e;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("getValue Evaluation threw exception:", e);
                }               
                throw new EvaluationException(e);
            }
        } 
        
        return result;
    }


    public void setValue(FacesContext context, Object value)
        throws EvaluationException, PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        if (isReservedIdentifier(ref)) {
            throw new ReferenceSyntaxException(
                Util.getExceptionMessageString(
                    Util.ILLEGAL_IDENTIFIER_LVALUE_MODE_ID, new Object[]{ref}));
        }
        // PENDING(edburns): check for readOnly-ness        

        try {
            exprInfo.setExpressionString(ref);
            exprInfo.setFacesContext(context);            
            Expression expr =
                Util.getExpressionEvaluator().parseExpression(exprInfo);
            expr.setValue(exprInfo, value);                       
        } catch (Throwable e) {
            if (e instanceof ElException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((ElException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("setValue Evaluation threw exception:", l);
                }                
                throw new EvaluationException(e);
            } else if (e instanceof PropertyNotFoundException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((PropertyNotFoundException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("setValue Evaluation threw exception:", l);
                }
                // Just rethrow it to keep detailed message                
                throw (PropertyNotFoundException) e;
            } else if (e instanceof EvaluationException) {
                if (log.isDebugEnabled()) {
                    log.debug("setValue Evaluation threw exception:", e);
                }                
                throw ((EvaluationException) e);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("setValue Evaluation threw exception:", e);
                }                
                throw new EvaluationException(e);
            }
        } 
    }


    public boolean isReadOnly(FacesContext context)
        throws PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }

        try {
            exprInfo.setExpressionString(ref);
            exprInfo.setFacesContext(context);            
            Expression expr =
                Util.getExpressionEvaluator().parseExpression(exprInfo);
            return expr.isReadOnly(exprInfo);           
        } catch (Throwable e) {
            if (e instanceof ElException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((ElException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("isReadOnly Evaluation threw exception:", l);
                }
                throw new EvaluationException(e);
            } else if (e instanceof PropertyNotFoundException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((PropertyNotFoundException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("isReadOnly Evaluation threw exception:", l);
                }
                // Just rethrow it to keep detailed message
                throw (PropertyNotFoundException) e;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("isReadOnly Evaluation threw exception:", e);
                }
                throw new EvaluationException(e);
            }
        } 
    }


    public Class getType(FacesContext context)
        throws PropertyNotFoundException {
        if (context == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }

        try {
            exprInfo.setExpressionString(ref);
            exprInfo.setFacesContext(context);            
            Expression expr =
                Util.getExpressionEvaluator().parseExpression(exprInfo);
            return expr.getType(exprInfo);            
        } catch (Throwable e) {
            if (e instanceof ElException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((ElException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("getType Evaluation threw exception:", l);
                }
                throw new EvaluationException(e);
            } else if (e instanceof PropertyNotFoundException) {
                if (log.isDebugEnabled()) {
                    Throwable l = e;
                    Throwable t = ((PropertyNotFoundException) e).getCause();
                    if (t != null) {
                        l = t;
                    }
                    log.debug("getType Evaluation threw exception:", l);
                }
                // Just rethrow it to keep detailed message
                throw (PropertyNotFoundException) e;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("getType Evaluation threw exception:", e);
                }
                throw new EvaluationException(e);
            }
        } 
    }


    public String getExpressionString() {
        return exprString;
    }

    /**
     * <p>Returns true if the profivided identifier is a reserved identifier,
     * otherwise false.</p>
     *
     * @param identifier the identifier to check
     * @return returns true if the profivided identifier is a
     *         reserved identifier, otherwisefalse
     */
    private boolean isReservedIdentifier(String identifier) {
        return (Arrays.binarySearch(FACES_IMPLICIT_OBJECTS, identifier) >= 0);
    }   

    // 
    // methods from StateHolder
    //

    public Object saveState(FacesContext context) {
        return ref;
    }


    public void restoreState(FacesContext context, Object state) {
        ref = state.toString();
        exprString = getExprString(ref);
        Application application = context.getApplication();
        exprInfo.setPropertyResolver(application.getPropertyResolver());
        exprInfo.setVariableResolver(application.getVariableResolver());
    }


    private boolean isTransient = false;


    public boolean isTransient() {
        return isTransient;
    }


    public void setTransient(boolean newTransientValue) {
        isTransient = newTransientValue;
    }

    // helper methods
    
    private static String getExprString(String ref) {
        return "#{" + ref + '}';
    }

} // end of class ValueBindingImpl
