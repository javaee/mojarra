/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * %W% %G%
 */
 
package com.sun.faces.systest.resources;

import java.util.ListResourceBundle;

public class Resources_en extends ListResourceBundle {
    
    public Resources_en() {
    }

    /**
     * See class description.
     */
    protected Object[][] getContents() {
        return new Object[][] {
            { "button_key", "RES-BUNDLE KEY" },
            { "image_key", "resbundle_image.gif" },
            { "hyperlink_key", "RES-BUNDLE LINK" }
        };
    }
}