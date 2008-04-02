/*
 * $Id: UIGraphicBase.java,v 1.2 2003/07/26 17:54:49 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.UIGraphic;


/**
 * <p><strong>UIGraphicBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIGraphic}.</p>
 */

public class UIGraphicBase extends UIOutputBase implements UIGraphic {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIGraphicBase} instance with default property
     * values.</p>
     */
    public UIGraphicBase() {

        super();
        setRendererType("Image");

    }


    // -------------------------------------------------------------- Properties


    public String getURL() {

        return ((String) getValue());

    }


    public void setURL(String url) {

        setValue(url);

    }


}
