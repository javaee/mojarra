/*
 * $Id: ConvertableValueHolderSupport.java,v 1.3 2003/10/16 18:43:28 craigmcc Exp $
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

        Repeater repeater = RepeaterSupport.findParentRepeater(this.component);
        if (repeater != null) {
            if (repeater.getRowIndex() >= 0) {
                return (repeater.isChildValid(this.component));
            } else {
                return (this.valid);
            }
        } else {
            return (this.valid);
        }

    }


    public void setValid(boolean valid) {

        Repeater repeater = RepeaterSupport.findParentRepeater(this.component);
        if (repeater != null) {
            if (repeater.getRowIndex() >= 0) {
                repeater.setChildValid(this.component, valid);
            } else {
                this.valid = valid;
            }
        } else {
            this.valid = valid;
        }

    }


    // ---------------------------------------------------- StateManager Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = UIComponentBase.saveAttachedState(context, converter);

        int rowCount = 0;
        Repeater repeater = RepeaterSupport.findParentRepeater(this.component);
        if ((repeater != null) && (repeater.getRowIndex() >= 0)) {
            rowCount = repeater.getRowCount();
            Object validValues[] = new Object[rowCount];
            for (int i = 0; i < rowCount; i++) {
                repeater.setRowIndex(i);
                validValues[i] =
                    repeater.isChildValid(this.component) ?
                    Boolean.TRUE : Boolean.FALSE;
            }
            values[2] = validValues;
        } else {
            values[2] = this.valid ? Boolean.TRUE : Boolean.FALSE;
        }

        return (values);

    }


    private Object extraStateToRestore = null;

    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        extraStateToRestore = state;

    }


    public void setComponent(UIComponent component) {

        super.setComponent(component);
        if ((extraStateToRestore == null) || (component == null)) {
            return;
        }
        Object values[] = (Object[]) extraStateToRestore;
        converter = (Converter) UIComponentBase.restoreAttachedState
            (FacesContext.getCurrentInstance(), values[1]);
        Repeater repeater = RepeaterSupport.findParentRepeater(component);
        if ((repeater != null) && (repeater.getRowIndex() >= 0)) {
            int rowCount = repeater.getRowCount();
            Object validValues[] = (Object[]) values[2];
            for (int i = 0; i < rowCount; i++) {
                repeater.setRowIndex(i);
                repeater.setChildValid(component,
                                       (((Boolean) validValues[i]).booleanValue()));
            }
        } else {
            valid = ((Boolean) values[2]).booleanValue();
        }
        extraStateToRestore = null;

    }



}
