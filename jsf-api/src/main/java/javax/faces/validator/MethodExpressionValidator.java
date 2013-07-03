/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

package javax.faces.validator;

import java.util.Map;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * <p><strong
 * class="changed_modified_2_0_rev_a">MethodExpressionValidator</strong>
 * is a {@link Validator} that wraps a {@link MethodExpression}, and it
 * performs validation by executing a method on an object identified by
 * the {@link MethodExpression}.</p>
 */

public class MethodExpressionValidator implements Validator, StateHolder {

    private static final String BEANS_VALIDATION_AVAILABLE =
            "javax.faces.private.BEANS_VALIDATION_AVAILABLE";
    
    private static final String VALIDATE_EMPTY_FIELDS_PARAM_NAME = 
            "javax.faces.VALIDATE_EMPTY_FIELDS";
    
    // ------------------------------------------------------ Instance Variables

    private MethodExpression methodExpression = null;

    private Boolean validateEmptyFields;
    
    public MethodExpressionValidator() {

        super();

    }

    /**
     * <p>Construct a {@link Validator} that contains a {@link MethodExpression}.</p>
     */
    public MethodExpressionValidator(MethodExpression methodExpression) {

        super();
        this.methodExpression = methodExpression;

    }

    // ------------------------------------------------------- Validator Methods

    /**
     * @throws NullPointerException {@inheritDoc}
     * @throws ValidatorException   {@inheritDoc}
     */
    public void validate(FacesContext context,
                         UIComponent component,
                         Object value) throws ValidatorException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        if (validateEmptyFields(context) || value != null) {
            try {
                ELContext elContext = context.getELContext();
                methodExpression.invoke(elContext, new Object[]{context, component, value});
            } catch (ELException ee) {
                Throwable e = ee.getCause();
                if (e instanceof ValidatorException) {
                    throw (ValidatorException) e;
                } else {
                    throw ee;
                }
            }
        }
    }

    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        Object values[] = new Object[1];
        values[0] = methodExpression;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (state == null) {
            return;
        }
        Object values[] = (Object[]) state;
        methodExpression = (MethodExpression) values[0];
    }


    private boolean transientValue = false;


    public boolean isTransient() {

        return (this.transientValue);

    }


    public void setTransient(boolean transientValue) {

        this.transientValue = transientValue;

    }

    private boolean validateEmptyFields(FacesContext ctx) {

        if (validateEmptyFields == null) {
            ExternalContext extCtx = ctx.getExternalContext();
            String val = extCtx.getInitParameter(VALIDATE_EMPTY_FIELDS_PARAM_NAME);

            if (null == val) {
                val = (String) extCtx.getApplicationMap().get(VALIDATE_EMPTY_FIELDS_PARAM_NAME);
            }
            if (val == null || "auto".equals(val)) {
                validateEmptyFields = isBeansValidationAvailable(ctx);
            } else {
                validateEmptyFields = Boolean.valueOf(val);
            }
        }

        return validateEmptyFields;

    }

    private boolean isBeansValidationAvailable(FacesContext context) {
        boolean result = false;

        Map<String,Object> appMap = context.getExternalContext().getApplicationMap();
        
        if (appMap.containsKey(BEANS_VALIDATION_AVAILABLE)) {
            result = (Boolean) appMap.get(BEANS_VALIDATION_AVAILABLE);
        } else {
            try {
                new BeanValidator();
                appMap.put(BEANS_VALIDATION_AVAILABLE, Boolean.TRUE);
                result = true;
            } catch (Throwable t) {
                appMap.put(BEANS_VALIDATION_AVAILABLE, Boolean.FALSE);
            }
        }

        return result;
    }
}
