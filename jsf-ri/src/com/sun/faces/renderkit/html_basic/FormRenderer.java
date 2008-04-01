/*
 * $Id: FormRenderer.java,v 1.23 2002/06/06 00:15:01 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FormRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.FacesException;
import javax.faces.event.FormEvent;


import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *  <B>FormRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormRenderer.java,v 1.23 2002/06/06 00:15:01 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class FormRenderer extends Renderer {
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
    public AttributeDescriptor getAttributeDescriptor(
        UIComponent component, String name) {
        return null;
    }

    public AttributeDescriptor getAttributeDescriptor(
        String componentType, String name) {
        return null;
    }

    public Iterator getAttributeNames(UIComponent component) {
        return null;
    }

    public Iterator getAttributeNames(String componentType) {
        return null;
    }

   public boolean supportsComponentType(UIComponent c) {
        if ( c == null ) {
            return false;
        }     
        return supportsComponentType(c.getComponentType());
    }

    public boolean supportsComponentType(String componentType) {
        if ( componentType == null ) {
            return false;
        }    
        return (componentType.equals(UIForm.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
            throws IOException{
        if ( context == null ) {
            throw new NullPointerException("FacesContext is null");
        }    
        ParameterCheck.nonNull(component);    
        
        // action parameter must have the value "form"
        String action = context.getServletRequest().getParameter("action");
        if (!"form".equals(action)) {
            return;
        }
        
        // name parameter must have a value equal to currentValue.
        String formName = context.getServletRequest().getParameter("name");
        if (formName == null) {
            return;
        }
        
        if (!formName.equals(component.currentValue(context))) {
            return;
        }

        // queue form event to be processed during Invoke Applications phase.
        context.addApplicationEvent(new FormEvent(component, formName));
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
             throws IOException{
        if ( context == null ) {
            throw new NullPointerException("FacesContext is null");
        }    
        ParameterCheck.nonNull(component);             
       
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
        StringBuffer sb = new StringBuffer(request.getContextPath());
        sb.append("/faces?action=form&name=");
        sb.append(form.currentValue(context)); 
        sb.append("&tree=");
        sb.append(context.getResponseTree().getTreeId());
        return (response.encodeURL(sb.toString()));
    }     

    public void encodeChildren(FacesContext context, UIComponent component) {

    }

    public void encodeEnd(FacesContext context, UIComponent component) 
             throws IOException{
        if ( context == null ) {
            throw new NullPointerException("FacesContext is null");
        }    
        ParameterCheck.nonNull(component);
        
        // Render the end tag for form
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null);
        writer.write("</FORM>");
    }

} // end of class FormRenderer
