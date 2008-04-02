/*
 * $Id: UICommandBase.java,v 1.9 2003/08/30 00:31:35 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.Action;
import javax.faces.application.Application;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;


/**
 * <p><strong>UICommandBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UICommand}.</p>
 */

public class UICommandBase extends UIComponentBase implements UICommand {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UICommandBase} instance with default property
     * values.</p>
     */
    public UICommandBase() {

        super();
        setRendererType("Button");

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>The literal outcome value.</p>
     */
    private String action = null;


    public String getAction() {

        return (this.action);

    }


    public void setAction(String action) {

        this.action = action;

    }


    /**
     * <p>The action reference.</p>
     */
    private String actionRef = null;


    public String getActionRef() {

        return (this.actionRef);

    }


    public void setActionRef(String actionRef) {

        this.actionRef = actionRef;

    }


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
     * <p>The immediate flag.</p>
     */
    private boolean immediate = false;


    public boolean isImmediate() {

        return (this.immediate);

    }


    public void setImmediate(boolean immediate) {

        this.immediate = immediate;

    }


    /**
     * <p>The local value of this {@link UIComponent} (if any).</p>
     */
    private Object value = null;


    public Object getValue() {

        return (this.value);

    }


    public void setValue(Object value) {

        this.value = value;

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


    // ---------------------------------------------------- ActionSource Methods


    public void addActionListener(ActionListener listener) {

        addFacesListener(listener);

    }


    public void removeActionListener(ActionListener listener) {

        removeFacesListener(listener);

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


    public Object getState(FacesContext context) {

        removeDefaultActionListener(context);

        Object values[] = new Object[7];
        values[0] = super.getState(context);
        values[1] = action;
        values[2] = actionRef;
        List[] converterList = new List[1];
        List theConverter = new ArrayList(1);
        theConverter.add(converter);
        converterList[0] = theConverter;
        values[3] =
            context.getApplication().getViewHandler().getStateManager().
            getAttachedObjectState(context, this, "converter", converterList);
        values[4] = immediate ? Boolean.TRUE : Boolean.FALSE;
        values[5] = value;
        values[6] = valueRef;

        addDefaultActionListener(context);
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        action = (String) values[1];
        actionRef = (String) values[2];
        List[] converterList = (List[])
            context.getApplication().getViewHandler().getStateManager().
            restoreAttachedObjectState(context, values[3]);
        // PENDING(craigmcc) - it shouldn't be this hard to restore converters
	if (converterList != null) {
            List theConverter = converterList[0];
            if ((theConverter != null) && (theConverter.size() > 0)) {
                converter = (Converter) theConverter.get(0);
            }
	}
        immediate = ((Boolean) values[4]).booleanValue();
        value = (String) values[5];
        valueRef = (String) values[6];

        addDefaultActionListener(context);

    }


    // --------------------------------------------------------- Private Methods


    // Add the default action listener
    private void addDefaultActionListener(FacesContext context) {

        ActionListener listener =
            context.getApplication().getActionListener();
        if (immediate) {
            addActionListener(new WrapperActionListener(listener));
        } else {
            addActionListener(listener);
        }

    }


    // Remove the default action listener
    private void removeDefaultActionListener(FacesContext context) {

        if (immediate) {
            if (listeners == null) {
                return;
            }
            List list = listeners[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()];
            if (list == null) {
                return;
            }
            Iterator items = list.iterator();
            while (items.hasNext()) {
                Object item = items.next();
                if (item instanceof WrapperActionListener) {
                    removeActionListener((ActionListener) item);
                    return;
                }
            }
        } else {
            removeActionListener(context.getApplication().getActionListener());
        }

    }



    // ------------------------------------------------ Private Wrapper Listener


    // Wrapper for the default ActionListener that overrides getPhaseId()
    // to return PhaseId.APPLY_REQUEST_VALUES, for "immediate" commands
    private class WrapperActionListener implements ActionListener {

        public WrapperActionListener(ActionListener wrapped) {
            this.wrapped = wrapped;
        }

        private ActionListener wrapped;

        public PhaseId getPhaseId() {
            return (PhaseId.APPLY_REQUEST_VALUES);
        }

        public void processAction(ActionEvent event) {
            wrapped.processAction(event);
        }

    }


}
