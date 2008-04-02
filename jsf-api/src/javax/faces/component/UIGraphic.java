/*
 * $Id: UIGraphic.java,v 1.16 2003/03/13 01:11:58 craigmcc Exp $
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


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIGraphic";


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


    public String getComponentType() {

        return (TYPE);

    }


}
