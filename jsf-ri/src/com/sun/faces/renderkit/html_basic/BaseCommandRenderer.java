/*
 * $Id: BaseCommandRenderer.java,v 1.1 2003/07/29 16:25:21 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// BaseCommandRenderer.java
package com.sun.faces.renderkit.html_basic;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import java.util.MissingResourceException;
import java.io.IOException;

public abstract class BaseCommandRenderer extends HtmlBasicRenderer {

    //
    // General Methods
    //

    /**
     * Obtain and return the image path for this {@link UIComponent}.
     * The attribute <code>image</code> will be checked first, if null,
     * then check for an image path contained in a resource bundle using
     * the key specified by the <code>imageKey</code> attribute.
     * @param context current FacesContext
     * @param component UIComponent
     * @return the path for this image, or null if it cannot be determined
     */
    protected String getImageSrc(FacesContext context,
                                 UIComponent component) {
        String result = (String) component.getAttribute("image");

        if (result == null) {
            try {
                result = getKeyAndLookupInBundle(context, component,
                    "imageKey");
            } catch (MissingResourceException e) {
                // Do nothing since the absence of a resource is not an
                // error.
            }
        }
        if (result == null) {
            return result;
        }

        StringBuffer sb = new StringBuffer();
        if (result.charAt(0) == '0') {
            sb.append(context.getExternalContext().getRequestContextPath());
        }
        sb.append(result);
        // PENDING (rlubke) Do we need to encode a path to an image?
        //return (context.getExternalContext().encodeURL(sb.toString()));
        return(sb.toString());

    }

    /**
     * <p>Returns a label for the button using the following algorithm:
     * <ul>
     * <li>Use the value, if not null, from currentValue()</li>
     * <li>If a ResourceBundle is defined (i.e. the <code>key</code> and
     * <code>bundle</code> attributes are available, use the value associated
     * with the specified key</li>
     * <li>Obtain the value from available MessageResources</li>
     * <li>If the all of the above lookups fail, a zero-length String will
     * returned</li>
     * </ul>
     * @param context current FacesContext
     * @param component UIComponent
     * @return the label for this component based on the alogrithm defined above
     * @throws java.io.IOException if an unexpected problem occurs during I/O operations
     */
    protected String getLabel(FacesContext context,
                              UIComponent component) throws IOException {
        String result = null;
        // First call currentValue()
        Object value = ((UICommand) component).currentValue(context);
        if (value != null) {
              result = value.toString();
        }

        if (result == null) {
            // no valueRef or explicit label
            try {
                result = getKeyAndLookupInBundle(context, component, "key");
            } catch (MissingResourceException e) {
                // Do nothing since the absence of a resource is not an
                // error.
            }
        }

        //PENDING (rlubke) MessageResources lookup
        if (result == null) {
            // all lookups have failed
            result = "";
        }
        return result;
    }

// The testcase for this class is com.sun.faces.renderkit.html_basic.TestRenderers_1


} // end of class BaseCommandRenderer

