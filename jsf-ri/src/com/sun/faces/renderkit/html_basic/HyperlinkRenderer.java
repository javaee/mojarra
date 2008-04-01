/*
 * $Id: HyperlinkRenderer.java,v 1.17 2002/06/05 22:29:55 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HyperlinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.CommandEvent;
import javax.faces.render.Renderer;
import javax.faces.FacesException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>HyperlinkRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HyperlinkRenderer.java,v 1.17 2002/06/05 22:29:55 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HyperlinkRenderer extends Renderer {
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

    public HyperlinkRenderer() {
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
        ParameterCheck.nonNull(c);
        return supportsComponentType(c.getComponentType());
    }

    public boolean supportsComponentType(String componentType) {
        ParameterCheck.nonNull(componentType);
        return (componentType.equals(UICommand.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
        throws IOException {

        if (context == null) {
            throw new NullPointerException("Null FacesContext");
        }
        ParameterCheck.nonNull(component);

        // Does the command match our own name?
        String action = context.getServletRequest().getParameter("action");
        if (!"command".equals(action)) {
            return;
        }
        String name = context.getServletRequest().getParameter("name");
        if (name == null) {
            return;
        }
        if (!name.equals(component.currentValue(context))) {
            return;
        }

        // Enqueue a command event to the application
        context.addApplicationEvent(new CommandEvent(component, name));
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
        throws IOException {

        if ( context == null ) {
            throw new NullPointerException("Null FacesContext");
        }
        ParameterCheck.nonNull(component);

        PrintWriter writer = context.getServletResponse().getWriter();
        Assert.assert_it( writer != null );

        writer.print("<a href=\"");
        writer.print(href(context, component));
        writer.print("\">");
        writer.print(component.currentValue(context));
        writer.print("</a>");
    }

    public void encodeChildren(FacesContext context, UIComponent component) 
        throws IOException {

    }

    public void encodeEnd(FacesContext context, UIComponent component) 
        throws IOException {

    }

    private String href(FacesContext context, UIComponent component) {
        if (context == null) {
            throw new NullPointerException("Null FacesContext");
        }
        ParameterCheck.nonNull(component);

        HttpServletRequest request =
            (HttpServletRequest) context.getServletRequest();
        HttpServletResponse response =
            (HttpServletResponse) context.getServletResponse();
        StringBuffer sb = new StringBuffer(request.getContextPath());
        sb.append("/faces?action=command&name=");
        sb.append(component.currentValue(context)); // FIXME - null handling?
        sb.append("&tree=");
        sb.append(context.getResponseTree().getTreeId());
        return (response.encodeURL(sb.toString()));

    }

} // end of class HyperlinkRenderer
