/*
 * $Id: UIForm.java,v 1.27 2003/07/28 22:18:42 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


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


}
