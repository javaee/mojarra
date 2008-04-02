/*
 * $Id: ConvertableValueHolderSupport.java,v 1.4 2003/10/21 05:37:43 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.Application;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>ConvertableValueHolderSupport</strong> is a utility class that
 * may be utilized by {@link UIComponent}s that implement
 * {@link ConvertableValueHolder} to delegate value holder management methods.
 * </p>
 *
 * <p>Typical use in a {@link UIComponent} implementation class would be:</p>
 * <pre>
 *   public class MyComponent extends UIComponentBase
 *     implements ConvertableValueHolder {
 *
 *     private ConvertableValueHolderSupport support =
 *       new ConvertableValueHolderSupport();
 *
 *     public Object getValue() {
 *       return (support.getValue());
 *     }
 *
 *     public void setValue(Object value) {
 *       support.setValue(value);
 *     }
 *
 *     ... and so on ...
 *
 * </pre>
 *
 * <p>{@link ConvertableValueHolderSupport} implements StateHolder,
 * and you will want to make sure that the <code>support</code>
 * instance variable gets saved and restored as part of the state
 * of your component class.</p>
 */

public class ConvertableValueHolderSupport extends ValueHolderSupport {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ConvertableValueHolderSupport} instance not
     * associated with any {@link UIComponent}.</p>
     */
    public ConvertableValueHolderSupport() {

        this(null);

    }


    /**
     * <p>Construct a new {@link ConvertableValueHolderSupport} instance
     * associated with the specified {@link UIComponent}.</p>
     *
     * @param component {@link UIComponent} with which we are associated
     */
    public ConvertableValueHolderSupport(UIComponent component) {

        super(component);

    }


    // ------------------------------------------------------ Instance Variables


    private Converter converter = null;
    private boolean valid = true;


    // -------------------------------------------------------------- Properties


    public Converter getConverter() {

        return (this.converter);

    }


    public void setConverter(Converter converter) {

        this.converter = converter;

    }


    public boolean isValid() {

        return (this.valid);

    }


    public void setValid(boolean valid) {

        this.valid = valid;

    }


    // ---------------------------------------------------- StateManager Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = UIComponentBase.saveAttachedState(context, converter);
        values[2] = this.valid ? Boolean.TRUE : Boolean.FALSE;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        converter = (Converter) UIComponentBase.restoreAttachedState
            (FacesContext.getCurrentInstance(), values[1]);
        valid = ((Boolean) values[2]).booleanValue();

    }


    /** @deprecated */
    public void setComponent(UIComponent component) {

        super.setComponent(component);

    }



}
