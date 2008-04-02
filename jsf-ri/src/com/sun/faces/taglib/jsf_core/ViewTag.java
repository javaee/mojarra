/*
 * $Id: ViewTag.java,v 1.32 2005/03/15 20:37:38 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.el.MethodBinding;
import javax.faces.event.PhaseEvent;
import javax.faces.webapp.UIComponentBodyTag;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

import java.io.IOException;
import java.util.Locale;

/**
 * All JSF component tags must be nested within UseFacesTag.  This tag
 * corresponds to the root of the UIComponent tree.  It does not have
 * any renderers or attributes. It exists mainly to save the state of
 * the response tree once all tags have been rendered.
 *
 * @version $Id: ViewTag.java,v 1.32 2005/03/15 20:37:38 edburns Exp $
 */

public class ViewTag extends UIComponentBodyTag {

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

    protected String beforePhase = null;

    public void setBeforePhase(String newBeforePhase) {
	beforePhase = newBeforePhase;
    }

    protected String afterPhase = null;

    public void setAfterPhase(String newAfterPhase) {
	afterPhase = newAfterPhase;
    }


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public ViewTag() {
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
        } catch (JspException e) {
            if (log.isDebugEnabled()) {
                log.debug("Can't leverage base class",
                          e);
            }
            throw e;
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("Can't leverage base class",
                          t);
            }
            throw new JspException(t);
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        assert (facesContext != null);

        // this must happen after our overriderProperties executes.
        pageContext.getResponse().setLocale(facesContext.getViewRoot().getLocale());
    
        ResponseWriter writer = facesContext.getResponseWriter();
        assert (writer != null);

