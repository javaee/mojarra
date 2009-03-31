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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpServletResponse;

import com.sun.faces.RIConstants;
import com.sun.faces.facelets.tag.jsf.CompositeComponentTagHandler;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import java.awt.event.ActionEvent;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.*;

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.component.*;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.event.MethodExpressionValueChangeListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.MethodExpressionValidator;
import javax.faces.view.*;
import javax.faces.view.facelets.BehaviorHandler;

/**
 * This {@link ViewHandler} implementation handles both JSP-based and
 * Facelets/PDL-based views.
 */
public class MultiViewHandler extends ViewHandler {

    // Log instance for this class
    private static final Logger logger = FacesLogger.APPLICATION.getLogger();

    private String[] configuredExtensions;
    
    private ViewDeclarationLanguageFactory pdlFactory;


    // ------------------------------------------------------------ Constructors


    public MultiViewHandler() {

        WebConfiguration config = WebConfiguration.getInstance();
        String defaultSuffixConfig =
              config.getOptionValue(WebConfiguration.WebContextInitParameter.DefaultSuffix);
        configuredExtensions = Util.split(defaultSuffixConfig, " ");
        pdlFactory = (ViewDeclarationLanguageFactory) 
                FactoryFinder.getFactory(FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY);

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
     * Call {@link ViewDeclarationLanguage#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)}
     * if the view can be rendered.
     * </p>
     *
     * @see ViewHandler#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
     */
    public void renderView(FacesContext context, UIViewRoot viewToRender)
    throws IOException, FacesException {

        Util.notNull("context", context);
        Util.notNull("viewToRender", viewToRender);

        pdlFactory.getViewDeclarationLanguage(viewToRender.getViewId())
              .renderView(context, viewToRender);

    }


    /**
     * <p>
     * Call {@link ViewDeclarationLanguage#restoreView(javax.faces.context.FacesContext, String)}.
     * </p>
     *
     * @see ViewHandler#restoreView(javax.faces.context.FacesContext, String)   
     */
    public UIViewRoot restoreView(FacesContext context, String viewId) {

        Util.notNull("context", context);
        return pdlFactory.getViewDeclarationLanguage(viewId)
              .restoreView(context, viewId);

    }


    /**
     * @see ViewHandler#retargetAttachedObjects(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.util.List)
     */
    @Override
    public void retargetAttachedObjects(FacesContext context,
                                        UIComponent topLevelComponent,
                                        List<AttachedObjectHandler> handlers) {

        //List<AttachedObjectHandler> handlers =
        //      getAttachedObjectHandlers(topLevelComponent, false);

        if (handlers == null || handlers.isEmpty()) {
            return;
        }
        
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

        // For each of the attached object handlers...
        for (AttachedObjectHandler curHandler : handlers) {
            // Get the name given to this attached object by the page author
            // in the consuming page.
            forAttributeValue = curHandler.getFor();
            // For each of the attached objects in the <composite:interface> section
            // of this composite component...
            for (AttachedObjectTarget curTarget : targetList) {
                // Get the name given to this attached object target by the
                // composite component author
                curTargetName = curTarget.getName();
                targetComponents = curTarget.getTargets(topLevelComponent);

                if (curHandler instanceof ActionSource2AttachedObjectHandler &&
                    curTarget instanceof ActionSource2AttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            retargetHandler(context, curHandler, curTargetComponent);
                        }
                        break;
                    }
                }
                else if (curHandler instanceof EditableValueHolderAttachedObjectHandler &&
                         curTarget instanceof EditableValueHolderAttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            retargetHandler(context, curHandler, curTargetComponent);
                        }
                        break;
                    }
                }
                else if (curHandler instanceof ValueHolderAttachedObjectHandler &&
                         curTarget instanceof ValueHolderAttachedObjectTarget) {
                    if (forAttributeValue.equals(curTargetName)) {
                        for (UIComponent curTargetComponent : targetComponents) {
                            retargetHandler(context, curHandler, curTargetComponent);
                        }
                        break;
                    }
                } else if(curHandler instanceof BehaviorHolderAttachedObjectHandler && 
                        curTarget instanceof BehaviorHolderAttachedObjectTarget) {
                    BehaviorHolderAttachedObjectHandler behaviorHandler = (BehaviorHolderAttachedObjectHandler) curHandler;
                    BehaviorHolderAttachedObjectTarget behaviorTarget = (BehaviorHolderAttachedObjectTarget) curTarget;
                    String eventName = behaviorHandler.getEventName();
                    if((null !=eventName && eventName.equals(curTargetName))||(null ==eventName && behaviorTarget.isDefaultEvent())){
                        for (UIComponent curTargetComponent : targetComponents) {
                            retargetHandler(context, curHandler, curTargetComponent);
                        }
                    }
                }


            }
        }
    }



    /**
     * @see ViewHandler#retargetMethodExpressions(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
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
        ExpressionFactory expressionFactory = null;
        ValueExpression valueExpression = null;
        MethodExpression toApply = null;
        Class expectedReturnType = null;
        Class expectedParameters[] = null;

        for (PropertyDescriptor cur : attributes) {
            // If the current attribute represents a ValueExpression
            if (null != (valueExpression =
                  (ValueExpression) cur.getValue("type"))) {
                // take no action on this attribute.
                continue;
            }
            // If the current attribute representes a MethodExpression
            if (null != (valueExpression = (ValueExpression) cur.getValue("method-signature"))) {
                String methodSignature = (String) valueExpression.getValue(context.getELContext());
                if (null != methodSignature) {
                
                    // This is the name of the attribute on the top level component,
                    // and on the inner component.
                    String targets = null;
                    if (null != (valueExpression = (ValueExpression) cur.getValue("targets"))) {
                        targets = (String) valueExpression.getValue(context.getELContext());
                    }
                    
                    if (targets == null) {
                        targets = cur.getName();
                    }
                    
                    if (null == targets || 0 == targets.length()) {
                        // PENDING error message in page?
                        logger.severe("Unable to retarget MethodExpression: " +
                                methodSignature);
                        continue;
                    }
                    
                    
                    String[] targetIds = targets.split(" ");
                    
                    for (String curTarget : targetIds) {
                    
                        String attrName = cur.getName();
                        UIComponent target = topLevelComponent.findComponent(curTarget);
                        // Find the attribute on the top level component
                        valueExpression = (ValueExpression) topLevelComponent.getAttributes().
                                get(attrName);
                        if (null == valueExpression) {
                            throw new FacesException(
                                  "Unable to find attribute with name \""
                                  + attrName
                                  + "\" in top level component in consuming page.  "
                                  + "Page author error.");
                        }

                        // lazily initialize this local variable
                        if (null == expressionFactory) {
                            expressionFactory = context.getApplication().getExpressionFactory();
                        }

                        // If the attribute is one of the pre-defined 
                        // MethodExpression attributes
                        boolean
                                isAction = false, isActionListener = false, 
                                isValidator = false, isValueChangeListener = false;
                        if ((isAction = attrName.equals("action")) ||
                            (isActionListener = attrName.equals("actionListener")) ||
                            (isValidator = attrName.equals("validator")) ||
                            (isValueChangeListener = attrName.equals("valueChangeListener"))) {
                            // This is the inner component to which the attribute should 
                            // be applied
                            target = topLevelComponent.findComponent(curTarget);
                            if (null == target) {
                                throw new FacesException(valueExpression.toString()
                                                         + " : Unable to re-target MethodExpression as inner component referenced by target id '"
                                                         + curTarget
                                                         + "' cannot be found.");
                            }

                            if (isAction) {
                                expectedReturnType = Object.class;
                                expectedParameters = new Class[]{};
                                toApply = expressionFactory.createMethodExpression(context.getELContext(),
                                        valueExpression.getExpressionString(),
                                        expectedReturnType, expectedParameters);
                                ((ActionSource2) target).setActionExpression(toApply);
                            } else if (isActionListener) {
                                expectedReturnType = Void.TYPE;
                                expectedParameters = new Class[]{
                                            ActionEvent.class
                                        };
                                toApply = expressionFactory.createMethodExpression(context.getELContext(),
                                        valueExpression.getExpressionString(),
                                        expectedReturnType, expectedParameters);
                                ((ActionSource2) target).addActionListener(new MethodExpressionActionListener(toApply));
                            } else if (isValidator) {
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
                            } else if (isValueChangeListener) {
                                expectedReturnType = Void.TYPE;
                                expectedParameters = new Class[]{
                                            ValueChangeEvent.class
                                        };
                                toApply = expressionFactory.createMethodExpression(context.getELContext(),
                                        valueExpression.getExpressionString(),
                                        expectedReturnType, expectedParameters);
                                ((EditableValueHolder) target).addValueChangeListener(new MethodExpressionValueChangeListener(toApply));
                            }
                        } else {
                            // There is no explicit methodExpression property on
                            // an inner component to which this MethodExpression
                            // should be retargeted.  In this case, replace the
                            // ValueExpression with a method expresson.
                            
                            // Pull apart the methodSignature to derive the 
                            // expectedReturnType and expectedParameters

                            // PENDING(rlubke,jimdriscoll) bulletproof this
                            
                            assert(null != methodSignature);
                            methodSignature = methodSignature.trim();
                            
                            // Get expectedReturnType
                            int j, i = methodSignature.indexOf(" ");
                            if (-1 != i) {
                                String strValue = methodSignature.substring(0, i);
                                try {
                                    expectedReturnType = Util.getTypeFromString(strValue);
                                } catch (ClassNotFoundException cnfe) {
                                    throw new FacesException(cur.getValue("method-signature") + " : Unable to load type '" + strValue + '\'');
                                }
                            } else {
                                logger.severe("Unable to determine expected return type for " +
                                        methodSignature);
                                continue;
                            }
                            
                            // derive the arguments
                            i = methodSignature.indexOf("(");
                            if (-1 != i) {
                                j = methodSignature.indexOf(")", i+1);
                                if (-1 != j) {
                                    String strValue = methodSignature.substring(i + 1, j);
                                    if (0 < strValue.length()) {
                                        String [] params = strValue.split(",");
                                        expectedParameters = new Class[params.length];
                                        boolean exceptionThrown = false;
                                        for (i = 0; i < params.length; i++) {
                                            try {
                                                expectedParameters[i] = 
                                                        Util.getTypeFromString(params[i]);
                                            } catch (ClassNotFoundException cnfe) {
                                                logger.log(Level.SEVERE,
                                                        "Unable to determine expected return type for " +
                                                        methodSignature, cnfe);
                                                exceptionThrown = true;
                                                break;
                                            }
                                        }
                                        if (exceptionThrown) {
                                            continue;
                                        }
                                        
                                    } else {
                                        expectedParameters = new Class[]{};
                                    }
                                }
                                
                            }
                            
                            assert(null != expectedReturnType);
                            assert(null != expectedParameters);
                            
                            toApply = expressionFactory.createMethodExpression(context.getELContext(),
                                    valueExpression.getExpressionString(),
                                    expectedReturnType, expectedParameters);
                            topLevelComponent.getAttributes().put(attrName, toApply);
                        }
                    }
                }
            }
        }

    }
    
    
    /**
     * <p>
     * Derive the actual view ID (i.e. the physical resource) and call
     * call {@link ViewDeclarationLanguage#createView(javax.faces.context.FacesContext, String)}.
     * </p>
     *
     * @see ViewHandler#restoreView(javax.faces.context.FacesContext, String)
     */
    public UIViewRoot createView(FacesContext context, String viewId) {

        Util.notNull("context", context);
        return pdlFactory.getViewDeclarationLanguage(viewId).createView(context,
                                                                   viewId);

    }

    @Override
    public String deriveViewId(FacesContext facesContext, String input) {
        String viewId = null;
        
        viewId = Util.deriveViewId(facesContext, input);
        
        return viewId;
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


    @Override
    public String getBookmarkableURL(FacesContext context,
                                     String viewId,
                                     Map<String,List<String>> parameters,
                                     boolean includeViewParams) {

        Map<String,List<String>> params;
        if (includeViewParams) {
            params = getFullParameterList(context, viewId, parameters);
        } else {
            params = parameters;
        }
        ExternalContext ectx = context.getExternalContext();
        return ectx.encodeActionURL(ectx.encodeBookmarkableURL(getActionURL(context, viewId), params));

    }


    /**
     * @see ViewHandler#getRedirectURL(javax.faces.context.FacesContext, String, java.util.Map, boolean)
     */
    @Override
    public String getRedirectURL(FacesContext context, String viewId, Map<String, List<String>> parameters, boolean includeViewParams) {

        Map<String,List<String>> params;
        if (includeViewParams) {
            params = getFullParameterList(context, viewId, parameters);
        } else {
            params = parameters;
        }
        ExternalContext ectx = context.getExternalContext();
        return ectx.encodeActionURL(ectx.encodeRedirectURL(getActionURL(context, viewId), params));

    }

    /**
     * @see ViewHandler#getPageDeclarationLanguage(javax.faces.context.FacesContext, String) 
     */
    @Override
    public ViewDeclarationLanguage getViewDeclarationLanguage(FacesContext context,
                                                              String viewId) {

        return pdlFactory.getViewDeclarationLanguage(viewId);

    }
    

    // ------------------------------------------------------- Protected Methods


    protected Map<String,List<String>> getFullParameterList(FacesContext ctx,
                                                            String viewId,
                                                            Map<String,List<String>> existingParameters) {

        Map<String,List<String>> copy;
        if (existingParameters == null || existingParameters.isEmpty()) {
            copy = new LinkedHashMap<String,List<String>>(4);
        } else {
          copy = new LinkedHashMap<String,List<String>>(existingParameters);
        }
        addViewParameters(ctx, viewId, copy);
        return copy;

    }


    protected void addViewParameters(FacesContext ctx,
                                     String viewId,
                                     Map<String,List<String>> existingParameters) {

        UIViewRoot currentRoot = ctx.getViewRoot();
        String currentViewId = extractViewId(currentRoot.getViewId());
        ViewDeclarationLanguage pdl = null;
        Collection<UIViewParameter> toViewParams;
        Collection<UIViewParameter> currentViewParams;
        boolean currentIsSameAsNew = false;
        String targetViewId = extractViewId(viewId);

        pdl = getViewDeclarationLanguage(ctx, currentViewId);
        currentViewParams = ViewMetadata.getViewParameters(currentRoot);

        if (currentViewId.equals(targetViewId)) {
            currentIsSameAsNew = true;
            toViewParams = currentViewParams;
        }
        else {
            pdl = getViewDeclarationLanguage(ctx, targetViewId);
            ViewMetadata viewMetadata = pdl.getViewMetadata(ctx, targetViewId);
            UIViewRoot root = viewMetadata.createMetadataView(ctx);
            toViewParams = ViewMetadata.getViewParameters(root);
        }

        if (toViewParams.isEmpty()) {
            return;
        }

        for (UIViewParameter viewParam : toViewParams) {
            String value = null;
            // don't bother looking at view parameter if it's been overridden
            if (existingParameters.containsKey(viewParam.getName())) {
                continue;
            }
            else if (paramHasValueExpression(viewParam)) {
                value = viewParam.getStringValueFromModel(ctx);
            }
            else {
                // Anonymous view parameter:
                // Get string value from UIViewParameter instance stored in current view
                if (currentIsSameAsNew) {
                    value = viewParam.getStringValue(ctx);
                }
                // ...or transfer string value from matching UIViewParameter instance stored in current view
                else {
                    value = getStringValueToTransfer(ctx,
                                                     viewParam,
                                                     toViewParams);
                }
            }
            if (value != null) {
                List<String> existing = existingParameters.get(viewParam.getName());
                if (existing == null) {
                    existing = new ArrayList<String>(4);
                    existingParameters.put(viewParam.getName(), existing);
                }
                existing.add(value);
            }

        }
    }





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
     * <p>
     * Send {@link HttpServletResponse#SC_NOT_FOUND} (404) to the client.
     * </p>
     *
     * @param context the {@link FacesContext} for the current request
     */
    protected void send404Error(FacesContext context) {

        try {
            context.responseComplete();
            context.getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "");
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }

    }


    // --------------------------------------------------------- Private Methods


    private void retargetHandler(FacesContext context,
                                 AttachedObjectHandler handler,
                                 UIComponent targetComponent) {

        if (UIComponent.isCompositeComponent(targetComponent)) {
            // RELEASE_PENDING Not keen on calling CompositeComponentTagHandler here....
            List<AttachedObjectHandler> nHandlers =
                  CompositeComponentTagHandler
                        .getAttachedObjectHandlers(targetComponent);
            nHandlers.add(handler);
            retargetAttachedObjects(context, targetComponent, nHandlers);
        } else {
            handler.applyAttachedObject(context, targetComponent);
        }
        
    }


    private static boolean paramHasValueExpression(UIViewParameter param) {

        boolean result = false;
        result = (null != param.getValueExpression("value"));
        return result;

    }


    private static String getStringValueToTransfer(FacesContext context,
                                                   UIViewParameter param,
                                                   Collection<UIViewParameter> viewParams) {

        if (viewParams != null && !viewParams.isEmpty()) {
            for (UIViewParameter candidate : viewParams) {
                if ((null != candidate.getName() && null != param.getName()) &&
                    candidate.getName().equals(param.getName())) {
                    return candidate.getStringValue(context);
                }
            }
        }

        return null;

    }


    private static String extractViewId(String viewId) {

        int idx = viewId.indexOf('?');
        if (idx != -1) {
            return viewId.substring(0, idx);
        }
        return viewId;

    }

    
}
