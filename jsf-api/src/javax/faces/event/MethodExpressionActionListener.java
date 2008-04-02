/*
 * $Id: MethodExpressionActionListener.java,v 1.4 2006/02/22 21:56:51 rlubke Exp $
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

package javax.faces.event;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * <p><strong>MethodExpressionActionListener</strong> is an {@link ActionListener} that
 * wraps a {@link MethodExpression}. When it receives a {@link ActionEvent}, it executes
 * a method on an object identified by the {@link MethodExpression}.</p>
 */

public class MethodExpressionActionListener implements ActionListener,
    StateHolder {

    private static final Logger LOGGER =
          Logger.getLogger("javax.faces.event", "javax.faces.LogStrings");
    

    // ------------------------------------------------------ Instance Variables

    private MethodExpression methodExpression = null;
    private boolean isTransient;

    public MethodExpressionActionListener() { }

    /**
     * <p>Construct a {@link ValueChangeListener} that contains a {@link MethodExpression}.</p>
     */
    public MethodExpressionActionListener(MethodExpression methodExpression) {

        super();
        this.methodExpression = methodExpression;

    }


    // ------------------------------------------------------- Event Method

    /**
     * @throws NullPointerException {@inheritDoc}     
     * @throws AbortProcessingException {@inheritDoc}     
     */
    public void processAction(ActionEvent actionEvent) throws AbortProcessingException {

        if (actionEvent == null) {
            throw new NullPointerException();
        }
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ELContext elContext = context.getELContext();
            methodExpression.invoke(elContext, new Object[] {actionEvent});
        } catch (ELException ee) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           "severe.event.exception_invoking_processaction",
                           new Object[]{
                                 ee.getCause().getClass().getName(),
                                 methodExpression.getExpressionString(),
                                 actionEvent.getComponent().getId()
                           });
                StringWriter writer = new StringWriter(1024);
                ee.getCause().printStackTrace(new PrintWriter(writer));
                LOGGER.severe(writer.toString());
            }
            throw new AbortProcessingException(ee.getMessage(), ee.getCause());
        }
    }


    // ------------------------------------------------ Methods from StateHolder


    public Object saveState(FacesContext context) {

        return new Object[] { methodExpression };

    }


    public void restoreState(FacesContext context, Object state) {

        methodExpression = (MethodExpression) ((Object[]) state)[0];

    }


    public boolean isTransient() {

        return isTransient;

    }


    public void setTransient(boolean newTransientValue) {

        isTransient = newTransientValue;

    }

}
