/*
 * $Id: UISelectItemBase.java,v 1.11 2003/09/18 00:53:45 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.Application;
import javax.faces.component.Repeater;
import javax.faces.component.RepeaterSupport;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>UISelectItemBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UISelectItem}.</p>
 */

public class UISelectItemBase extends UIComponentBase
    implements UISelectItem, ValueHolder {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectItemBase} instance with default property
     * values.</p>
     */
    public UISelectItemBase() {

        super();
        setRendererType(null);

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>The {@link Converter} (if any)
     * that is registered for this {@link UIComponent}.</p>
     */
    private Converter converter = null;


    public Converter getConverter() {

        return (this.converter);

    }


    public void setConverter(Converter converter) {

        this.converter = converter;

    }


    /**
     * <p>The description for this selection item.</p>
     */
    private String itemDescription = null;


    public String getItemDescription() {

        return (this.itemDescription);

    }


    public void setItemDescription(String itemDescription) {

        this.itemDescription = itemDescription;

    }


    /**
     * <p>The localized label for this selection item.</p>
     */
    private String itemLabel = null;


    public String getItemLabel() {

        return (this.itemLabel);

    }


    public void setItemLabel(String itemLabel) {

        this.itemLabel = itemLabel;

    }


    /**
     * <p>The server value for this selection item.</p>
     */
    private String itemValue = null;


    public String getItemValue() {

        return (this.itemValue);

    }


    public void setItemValue(String itemValue) {

        this.itemValue = itemValue;

    }


    /**
     * <p>The local value of this {@link UIComponent} (if any).</p>
     */
    private Object value = null;


    public Object getValue() {

        Repeater repeater = RepeaterSupport.findParentRepeater(this);
        if (repeater != null) {
            if (repeater.getRowIndex() > 0) {
                return (repeater.getChildValue(this));
            } else {
                return (this.value);
            }
        } else {
            return (this.value);
        }

    }


    public void setValue(Object value) {

        Repeater repeater = RepeaterSupport.findParentRepeater(this);
        if (repeater != null) {
            if (repeater.getRowIndex() > 0) {
                repeater.setChildValue(this, value);
            } else {
                this.value = value;
            }
        } else {
            this.value = value;
        }

    }


    /**
     * <p>The value reference expression for this {@link UIComponent}
     * (if any).</p>
     */
    private String valueRef = null;


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


    public Object saveState(FacesContext context) {

        Object values[] = new Object[7];
        values[0] = super.saveState(context);
        List[] converterList = new List[1];
        List theConverter = new ArrayList(1);
        theConverter.add(converter);
        converterList[0] = theConverter;
        values[1] =
            context.getApplication().getViewHandler().getStateManager().
            getAttachedObjectState(context, this, "converter", converterList);
        values[2] = itemDescription;
        values[3] = itemLabel;
        values[4] = itemValue;
        values[5] = value;
        values[6] = valueRef;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        List[] converterList = (List[])
            context.getApplication().getViewHandler().getStateManager().
            restoreAttachedObjectState(context, values[1], null, this);
        // PENDING(craigmcc) - it shouldn't be this hard to restore converters
	if (converterList != null) {
            List theConverter = converterList[0];
            if ((theConverter != null) && (theConverter.size() > 0)) {
                converter = (Converter) theConverter.get(0);
            }
	}
        itemDescription = (String) values[2];
        itemLabel = (String) values[3];
        itemValue = (String) values[4];
        value = values[5];
        valueRef = (String) values[6];

    }


}