        try {
            writer.startDocument();
        } catch (IOException e) {
            throw new JspException(e);
        }
        return rc;
    }


    public int doAfterBody() throws JspException {
        BodyContent bodyContent = null;
        String content = null;
        FacesContext context = FacesContext.getCurrentInstance();
        ResponseWriter responseWriter = context.getResponseWriter();
        StateManager stateManager = Util.getStateManager(context);
        Object view = null;
        int
            beginIndex = 0,
            markerIndex = 0,
            markerLen = RIConstants.SAVESTATE_FIELD_MARKER.length(),
            contentLen = 0;

        // get a writer that sends to the client
        responseWriter = responseWriter.cloneWithWriter(getPreviousOut());

        if (context == null) {
            throw new JspException(Util.getExceptionMessageString(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        context.setResponseWriter(responseWriter);

        
        if (null == (bodyContent = getBodyContent())) {
            Object params [] = {this.getClass().getName()};
            throw new JspException(Util.getExceptionMessageString(
                Util.NULL_BODY_CONTENT_ERROR_MESSAGE_ID, params));
        }
        content = bodyContent.getString();

        try {
            view = stateManager.saveView(context);
        } catch (IllegalStateException ise) {
            throw new JspException(ise);
        } catch (Exception ie) {
            // catch any exception thrown while saving the view in session.
            Object[] params = {"session", ie.getMessage()};
            throw new JspException(Util.getExceptionMessageString(
                Util.SAVING_STATE_ERROR_MESSAGE_ID, params), ie);    
        }
        try {
	    contentLen = content.length();
	    do {
		// if we have no more markers
		if (-1 == (markerIndex =
			   content.indexOf(RIConstants.SAVESTATE_FIELD_MARKER,
					   beginIndex))) {
		    // write out the rest of the content
		    responseWriter.write(content.substring(beginIndex));
		} else {
		    // we have more markers, write out the current chunk
		    responseWriter.write(content.substring(beginIndex,
							   markerIndex));
		    stateManager.writeState(context, view);
		    beginIndex = markerIndex + markerLen;
		}
	    } while (-1 != markerIndex && beginIndex < contentLen);
        } catch (IOException iox) {
            // catch any thrown while saving state in response.
            Object[] params = {"client", iox.getMessage()};
            throw new JspException(Util.getExceptionMessageString(
                Util.SAVING_STATE_ERROR_MESSAGE_ID, params), iox);
        }
        return EVAL_PAGE;
    }


    public int doEndTag() throws JspException {
        int rc = super.doEndTag();
        // PENDING(): remove these getCurrentInstance calls, since we
        // have a facesContext ivar.
        FacesContext context = FacesContext.getCurrentInstance();
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        try {
            writer.endDocument();
        } catch (IOException e) {
            throw new JspException(e);
        }

        // store the response character encoding
        HttpSession session = null;

        if (null != (session = pageContext.getSession())) {
            session.setAttribute(ViewHandler.CHARACTER_ENCODING_KEY,
                pageContext.getResponse().getCharacterEncoding());
        }
        return rc;
    }


    /**
     * This should never get called for PageTag.
     */
    public String getComponentType() {        
        throw new IllegalStateException();
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
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        Locale viewLocale = null;
        ValueBinding vb = null;
        MethodBinding mb = null;
	UIViewRoot viewRoot = (UIViewRoot) component;
        if (null != locale) {
            if (isValueReference(locale)) {
                viewRoot.setValueBinding("locale",
					 vb = Util.getValueBinding(locale));
                Object resultLocale =
                    vb.getValue(FacesContext.getCurrentInstance());
                if (resultLocale instanceof Locale) {
                    viewLocale = (Locale) resultLocale;
                } else if (resultLocale instanceof String) {
                    viewLocale = getLocaleFromString((String) resultLocale);
                }
            } else {
                viewLocale = getLocaleFromString(locale);
            }
            viewRoot.setLocale(viewLocale);
            // update the JSTL locale attribute in request scope so that
            // JSTL picks up the locale from viewRoot. This attribute
            // must be updated before the JSTL setBundle tag is called
            // because that is when the new LocalizationContext object
            // is created based on the locale.
            Config.set(pageContext.getRequest(),Config.FMT_LOCALE, viewLocale);
        }
        if (null != beforePhase) {
            if (isValueReference(beforePhase)) {
                Class args[] = { PhaseEvent.class };
                mb = FacesContext.getCurrentInstance().getApplication().createMethodBinding(beforePhase, args);
                viewRoot.setBeforePhaseListener(mb);
		
            } else {
		Object params [] = {beforePhase};
		throw new javax.faces.FacesException(Util.getExceptionMessageString(Util.INVALID_EXPRESSION_ID, params));
            }
        }
        if (null != afterPhase) {
            if (isValueReference(afterPhase)) {
                Class args[] = { PhaseEvent.class };
                mb = FacesContext.getCurrentInstance().getApplication().createMethodBinding(afterPhase, args);
                viewRoot.setAfterPhaseListener(mb);
		
            } else {
		Object params [] = {afterPhase};
		throw new javax.faces.FacesException(Util.getExceptionMessageString(Util.INVALID_EXPRESSION_ID, params));
            }
        }
    }


    /**
     * Returns the locale represented by the expression.
     *
     * @param localeExpr a String in the format specified by JSTL Specification
     *                   as follows:
     *                   "A String value is interpreted as the printable
     *                   representation of a locale, which must contain a
     *                   two-letter (lower-case) language code (as defined by
     *                   ISO-639), and may contain a two-letter (upper-case)
     *                   country code (as defined by ISO-3166). Language and
     *                   country codes must be separated by hyphen (�-�) or
     *                   underscore (�_�)."
     * @return Locale instance cosntructed from the expression.
     */
    protected Locale getLocaleFromString(String localeExpr) {
        Locale result = Locale.getDefault();
        if (localeExpr.indexOf("_") == -1 && localeExpr.indexOf("-") == -1) {
            // expression has just language code in it. make sure the 
            // expression contains exactly 2 characters.
            if (localeExpr.length() == 2) {
                result = new Locale(localeExpr, "");
            }
        } else {
            // expression has country code in it. make sure the expression 
            // contains exactly 5 characters.
            if (localeExpr.length() == 5) {
                // get the language and country to construct the locale.
                String language = localeExpr.substring(0, 2);
                String country = localeExpr.substring(3, localeExpr.length());
                result = new Locale(language, country);
            }
        }
        return result;
    }

} // end of class ViewTag
