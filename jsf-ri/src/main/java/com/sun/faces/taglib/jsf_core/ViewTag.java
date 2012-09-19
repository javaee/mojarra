/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.Method;

import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.RIConstants;

/**
 * All JSF component tags must be nested within a f:view tag.  This tag
 * corresponds to the root of the UIComponent tree.  It does not have a
 * Renderer. It exists mainly to provide a guarantee that all faces
 * components reside inside of this tag.
 *
 */

public class ViewTag extends UIComponentELTag {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    private static final Logger LOGGER = FacesLogger.TAGLIB.getLogger();

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
     * <li><p>Reflect the response object for a method called flushContentToWrappedResponse
     * and invoke it. This causes any content that appears before the view to be written out
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
        Method customFlush = ReflectionUtils.lookupMethod(response.getClass(),
                                                          "flushContentToWrappedResponse",
                                                          RIConstants.EMPTY_CLASS_ARGS);
        if (customFlush != null) {
            try {
                pageContext.getOut().flush();
                customFlush.invoke(response, RIConstants.EMPTY_METH_ARGS);
            } catch (Exception e) {
                throw new JspException("Exception attemtping to write content above the <f:view> tag.", e);
            }
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "jsf.core.taglib.viewtag.interweaving_failed");
            }
        }

        int rc;
        try {
            rc = super.doStartTag();
        } catch (JspException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Can't leverage base class", e);
            }
            throw e;
        } catch (Throwable t) {
             if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Can't leverage base class", t);
            }
            throw new JspException(t);
        }

        // this must happen after our overriderProperties executes.
        pageContext.getResponse().setLocale(facesContext.getViewRoot().getLocale());
        
        List<UIComponent> preViewLoadBundleComponents = LoadBundleTag.getPreViewLoadBundleComponentList();
        if (!preViewLoadBundleComponents.isEmpty()) {
            Iterator<UIComponent> iter = preViewLoadBundleComponents.iterator();
            UIComponent cur;
            while (iter.hasNext()) {
                cur = iter.next();
                LoadBundleTag.addChildToParentTagAndParentComponent(cur, this);
            }
            preViewLoadBundleComponents.clear();
        }
        Stack<UIComponentClassicTagBase> viewTagStack = SubviewTag.getViewTagStack();
        viewTagStack.push(this);
        return rc;
    }

    /**
     * <p>Examine the body content of this tag.  If it is
     * non-<code>null</code>, non-zero length, and not an HTML comment,
     * call {@link javax.faces.webapp.UIComponentClassicTagBase#createVerbatimComponent()}.</p>
     *
     * <p>Set the value of the verbatim component to be
     * <code>content</code>.</p>
     *
     * <p>Add this child to the end of the child list for
     * <code>UIViewRoot</code>.</p>
     */

    public int doAfterBody() throws JspException {
        int result = EVAL_PAGE;
        BodyContent bodyContent;
        UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
        UIOutput verbatim;
        String content;
        String trimContent;

        Stack<UIComponentClassicTagBase> viewTagStack =
              SubviewTag.getViewTagStack();
        viewTagStack.pop();

        if (null == (bodyContent = getBodyContent()) ||
            null == (content = bodyContent.getString()) ||
            0 == (trimContent = content.trim()).length() ||
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
        HttpSession session;

        if (null != (session = pageContext.getSession())) {
            session.setAttribute(ViewHandler.CHARACTER_ENCODING_KEY,
                                 pageContext.getResponse().getCharacterEncoding());
        }
        return rc;
    }


    public String getComponentType() {
        return UIViewRoot.COMPONENT_TYPE;
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
        }
        // BUGDB 10235218
        if (null != viewLocale) {
            ((UIViewRoot) component).setLocale(viewLocale);
            // update the JSTL locale attribute in request scope so that
            // JSTL picks up the locale from viewRoot. This attribute
            // must be updated before the JSTL setBundle tag is called
            // because that is when the new LocalizationContext object
            // is created based on the locale.
            Config.set(pageContext.getRequest(), Config.FMT_LOCALE, viewLocale);
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
