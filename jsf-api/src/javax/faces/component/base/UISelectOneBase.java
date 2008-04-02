/*
 * $Id: UISelectOneBase.java,v 1.2 2003/07/26 17:54:53 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.UISelectOne;


/**
 * <p><strong>UISelectOneBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UISelectOne}.</p>
 */

public class UISelectOneBase extends UIInputBase implements UISelectOne {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectOneBase} instance with default property
     * values.</p>
     */
    public UISelectOneBase() {

        super();
        setRendererType("Menu");

    }


    // -------------------------------------------------------------- Properties


    public Object getSelectedValue() {

        return (getValue());

    }


    public void setSelectedValue(Object selectedValue) {

        setValue(selectedValue);

    }


}
