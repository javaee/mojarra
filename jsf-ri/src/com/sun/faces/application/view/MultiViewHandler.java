/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.application.view;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.MalformedURLException;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import javax.faces.webapp.pdl.PageDeclarationLanguage;
import javax.servlet.http.HttpServletResponse;

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.facelets.impl.PageDeclarationLanguageImpl;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import java.awt.event.ActionEvent;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.List;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource2;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.event.MethodExpressionValueChangeListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.MethodExpressionValidator;
import javax.faces.webapp.pdl.ActionSource2AttachedObjectHandler;
import javax.faces.webapp.pdl.ActionSource2AttachedObjectTarget;
import javax.faces.webapp.pdl.AttachedObjectHandler;
import javax.faces.webapp.pdl.AttachedObjectTarget;
import javax.faces.webapp.pdl.EditableValueHolderAttachedObjectHandler;
import javax.faces.webapp.pdl.EditableValueHolderAttachedObjectTarget;
import javax.faces.webapp.pdl.ValueHolderAttachedObjectHandler;
import javax.faces.webapp.pdl.ValueHolderAttachedObjectTarget;

/**
 * This {@link ViewHandler} implementation handles both JSP-based and
 * Facelets/PDL-based views.
 */
public class MultiViewHandler extends ViewHandler {

    // Log instance for this class
    private static final Logger logger = FacesLogger.APPLICATION.getLogger();

    /**
     * The {@link ViewHandlingStrategy} instances used by this {@link ViewHandler}.
     */
    protected ViewHandlingStrategyManager viewHandlingStrategy;
    private PageDeclarationLanguage pdl;

    private String[] configuredExtensions;


    // ------------------------------------------------------------ Constructors


    public MultiViewHandler() {

        viewHandlingStrategy = new ViewHandlingStrategyManager();
        WebConfiguration config = WebConfiguration.getInstance();
        String defaultSuffixConfig =
              config.getOptionValue(WebConfiguration.WebContextInitParameter.DefaultSuffix);
        configuredExtensions = Util.split(defaultSuffixConfig, " ");

    }


    // ------------------------------------------------ Methods from ViewHandler


    /**
     * Do not call the default implementation of {@link javax.faces.application.ViewHandler#initView(javax.faces.context.FacesContext)}
     * if the {@link javax.faces.context.ExternalContext#getRequestCharacterEncoding()} returns a
     * <code>non-null</code> result.
     *
     * @see javax.faces.application.ViewHandler#initView(javax.faces.context.FacesContext)
     */
    @Override
    public void initView(FacesContext context) throws FacesException {

        if (context.getExternalContext().getRequestCharacterEncoding() == null) {
            super.initView(context);
        }

    }


    /**
     * <p>
     * Call {@link com.sun.faces.application.view.ViewHandlingStrategy#renderView(javax.faces.context.FacesContext, MultiViewHandler, javax.faces.component.UIViewRoot)}
     * if the view can be rendered.
     * </p>
     *
     * @see ViewHandler#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
     */
    public void renderView(FacesContext context,
            UIViewRoot viewToRender) throws IOException,
            FacesException {

        Util.notNull("context", context);
        Util.notNull("viewToRender", viewToRender);
        // suppress rendering if "rendered" property on the component is
        // false
        if (!viewToRender.isRendered()) {
            return;
        }

        viewHandlingStrategy.getStrategy(viewToRender.getViewId())
              .renderView(context, this, viewToRender);

    }


    /**
     * <p>
     * Call {@link com.sun.faces.application.view.ViewHandlingStrategy#restoreView(javax.faces.context.FacesContext, MultiViewHandler, String)}.
     * </p>
     *
     * @see ViewHandler#restoreView(javax.faces.context.FacesContext, String)   
     */
    public UIViewRoot restoreView(FacesContext context, String viewId) {

        Util.notNull("context", context);

        return viewHandlingStrategy.getStrategy(viewId).restoreView(context,
                                                                    this,
                                                                    viewId);

    }
    
