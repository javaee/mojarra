/*
 * $Id: LongConverter.java,v 1.2 2003/08/12 17:39:16 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.convert;


import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p>{@link Converter} implementation for <code>java.lang.Long</code>
 * (and long primitive) values.</p>
 */

public class LongConverter implements Converter, StateHolder {


    // ------------------------------------------------------- Converter Methods


    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) throws ConverterException {

        try {
            return (Long.valueOf(value));
        } catch (ConverterException e) {
            throw new ConverterException(e);
        }


    }


    public String getAsString(FacesContext context, UIComponent component,
                              Object value) throws ConverterException {

        try {
            return (Long.toString(((Long) value).longValue()));
        } catch (ConverterException e) {
            throw new ConverterException(e);
        }

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object getState(FacesContext context) {
        return ("");
    }


    public void restoreState(FacesContext context, Object state) {
    }


    private boolean transientFlag = false;


    public boolean isTransient() {
        return (transientFlag);
    }


    public void setTransient(boolean transientFlag) {
        this.transientFlag = transientFlag;
    }


}
