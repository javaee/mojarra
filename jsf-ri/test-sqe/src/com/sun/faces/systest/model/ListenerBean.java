/*
 * $Id: ListenerBean.java,v 1.1 2005/07/25 18:34:34 rajprem Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;


public class ListenerBean extends Object {

    public ListenerBean() {
    }

    private ActionListener actionListener = null;
    public ActionListener getActionListener() {
        return actionListener;
    }
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private ValueChangeListener valueChangeListener = null;
    public ValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }
    public void setValueChangeListener(ValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}
