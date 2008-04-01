/*
 * $Id: FormRenderer.java,v 1.30 2002/07/22 16:58:01 jvisvanathan Exp $
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
import javax.faces.component.UICommand;
import com.sun.faces.RIConstants;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletRequest;
import java.net.URLEncoder;

/**
 *
 *  <B>FormRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormRenderer.java,v 1.30 2002/07/22 16:58:01 jvisvanathan Exp $
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
            return false;
        }    
        return (componentType.equals(UIForm.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
            throws IOException{
        if (context == null) {
            throw new NullPointerException();
        }
        ParameterCheck.nonNull(component);
        
        // Does the extra path info on this request identify a form submit
        // for this UIForm component?
        String pathInfo = 
            ((HttpServletRequest) context.getServletRequest()).getPathInfo();
       
        if (pathInfo == null) {
            return;
        }
        if (!pathInfo.startsWith(RIConstants.FORM_PREFIX)) {
            return;
        }
        String formName = pathInfo.substring(RIConstants.FORM_PREFIX.length());
        int slash = formName.indexOf('/');
        if (slash >= 0) {
            formName = formName.substring(0, slash);
        }
        if (!formName.equals(component.currentValue(context))) {
            return;
        }

        // Which of our nested UICommand children triggered this submit?
        // PENDING(visvan)- assumes commandName won't have name clash 
        // with components!
        String commandName =
            extract(context, context.getServletRequest(), component);
        // Enqueue a form event to the application
        context.addApplicationEvent
            (new FormEvent(component, formName, commandName));
    }
    
    /**
     * <p>Extract the command name of the child {@link UICommand} that
     * caused this form to be submitted.  The specified component, and
     * all of its children, are to be checked.</p>
     *
     * @param context FacesContext for the request we are processing
     * @param request ServletRequest we are processing
     * @param component Component to be checked
     */
    private String extract(FacesContext context, ServletRequest request,
                           UIComponent component) {

        // Check the current component
        if ((component.getComponentType()).equals(UICommand.TYPE)) {
            Object value = component.currentValue(context);
            if (value != null) {
                String commandName = value.toString();
                if (request.getParameter(commandName) != null) {
                    return (commandName);
                } else if (request.getParameter(commandName+".x") != null &&
                    request.getParameter(commandName+".y") != null) {
                    return (commandName);
                }
            }
        }

        // Check the children of the current component
        Iterator kids = component.getChildren();
        while (kids.hasNext()) {
            String commandName =
                extract(context, request, (UIComponent) kids.next());
            if (commandName != null) {
                return (commandName);
            }
        }

        // No matching command name was found
        return (null);
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
