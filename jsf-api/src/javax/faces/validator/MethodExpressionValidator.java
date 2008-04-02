/*
 * $Id: MethodExpressionValidator.java,v 1.5 2006/12/15 17:44:44 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.validator;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p><strong>MethodExpressionValidator</strong> is a {@link Validator} that
 * wraps a {@link MethodExpression}, and it performs validation by executing
 * a method on an object identified by the {@link MethodExpression}.</p>
 */

public class MethodExpressionValidator implements Validator, StateHolder {

    // ------------------------------------------------------ Instance Variables

    private MethodExpression methodExpression = null;

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
        if (value != null) {
            try {
                ELContext elContext = context.getELContext();
                methodExpression.invoke(elContext, new Object[]{context, component, value});
            } catch (ELException ee) {
                Throwable e = ee.getCause();
                if (e instanceof ValidatorException) {
                    throw (ValidatorException) e;
                }
                FacesMessage message = new FacesMessage(ee.getMessage());
                throw new ValidatorException(message, ee.getCause());
            }
        }

    }

    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[1];
        values[0] = methodExpression;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

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

}
