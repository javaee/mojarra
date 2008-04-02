/*
 * $Id: UIFormBase.java,v 1.3 2003/07/28 22:18:46 eburns Exp $
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

public class UIFormBase extends UINamingContainerBase implements UIForm {


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
