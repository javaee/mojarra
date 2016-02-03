/*
 * $Id: ViewTag.java,v 1.26.8.5 2007/04/27 21:27:48 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.taglib.jsf_core;

import javax.faces.application.StateManager;
import javax.faces.application.StateManager.SerializedView;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentBodyTag;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

/**
 * All JSF component tags must be nested within UseFacesTag.  This tag
 * corresponds to the root of the UIComponent tree.  It does not have
 * any renderers or attributes. It exists mainly to save the state of
 * the response tree once all tags have been rendered.
 *
 * @version $Id: ViewTag.java,v 1.26.8.5 2007/04/27 21:27:48 ofung Exp $
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
        Util.doAssert(facesContext != null);

        // this must happen after our overriderProperties executes.
        pageContext.getResponse().setLocale(facesContext.getViewRoot().getLocale());
    
        ResponseWriter writer = facesContext.getResponseWriter();
        Util.doAssert(writer != null);

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
              
        try {
            view = stateManager.saveSerializedView(context);
        } catch (IllegalStateException ise) {
            throw new JspException(ise);
        } catch (Exception ie) {
            // catch any exception thrown while saving the view in session.
            Object[] params = {"session", ie.getMessage()};
            throw new JspException(Util.getExceptionMessageString(
                Util.SAVING_STATE_ERROR_MESSAGE_ID, params), ie);    
        }

        WriteBehindWriter writeBehind = new WriteBehindWriter(context,
                                                              stateManager,
                                                              view);
        try {
            bodyContent.writeOut(writeBehind);
            writeBehind.flushToWriter(responseWriter);
        } catch (IOException ioe) {
            throw new JspException(ioe);
            // Object[] params = {"client", iox.getMessage()};
            //throw new JspException(Util.getExceptionMessageString(
            //    Util.SAVING_STATE_ERROR_MESSAGE_ID, params), iox);
        }
       
        return EVAL_PAGE;
    }


    public int doEndTag() throws JspException {
        int rc = super.doEndTag();
        // PENDING(): remove these getCurrentInstance calls, since we
        // have a facesContext ivar.
        FacesContext context = FacesContext.getCurrentInstance();
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null);
        try {
            writer.endDocument();
        } catch (IOException e) {
            throw new JspException(e.getMessage());
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
        if (null != locale) {
            if (isValueReference(locale)) {
                component.setValueBinding("locale",
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
            ((UIViewRoot) component).setLocale(viewLocale);
            // update the JSTL locale attribute in request scope so that
            // JSTL picks up the locale from viewRoot. This attribute
            // must be updated before the JSTL setBundle tag is called
            // because that is when the new LocalizationContext object
            // is created based on the locale.
            Config.set(pageContext.getRequest(),Config.FMT_LOCALE, viewLocale);
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
     *                   country codes must be separated by hyphen (???-???) or
     *                   underscore (???_???)."
     * @return Locale instance cosntructed from the expression.
     */
    protected Locale getLocaleFromString(String localeExpr) {
        Locale result = Locale.getDefault();
        if (localeExpr.indexOf('_') == -1 && localeExpr.indexOf('-') == -1) {
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
    
    
    // ----------------------------------------------------------- Inner Classes
    
    private static final class WriteBehindWriter extends Writer {
        
        private static final int STATE_MARKER_LEN = 
              RIConstants.SAVESTATE_FIELD_MARKER.length();
        
        private final FacesContext context;               
        private final SerializedView view;
        private final StateManager manager;
        private char[] input;
        private int inputOff;
        private int inputLen;
        
        // -------------------------------------------------------- Constructors
        
        public WriteBehindWriter(FacesContext context, 
                                 StateManager manager, 
                                 SerializedView view) {
            this.context = context;
            this.manager = manager;
            this.view = view;
        }
        
        
        // ------------------------------------------------- Methods from Writer


        public void write(char cbuf[], int off, int len) throws IOException {
            input = cbuf;
            inputOff = off;
            inputLen = len;            
        }

        public void flush() throws IOException {
            // NO-OP
        }

        public void close() throws IOException {
            // NO-OP
        }
        
        
        // ------------------------------------------------------ Public Methods


        public void flushToWriter(Writer writer) throws IOException {           
            int totalLen = inputLen;
            int pos = inputOff;
            int tildeIdx = getNextDelimiterIndex(pos);
            while (pos < totalLen) {
                if (tildeIdx != -1) {

                    // write all content up to the first '~'                       
                    int len = (tildeIdx - pos);
                    writer.write(input, pos, len);
                    // now check to see if the state saving string is
                    // at the begining of pos, if so, write our
                    // state out.
                    if (input[tildeIdx + STATE_MARKER_LEN - 1]
                        == RIConstants
                          .SAVESTATE_FIELD_DELIMITER) {
                        manager.writeState(context, view);
                    }

                    // push us past the last '~' at the end of the marker
                    pos += (len + STATE_MARKER_LEN);
                    tildeIdx = getNextDelimiterIndex(pos);

                } else {

                    // we're near the end of the response                       
                    int len = (totalLen - pos);
                    writer.write(input, pos, len);
                    pos += (len + 1);

                }
            }
        }

        // ----------------------------------------------------- Private Methods
        
        private int getNextDelimiterIndex(int fromIndex) {
            return indexOf(RIConstants.SAVESTATE_FIELD_DELIMITER, fromIndex);
        }

        private int indexOf(char c, int fromIndex) {
            int max = inputOff + inputLen;            

            if (fromIndex < 0) {
                fromIndex = 0;
            } else if (fromIndex >= inputLen) {               
                return -1;
            }
            for (int i = inputOff + fromIndex; i < max; i++) {
                if (input[i] == c) {
                    return i - inputOff;
                }
            }
            return -1;
        }
        
        
    }

} // end of class ViewTag
