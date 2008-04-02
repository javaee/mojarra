/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */
 
package components.taglib;


import components.components.AreaSelectedListener;
import components.components.MapComponent;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p>Register an {@link AreaSelectedListener} with the {@link MapComponent}
 * from the tag in which we are nested.</p>
 */

public class AreaSelectedTag extends TagSupport {


    // -------------------------------------------------------------- Attributes


    // Fully qualified classname of the {@link AreaSelectedListener}
    // instance to be created and registered
    private String type = null;
    public void setType(String type) {
        this.type = type;
    }


    // ------------------------------------------------------------- Tag Methods


    /**
     * <p>Create and register a new {@link AreaSelectedListener}.</p>
     *
     * @exception JspException if an error occurs
     */
    public int doStartTag() throws JspException {

        MapComponent map = getMapComponent();
        if (map != null) {
            map.addAreaSelectedListener(createAreaSelectedListener());
        }
        return (SKIP_BODY);

    }


    /**
     * <p>Release references to allocated resources.</p>
     */
    public void release() {

        this.type = null;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Create and return a new {@link AreaSelectedListener} instance.</p>
     *
     * @exception JspException if a new instance cannot be created
     */
    private AreaSelectedListener createAreaSelectedListener()
        throws JspException {

        try {

            ClassLoader loader =
                Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = this.getClass().getClassLoader();
            }
            Class clazz = loader.loadClass(type);
            return ((AreaSelectedListener) clazz.newInstance());

        } catch (Exception e) {

            throw new JspException(e);

        }

    }


    /**
     * <p>Return the {@link MapComponent} created by our parent component tag
     * (if any); otherwise, return <code>null</code>.</p>
     *
     * @exception JspException if an error occurs
     */
    private MapComponent getMapComponent() throws JspException {

        try {

            // Locate our parent UIComponentTag
            Tag tag = getParent();
            while (!(tag instanceof UIComponentTag)) {
                tag = tag.getParent();
            }
            UIComponentTag componentTag = (UIComponentTag) tag;

            // Return the created component (if any)
            if (componentTag.getCreated()) {
                return ((MapComponent) componentTag.getComponent());
            } else {
                return (null);
            }

        } catch (Exception e) {

            throw new JspException(e);

        }

    }


}
