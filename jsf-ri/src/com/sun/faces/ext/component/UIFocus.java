/*
 * $Id: UIFocus.java,v 1.1.2.1 2008/04/10 16:56:49 driscoll Exp $ 
 */

package com.sun.faces.ext.component;

import javax.faces.component.UIComponentBase;

/**
 * Component to set a focus on a given field.
 * 
 * @author driscoll
 */
public class UIFocus extends UIComponentBase {
    
    private static final String FAMILY = "FocusFamily";
            
    @Override
    public String getFamily() {
        return FAMILY;
    }
}  