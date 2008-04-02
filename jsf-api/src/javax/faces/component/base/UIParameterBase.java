/*
 * $Id: UIParameterBase.java,v 1.2 2003/07/26 17:54:51 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.UIParameter;


/**
 * <p><strong>UIParameterBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIParameter}.</p>
 */

public class UIParameterBase extends UIOutputBase implements UIParameter {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIParameterBase} instance with default property
     * values.</p>
     */
    public UIParameterBase() {

        super();
        setRendererType(null);

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>The optional parameter name for this parameter.</p>
     */
    private String name = null;


    public String getName() {

        return (this.name);

    }


    public void setName(String name) {

        this.name = name;

    }


}
