/*
 * $Id: ValueHolderSupport.java,v 1.3 2003/09/22 19:03:34 eburns Exp $
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
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>ValueHolderSupport</strong> is a utility class that may be
 * utilized by {@link UIComponent}s that implement {@link ValueHolder} to
 * delegate value holder management methods.</p>
 *
 * <p>Typical use in a {@link UIComponent} implementation class would be:</p>
 * <pre>
 *   public class MyComponent extends UIComponentBase
 *     implements ValueHolder {
 *
 *     private ValueHolderSupport support = new ValueHolderSupport();
 *
 *     public Converter getConverter() {
 *       return (support.getConverter());
 *     }
 *
 *     public void setConverter(Converter converter) {
 *       support.setConverter(converter);
 *     }
 *
 *     ... and so on ...
 *
 * </pre>
 *
 * <p>{@link ValueHolderSupport} implements StateHolder, and you will want
 * to make sure that the <code>support</code> instance variable gets saved
 * and restored as part of the state of your component class.</p>
 */

public class ValueHolderSupport
    implements StateHolder, ValueHolder {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ValueHolderSupport} instance not associated
     * with any {@link UIComponent}.</p>
     */
    public ValueHolderSupport() {

        this(null);

    }


    /**
     * <p>Construct a new {@link ValueHolderSupport} instance associated with
     * the specified {@link UIComponent}.</p>
     *
     * @param component {@link UIComponent} with which we are associated
     */
    public ValueHolderSupport(UIComponent component) {

        this.component = component;

    }


    // ------------------------------------------------------ Instance Variables


    private UIComponent component = null;
    private Converter converter = null;
    private Object value = null;
    private String valueRef = null;


    // -------------------------------------------------------------- Properties



    public Converter getConverter() {

        return (this.converter);

    }


    public void setConverter(Converter converter) {

        this.converter = converter;

    }


    public Object getValue() {

        Repeater repeater = RepeaterSupport.findParentRepeater(component);
        if (repeater != null) {
            if (repeater.getRowIndex() > 0) {
                return (repeater.getChildValue(component));
            } else {
                return (this.value);
            }
        } else {
            return (this.value);
        }

    }


    public void setValue(Object value) {

        Repeater repeater = RepeaterSupport.findParentRepeater(component);
        if (repeater != null) {
            if (repeater.getRowIndex() > 0) {
                repeater.setChildValue(component, value);
            } else {
                this.value = value;
            }
        } else {
            this.value = value;
        }

    }


    public String getValueRef() {

        return (this.valueRef);

    }


    public void setValueRef(String valueRef) {

        this.valueRef = valueRef;

    }


    // ----------------------------------------------------- ValueHolder Methods


    public Object currentValue(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        Object value = getValue();
        if (value != null) {
            return (value);
        }
        String valueRef = getValueRef();
        if (valueRef != null) {
            Application application = context.getApplication();
            ValueBinding binding = application.getValueBinding(valueRef);
            return (binding.getValue(context));
        }
        return (null);

    }


    // ----------------------------------------------------- StateHolder Methods


    private boolean transientFlag = false;


    public boolean isTransient() {

        return (this.transientFlag);

    }


    public void setTransient(boolean transientFlag) {

        this.transientFlag = transientFlag;

    }


    public Object saveState(FacesContext context) {

        // NOTE:  The associated component is not stored as part of the state,
        // because it will be a different object instance on restoration

        Object values[] = new Object[3];
        List[] converterList = new List[1];
        List theConverter = new ArrayList(1);
        theConverter.add(converter);
        converterList[0] = theConverter;
        values[0] =
            context.getApplication().getViewHandler().getStateManager().
            getAttachedObjectState(context, component,
                                   "converter", converterList);
        values[1] = value;
        values[2] = valueRef;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {
        // Restore other state information from saved state
        Object values[] = (Object[]) state;
        List[] converterList = (List[])
            context.getApplication().getViewHandler().getStateManager().
            restoreAttachedObjectState(context, values[0], null, component);
        // PENDING(craigmcc) - it shouldn't be this hard to restore converters
	if (converterList != null) {
            List theConverter = converterList[0];
            if ((theConverter != null) && (theConverter.size() > 0)) {
                converter = (Converter) theConverter.get(0);
            }
	}
        value = values[1];
        valueRef = (String) values[2];

    }

    public void setComponent(UIComponent yourComponent) {
        // Restore component reference from parameter
        this.component = yourComponent;
    }




}
