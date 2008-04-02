/*
 * $Id: FormRenderer.java,v 1.41 2003/02/07 00:18:09 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FormRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import com.sun.faces.context.FacesContextImpl;

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
 * @version $Id: FormRenderer.java,v 1.41 2003/02/07 00:18:09 eburns Exp $
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

    public void encodeBegin(FacesContext context, UIComponent component) 
             throws IOException{
        String formClass = null;         
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                   Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it( writer != null );
        // since method and action are rendered here they are not added
        // to the pass through attributes in Util class.
        writer.write("<form method=\"post\" action=\"");
        writer.write(getActionStr(context, component));
        writer.write("\"");
        if (null != (formClass = (String) 
		     component.getAttribute("formClass"))) {
	    writer.write(" class=\"" + formClass + "\" ");
	}
        writer.write(Util.renderPassthruAttributes(context, component));
        writer.write(Util.renderBooleanPassthruAttributes(context, 
                component));
        writer.write(">");
	updateFormNumber(context, component);
    }

    /**

    * This method keeps track of the number of forms in a page.  This is
    * necessary for any renderer that needs to use Javascript, as in,
    * document.forms[N].

    */

    protected int updateFormNumber(FacesContext context, 
				   UIComponent component) {
	HttpServletRequest request = (HttpServletRequest) 
	    context.getServletRequest();
	int numForms = 0;
	Integer formsInt = null;
	// find out the current number of forms in the page.
	if (null != (formsInt = (Integer) 
		     request.getAttribute(RIConstants.FORM_NUMBER_ATTR))) {
	    numForms = formsInt.intValue();
	}
	component.setAttribute(RIConstants.FORM_NUMBER_ATTR, 
			       formsInt = new Integer(numForms));
	request.setAttribute(RIConstants.FORM_NUMBER_ATTR, 
			     formsInt = new Integer(++numForms));
	return numForms;
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
        sb.append(RIConstants.URL_PREFIX);
	sb.append(context.getTree().getTreeId());
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
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        // Render the end tag for form
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null);
        // if we are saving state in page, insert a marker into buffer so that 
        // UseFaces tag can replace it state information.
        String saveStateParam = (context.getServletContext()).
                getInitParameter(RIConstants.SAVESTATE_INITPARAM);
        if ( saveStateParam != null && saveStateParam.equalsIgnoreCase("true")){
            writer.write(RIConstants.SAVESTATE_MARKER);
        }    
        writer.write("</form>");
    }

} // end of class FormRenderer
