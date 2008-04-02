/*
 * $Id: ViewTag.java,v 1.4 2003/09/13 12:58:51 eburns Exp $
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
 * @version $Id: ViewTag.java,v 1.4 2003/09/13 12:58:51 eburns Exp $
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
	FacesContext context = FacesContext.getCurrentInstance();
	ResponseWriter responseWriter = context.getResponseWriter();
	StateManager stateManager = Util.getStateManager(context);
	SerializedView view = null;
	int 
	    beginIndex = 0,
	    markerIndex = 0,
	    markerLen = RIConstants.SAVESTATE_FIELD_MARKER.length(),
	    contentLen = 0;
        
	// get a writer that sends to the client
	responseWriter = responseWriter.cloneWithWriter(getPreviousOut());
	
	if (context == null) { 
	    throw new JspException(Util.getExceptionMessage(
				    Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
	}
	context.setResponseWriter(responseWriter);

        try {
            if (null == (bodyContent = getBodyContent())) {
		Object params [] = { this.getClass().getName() };
                throw new JspException(Util.getExceptionMessage(
                        Util.NULL_BODY_CONTENT_ERROR_MESSAGE_ID, params));
            }    
	    content = bodyContent.getString();
	    
	    if (null == (view = stateManager.getSerializedView(context))) {
		getPreviousOut().write(content);
	    }
	    else {
		contentLen = content.length();
		do {
		    // if we have no more markers
		    if (-1 == (markerIndex = 
			       content.indexOf(RIConstants.SAVESTATE_FIELD_MARKER,
					       beginIndex))) {
			// write out the rest of the content
			responseWriter.write(content.substring(beginIndex));
		    }
		    else {
			// we have more markers, write out the current chunk
			responseWriter.write(content.substring(beginIndex,
							       markerIndex));
			stateManager.writeState(context, view);
			beginIndex = markerIndex + markerLen;
		    }
		}
		while (-1 != markerIndex && beginIndex < contentLen);
	    }
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
