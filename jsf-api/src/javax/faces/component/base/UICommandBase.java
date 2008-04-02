/*
 * $Id: UICommandBase.java,v 1.16 2003/09/19 00:57:07 craigmcc Exp $
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
import javax.faces.component.Repeater;
import javax.faces.component.RepeaterSupport;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolderSupport;
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
        // add the default action listener
        FacesContext context = FacesContext.getCurrentInstance();
	addDefaultActionListener(context);
    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link ValueHolderSupport} instance to which we delegate
     * our {@link ValueHolder} implementation processing.</p>
     */
    private ValueHolderSupport support = new ValueHolderSupport(this);



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


    public Converter getConverter() {

        return (support.getConverter());

    }


    public void setConverter(Converter converter) {

        support.setConverter(converter);

    }


    /**
     * <p>The immediate flag.</p>
     */
    private boolean immediate = false;


    public boolean isImmediate() {

        return (this.immediate);

    }


    public void setImmediate(boolean immediate) {
	// if the immediate value is changing.
	if (immediate != this.immediate) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    // remove the current default action listener
	    removeDefaultActionListener(context);
	    this.immediate = immediate;
	    addDefaultActionListener(context);
	}
    }


    public Object getValue() {

        return (support.getValue());

    }


    public void setValue(Object value) {

        support.setValue(value);

    }


    public String getValueRef() {

        return (support.getValueRef());

    }


    public void setValueRef(String valueRef) {

        support.setValueRef(valueRef);

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

        return (support.currentValue(context));

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        removeDefaultActionListener(context);

        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        List[] supportList = new List[1];
        List theSupport = new ArrayList(1);
        theSupport.add(support);
        supportList[0] = theSupport;
        values[1] =
            context.getApplication().getViewHandler().getStateManager().
            getAttachedObjectState(context, this, "support", supportList);
        values[2] = action;
        values[3] = actionRef;
        values[4] = immediate ? Boolean.TRUE : Boolean.FALSE;

        addDefaultActionListener(context);
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {
        removeDefaultActionListener(context);

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        List[] supportList = (List[])
            context.getApplication().getViewHandler().getStateManager().
            restoreAttachedObjectState(context, values[1], null, this);
	if (supportList != null) {
            List theSupport = supportList[0];
            if ((theSupport != null) && (theSupport.size() > 0)) {
                support = (ValueHolderSupport) theSupport.get(0);
            }
	}
        action = (String) values[2];
        actionRef = (String) values[3];
        immediate = ((Boolean) values[4]).booleanValue();

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
