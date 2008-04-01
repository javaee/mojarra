/*
 * $Id: FormRenderer.java,v 1.33 2002/08/02 19:31:59 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FormRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.FacesException;
import com.sun.faces.RIConstants;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletRequest;

/**
 *
 *  <B>FormRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormRenderer.java,v 1.33 2002/08/02 19:31:59 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class FormRenderer extends HtmlBasicRenderer {
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

    public FormRenderer() {
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
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }    
        return (componentType.equals(UIForm.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
            throws IOException{
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }
    
    public void encodeBegin(FacesContext context, UIComponent component) 
             throws IOException{
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
      
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it( writer != null );
        
        writer.write("<FORM METHOD=\"post\" ACTION=\"");
        writer.write(getActionStr(context, component));
        writer.write("\">");
    }
    
    /**
     * <p>Return the value to be rendered as the <code>action</code> attribute
     * of the form generated for this component.</p>
     *
     * @param context FacesContext for the response we are creating
     * @param form UIComponent representing form that's being processed.
     */
    private String getActionStr(FacesContext context, UIComponent form) {

        HttpServletRequest request =
            (HttpServletRequest) context.getServletRequest();
        HttpServletResponse response =
            (HttpServletResponse) context.getServletResponse();
        String contextPath = request.getContextPath();
        if ( contextPath.indexOf("/") == -1 ) {
            contextPath = contextPath + "/";
        }    
        StringBuffer sb = new StringBuffer(contextPath);
        sb.append(( RIConstants.URL_PREFIX + RIConstants.FORM_PREFIX));
        sb.append(form.currentValue(context).toString());
        return (response.encodeURL(sb.toString()));
    }     

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

    }

    public void encodeEnd(FacesContext context, UIComponent component) 
             throws IOException{
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // Render the end tag for form
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null);
        // if we are saving state in page, insert a marker into buffer so that 
        // UseFaces tag can replace it state information.
        if ( (context.getServletContext()).
                getAttribute(RIConstants.SAVESTATE_INITPARAM) != null ) {
            writer.write(RIConstants.SAVESTATE_MARKER);
        }    
        writer.write("</FORM>");
    }

} // end of class FormRenderer
