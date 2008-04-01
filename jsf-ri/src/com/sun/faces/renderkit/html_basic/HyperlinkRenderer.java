/*
 * $Id: HyperlinkRenderer.java,v 1.27 2002/08/01 23:47:36 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HyperlinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.io.IOException;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.CommandEvent;
import javax.faces.FacesException;
import javax.faces.render.Renderer;
import javax.faces.tree.TreeFactory;
import javax.faces.FactoryFinder;

import javax.servlet.ServletContext;
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
 * @version $Id: HyperlinkRenderer.java,v 1.27 2002/08/01 23:47:36 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HyperlinkRenderer extends HtmlBasicRenderer {
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
    public boolean supportsComponentType(String componentType) {
        if ( componentType == null ) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }    
        return (componentType.equals(UICommand.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        String pathInfo =
            ((HttpServletRequest) context.getServletRequest()).getPathInfo();

        if (pathInfo == null) {
            return;
        }
        if (!pathInfo.startsWith(RIConstants.COMMAND_PREFIX)) {
            return;
        }
        String cmdName = pathInfo.substring(RIConstants.COMMAND_PREFIX.length());

        int slash = cmdName.indexOf('/');
        if (slash >= 0) {
            cmdName = cmdName.substring(0, slash);
        }
        if (!cmdName.equals(component.currentValue(context))) {
            return;
        }

        // Enqueue a command event to the application
        context.addApplicationEvent(new CommandEvent(component, cmdName));
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

    }

    public void encodeEnd(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it( writer != null );

        writer.write("<A HREF=\"");

        //PENDING(rogerk) don't call this if "target" (destination) is
        // a non-faces page.  For non-faces pages, we would simply 
        // include the "target attribute's value.
        // ex: <a href="http://java.sun.com".....
        // For faces pages, we are expecting the "target" attribute to
        // begin with "/faces" (ex: /faces/Faces_Basic.xul).
        //PENDING(rogerk) what if "target" attribute is not set (null)???
        //
        String target = (String)component.getAttribute("target");
        if (target != null) {
            if (target.startsWith(RIConstants.URL_PREFIX)) {
                writer.write(href(context, component));
            } else {
                writer.write(target);
            }
        }

        writer.write("\">");
        String image = (String)component.getAttribute("image");
        if (image != null) {
            writer.write("<IMAGE SRC=\"");
            writer.write(image);
            writer.write("\">");
        }
        String text = (String)component.getAttribute("label");
        if (text != null) {
            writer.write(text);
        }
        writer.write("</A>");        

    }

    private String href(FacesContext context, UIComponent component) {

        HttpServletRequest request =
            (HttpServletRequest) context.getServletRequest();
        HttpServletResponse response =
            (HttpServletResponse) context.getServletResponse();
        String contextPath = request.getContextPath();
        if ( contextPath.indexOf("/") == -1 ) {
            contextPath = contextPath + "/";
        }
        StringBuffer sb = new StringBuffer(request.getContextPath());
        sb.append(( RIConstants.URL_PREFIX + RIConstants.COMMAND_PREFIX));
        sb.append(component.currentValue(context).toString());
        sb.append("/");

        // need to make sure the rendered string contains where we
        // want to go next (target).
        //PENDING(rogerk) should "target" be required attribute??
        //
        String target = (String)component.getAttribute("target");
        if (target != null) {
            target = target.substring(RIConstants.URL_PREFIX.length());
            sb.append(target);
        } else {
            sb.append(context.getResponseTree().getTreeId());
        }
        return (response.encodeURL(sb.toString()));
    }

} // end of class HyperlinkRenderer
