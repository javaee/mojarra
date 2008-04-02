/*
 * $Id: ListenerBean.java,v 1.3 2006/03/29 22:38:52 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.systest.model;

import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeListener;


public class ListenerBean extends Object {


    private ActionListener actionListener = null;

    private ValueChangeListener valueChangeListener = null;

    // ------------------------------------------------------------ Constructors


    public ListenerBean() {
    }

    // ---------------------------------------------------------- Public Methods


    public ActionListener getActionListener() {

        return actionListener;

    }


    public void setActionListener(ActionListener actionListener) {

        this.actionListener = actionListener;

    }


    public ValueChangeListener getValueChangeListener() {

        return valueChangeListener;

    }


    public void setValueChangeListener(
          ValueChangeListener valueChangeListener) {

        this.valueChangeListener = valueChangeListener;

    }

}
