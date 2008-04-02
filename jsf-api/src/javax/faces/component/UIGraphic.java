/*
 * $Id: UIGraphic.java,v 1.17 2003/04/29 18:51:30 eburns Exp $
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
 * <p>By default, the <code>rendererType</code> property is set to
 * "<code>Image</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIGraphic extends UIOutput {


    // ------------------------------------------------------- Static Variables


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a new {@link UIGraphic} instance with default property
     * values.</p>
     */
    public UIGraphic() {

        super();
        setRendererType("Image");

    }


    // ------------------------------------------------------------- Attributes


    /**
     * <p>Return the image URL for this {@link UIGraphic}.</p>
     */
    public String getURL() {

        return ((String) getValue());

    }


    /**
     * <p>Set the image URL for this {@link UIGraphic}.</p>
     *
     * @param url The new image URL
     */
    public void setURL(String url) {

        setValue(url);

    }


    // ------------------------------------------------------------- Properties


}