    @Override
    public void retargetAttachedObjects(FacesContext context,
            UIComponent topLevelComponent,
            List<AttachedObjectHandler> handlers) {
        
        BeanInfo componentBeanInfo = (BeanInfo) 
                topLevelComponent.getAttributes().get(UIComponent.BEANINFO_KEY);
        // PENDING(edburns): log error message if componentBeanInfo is null;
        if (null == componentBeanInfo) {
            return;
        }
        BeanDescriptor componentDescriptor = componentBeanInfo.getBeanDescriptor();
        // There is an entry in targetList for each attached object in the 
        // <composite:interface> section of the composite component.
        List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>)
                componentDescriptor.getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
        // Each entry in targetList will vend one or more UIComponent instances
        // that is to serve as the target of an attached object in the consuming
        // page.
        List<UIComponent> targetComponents = null;
        String forAttributeValue, curTargetName, handlerTagId, componentTagId;
        boolean foundMatch = false;
        
        // For each of the attached object handlers...
        for (AttachedObjectHandler curHandler : handlers) {
            // Get the name given to this attached object by the page author
            // in the consuming page.
            forAttributeValue = curHandler.getFor();
            // For each of the attached objects in the <composite:interface> section
            // of this composite component...
            foundMatch = false;
            for (AttachedObjectTarget curTarget : targetList) {
                if (foundMatch) {
                    break;
                }
                // Get the name given to this attached object target by the
                // composite component author
                curTargetName = curTarget.getName();
                targetComponents = curTarget.getTargets();

                if (curHandler instanceof ActionSource2AttachedObjectHandler &&
                    curTarget instanceof ActionSource2AttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            curHandler.applyAttachedObject(context, curTargetComponent);
                            foundMatch = true;
                        }
                    }
                }
                else if (curHandler instanceof EditableValueHolderAttachedObjectHandler &&
                         curTarget instanceof EditableValueHolderAttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            curHandler.applyAttachedObject(context, curTargetComponent);
                            foundMatch = true;
                        }
                    }
                }
                else if (curHandler instanceof ValueHolderAttachedObjectHandler &&
                         curTarget instanceof ValueHolderAttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            curHandler.applyAttachedObject(context, curTargetComponent);
                            foundMatch = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void retargetMethodExpressions(FacesContext context,
            UIComponent topLevelComponent) {
        BeanInfo componentBeanInfo = (BeanInfo) 
                topLevelComponent.getAttributes().get(UIComponent.BEANINFO_KEY);
        // PENDING(edburns): log error message if componentBeanInfo is null;
        if (null == componentBeanInfo) {
            return;
        }
        PropertyDescriptor attributes[] = componentBeanInfo.getPropertyDescriptors();
        String applyTo = null, strValue = null;
        UIComponent target = null;
        ExpressionFactory expressionFactory = null;
        ValueExpression valueExpression = null;
        MethodExpression toApply = null;
        Class expectedReturnType = null;
        Class expectedParameters[] = null;
        boolean logError = false;

        for (PropertyDescriptor cur : attributes) {
            // If the current attribute represents a ValueExpression
            if (null != (valueExpression =
                  (ValueExpression) cur.getValue("type"))) {
                // take no action on this attribute.
                continue;
            }
            // If the current attribute representes a MethodExpression
            if (null != (valueExpression = (ValueExpression) cur.getValue("method-signature"))) {
                strValue = (String) valueExpression.getValue(context.getELContext());
                if (null != strValue) {
                
                    // This is the name of the attribute on the top level component,
                    // and on the inner component.
                    logError = false;
                    if (null != (valueExpression = (ValueExpression) cur.getValue("applyTo"))) {
                        applyTo = (String) valueExpression.getValue(context.getELContext());
                        if (null == applyTo) {
                            logError = true;
                        }
                    }
                    
                    if (logError) {
                        // PENDING error message in page?
                        logger.severe("Unable to retarget MethodExpression.  " +
                                "Please specify \"applyTo\" attribute on <composite:attribute />");
                        continue;
                    }
                    
                    // This is the inner component to which the attribute should 
                    // be applied
                    target = topLevelComponent.findComponent(applyTo);
                    if (null == applyTo) {
                        // PENDING error message in page?
                        logger.severe("Unable to retarget MethodExpression.  " +
                                "Unable to find inner component with id " + 
                                applyTo + ".");
                        continue;
                    }

                    strValue = cur.getName();

                    // Find the attribute on the top level component
                    valueExpression = (ValueExpression) topLevelComponent.getAttributes().
                            get(strValue);
                    if (null == valueExpression) {
                        // PENDING error message in page?
                        logger.severe("Unable to find attribute with name \""
                                      + strValue
                                      + "\" in top level component in consuming page.  "
                                      + "Page author error.");
                        continue;
                    }

                    // lazily initialize this local variable
                    if (null == expressionFactory) {
                        expressionFactory = context.getApplication().getExpressionFactory();
                    }

                    // If the attribute is one of the pre-defined 
                    // MethodExpression attributes
                    if (strValue.equals("action")) {
                        expectedReturnType = Object.class;
                        expectedParameters = new Class[]{};
                        toApply = expressionFactory.createMethodExpression(context.getELContext(),
                                valueExpression.getExpressionString(),
                                expectedReturnType, expectedParameters);
                        ((ActionSource2) target).setActionExpression(toApply);
                    } else if (strValue.equals("actionListener")) {
                        expectedReturnType = Void.TYPE;
                        expectedParameters = new Class[]{
                                    ActionEvent.class
                                };
                        toApply = expressionFactory.createMethodExpression(context.getELContext(),
                                valueExpression.getExpressionString(),
                                expectedReturnType, expectedParameters);
                        ((ActionSource2) target).addActionListener(new MethodExpressionActionListener(toApply));
                    } else if (strValue.equals("validator")) {
                        expectedReturnType = Void.TYPE;
                        expectedParameters = new Class[]{
                                    FacesContext.class,
                                    UIComponent.class,
                                    Object.class
                                };
                        toApply = expressionFactory.createMethodExpression(context.getELContext(),
                                valueExpression.getExpressionString(),
                                expectedReturnType, expectedParameters);
                        ((EditableValueHolder) target).addValidator(new MethodExpressionValidator(toApply));
                    } else if (strValue.equals("valueChangeListener")) {
                        expectedReturnType = Void.TYPE;
                        expectedParameters = new Class[]{
                                    ValueChangeEvent.class
                                };
                        toApply = expressionFactory.createMethodExpression(context.getELContext(),
                                valueExpression.getExpressionString(),
                                expectedReturnType, expectedParameters);
                        ((EditableValueHolder) target).addValueChangeListener(new MethodExpressionValueChangeListener(toApply));
                    } else {
                        // If the attribute is not one of the pre-defined 
                        // MethodExpression attributes, look it up reflectively,
                        // assuming there is a setter of type MethodExpression

                    }
                }
            }
            
        }
        
    }
    
    
    /**
     * <p>
     * Call {@link com.sun.faces.application.view.ViewHandlingStrategy#createView(javax.faces.context.FacesContext, MultiViewHandler, String)}.
     * </p>
     *
     * @see ViewHandler#restoreView(javax.faces.context.FacesContext, String)
     */
    public UIViewRoot createView(FacesContext context, String viewId) {

        Util.notNull("context", context);

        return viewHandlingStrategy.getStrategy(viewId).createView(context,
                                                                   this,
                                                                   viewId);
    }


    /**
     * <p>
     * This code is currently common to all {@link ViewHandlingStrategy} instances.
     * </p>
     *
     * @see ViewHandler#calculateLocale(javax.faces.context.FacesContext)
     */
    public Locale calculateLocale(FacesContext context) {

        Util.notNull("context", context);

        Locale result = null;
        // determine the locales that are acceptable to the client based on the
        // Accept-Language header and the find the best match among the
        // supported locales specified by the client.
        Iterator<Locale> locales = context.getExternalContext().getRequestLocales();
        while (locales.hasNext()) {
            Locale perf = locales.next();
            result = findMatch(context, perf);
            if (result != null) {
                break;
            }
        }
        // no match is found.
        if (result == null) {
            if (context.getApplication().getDefaultLocale() == null) {
                result = Locale.getDefault();
            } else {
                result = context.getApplication().getDefaultLocale();
            }
        }
        return result;
    }


    /**
     * <p>
     * This code is currently common to all {@link ViewHandlingStrategy} instances.
     * </p>
     *
     * @see ViewHandler#calculateRenderKitId(javax.faces.context.FacesContext)
     */
    public String calculateRenderKitId(FacesContext context) {

        Util.notNull("context", context);

        Map<String,String> requestParamMap = context.getExternalContext()
            .getRequestParameterMap();
        String result = requestParamMap.get(
            ResponseStateManager.RENDER_KIT_ID_PARAM);

        if (result == null) {
            if (null ==
                (result = context.getApplication().getDefaultRenderKitId())) {
                result = RenderKitFactory.HTML_BASIC_RENDER_KIT;
            }
        }
        return result;
    }


    /**
     * <p>
     * This code is currently common to all {@link ViewHandlingStrategy} instances.
     * </p>
     *
     * @see ViewHandler#writeState(javax.faces.context.FacesContext)
     */
    public void writeState(FacesContext context) throws IOException {

        Util.notNull("context", context);
        if (!context.getPartialViewContext().isAjaxRequest()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Begin writing marker for viewId " +
                            context.getViewRoot().getViewId());
            }

            WriteBehindStateWriter writer =
                  WriteBehindStateWriter.getCurrentInstance();
            if (writer != null) {
                writer.writingState();
            }
            context.getResponseWriter()
                  .write(RIConstants.SAVESTATE_FIELD_MARKER);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End writing marker for viewId " +
                            context.getViewRoot().getViewId());
            }
        }

    }


    /**
     * <p>
     * This code is currently common to all {@link ViewHandlingStrategy} instances.
     * </p>
     *
     * @see ViewHandler#getActionURL(javax.faces.context.FacesContext, String)
     */
    public String getActionURL(FacesContext context, String viewId) {

        Util.notNull("context", context);
        Util.notNull("viewId", viewId);

        if (viewId.charAt(0) != '/') {
            String message =
                  MessageUtils.getExceptionMessageString(
                        MessageUtils.ILLEGAL_VIEW_ID_ID,
                        viewId);
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "jsf.illegal_view_id_error", viewId);
            }
        throw new IllegalArgumentException(message);
        }

        // Acquire the context path, which we will prefix on all results
        ExternalContext extContext = context.getExternalContext();
        String contextPath = extContext.getRequestContextPath();

        // Acquire the mapping used to execute this request (if any)
        String mapping = Util.getFacesMapping(context);

        // If no mapping can be identified, just return a server-relative path
        if (mapping == null) {
            return (contextPath + viewId);
        }

        // Deal with prefix mapping
        if (Util.isPrefixMapped(mapping)) {
            if (mapping.equals("/*")) {
                return (contextPath + viewId);
            } else {
                return (contextPath + mapping + viewId);
            }
        }

        // Deal with extension mapping
        int period = viewId.lastIndexOf('.');
        if (period < 0) {
            return (contextPath + viewId + mapping);
        } else if (!viewId.endsWith(mapping)) {
            return (contextPath + viewId.substring(0, period) + mapping);
        } else {
            return (contextPath + viewId);
        }

    }


    /**
     * <p>
     * This code is currently common to all {@link ViewHandlingStrategy} instances.
     * </p>
     *
     * @see ViewHandler#getResourceURL(javax.faces.context.FacesContext, String)
     */
    public String getResourceURL(FacesContext context, String path) {

        ExternalContext extContext = context.getExternalContext();
        if (path.charAt(0) == '/') {
            return (extContext.getRequestContextPath() + path);
        } else {
            return path;
        }

    }
    
    /*
     * PENDING(edburns): This is temporary for Public Review Draft.
     * Really, I think the way to go is to change PageDeclarationLanguage to
     * to take on all of the mantle of ViewHandlingStrategy, do away with
     * ViewHandlingStrategy, provide a factory for PageDeclarationLanguage
     * and specify that the runtime must have a PDL impl for both JSP
     * and Facelets.
     */ 

    @Override
    public PageDeclarationLanguage getPageDeclarationLanguage() {
        if (null == pdl) {
            pdl = new PageDeclarationLanguageImpl();
        }
        return pdl;
    }
    
    


    // ---------------------------------------------------------- Public Methods


   /**
     * <p>
     * Called by {@link com.sun.faces.application.view.ViewHandlingStrategy#createView(javax.faces.context.FacesContext, MultiViewHandler, String)}
     * if the {@link ViewHandlingStrategy} implementation doesn't require custom
     * behavior when restoring the view.
     * </p>
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param viewId the view ID
     * @return a new {@link UIViewRoot}
     *
     * @see ViewHandler#createView(javax.faces.context.FacesContext, String)
     */
    public UIViewRoot createViewPrivate(FacesContext ctx, String viewId) {

        Util.notNull("context", ctx);

        UIViewRoot result = (UIViewRoot)
              ctx.getApplication()
                    .createComponent(UIViewRoot.COMPONENT_TYPE);

        if (viewId != null) {
            String mapping = Util.getFacesMapping(ctx);

            if (mapping != null) {
                if (!Util.isPrefixMapped(mapping)) {
                    viewId = convertViewId(ctx, viewId);
                } else {
                    viewId = normalizeRequestURI(viewId, mapping);
                    if (viewId.equals(mapping)) {
                        // The request was to the FacesServlet only - no
                        // path info
                        // on some containers this causes a recursion in the
                        // RequestDispatcher and the request appears to hang.
                        // If this is detected, return status 404
                        send404Error(ctx);
                    }
                }
            }

            result.setViewId(viewId);
        }

        Locale locale = null;
        String renderKitId = null;

        // use the locale from the previous view if is was one which will be
        // the case if this is called from NavigationHandler. There wouldn't be
        // one for the initial case.
        if (ctx.getViewRoot() != null) {
            locale = ctx.getViewRoot().getLocale();
            renderKitId = ctx.getViewRoot().getRenderKitId();
        }

        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Created new view for " + viewId);
        }
        // PENDING(): not sure if we should set the RenderKitId here.
        // The UIViewRoot ctor sets the renderKitId to the default
        // one.
        // if there was no locale from the previous view, calculate the locale
        // for this view.
        if (locale == null) {
            locale = ctx.getApplication().getViewHandler().calculateLocale(ctx);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(
                      "Locale for this view as determined by calculateLocale "
                      + locale.toString());
            }
        } else {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Using locale from previous view "
                            + locale.toString());
            }
        }

        if (renderKitId == null) {
            renderKitId =
                  ctx.getApplication().getViewHandler()
                        .calculateRenderKitId(ctx);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(
                      "RenderKitId for this view as determined by calculateRenderKitId "
                      + renderKitId);
            }
        } else {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Using renderKitId from previous view "
                            + renderKitId);
            }
        }

        result.setLocale(locale);
        result.setRenderKitId(renderKitId);

        return result;
    }


    /**
     * <p>
     * Called by {@link com.sun.faces.application.view.ViewHandlingStrategy#restoreView(javax.faces.context.FacesContext, MultiViewHandler, String)}
     * if the {@link ViewHandlingStrategy} implementation doesn't require custom
     * behavior when restoring the view.
     * </p>
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param viewId the view ID
     * @return the restored {@link UIViewRoot} or <code>null</code> if the
     *  view cann't be restored.
     *
     * @see ViewHandler#restoreView(javax.faces.context.FacesContext, String)
     */
    public UIViewRoot restoreViewPrivate(FacesContext ctx, String viewId) {

        ExternalContext extContext = ctx.getExternalContext();

        String mapping = Util.getFacesMapping(ctx);
        UIViewRoot viewRoot = null;

        if (mapping != null) {
            if (!Util.isPrefixMapped(mapping)) {
                viewId = convertViewId(ctx, viewId);
            } else {
                viewId = normalizeRequestURI(viewId, mapping);
            }
        }

        // maping could be null if a non-faces request triggered
        // this response.
        if (extContext.getRequestPathInfo() == null && mapping != null &&
            Util.isPrefixMapped(mapping)) {
            // this was probably an initial request
            // send them off to the root of the web application
            try {
                ctx.responseComplete();
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "Response Complete for" + viewId);
                }
                extContext.redirect(extContext.getRequestContextPath());
            } catch (IOException ioe) {
                throw new FacesException(ioe);
            }
        } else {
            // this is necessary to allow decorated impls.
            ViewHandler outerViewHandler =
                  ctx.getApplication().getViewHandler();
            String renderKitId =
                  outerViewHandler.calculateRenderKitId(ctx);
            viewRoot = Util.getStateManager(ctx).restoreView(ctx,
                                                             viewId,
                                                             renderKitId);
        }

        return viewRoot;
        
    }


    // ------------------------------------------------------- Protected Methods


     /**
     * Attempts to find a matching locale based on <code>pref</code> and
     * list of supported locales, using the matching algorithm
     * as described in JSTL 8.3.2.
     * @param context the <code>FacesContext</code> for the current request
     * @param pref the preferred locale
     * @return the Locale based on pref and the matching alogritm specified
     *  in JSTL 8.3.2
     */
    protected Locale findMatch(FacesContext context, Locale pref) {

        Locale result = null;
        Iterator<Locale> it = context.getApplication().getSupportedLocales();
        while (it.hasNext()) {
            Locale supportedLocale = it.next();

            if (pref.equals(supportedLocale)) {
                // exact match
                result = supportedLocale;
                break;
            } else {
                // Make sure the preferred locale doesn't have country
                // set, when doing a language match, For ex., if the
                // preferred locale is "en-US", if one of supported
                // locales is "en-UK", even though its language matches
                // that of the preferred locale, we must ignore it.
                if (pref.getLanguage().equals(supportedLocale.getLanguage()) &&
                     supportedLocale.getCountry().length() == 0) {
                    result = supportedLocale;
                }
            }
        }
        // if it's not in the supported locales,
        if (null == result) {
            Locale defaultLocale = context.getApplication().getDefaultLocale();
            if (defaultLocale != null) {
                if ( pref.equals(defaultLocale)) {
                    // exact match
                    result = defaultLocale;
                } else {
                    // Make sure the preferred locale doesn't have country
                    // set, when doing a language match, For ex., if the
                    // preferred locale is "en-US", if one of supported
                    // locales is "en-UK", even though its language matches
                    // that of the preferred locale, we must ignore it.
                    if (pref.getLanguage().equals(defaultLocale.getLanguage()) &&
                         defaultLocale.getCountry().length() == 0) {
                        result = defaultLocale;
                    }
                }
            }
        }

        return result;

    }


    /**
     * <p>if the specified mapping is a prefix mapping, and the provided
     * request URI (usually the value from <code>ExternalContext.getRequestServletPath()</code>)
     * starts with <code>mapping + '/'</code>, prune the mapping from the
     * URI and return it, otherwise, return the original URI.
     * @param uri the servlet request path
     * @param mapping the FacesServlet mapping used for this request
     * @return the URI without additional FacesServlet mappings
     * @since 1.2
     */
    protected String normalizeRequestURI(String uri, String mapping) {

        if (mapping == null || !Util.isPrefixMapped(mapping)) {
            return uri;
        } else {
            int length = mapping.length() + 1;
            StringBuilder builder = new StringBuilder(length);
            builder.append(mapping).append('/');
            String mappingMod = builder.toString();
            boolean logged = false;
            while (uri.startsWith(mappingMod)) {
                if (!logged && logger.isLoggable(Level.WARNING)) {
                    logged = true;
                    logger.log(Level.WARNING,
                               "jsf.viewhandler.requestpath.recursion",
                               new Object[] {uri, mapping});
                }
                uri = uri.substring(length - 1);
            }
            return uri;
        }

    }


    /**
     * <p>
     * Send {@link HttpServletResponse#SC_NOT_FOUND} (404) to the client.
     * </p>
     *
     * @param context the {@link FacesContext} for the current request
     */
    protected void send404Error(FacesContext context) {

        HttpServletResponse response = (HttpServletResponse)
             context.getExternalContext().getResponse();
        try {
            context.responseComplete();
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }

    }


    /**
     * <p>Adjust the viewID per the requirements of {@link #renderView}.</p>
     *
     * @param context current {@link javax.faces.context.FacesContext}
     * @param viewId  incoming view ID
     * @return the view ID with an altered suffix mapping (if necessary)
     */
    protected String convertViewId(FacesContext context, String viewId) {

        // if the viewId doesn't already use the above suffix,
        // replace or append.
        StringBuilder buffer = new StringBuilder(viewId);
        for (String ext : configuredExtensions) {
            if (viewId.endsWith(ext)) {
                return viewId;
            }
            int extIdx = viewId.lastIndexOf('.');
            if (extIdx != -1) {
                buffer.replace(extIdx, viewId.length(), ext);
            } else {
                // no extension in the provided viewId, append the suffix
                buffer.append(ext);
            }
            String convertedViewId = buffer.toString();
            try {
                if (context.getExternalContext().getResource(convertedViewId) != null) {
                    // RELEASE_PENDING (rlubke,driscoll) cache the lookup
                    return convertedViewId;
                } else {
                    // reset the buffer to check for the next extension
                    buffer.setLength(0);
                    buffer.append(viewId);
                }
            } catch (MalformedURLException e) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE,
                               e.toString(),
                               e);
                }
            }
        }

        // unable to find any resource match that the default ViewHandler
        // can deal with.  Return the viewId as it was passed.  There is
        // probably another ViewHandler in the stack that will handle this.
        return viewId;

    }


}
