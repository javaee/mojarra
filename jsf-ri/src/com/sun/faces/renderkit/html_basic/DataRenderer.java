/*
 * $Id: DataRenderer.java,v 1.3 2002/09/11 20:02:21 edburns Exp $
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
 * @version $Id: DataRenderer.java,v 1.3 2002/09/11 20:02:21 edburns Exp $
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

    public boolean decode(FacesContext context, UIComponent component) 
            throws IOException{
	return true;
    }
    
    public void encodeBegin(FacesContext context, UIComponent component) 
             throws IOException{
        // "panel_data" component is just a holder for an Iterator 
        // over a set of model beans.  It doesn't have any rendering behavior of 
        // its own -- that responsibility belongs to the surrounding panel.  
        // See ListRenderer.         
    }   

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
    }
}
