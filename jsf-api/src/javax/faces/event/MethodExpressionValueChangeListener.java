/*
 * $Id: MethodExpressionValueChangeListener.java,v 1.1 2005/05/05 20:51:11 edburns Exp $
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
 * <p><strong>MethodExpressionValueChangeListener</strong> is a {@link ValueChangeListener} that 
 * wraps a {@link MethodExpression}. When it receives a {@link ValueChangeEvent}, it executes
 * a method on an object identified by the {@link MethodExpression}.</p>
 */

public class MethodExpressionValueChangeListener implements ValueChangeListener,
    StateHolder {


    // ------------------------------------------------------ Instance Variables
    
    private MethodExpression methodExpression = null;
    private boolean isTransient;

    public MethodExpressionValueChangeListener() { }

   /**
     * <p>Construct a {@link ValueChangeListener} that contains a {@link MethodExpression}.</p>
     */
    public MethodExpressionValueChangeListener(MethodExpression methodExpression) {

        super();
        this.methodExpression = methodExpression;

    }


    // ------------------------------------------------------- Event Method

    /**
     * @exception NullPointerException {@inheritDoc}     
     * @exception AbortProcessingException {@inheritDoc}     
     */ 
    public void processValueChange(ValueChangeEvent valueChangeEvent) throws AbortProcessingException {
                         
        if (valueChangeEvent == null) {
            throw new NullPointerException();
        }
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ELContext elContext = context.getELContext();
            methodExpression.invoke(elContext, new Object[] {valueChangeEvent});
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
