/*
 * $Id: UICommandBase.java,v 1.3 2003/07/27 00:48:23 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;


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


    // -------------------------------------------------- Event Listener Methods


    public void addActionListener(ActionListener listener) {

        addFacesListener(listener);

    }


    public void removeActionListener(ActionListener listener) {

        removeFacesListener(listener);

    }


}
