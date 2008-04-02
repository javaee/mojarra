/*
 * $Id: MethodExpressionActionListener.java,v 1.1 2005/05/05 20:51:11 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;

/**
 * <p><strong>MethodExpressionActionListener</strong> is an {@link ActionListener} that
 * wraps a {@link MethodExpression}. When it receives a {@link ActionEvent}, it executes
 * a method on an object identified by the {@link MethodExpression}.</p>
 */

public class MethodExpressionActionListener implements ActionListener,
    StateHolder {


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
     * @exception NullPointerException {@inheritDoc}     
     * @exception AbortProcessingException {@inheritDoc}     
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
