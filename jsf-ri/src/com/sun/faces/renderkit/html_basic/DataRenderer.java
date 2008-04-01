/*
 * $Id: DataRenderer.java,v 1.1 2002/09/06 18:05:23 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import com.sun.faces.util.Util;
/**
 *
 *  DataRenderer is an arbitrary grouping "renderer" with no actual 
 *  output functionality
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: DataRenderer.java,v 1.1 2002/09/06 18:05:23 jvisvanathan Exp $
 *  
 */

public class DataRenderer extends HtmlBasicRenderer {
    
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public DataRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    public boolean supportsComponentType(String componentType) {
        if ( componentType == null ) {
            throw new NullPointerException(
                Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }    
        return (componentType.equals(UIPanel.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
            throws IOException{
    }
    
    public void encodeBegin(FacesContext context, UIComponent component) 
             throws IOException{
    }   

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
    }
}
