/*
 * $Id: UIFormBase.java,v 1.2 2003/07/26 17:54:49 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.UIForm;


/**
 * <p><strong>UIFormBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIForm}.</p>
 */

public class UIFormBase extends UIComponentBase implements UIForm {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIFormBase} instance with default property
     * values.</p>
     */
    public UIFormBase() {

        super();
        setRendererType("Form");

    }


}
