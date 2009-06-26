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

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.WebConfiguration;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.PartialStateSaving;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FullStateSavingViewIds;
import com.sun.faces.facelets.Facelet;
import com.sun.faces.facelets.FaceletFactory;
import com.sun.faces.facelets.compiler.Compiler;
import com.sun.faces.facelets.compiler.SAXCompiler;
import com.sun.faces.facelets.el.VariableMapperWrapper;
import com.sun.faces.facelets.tag.composite.CompositeComponentBeanInfo;
import com.sun.faces.facelets.tag.jsf.CompositeComponentTagHandler;
import com.sun.faces.facelets.tag.ui.UIDebug;
import com.sun.faces.scripting.GroovyHelper;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.RequestStateManager;
import com.sun.faces.util.Util;

import java.awt.event.ActionEvent;
import java.beans.BeanDescriptor;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.view.StateManagementStrategy;
import javax.faces.view.ViewMetadata;
import javax.faces.view.facelets.FaceletContext;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.component.ActionSource2;
import javax.faces.component.EditableValueHolder;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.event.MethodExpressionValueChangeListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.MethodExpressionValidator;
import javax.faces.view.ActionSource2AttachedObjectHandler;
import javax.faces.view.ActionSource2AttachedObjectTarget;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.AttachedObjectTarget;
import javax.faces.view.BehaviorHolderAttachedObjectHandler;
import javax.faces.view.BehaviorHolderAttachedObjectTarget;
import javax.faces.view.EditableValueHolderAttachedObjectHandler;
import javax.faces.view.EditableValueHolderAttachedObjectTarget;
import javax.faces.view.ValueHolderAttachedObjectHandler;
import javax.faces.view.ValueHolderAttachedObjectTarget;

/**
 * This {@link ViewHandlingStrategy} handles Facelets/PDL-based views.
 */
public class FaceletViewHandlingStrategy extends ViewHandlingStrategy {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    // FaceletFactory singleton for this application
    private FaceletFactory faceletFactory;

    // Array of viewId extensions that should be handled by Facelets
    private String[] extensionsArray;

    // Array of viewId prefixes that should be handled by Facelets
    private String[] prefixesArray;
    
    public static final String IS_BUILDING_METADATA =
          FaceletViewHandlingStrategy.class.getName() + ".IS_BUILDING_METADATA";
    
    private StateManagementStrategyImpl stateManagementStrategy;

    private boolean partialStateSaving;
    private Set<String> fullStateViewIds;
    private boolean groovyAvailable;
    
    // ------------------------------------------------------------ Constructors


    public FaceletViewHandlingStrategy() {

        initialize();

    }

    // ------------------------------------ Methods from ViewDeclarationLanguage

     @Override
     public StateManagementStrategy getStateManagementStrategy(FacesContext context, String viewId) {

         // 'null' return here means we're defaulting to the 1.2 style state saving.
         return (context.getAttributes().containsKey("partialStateSaving") ? stateManagementStrategy : null);

     }
    
    @Override
    public BeanInfo getComponentMetadata(FacesContext context, 
            Resource ccResource) {
        // PENDING this implementation is terribly wasteful.
        // Must find a better way.
        CompositeComponentBeanInfo result;
        FaceletContext ctx = (FaceletContext)
                context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
        FaceletFactory factory = (FaceletFactory)
              RequestStateManager.get(context, RequestStateManager.FACELET_FACTORY);
        VariableMapper orig = ctx.getVariableMapper();
        UIComponent tmp = context.getApplication().createComponent("javax.faces.NamingContainer");
        UIPanel facetComponent = (UIPanel)
                context.getApplication().createComponent("javax.faces.Panel");
        facetComponent.setRendererType("javax.faces.Group");
        tmp.getFacets().put(UIComponent.COMPOSITE_FACET_NAME, facetComponent);
        // We have to put the resource in here just so the classes that eventually
        // get called by facelets have access to it.
        tmp.getAttributes().put(Resource.COMPONENT_RESOURCE_KEY, 
                ccResource);
        
        Facelet f;

        try {
            f = factory.getFacelet(ccResource.getURL());
            VariableMapper wrapper = new VariableMapperWrapper(orig) {

                @Override
                public ValueExpression resolveVariable(String variable) {
                    return super.resolveVariable(variable);
                }
                
            };
            ctx.setVariableMapper(wrapper);
            context.getAttributes().put(IS_BUILDING_METADATA, Boolean.TRUE);
            f.apply(context, facetComponent);
        } catch (Exception e) {
            if (e instanceof FacesException) {
                throw (FacesException) e;
            } else {
                throw new FacesException(e);
            }
        }
        finally {
            context.getAttributes().remove(IS_BUILDING_METADATA);
            ctx.setVariableMapper(orig);
        }
        result = (CompositeComponentBeanInfo) 
                tmp.getAttributes().get(UIComponent.BEANINFO_KEY);
        
        return result;
    }

