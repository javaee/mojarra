/*
 * $Id: UIForm.java,v 1.1 2002/05/14 00:41:37 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;


/**
 * <p><strong>UIForm</strong> is a {@link UIComponent} that manages the
 * layout of its child components.</p>
 *
 * <p><strong>FIXME</strong> - Does a form create a "form event" for the
 * application?  A command event?  Does it need a form name to select
 * the corresponding handler?</p>
 */

public class UIForm extends UIComponent {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "Form";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>If this command has been selected, record it so that the application
     * will be notified that this command should be processed.</p>
     *
     * <p><strong>FIXME</strong> - How should the generic component class
     * recognize that it has been submitted?</p>
     *
     * <p><strong>FIXME</strong> - How are form events propogated
     * to the application?</p>
     *
     * @param context FacesContext for the current request being processed
     */
    public void applyRequestValues(FacesContext context) {

        ; // FIXME - provide default implementation

    }


}
