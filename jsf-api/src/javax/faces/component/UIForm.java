/*
 * $Id: UIForm.java,v 1.29 2003/09/22 16:08:42 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * <p><strong>UIForm</strong> is a {@link UIComponent} that represents an
 * input form to be presented to the user, and whose child components represent
 * (among other things) the input fields to be included when the form is
 * submitted.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>Form</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public interface UIForm extends UIComponent, NamingContainer {

    /**
     * <p>Returns the current value of the <code>submitted</code>
     * property.  The default value is <code>false</code>.  See {@link
     * #setSubmitted} for details.</p>
     *
     */

    public boolean isSubmitted();

    /**
     * <p>If this <code>UIForm</code> instance is experiencing a submit
     * during this request processing lifecycle, this method must be
     * called, passing <code>true</code>, during the {@link
     * UIComponent#decode} for this <code>UIForm</code> instance.</p>
     *
     * <p>The standard implementation delegates the responsibility for
     * calling this method to the {@link javax.faces.render.Renderer}
     * for this <code>UIForm</code> instance.  The standard form
     * renderer includes a hidden field in the form, named by the
     * <code>clientId</code> of this <code>UIForm</code> instance.  The
     * renderer checks for the presence of a request parameter named the
     * by the <code>clientId</code> of this <code>UIForm</code>
     * instance.  If present, it calls <code>setSubmitted(true)</code>.
     * If not present, it may call <code>setSubmitted(false)</code>.</p>
     *
     * <p>The value of a <code>UIForm</code>'s submitted property must
     * not be saved as part of its state.</p>
     */

    public void setSubmitted(boolean submitted);

    /**
     * <p>Override {@link UIComponent#processDecodes} to ensure that the
     * form is decoded <strong>before</strong> its children.  This is
     * necessary to allow the <code>submitted</code> property to be
     * correctly set.</p>
     *
     */

    public void processDecodes(FacesContext context) throws IOException;

    /**
     * <p>Override {@link UIComponent#processValidators} to ensure that
     * the children of this <code>UIForm</code> instance are only
     * processed if {@link #isSubmitted} returns <code>true</code>.</p>
     */

    public void processValidators(FacesContext context);

    /**
     * <p>Override {@link UIComponent#processUpdates} to ensure that the
     * children of this <code>UIForm</code> instance are only processed
     * if {@link #isSubmitted} returns <code>true</code>.</p>
     */

    public void processUpdates(FacesContext context);

    /*
     * <p>Override {@link UIComponent.saveState} to call
     * <code>setSubmitted(false)</code>.</p>
     */

    public Object saveState(FacesContext context);

}