    @Override
    public ViewMetadata getViewMetadata(FacesContext context, String viewId) {

        return new ViewMetadataImpl(viewId);

    }
    
    /**
     * @see javax.faces.view.ViewDeclarationLanguage#getScriptComponentResource(javax.faces.context.FacesContext, javax.faces.application.Resource)
     */
    public Resource getScriptComponentResource(FacesContext context,
            Resource componentResource) {

        if (!groovyAvailable) {
            return null;
        }
        Resource result = null;

        String resourceName = componentResource.getResourceName();
        if (resourceName.endsWith(".xhtml")) {
            resourceName = resourceName.substring(0, 
                    resourceName.length() - 6) + ".groovy";
            ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
            result = resourceHandler.createResource(resourceName, 
                    componentResource.getLibraryName());
        }
        
        return result;
    }


    /**
     * @see javax.faces.view.ViewDeclarationLanguage#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
     */
    public void renderView(FacesContext ctx,
                           UIViewRoot viewToRender)
    throws IOException {

        // suppress rendering if "rendered" property on the component is
        // false
        if (!viewToRender.isRendered()) {
            return;
        }

        // log request
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Rendering View: " + viewToRender.getViewId());
        }

        WriteBehindStateWriter stateWriter = null;
        try {
            // Only build the view if this view has not yet been built.
            if (!Util.isViewPopulated(ctx, viewToRender)) {
                this.buildView(ctx, viewToRender);
            }

            // setup writer and assign it to the ctx
            ResponseWriter origWriter = ctx.getResponseWriter();
            if (origWriter == null) {
                origWriter = createResponseWriter(ctx);
            }

            stateWriter = new WriteBehindStateWriter(origWriter,
                                                     ctx,
                                                     responseBufferSize);

            ResponseWriter writer = origWriter.cloneWithWriter(stateWriter);
            ctx.setResponseWriter(writer);

            // render the view to the response
            writer.startDocument();
            viewToRender.encodeAll(ctx);
            writer.endDocument();

            // finish writing
            writer.close();


            boolean writtenState = stateWriter.stateWritten();
            // flush to origWriter
            if (writtenState) {
                stateWriter.flushToWriter();
            }

        } catch (FileNotFoundException fnfe) {
            this.handleFaceletNotFound(ctx,
                                       viewToRender.getViewId(),
                                       fnfe.getMessage());
        } catch (Exception e) {
            this.handleRenderException(ctx, e);
        } finally {
            if (stateWriter != null)
                stateWriter.release();
        }

    }


    /**
     * <p>
     * If {@link UIDebug#debugRequest(javax.faces.context.FacesContext)}} is <code>true</code>,
     * simply return a new UIViewRoot(), otherwise, call the default logic.
     * </p>
     * @see {@link javax.faces.view.ViewDeclarationLanguage#restoreView(javax.faces.context.FacesContext, String)}
     */
    @Override
    public UIViewRoot restoreView(FacesContext ctx,
                                  String viewId) {

        updateStateSavingType(ctx, viewId);
        if (UIDebug.debugRequest(ctx)) {
            ctx.getApplication().createComponent(UIViewRoot.COMPONENT_TYPE);
        }

        return super.restoreView(ctx, viewId);

    }


    /**
     * @see ViewHandlingStrategy#retargetAttachedObjects(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.util.List)
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
     * @see ViewHandlingStrategy#retargetMethodExpressions(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
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
                        LOGGER.severe("Unable to retarget MethodExpression: " +
                                methodSignature);
                        continue;
                    }
                    
                    
                    String[] targetIds = targets.split(" ");
                    
                    for (String curTarget : targetIds) {
                    
                        String attrName = cur.getName();
                        UIComponent target = topLevelComponent.findComponent(curTarget);
                        // Find the attribute on the top level component
                        Object attrValue = topLevelComponent.getValueExpression(attrName);
                        // In all cases but one, the attrValue will be a ValueExpression.
                        // The only case when it will not be a ValueExpression is
                        // the case when the attrName is an action, and even then, it'll be a
                        // ValueExpression in all cases except when it's a literal string.
                        if (null == attrValue) {
                            attrValue = cur.getValue("default");
                            if (null == attrValue) {
                                throw new FacesException(
                                        "Unable to find attribute with name \"" + attrName
                                  + "\" in top level component in consuming page, "
                                  + " or with default value in composite component.  "
                                  + "Page author or composite component author error.");
                            }
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
                            
                            // Special case: explicitly rul out the case where 
                            // the action is a literal string.  This case will be
                            // handled below.
                            if (!isAction && attrValue instanceof ValueExpression) {
                                valueExpression = (ValueExpression) attrValue;
                            }
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
                                String expr = (attrValue instanceof ValueExpression) ?
                                    ((ValueExpression) attrValue).getExpressionString() : attrValue.toString();
                                toApply = expressionFactory.createMethodExpression(context.getELContext(),
                                        expr,
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
                            valueExpression = (ValueExpression) attrValue;
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
                                LOGGER.severe("Unable to determine expected return type for " +
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
                                                LOGGER.log(Level.SEVERE,
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
                    topLevelComponent.setValueExpression(cur.getName(), null);
                }
            }
        }

    }

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


    /**
     * @see javax.faces.view.ViewDeclarationLanguage#createView(javax.faces.context.FacesContext, String)
     * @return
     */
    @Override
    public UIViewRoot createView(FacesContext ctx,
                                 String viewId) {

        if (UIDebug.debugRequest(ctx)) {
            UIViewRoot root = (UIViewRoot)
                  ctx.getApplication().createComponent(UIViewRoot.COMPONENT_TYPE);
            root.setViewId(viewId);
            return root;
        }

        return super.createView(ctx, viewId);
        
    }
    

    // --------------------------------------- Methods from ViewHandlingStrategy


    /**
     * @param viewId the view ID to check
     * @return <code>true</code> if assuming a default configuration and the
     *  view ID's extension is <code>.xhtml</code>  Otherwise try to match
     *  the view ID based on the configured extendsion and prefixes.
     *
     * @see com.sun.faces.config.WebConfiguration.WebContextInitParameter#FaceletsViewMappings
     */
    @Override
    public boolean handlesViewId(String viewId) {
         if (viewId != null) {
            // If there's no extensions array or prefixes array, then
            // assume defaults.  .xhtml extension is handled by
            // the FaceletViewHandler and .jsp will be handled by
            // the JSP view handler
            if ((extensionsArray == null) && (prefixesArray == null)) {
                return (viewId.endsWith(ViewHandler.DEFAULT_FACELETS_SUFFIX));
            }

            if (extensionsArray != null) {
                for (int i = 0; i < extensionsArray.length; i++) {
                    String extension = extensionsArray[i];
                    if (viewId.endsWith(extension)) {
                        return true;
                    }
                }
            }

            if (prefixesArray != null) {
                for (int i = 0; i < prefixesArray.length; i++) {
                    String prefix = prefixesArray[i];
                    if (viewId.startsWith(prefix)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * Build the view.
     * @param ctx the {@link FacesContext} for the current request
     * @param view the {@link UIViewRoot} to populate based
     *  of the Facelet template
     * @throws IOException if an error occurs building the view.
     */
    @Override
    public void buildView(FacesContext ctx, UIViewRoot view)
    throws IOException {

        if (Util.isViewPopulated(ctx, view)) {
            return;
        }
        updateStateSavingType(ctx, view.getViewId());
        view.setViewId(view.getViewId());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Building View: " + view.getViewId());
        }
        if (faceletFactory == null) {
            ApplicationAssociate associate = ApplicationAssociate.getInstance(ctx.getExternalContext());
            faceletFactory = associate.getFaceletFactory();
            assert (faceletFactory != null);
        }
        RequestStateManager.set(ctx,
                                RequestStateManager.FACELET_FACTORY,
                                faceletFactory);
        Facelet f = faceletFactory.getFacelet(view.getViewId());

        // populate UIViewRoot
        f.apply(ctx, view);
        doPostBuildActions(view);
        Util.setViewPopulated(ctx, view);

    }


    // ------------------------------------------------------- Protected Methods





    /**
     * Initialize the core Facelets runtime.
     */
    protected void initialize() {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Initializing FaceletViewHandlingStrategy");
        }

        this.initializeMappings();
        WebConfiguration config = WebConfiguration.getInstance();
        partialStateSaving = config.isOptionEnabled(PartialStateSaving);
        
        if (partialStateSaving) {
            String[] viewIds = config.getOptionValue(FullStateSavingViewIds, ",");
            fullStateViewIds = new HashSet<String>(viewIds.length, 1.0f);
            fullStateViewIds.addAll(Arrays.asList(viewIds));
            this.stateManagementStrategy = new StateManagementStrategyImpl(this);
        }

        groovyAvailable = GroovyHelper.isGroovyAvailable(FacesContext.getCurrentInstance());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Initialization Successful");
        }

    }


    /**
     * Initialize mappings, during the first request.
     */
    protected void initializeMappings() {

        String viewMappings = webConfig
              .getOptionValue(WebContextInitParameter.FaceletsViewMappings);
        if ((viewMappings != null) && (viewMappings.length() > 0)) {
            String[] mappingsArray = Util.split(viewMappings, ";");

            List<String> extensionsList = new ArrayList<String>(mappingsArray.length);
            List<String> prefixesList = new ArrayList<String>(mappingsArray.length);

            for (int i = 0; i < mappingsArray.length; i++) {
                String mapping = mappingsArray[i].trim();
                int mappingLength = mapping.length();
                if (mappingLength <= 1) {
                    continue;
                }

                if (mapping.charAt(0) == '*') {
                    extensionsList.add(mapping.substring(1));
                } else if (mapping.charAt(mappingLength - 1) == '*') {
                    prefixesList.add(mapping.substring(0, mappingLength - 1));
                }
            }

            extensionsArray = new String[extensionsList.size()];
            extensionsList.toArray(extensionsArray);

            prefixesArray = new String[prefixesList.size()];
            prefixesList.toArray(prefixesArray);
        }
    }


    /**
     * @return a new Compiler for Facelet processing.
     */
    protected Compiler createCompiler() {
        return new SAXCompiler();
    }


    /**
     * @param context the {@link FacesContext} for the current request
     * @return a {@link ResponseWriter} for processing the request
     * @throws IOException if the writer cannot be created
     */
    protected ResponseWriter createResponseWriter(FacesContext context)
    throws IOException {

        ExternalContext extContext = context.getExternalContext();
        RenderKit renderKit = context.getRenderKit();
        // Avoid a cryptic NullPointerException when the renderkit ID
        // is incorrectly set
        if (renderKit == null) {
            String id = context.getViewRoot().getRenderKitId();
            throw new IllegalStateException(
                  "No render kit was available for id \"" + id + "\"");
        }

        if (responseBufferSizeSet) {
            // set the buffer for content
            extContext.setResponseBufferSize(responseBufferSize);
        }


        // get our content type
        String contentType =
              (String) context.getAttributes().get("facelets.ContentType");

        // get the encoding
        String encoding =
              (String) context.getAttributes().get("facelets.Encoding");

        // Create a dummy ResponseWriter with a bogus writer,
        // so we can figure out what content type the ReponseWriter
        // is really going to ask for
        ResponseWriter writer = renderKit.createResponseWriter(NullWriter.Instance,
                                                               contentType,
                                                               encoding);

        contentType = getResponseContentType(context, writer.getContentType());
        encoding = getResponseEncoding(context, writer.getCharacterEncoding());

        // apply them to the response
        extContext.setResponseContentType(contentType);
        extContext.setResponseCharacterEncoding(encoding);

        // Now, clone with the real writer
        writer = writer.cloneWithWriter(extContext.getResponseOutputWriter());

        return writer;

    }


    /**
     * Handles the case where rendering throws an Exception.
     *
     * @param context the {@link FacesContext} for the current request
     * @param e the caught Exception
     * @throws IOException if the custom debug content cannot be written
     */
    protected void handleRenderException(FacesContext context, Exception e)
    throws IOException {

        // always log
        if (LOGGER.isLoggable(Level.SEVERE)) {
            UIViewRoot root = context.getViewRoot();
            StringBuffer sb = new StringBuffer(64);
            sb.append("Error Rendering View");
            if (root != null) {
                sb.append('[');
                sb.append(root.getViewId());
                sb.append(']');
            }
            LOGGER.log(Level.SEVERE, sb.toString(), e);
        }

        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else if (e instanceof IOException) {
            throw (IOException) e;
        } else {
            throw new FacesException(e.getMessage(), e);
        }

    }


    /**
     * Handles the case where a Facelet cannot be found.
     *
     * @param context the {@link FacesContext} for the current request
     * @param viewId the view ID that was to be mapped to a Facelet
     * @param message optional message to include in the 404
     * @throws IOException if an error occurs sending the 404 to the client
     */
    protected void handleFaceletNotFound(FacesContext context,
                                         String viewId,
                                         String message)
    throws IOException {

        context.getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND,  ((message != null)
                                                                  ? (viewId + ": " + message)
                                                                  : viewId));
        context.responseComplete();

    }


    /**
     * @param context the {@link FacesContext} for the current request
     * @param orig the original encoding
     * @return the encoding to be used for this response
     */
    protected String getResponseEncoding(FacesContext context, String orig) {

        String encoding = orig;

        // see if we need to override the encoding
        Map<Object,Object> ctxAttributes = context.getAttributes();
        Map<String,Object> sessionMap =
              context.getExternalContext().getSessionMap();

        // 1. check the request attribute
        if (ctxAttributes.containsKey("facelets.Encoding")) {
            encoding = (String) ctxAttributes.get("facelets.Encoding");
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST,
                           "Facelet specified alternate encoding {0}",
                           encoding);
            }
            sessionMap.put(ViewHandler.CHARACTER_ENCODING_KEY, encoding);
        }

        // 2. get it from request
        if (encoding == null) {
            encoding = context.getExternalContext().getRequestCharacterEncoding();
        }

        // 3. get it from the session
        if (encoding == null) {
            encoding = (String) sessionMap.get(ViewHandler.CHARACTER_ENCODING_KEY);
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST,
                           "Session specified alternate encoding {0}",
                           encoding);
            }
        }

        // 4. default it
        if (encoding == null) {
            encoding = "UTF-8";
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("ResponseWriter created had a null CharacterEncoding, defaulting to UTF-8");
            }
        }

        return encoding;

    }


    /**
     * @param context the {@link FacesContext} for the current request
     * @param orig the original contentType
     * @return the content type to be used for this response
     */
    protected String getResponseContentType(FacesContext context, String orig) {

        String contentType = orig;

        // see if we need to override the contentType
        Map<Object,Object> m = context.getAttributes();
        if (m.containsKey("facelets.ContentType")) {
            contentType = (String) m.get("facelets.ContentType");
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("Facelet specified alternate contentType '"
                        + contentType + "'");
            }
        }

        // safety check
        if (contentType == null) {
            contentType = "text/html";
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("ResponseWriter created had a null ContentType, defaulting to text/html");
            }
        }

        return contentType;

    }

     private void updateStateSavingType(FacesContext ctx, String viewId) {

        if (!ctx.getAttributes().containsKey("partialStateSaving")) {
            ctx.getAttributes().put("partialStateSaving",
                                    usePartialSaving(viewId));
        }

    }

    private boolean usePartialSaving(String viewId) {
        return (partialStateSaving && !fullStateViewIds.contains(viewId));
    }

    private void doPostBuildActions(UIViewRoot root) {
        if (usePartialSaving(root.getViewId())) {
            stateManagementStrategy.notifyTrackChanges(root);    
        }
    }


    // ---------------------------------------------------------- Nested Classes


    /**
     * Simple no-op writer.
     */
    protected static final class NullWriter extends Writer {

        static final NullWriter Instance = new NullWriter();

        public void write(char[] buffer) {
        }

        public void write(char[] buffer, int off, int len) {
        }

        public void write(String str) {
        }

        public void write(int c) {
        }

        public void write(String str, int off, int len) {
        }

        public void close() {
        }

        public void flush() {
        }

    } // END NullWriter

}
