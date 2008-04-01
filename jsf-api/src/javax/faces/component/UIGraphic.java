/*
 * $Id: UIGraphic.java,v 1.2 2002/05/15 18:20:07 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UIGraphic</strong> is a {@link UIComponent} that can display
 * a graphical image to the user.  The user cannot manipulate this component;
 * it is for display purposes only.</p>
 *
 * <p>The local value of the URL of the image that is to be displayed by
 * this component is stored in the <code>value</code> property, and
 * must be a <code>java.lang.String</code> (as must the model property
 * corresponding to any model reference for this component).  This URL is
 * interpreted as a context-relative path if it starts with a slash ('/')
 * character; otherwise, it is interpreted as an absolute or relative path
 * that should be used unchanged.</p>
 *
 * <p>For convenience, the local value of the selected state is accessible
 * via the <code>getImagePath()</code> and <code>setImagePath()</code>
 * methods.  The <code>currentValue()</code> method should be used to
 * retrieve the value to be rendered.</p>
 */

public class UIGraphic extends UIComponent {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "Graphic";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the local value of the image path.</p>
     */
    public String getImagePath() {

        return ((String) getAttribute("value"));

    }


    /**
     * <p>Set the local value of the image path.</p>
     *
     * @param imagePath The new image path
     */
    public void setImagePath(String imagePath) {

        setAttribute("value", imagePath);

    }


    // ------------------------------------------- Lifecycle Processing Methods


}
