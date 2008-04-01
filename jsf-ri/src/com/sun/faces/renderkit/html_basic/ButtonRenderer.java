/*
 * $Id: ButtonRenderer.java,v 1.24 2002/06/28 22:46:58 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ButtonRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.event.CommandEvent;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConversionException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 *
 *  <B>ButtonRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ButtonRenderer.java,v 1.24 2002/06/28 22:46:58 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ButtonRenderer extends HtmlBasicRenderer {
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

    public ButtonRenderer() {
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
            return false;
        }    
        return (componentType.equals(UICommand.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
            throws IOException {
    }
    
    public void encodeBegin(FacesContext context, UIComponent component) 
            throws IOException {
        if ( context == null ) {
            throw new NullPointerException("FacesContext is null");
        }    
        ParameterCheck.nonNull(component);             
       
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it( writer != null );
        
        //can commandName be null ?
        String cmdName = (String) component.currentValue(context);
        Assert.assert_it(cmdName != null);
        
        writer.write("<INPUT TYPE=");
        if (component.getAttribute("image") != null) {
            writer.write("\"IMAGE\" SRC=\"");
            writer.write((String) component.getAttribute("image"));
            writer.write("\"");
            writer.write(" NAME=\"");
            writer.write(cmdName);
            writer.write("\"");
        } else {
            writer.write("\"SUBMIT\" NAME=\"");
            writer.write(cmdName);
            writer.write("\"");
            writer.write(" VALUE=\"");
            // Follow the UE Spec for Button:
            // http://javaweb.sfbay.sun.com/engineering/jsue/j2ee/WebServices/
            // JavaServerFaces/uispecs/UICommand_Button.html
            // If there is no label specified, default to commandName.
            String label = (String)component.getAttribute("label");
            if ( label == null ) {
                label = cmdName;
            }    
            if (label.length() == 3) {
                writer.write("&nbsp;&nbsp;");
                writer.write(label);
                writer.write("&nbsp;&nbsp;");
                writer.write("\"");
            } else if (label.length() == 2) {
                writer.write("&nbsp;&nbsp;&nbsp;");
                writer.write(label);
                writer.write("&nbsp;&nbsp;&nbsp;");
                writer.write("\"");
            } else {
                writer.write(label);
                writer.write("\"");
            }
        }
        writer.write(">");
    }
    
    public void encodeChildren(FacesContext context, UIComponent component) {

    }

    public void encodeEnd(FacesContext context, UIComponent component) {

    }

} // end of class ButtonRenderer
