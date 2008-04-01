/*
 * $Id: HyperlinkRenderer.java,v 1.21 2002/06/21 00:31:23 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HyperlinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.RIConstants;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;

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
 * @version $Id: HyperlinkRenderer.java,v 1.21 2002/06/21 00:31:23 eburns Exp $
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

//PENDING(rogerk) FIXME nasty hack - overwriting the responseTree should 
// probably be done in the component event handling, but there are unanswered
//questions - currently the only way to do this would be to put this
//logic (which is specific to hyperlinks in UICommand.event (messy).
//
        String target = (String)component.getAttribute("target");
        if (target != null) {
            TreeFactory treeFactory = (TreeFactory)
                FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
            Assert.assert_it(null != treeFactory);
            ServletContext sc = context.getServletContext();
            context.setResponseTree(treeFactory.getTree(sc, target));
        }

        // Enqueue a command event to the application
        context.addApplicationEvent(new CommandEvent(component, cmdName));
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
        throws IOException {

        if ( context == null ) {
            throw new NullPointerException("Null FacesContext");
        }
        ParameterCheck.nonNull(component);

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
        String text = (String)component.getAttribute("text");
        if (text != null) {
            writer.write(text);
        }
        writer.write("</A>");
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
