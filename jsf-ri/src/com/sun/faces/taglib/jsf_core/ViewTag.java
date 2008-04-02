/*
 * $Id: ViewTag.java,v 1.12 2003/11/10 22:18:39 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.RIConstants;

import javax.servlet.ServletRequest;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.webapp.UIComponentBodyTag;
import javax.faces.application.StateManager.SerializedView;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.jstl.core.Config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.util.Util;
import org.mozilla.util.Assert;

/**
 *
 *  All JSF component tags must be nested within UseFacesTag.  This tag
 *  corresponds to the root of the UIComponent tree.  It does not have
 *  any renderers or attributes. It exists mainly to save the state of
 *  the response tree once all tags have been rendered.
 *
 * @version $Id: ViewTag.java,v 1.12 2003/11/10 22:18:39 eburns Exp $
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

    protected static Log log = LogFactory.getLog(ViewTag.class);

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    protected String locale = null;
    public void setLocale(String newLocale) {
	locale = newLocale;
    }


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
        int rc = 0;
        try {        
	    rc = super.doStartTag();
        }
        catch (JspException e) {
	    if (log.isDebugEnabled()) {
	        log.debug("Can't leverage base class", 
			  e);
	    }
	    throw e;
        }
        catch (Throwable t) {
	    if (log.isDebugEnabled()) {
	        log.debug("Can't leverage base class", 
			  t);
	    }
	    throw new JspException(t);
        }
	
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Assert.assert_it(facesContext != null);

	// this must happen after our overriderProperties executes.
	((ServletResponse)facesContext.getExternalContext().getResponse()).
	    setLocale(facesContext.getViewRoot().getLocale());

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
	    
        try {
            view = stateManager.saveSerializedView(context);
        } catch (IllegalStateException ise) {
            throw new JspException(ise);
        }
            
	    if (view == null) {
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
	// PENDING(): remove these getCurrentInstance calls, since we
	// have a facesContext ivar.
        FacesContext context = FacesContext.getCurrentInstance();
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null);
        try {
            writer.endDocument();
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
	
	// store the response character encoding
	HttpSession session = null;

	if (null != 
	    (session = 
	     (HttpSession)context.getExternalContext().getSession(false))){
	    ServletResponse response = (ServletResponse)
		context.getExternalContext().getResponse();
	    session.setAttribute(ViewHandler.CHARACTER_ENCODING_KEY,
				 response.getCharacterEncoding());
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

    //
    // Methods from Superclass
    // 
    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
	Locale viewLocale = null;
	ValueBinding vb = null;
	if (null != locale) {
	    if (isValueReference(locale)) {
		component.setValueBinding("locale",
					  vb = Util.getValueBinding(locale));
		viewLocale = 
		    (Locale) vb.getValue(FacesContext.getCurrentInstance());
	    }
	    else {
		viewLocale = Util.getLocaleFromString(locale);
	    }
	    ((UIViewRoot)component).setLocale(viewLocale);
	    // update the JSTL locale attribute in request scope so that
	    // JSTL picks up the locale from viewRoot. This attribute
	    // must be updated before the JSTL setBundle tag is called
	    // because that is when the new LocalizationContext object
	    // is created based on the locale.
	    Config.set((ServletRequest) context.getExternalContext().getRequest(), 
		       Config.FMT_LOCALE, viewLocale);
        }
    }
    
} // end of class UseFacesTag
