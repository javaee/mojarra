/*
 * $Id: UICommandBase.java,v 1.6 2003/08/26 21:50:04 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.io.IOException;


/**
 * <p><strong>UICommandBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UICommand}.</p>
 */

public class UICommandBase extends UIOutputBase implements UICommand {


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
     * <p>The immediate flag.</p>
     */
    // PENDING(craigmcc) - not saved as part of the state yet,
    // will be picked up when that mechanism is remodelled
    private boolean immediate = false;


    public boolean isImmediate() {

        return (this.immediate);

    }


    public void setImmediate(boolean immediate) {

        this.immediate = immediate;

    }


    // -------------------------------------------------- Event Listener Methods


    public void addActionListener(ActionListener listener) {

        addFacesListener(listener);

    }


    public void removeActionListener(ActionListener listener) {

        removeFacesListener(listener);

    }

    // ---------------------------------------------- methods from StateHolder

    public void restoreState(FacesContext context, 
			     Object stateObj) throws IOException {
	Object [] state = (Object []) stateObj;
	Object [] thisState = (Object []) state[THIS_INDEX];

	// restore the attributes
	String stateStr = (String) thisState[ATTRS_INDEX];
	int i = stateStr.indexOf(STATE_SEP);
	action = stateStr.substring(0, i);
	if (action.equals("null")) {
	    action = null;
	}
	actionRef = stateStr.substring(i + STATE_SEP_LEN);
	if (actionRef.equals("null")) {
	    actionRef = null;
	}
	// restore the listeners
	listeners = context.getApplication().getViewHandler().getStateManager()
	    .restoreAttachedObjectState(context, thisState[LISTENERS_INDEX]);
	// add the default action listener
	addActionListener(context.getApplication().getActionListener());
	
	super.restoreState(context, state[SUPER_INDEX]);
    }

    private static final int ATTRS_INDEX = 0;
    private static final int LISTENERS_INDEX = 1;

    public Object getState(FacesContext context) {
	// get the state of our superclasses.
	Object superState = super.getState(context);
	Object [] result = new Object[2];
	Object [] thisState = new Object[2];
	// save the attributes
	thisState[ATTRS_INDEX] = action + STATE_SEP + actionRef;
	// save the listeners

	// remove the default action listener
	removeActionListener(context.getApplication().getActionListener());
	
	thisState[LISTENERS_INDEX] = context.getApplication().
	    getViewHandler().getStateManager().getAttachedObjectState(context,
								      this,
								      null,
								      listeners);
	// re-add the default action listener
	addActionListener(context.getApplication().getActionListener());

	result[THIS_INDEX] = thisState;
	// save the state of our superclass
	result[SUPER_INDEX] = superState;
	return result;
    }




}
