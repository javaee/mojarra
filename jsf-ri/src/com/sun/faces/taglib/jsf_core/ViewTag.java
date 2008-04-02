/*
 * $Id: ViewTag.java,v 1.43 2006/03/29 23:03:53 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.taglib.jsf_core;

import java.io.IOException;
import java.util.Locale;

import javax.el.ELException;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.faces.application.ViewHandlerResponseWrapper;
import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;

/**
 * All JSF component tags must be nested within a f:view tag.  This tag
 * corresponds to the root of the UIComponent tree.  It does not have a
 * Renderer. It exists mainly to provide a guarantee that all faces
 * components reside inside of this tag.
 *
 * @version $Id: ViewTag.java,v 1.43 2006/03/29 23:03:53 rlubke Exp $
 */

public class ViewTag extends UIComponentELTag {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    private static final Logger logger =
            Util.getLogger(Util.FACES_LOGGER + Util.TAGLIB_LOGGER);

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    protected ValueExpression renderKitId = null;

    public void setRenderKitId(ValueExpression renderKitId) {
        this.renderKitId = renderKitId;
    }

    protected ValueExpression locale = null;

    public void setLocale(ValueExpression newLocale) {
        locale = newLocale;
    }

    protected MethodExpression beforePhase = null;

    public void setBeforePhase(MethodExpression newBeforePhase) {
    beforePhase = newBeforePhase;
    }

    protected MethodExpression afterPhase = null;

    public void setAfterPhase(MethodExpression newAfterPhase) {
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

    protected int getDoStartValue() throws JspException {
        return BodyTag.EVAL_BODY_BUFFERED;
    }

    /**
     * <p>Override parent <code>doStartTag()</code> to do the following:</p>
     *
     * <ul>
     *
     * <li><p>Get the {@link ViewHandlerResponseWrapper} from the
     * request, which was placed there by {@link
     * ViewHandler#renderView}, and call {@link
     * ViewHandlerResponseWrapper#flushContentToWrappedResponse}.  This
     * causes any content that appears before the view to be written out
     * to the response.  This is necessary to allow proper ordering to
     * happen.</p></li>
     *
     * </ul>
     *
     */

    public int doStartTag() throws JspException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            throw new IllegalStateException(
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.FACES_CONTEXT_NOT_FOUND_ID));
        }


    // flush out any content above the view tag
    Object response = facesContext.getExternalContext().getResponse();
    if (response instanceof ViewHandlerResponseWrapper) {
        try {
        pageContext.getOut().flush();
        ((ViewHandlerResponseWrapper)response).flushContentToWrappedResponse();
        }
        catch (IOException e) {
        throw new JspException("Can't write content above <f:view> tag"
                               + " " + e.getMessage());
        }
    }

        int rc = 0;
        try {
            rc = super.doStartTag();
        } catch (JspException e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "Can't leverage base class", e);
            }
            throw e;
        } catch (Throwable t) {
             if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "Can't leverage base class", t);
            }
            throw new JspException(t);
        }

        // this must happen after our overriderProperties executes.
        pageContext.getResponse().setLocale(facesContext.getViewRoot().getLocale());
    return rc;
    }

    /**
     * <p>Examine the body content of this tag.  If it is
     * non-<code>null</code>, non-zero length, and not an HTML comment,
     * call {@link createVerbatimComponent}.</p>
     *
     * <p>Set the value of the verbatim component to be
     * <code>content</code>.</p>
     *
     * <p>Add this child to the end of the child list for
     * <code>UIViewRoot</code>.</p>
     */

    public int doAfterBody() throws JspException {
    int result = EVAL_PAGE;
        BodyContent bodyContent = null;
    UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
    UIOutput verbatim = null;
    String content, trimContent;
    int contentLen;

        if (null == (bodyContent = getBodyContent()) ||
            null == (content = bodyContent.getString()) ||
            0 == (contentLen = (trimContent = content.trim()).length()) ||
            (trimContent.startsWith("<!--") && trimContent.endsWith("-->"))) {
        return result;
        }

    bodyContent.clearBody();

    verbatim = createVerbatimComponent();
    verbatim.setValue(content);

    root.getChildren().add(verbatim);

    return result;
    }

    /**
     * <p>Exercise a contract with the {@link ViewHandler} to get the
     * character encoding from the response and set it into the
     * session.</p>
     */

    public int doEndTag() throws JspException {
        int rc = super.doEndTag();
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
        assert (false);
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
    UIViewRoot viewRoot = (UIViewRoot) component;
    FacesContext context = FacesContext.getCurrentInstance();
    ELContext elContext = context.getELContext();
    try {

        if (null != renderKitId) {
        if (renderKitId.isLiteralText()) {
            // PENDING(edburns): better error message than NPE
            // possible here.
            viewRoot.setRenderKitId(renderKitId.getValue(elContext).toString());
        } else {
            // clear out the literal value to force using the
            // expression
            viewRoot.setRenderKitId(null);
            viewRoot.setValueExpression("renderKitId", renderKitId);
        }
        }
        else if (viewRoot.getRenderKitId() == null) {
        String renderKitIdString =
            context.getApplication().getDefaultRenderKitId();
        if (null == renderKitIdString) {
            renderKitIdString = RenderKitFactory.HTML_BASIC_RENDER_KIT;
        }
        viewRoot.setRenderKitId(renderKitIdString);
        }

        if (null != locale) {
        if (locale.isLiteralText()) {
            // PENDING(edburns): better error message than NPE
            // possible here.
            viewLocale =
            getLocaleFromString(locale.getValue(elContext).toString());
        }
        else {
            component.setValueExpression("locale", locale);
                    Object result = locale.getValue(context.getELContext());
                    if (result instanceof Locale) {
                        viewLocale = (Locale) result;
                    } else if (result instanceof String) {
                        viewLocale = getLocaleFromString((String) result);
                    }
        }

        assert(null != viewLocale);

        ((UIViewRoot) component).setLocale(viewLocale);
        // update the JSTL locale attribute in request scope so that
        // JSTL picks up the locale from viewRoot. This attribute
        // must be updated before the JSTL setBundle tag is called
        // because that is when the new LocalizationContext object
        // is created based on the locale.
        Config.set(pageContext.getRequest(),Config.FMT_LOCALE, viewLocale);
        }

        if (null != beforePhase) {
        if (beforePhase.isLiteralText()) {
            Object params [] = {beforePhase};
            throw new javax.faces.FacesException(MessageUtils.getExceptionMessageString(MessageUtils.INVALID_EXPRESSION_ID, params));
        }
        else {
            viewRoot.setBeforePhaseListener(beforePhase);

        }
        }
        if (null != afterPhase) {
        if (afterPhase.isLiteralText()) {
            Object params [] = {afterPhase};
            throw new javax.faces.FacesException(MessageUtils.getExceptionMessageString(MessageUtils.INVALID_EXPRESSION_ID, params));
        }
        else {
            viewRoot.setAfterPhaseListener(afterPhase);
        }
        }
    } catch (ELException ele) {
        throw new FacesException(ele);
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
