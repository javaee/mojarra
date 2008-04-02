/*
 * $Id: UISelectBooleanBase.java,v 1.2 2003/07/26 17:54:51 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.UISelectBoolean;


/**
 * <p><strong>UISelectBooleanBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UISelectBoolean}.</p>
 */

public class UISelectBooleanBase
    extends UIInputBase implements UISelectBoolean {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectBooleanBase} instance with default
     * property values.</p>
     */
    public UISelectBooleanBase() {

        super();
        setRendererType("Checkbox");

    }


    // -------------------------------------------------------------- Properties


    public boolean isSelected() {

        Boolean value = (Boolean) getValue();
        if (value != null) {
            return (value.booleanValue());
        } else {
            return (false);
        }

    }


    public void setSelected(boolean selected) {

        if (selected) {
            setValue(Boolean.TRUE);
        } else {
            setValue(Boolean.FALSE);
        }

    }


}
