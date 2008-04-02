/*
 * $Id: UIGraphic.java,v 1.19 2003/08/30 00:31:31 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UIGraphic</strong> is a {@link UIComponent} that displays
 * a graphical image to the user.  The user cannot manipulate this component;
 * it is for display purposes only.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>Image</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public interface UIGraphic extends UIComponent, ValueHolder {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the image URL for this {@link UIGraphic}.  This method is a
     * typesafe alias for <code>getValue()</code>.</p>
     */
    public String getURL();


    /**
     * <p>Set the image URL for this {@link UIGraphic}.  This method is a
     * typesafe alias for <code>setValue()</code>.</p>
     *
     * @param url The new image URL
     */
    public void setURL(String url);


}
