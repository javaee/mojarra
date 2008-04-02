/*
 * $Id: ViewTag.java,v 1.3 2003/09/04 21:15:07 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.RIConstants;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.StateManager;
import javax.faces.render.ResponseStateManager;
import javax.faces.webapp.UIComponentBodyTag;
import javax.faces.application.StateManager.SerializedView;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;

import com.sun.faces.util.Util;
import org.mozilla.util.Assert;

/**
 *
 *  All JSF component tags must be nested within UseFacesTag.  This tag
 *  corresponds to the root of the UIComponent tree.  It does not have
 *  any renderers or attributes. It exists mainly to save the state of
 *  the response tree once all tags have been rendered.
 *
 * @version $Id: ViewTag.java,v 1.3 2003/09/04 21:15:07 jvisvanathan Exp $
 * 
 *
 */

public class ViewTag extends UIComponentBodyTag
{
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

    public ViewTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    //
    // General Methods
    //
    
    //
    // Methods from FacesBodyTag
    //

    protected int getDoStartValue() throws JspException {
        return BodyTag.EVAL_BODY_BUFFERED;
    }
    
    public int doStartTag() throws JspException {
        int rc = super.doStartTag();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Assert.assert_it(facesContext != null);
	ResponseWriter writer = facesContext.getResponseWriter();
        Assert.assert_it(writer != null);
	try {
            writer.startDocument();
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return rc;
    }
 
    public int doAfterBody() throws JspException {
	BodyContent bodyContent = null;
	String content = null;
        Object treeStructure = null;
        Object treeState = null;
        
        try {
            if (null == (bodyContent = getBodyContent())) {
		Object params [] = { this.getClass().getName() };
                throw new JspException(Util.getExceptionMessage(
                        Util.NULL_BODY_CONTENT_ERROR_MESSAGE_ID, params));
            }    
	    content = bodyContent.getString();
            // long beginTime = System.currentTimeMillis();
            // replace the marker in the buffered response, with response tree's
            // state info. To do this we we first serialize the tree and encode
            // it using Apache's utility and write it to the page using an
            // hidden field.
	    
	    // Look up the FacesContext instance for this request
	    FacesContext facesContext = FacesContext.getCurrentInstance();
	    if (facesContext == null) { 
		throw new JspException(Util.getExceptionMessage(
                        Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
	    }
	    
	    StateManager stateManager = Util.getStateManager(facesContext);
	    Assert.assert_it(stateManager != null);
            
            SerializedView view = stateManager.getSerializedView(facesContext);
            // If the state is going to be saved on the server, then view will
            // be null. 
            if ( view != null) {
                Object result = stateManager.saveView(context, content, view);
                Assert.assert_it(result != null);
                content = result.toString();
            }
            
            // for the saveStateInSession case, bodyContent is not altered.
            // write the buffered response along with the state information
            // to output.
            getPreviousOut().write(content);
        } catch (IOException iox) {
            Object [] params = { "session", iox.getMessage() };
            throw new JspException(Util.getExceptionMessage(
                    Util.SAVING_STATE_ERROR_MESSAGE_ID, params));
        }  
	return EVAL_PAGE;
    }
    
    
    public int doEndTag() throws JspException {
        int rc = super.doEndTag();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResponseWriter writer = facesContext.getResponseWriter();
        Assert.assert_it(writer != null);
        try {
            writer.endDocument();
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return rc;
    }

    
    /**
     * This should never get called for PageTag.
     */ 
    public String getComponentType() {
	Assert.assert_it(false);
	throw new IllegalStateException();
    }    

    protected boolean isSuppressed() {
	return true;
    }

    public String getRendererType() {
        return null;
    }

    protected int getDoEndValue() throws JspException {
        return (EVAL_PAGE);
    }
    
} // end of class UseFacesTag
