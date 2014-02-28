/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.application.view;

import com.sun.faces.application.ApplicationAssociate;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FaceletsViewMappings;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FaceletsBufferSize;

import com.sun.faces.context.StateContext;
import com.sun.faces.facelets.Facelet;
import com.sun.faces.facelets.FaceletFactory;
import com.sun.faces.facelets.el.VariableMapperWrapper;
import com.sun.faces.facelets.el.ContextualCompositeMethodExpression;
import com.sun.faces.facelets.tag.composite.CompositeComponentBeanInfo;
import com.sun.faces.facelets.tag.jsf.CompositeComponentTagHandler;
import com.sun.faces.facelets.tag.ui.UIDebug;
import com.sun.faces.scripting.groovy.GroovyHelper;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.RequestStateManager;
import com.sun.faces.util.Util;
import com.sun.faces.util.Cache;
import com.sun.faces.util.Cache.Factory;

import com.sun.faces.facelets.impl.DefaultFaceletFactory;

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
import javax.faces.event.PostAddToViewEvent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.FactoryFinder;
import javax.faces.component.ActionSource2;
import javax.faces.component.EditableValueHolder;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.event.MethodExpressionValueChangeListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ActionEvent;
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
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;

/**
 * This {@link ViewHandlingStrategy} handles Facelets/PDL-based views.
 */
public class FaceletViewHandlingStrategy extends ViewHandlingStrategy {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    private ViewDeclarationLanguageFactory vdlFactory;

    // FaceletFactory singleton for this application
    private FaceletFactory faceletFactory;

    // Array of viewId extensions that should be handled by Facelets
    private String[] extensionsArray;

    // Array of viewId prefixes that should be handled by Facelets
    private String[] prefixesArray;
    
    public static final String IS_BUILDING_METADATA =
          FaceletViewHandlingStrategy.class.getName() + ".IS_BUILDING_METADATA";
    
    private volatile StateManagementStrategyImpl stateManagementStrategy;
    private MethodRetargetHandlerManager retargetHandlerManager =
          new MethodRetargetHandlerManager();


    private boolean groovyAvailable;
    private int responseBufferSize;
    private boolean responseBufferSizeSet;

    private Cache<Resource, BeanInfo> metadataCache;


    // ------------------------------------------------------------ Constructors


    public FaceletViewHandlingStrategy() {

        initialize();

    }

    // ------------------------------------------------------------ Constructors

    public static boolean isBuildingMetadata(FacesContext context) {
        return context.getAttributes().containsKey(FaceletViewHandlingStrategy.IS_BUILDING_METADATA);
    }

    // ------------------------------------ Methods from ViewDeclarationLanguage

    @Override
    public StateManagementStrategy getStateManagementStrategy(FacesContext context, String viewId) {

        StateContext stateCtx = StateContext.getStateContext(context);
        if (stateCtx.partialStateSaving(context, viewId)) {
            if (stateManagementStrategy == null) {
                synchronized (this) {
                    if (stateManagementStrategy == null) {
                        stateManagementStrategy = new StateManagementStrategyImpl();
                    }
                }
            }
            return stateManagementStrategy;
        }
        return null; // a null return means use full state saving

    }
    
    /*
     * Called by Application.createComponent(Resource).
     * 
     * This method creates two temporary UIComponent instances to aid in
     * the creation of the compcomp metadata.  These instances no longer
     * needed after the method returns and can be safely garbage
     * collected.
     *
     * PENDING(): memory analysis should be done to verify there are no
     * memory leaks as a result of this implementation.

     * The instances are

     * 1. tmp: a javax.faces.NamingContainer to serve as the temporary
     * top level component

     * 2. facetComponent: a javax.faces.Panel to serve as the parent
     * UIComponent that is passed to Facelets so that the <cc:interface>
     * section can be parsed and understood.

     * Per the compcomp spec, tmp has the compcomp Resource stored in
     * its attr set under the key Resource.COMPONENT_RESOURCE_KEY.  tmp
     * has the facetComponent added as its
     * UIComponent.COMPOSITE_FACET_NAME facet.

     */

    @Override
    public BeanInfo getComponentMetadata(FacesContext context, 
            Resource ccResource) {

        FaceletFactory factory = (FaceletFactory)
                RequestStateManager.get(context, RequestStateManager.FACELET_FACTORY);
        assert(factory instanceof DefaultFaceletFactory);
        DefaultFaceletFactory ourFactory = (DefaultFaceletFactory) factory;
        if (ourFactory.needsToBeRefreshed(ccResource.getURL())) {
            metadataCache.remove(ccResource);
        }

        return metadataCache.get(ccResource);
    }
    
    public BeanInfo createComponentMetadata(FacesContext context,
            Resource ccResource) {

        // PENDING this implementation is terribly wasteful.
        // Must find a better way.
        CompositeComponentBeanInfo result;
        FaceletContext ctx = (FaceletContext)
                context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
        FaceletFactory factory = (FaceletFactory)
              RequestStateManager.get(context, RequestStateManager.FACELET_FACTORY);
        VariableMapper orig = ctx.getVariableMapper();

    // create tmp and facetComponent
        UIComponent tmp = context.getApplication().createComponent("javax.faces.NamingContainer");
        UIPanel facetComponent = (UIPanel)
                context.getApplication().createComponent("javax.faces.Panel");

    // PENDING I think this can be skipped because we don't render
    // this component instance.
        facetComponent.setRendererType("javax.faces.Group");

    // PENDING This could possibly be skipped too.  However, I think
    // this is important because other tag handlers, within
    // <cc:interface> expect it will be there.
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

        // Because mojarra currently requires a <cc:interface>
        // element within the compcomp markup, we can rely on the
        // fact that its tag handler, InterfaceHandler.apply(), is
        // called.  In this method, we first imbue facetComponent
        // with any config information present on the <cc:interface>
        // element.

        // Then we do the normal facelet thing:
        // this.nextHandler.apply().  This causes any child tag
        // handlers of the <cc:interface> to be called.  The
        // compcomp spec says each such tag handler is responsible
        // for adding to the compcomp metadata, referenced from the
        // facetComponent parent.

            f.apply(context, facetComponent);

        // When f.apply() returns (and therefore
        // InterfaceHandler.apply() returns), the compcomp metadata
        // pointed to by facetComponent is fully populated.

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
    // we extract the compcomp metadata and return it, making sure
    // to discard tmp and facetComponent.  The compcomp metadata
    // should be cacheable and shareable across threads, but this is
    // not yet implemented.
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
                ViewDeclarationLanguage vdl = vdlFactory.getViewDeclarationLanguage(viewToRender.getViewId());
                vdl.buildView(ctx, viewToRender);
            }

            // setup writer and assign it to the ctx
            ResponseWriter origWriter = ctx.getResponseWriter();
            if (origWriter == null) {
                origWriter = createResponseWriter(ctx);
            }

            ExternalContext extContext = ctx.getExternalContext();
            Writer outputWriter = extContext.getResponseOutputWriter();
            stateWriter = new WriteBehindStateWriter(outputWriter,
                                                     ctx,
                                                     responseBufferSize);

            ResponseWriter writer = origWriter.cloneWithWriter(stateWriter);
            ctx.setResponseWriter(writer);

            //  Don't call startDoc and endDoc on a partial response
            if (ctx.getPartialViewContext().isPartialRequest()) {
                viewToRender.encodeAll(ctx);
                try {
                    ctx.getExternalContext().getFlash().doPostPhaseActions(ctx);
                } catch (UnsupportedOperationException uoe) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("ExternalContext.getFlash() throw UnsupportedOperationException -> Flash unavailable");
                    }
                }
            } else {
                // render the view to the response
                writer.startDocument();
                viewToRender.encodeAll(ctx);
                try {
                    ctx.getExternalContext().getFlash().doPostPhaseActions(ctx);
                } catch (UnsupportedOperationException uoe) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("ExternalContext.getFlash() throw UnsupportedOperationException -> Flash unavailable");
                    }
                }
                writer.endDocument();
            }
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

        if (UIDebug.debugRequest(ctx)) {
            ctx.getApplication().createComponent(UIViewRoot.COMPONENT_TYPE);
        }

        return super.restoreView(ctx, viewId);

    }


    /**
     * @see ViewHandlingStrategy#retargetAttachedObjects(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.util.List)
     */
    @SuppressWarnings({"unchecked"})
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
        List<UIComponent> targetComponents;
        String forAttributeValue, curTargetName;

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

        MethodMetadataIterator allMetadata = new MethodMetadataIterator(attributes);
        for (CompCompInterfaceMethodMetadata metadata : allMetadata) {

            String attrName = metadata.getName();
            String[] targets = metadata.getTargets(context);

            Object attrValue = topLevelComponent.getValueExpression(attrName);

            // In all cases but one, the attrValue will be a ValueExpression.
            // The only case when it will not be a ValueExpression is
            // the case when the attrName is an action, and even then, it'll be a
            // ValueExpression in all cases except when it's a literal string.
            if (null == attrValue) {
                Map<String, Object> attrs = topLevelComponent.getAttributes();
                attrValue = (attrs.containsKey(attrName)) ?
                    attrs.get(attrName) : metadata.getDefault();
                if (attrValue == null) {
                    if (metadata.isRequired(context)) {
                        Object location = attrs.get(UIComponent.VIEW_LOCATION_KEY);
                        if (location == null) {
                            location = "";
                        }
                        throw new FacesException(
                              // RELEASE_PENDING need a better message
                              location.toString()
                              + ": Unable to find attribute with name \""
                              + attrName
                              + "\" in top level component in consuming page, "
                              + " or with default value in composite component.  "
                              + "Page author or composite component author error.");
                    } else {
                        continue;
                    }
                }
            }

            if (targets != null) {
                MethodRetargetHandler handler = retargetHandlerManager.getRetargetHandler(attrName);
                if (handler != null) {
                    for (String curTarget : targets) {
                        UIComponent targetComp = topLevelComponent
                              .findComponent(curTarget);
                        if (null == targetComp) {
                            throw new FacesException(attrValue.toString()
                                                     + " : Unable to re-target MethodExpression as inner component referenced by target id '"
                                                     + curTarget
                                                     + "' cannot be found.");
                        }
                        handler.retarget(context,
                                         metadata,
                                         attrValue,
                                         targetComp);
                    }
                } else {
                    // the developer has specified a target for a MethodExpression
                    // but the attribute name doesn't match one action, actionListener,
                    // validator, or valueChangeListener.  We can ignore the
                    // target(s) in this case
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING,
                                   "jsf.compcomp.unecessary.targets.attribute",
                                   new Object[] { getCompositeComponentName(topLevelComponent),
                                                  attrName });
                    }
                    handler = retargetHandlerManager.getDefaultHandler();
                    handler.retarget(context, metadata, attrValue, topLevelComponent);
                }
            } else {
                MethodRetargetHandler handler = retargetHandlerManager.getDefaultHandler();
                handler.retarget(context, metadata, attrValue, topLevelComponent);
            }

            // clear out the ValueExpression that we've retargeted as a
            // MethodExpression
            topLevelComponent.setValueExpression(attrName, null);

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
                for (String extension : extensionsArray) {
                    if (viewId.endsWith(extension)) {
                        return true;
                    }
                }
            }

            if (prefixesArray != null) {
                for (String prefix : prefixesArray) {
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
            Facelet f = faceletFactory.getFacelet(view.getViewId());
            StateContext stateCtx = StateContext.getStateContext(ctx);
            // Disable events from being intercepted by the StateContext by
            // virute of re-applying the handlers. 
            try {
                stateCtx.setTrackViewModifications(false);
                f.apply(ctx, view);
            } finally {
                stateCtx.setTrackViewModifications(true);
            }
            return;
        }

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
        doPostBuildActions(ctx, view);
        ctx.getApplication().publishEvent(ctx,
                                          PostAddToViewEvent.class,
                                          UIViewRoot.class,
                                          view);
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

        groovyAvailable = GroovyHelper.isGroovyAvailable(FacesContext.getCurrentInstance());

        metadataCache = new Cache<Resource, BeanInfo>(new Factory<Resource, BeanInfo>() {

            public BeanInfo newInstance(Resource ccResource) throws InterruptedException {
                FacesContext context = FacesContext.getCurrentInstance();
                return FaceletViewHandlingStrategy.this.createComponentMetadata(context, ccResource);
            }
        });

        try {
            responseBufferSizeSet = webConfig.isSet(FaceletsBufferSize);
            responseBufferSize =
                  Integer.parseInt(webConfig.getOptionValue(FaceletsBufferSize));
        } catch (NumberFormatException nfe) {
            responseBufferSize = Integer.parseInt(FaceletsBufferSize.getDefaultValue());
        }


        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Initialization Successful");
        }

        vdlFactory = (ViewDeclarationLanguageFactory) FactoryFinder.getFactory(FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY);

    }


    /**
     * Initialize mappings, during the first request.
     */
    protected void initializeMappings() {

        String viewMappings = webConfig.getOptionValue(FaceletsViewMappings);
        if ((viewMappings != null) && (viewMappings.length() > 0)) {
            String[] mappingsArray = Util.split(viewMappings, ";");

            List<String> extensionsList = new ArrayList<String>(mappingsArray.length);
            List<String> prefixesList = new ArrayList<String>(mappingsArray.length);

            for (String aMappingsArray : mappingsArray) {
                String mapping = aMappingsArray.trim();
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


    // --------------------------------------------------------- Private Methods

    private String getCompositeComponentName(UIComponent compositeComponent) {

        Resource resource =
              (Resource) compositeComponent.getAttributes().get(Resource.COMPONENT_RESOURCE_KEY);
        String name = resource.getResourceName();
        String library = resource.getLibraryName();

        if (library != null) {
            return "Composite Component: " + name + ", library: " + library;
        } else {
            return "Composite Component: " + name;
        }

    }


    private void doPostBuildActions(FacesContext ctx, UIViewRoot root) {
        StateContext stateCtx = StateContext.getStateContext(ctx);
        if (stateCtx.partialStateSaving(ctx, root.getViewId())) {
            root.markInitialState();
        }
        stateCtx.startTrackViewModifications();
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


    // ---------------------------------------------------------- Nested Classes


    /**
     * Provides iteration services over a composite component's
     * MethodExpression-enabled <code>PropertyDescriptors</code>.
     */
    private static final class MethodMetadataIterator implements Iterable<CompCompInterfaceMethodMetadata>, Iterator<CompCompInterfaceMethodMetadata> {

        private final PropertyDescriptor[] descriptors;
        private int curIndex = -1;

        // -------------------------------------------------------- Constructors


        MethodMetadataIterator(PropertyDescriptor[] descriptors) {

            this.descriptors = descriptors;
            if (descriptors != null && descriptors.length > 0) {
                curIndex = 0;
            }

        }


        // ----------------------------------------------- Methods from Iterable


        public Iterator<CompCompInterfaceMethodMetadata> iterator() {
            return this;
        }


        // ----------------------------------------------- Methods from Iterator


        public boolean hasNext() {

            if (curIndex != -1 && curIndex < descriptors.length) {
                int idx = curIndex;

                while (idx < descriptors.length) {
                    PropertyDescriptor pd = descriptors[idx];
                    if (shouldSkip(pd)) {
                        // this is a ValueExpression-enabled attribute and
                        // should be ignored.
                        idx++;
                    } else {
                        if (idx != curIndex) {
                            // the PD that was found to be returned by the
                            // next() call has a different offset from the
                            // current index; update the current index.
                            curIndex = idx;
                        }
                        return (curIndex < descriptors.length);
                    }
                }
            }
            return false;

        }

        public CompCompInterfaceMethodMetadata next() {


            return new CompCompInterfaceMethodMetadata(descriptors[curIndex++]);

        }

        public void remove() {

            throw new UnsupportedOperationException();

        }

        private boolean shouldSkip(PropertyDescriptor pd) {
            boolean result;
            String name = pd.getName();
            boolean isSpecialAttributeName = name.equals("action") ||
                            name.equals("actionListener") || name.equals("validator")
                            || name.equals("valueChangeListener");
            result = (!isSpecialAttributeName &&
                     (pd.getValue("type") != null ||
                      pd.getValue("method-signature") == null));

            return result;
        }

    } // END MethodMetadataIterator


    /**
     * Utility class to encapsulate the ValueExpression evaluation of the various
     * MethodExpression composite component properties.
     */
    private static final class CompCompInterfaceMethodMetadata {

        private final PropertyDescriptor pd;


        // -------------------------------------------------------- Constructors


        CompCompInterfaceMethodMetadata(PropertyDescriptor pd) {

            this.pd = pd;

        }


        // ------------------------------------------------------ Public Methods


        /**
         * @param ctx the <code>FacesContext</code> for the current request
         * @return the <code>method-signature</code> for this attribute
         */
        public String getMethodSignature(FacesContext ctx) {

            ValueExpression ms = (ValueExpression) pd.getValue("method-signature");
            if (ms != null) {
                return (String) ms.getValue(ctx.getELContext());
            }
            return null;

        }


        /**
         * @param ctx the <code>FacesContext</code> for the current request
         * @return an array of component targets to which a MethodExpression
         *  should be retargeted
         */
        public String[] getTargets(FacesContext ctx) {

            ValueExpression ts = (ValueExpression) pd.getValue("targets");
            if (ts != null) {
                String targets = (String) ts.getValue(ctx.getELContext());
                if (targets != null) {
                    return Util.split(targets, " ");
                }
            }

            return null;

        }


        /**
         * @param ctx the <code>FacesContext</code> for the current request
         * @return <code>true<code> if this attribute is required to be present,
         *  otherwise, returns <code>false</code>
         */
        public boolean isRequired(FacesContext ctx) {

            ValueExpression rd = (ValueExpression) pd.getValue("required");
            return ((rd != null) ? Boolean.valueOf(rd.getValue(ctx.getELContext()).toString()) : false);

        }


        /**
         * @return the default value as designated by the composite component
         *  author if no attribute was specified by the composite component
         *  consumer.  This value may be a ValueExpression, or a literal.
         */
        public Object getDefault() {

            return pd.getValue("default");

        }


        /**
         * @return the composite component attribute name
         */
        public String getName() {

            return pd.getName();

        }

    } // END CompCompInterfaceMethodMetadata


    /**
     * Managed the <code>MethodRetargetHandler</code> implementations for the
     * current <code>MethodExpression</code> enabled component attributes:
     * <ul>
     *    <li>action</li>
     *    <li>actionListener</li>
     *    <li>validator</li>
     *    <li>valueChangeListener</li>
     * </ul>
     *
     * Instances of this object also provide a default handler that can be
     * used to re-target <code>MethodExperssions</code> that don't match
     * on of the four names described above.
     */
    private static final class MethodRetargetHandlerManager {

        private Map<String,MethodRetargetHandler> handlerMap =
              new HashMap<String,MethodRetargetHandler>(4, 1.0f);
        private MethodRetargetHandler arbitraryHandler = new ArbitraryMethodRegargetHandler();

        // -------------------------------------------------------- Constructors


        MethodRetargetHandlerManager() {

            MethodRetargetHandler[] handlers = {
                  new ActionRegargetHandler(),
                  new ActionListenerRegargetHandler(),
                  new ValidatorRegargetHandler(),
                  new ValueChangeListenerRegargetHandler()
            };
            for (MethodRetargetHandler h : handlers) {
                handlerMap.put(h.getAttribute(), h);
            }

        }


        // ------------------------------------------------------ Public Methods


        /**
         * Lookup/return a <code>MethodRetargetHandler</code> appropriate to the
         * provided attribute name
         * @param attrName the attribute name
         * @return a <code>MethodRetargetHandler</code> that can properly handle
         *  retargeting expressions for the specified attribute, or </code>null</code>
         *  if there is no handler available.
         */
        private MethodRetargetHandler getRetargetHandler(String attrName) {

            return handlerMap.get(attrName);

        }


        /**
         * @return a <code>MethodRetargetHandler</code> that can retarget
         * arbitrarily named MethodExpressions.
         */
        private MethodRetargetHandler getDefaultHandler() {

            return arbitraryHandler;

        }


        // ------------------------------------------------------ Nested Classes


        /**
         * Base MethodRetargetHandler implementation.
         */
        private static abstract class AbstractRetargetHandler implements MethodRetargetHandler {

            protected static final Class[] NO_ARGS = new Class[0];


        } // END AbstractRetargetHandler


        /**
         * This handler is responsible for creating/retargeting MethodExpressions defined
         * associated with the <code>action</code> attribute
         */
        private static final class ActionRegargetHandler extends AbstractRetargetHandler {

            private static final String ACTION = "action";


            // ------------------------------ Methods from MethodRetargetHandler


            public void retarget(FacesContext ctx,
                                 CompCompInterfaceMethodMetadata metadata,
                                 Object sourceValue,
                                 UIComponent target) {

                String expr = (sourceValue instanceof ValueExpression)
                                 ? ((ValueExpression) sourceValue).getExpressionString()
                                 : sourceValue.toString();
                ExpressionFactory f = ctx.getApplication().getExpressionFactory();
                MethodExpression me = f.createMethodExpression(ctx.getELContext(),
                                                               expr,
                                                               Object.class,
                                                               NO_ARGS);
                ((ActionSource2) target)
                      .setActionExpression(
                            new ContextualCompositeMethodExpression(((sourceValue instanceof ValueExpression)
                                                                     ? (ValueExpression) sourceValue
                                                                        : null),
                                                                    me));

            }


            public String getAttribute() {

                return ACTION;

            }

        } // END ActionRegargetHandler


        /**
         * This handler is responsible for creating/retargeting MethodExpressions defined
         * associated with the <code>actionListener</code> attribute
         */
        private static final class ActionListenerRegargetHandler extends AbstractRetargetHandler {

            private static final String ACTION_LISTENER = "actionListener";
            private static final Class[] ACTION_LISTENER_ARGS = new Class[] { ActionEvent.class };


            // ------------------------------ Methods from MethodRetargetHandler


            public void retarget(FacesContext ctx,
                                 CompCompInterfaceMethodMetadata metadata,
                                 Object sourceValue,
                                 UIComponent target) {

                ValueExpression ve = (ValueExpression) sourceValue;
                ExpressionFactory f = ctx.getApplication().getExpressionFactory();
                MethodExpression me = f.createMethodExpression(ctx.getELContext(),
                                                               ve.getExpressionString(),
                                                               Void.TYPE,
                                                               ACTION_LISTENER_ARGS);
                MethodExpression noArg = f.createMethodExpression(ctx.getELContext(),
                                                                  ve.getExpressionString(),
                                                                  Void.TYPE,
                                                                  NO_ARGS);

                ((ActionSource2) target).addActionListener(
                      new MethodExpressionActionListener(
                            new ContextualCompositeMethodExpression(ve,
                                                                    me),
                            new ContextualCompositeMethodExpression(ve,
                                                                    noArg)));

            }


            public String getAttribute() {

                return ACTION_LISTENER;

            }

        } // END ActionListenerRegargetHandler


        /**
         * This handler is responsible for creating/retargeting MethodExpressions defined
         * associated with the <code>validator</code> attribute
         */
        private static final class ValidatorRegargetHandler extends AbstractRetargetHandler {

            private static final String VALIDATOR = "validator";
            private static final Class[] VALIDATOR_ARGS = new Class[]{
                  FacesContext.class,
                  UIComponent.class,
                  Object.class
            };


            // ------------------------------ Methods from MethodRetargetHandler


            public void retarget(FacesContext ctx,
                                 CompCompInterfaceMethodMetadata metadata,
                                 Object sourceValue,
                                 UIComponent target) {

                ValueExpression ve = (ValueExpression) sourceValue;
                ExpressionFactory f = ctx.getApplication().getExpressionFactory();
                MethodExpression me = f.createMethodExpression(ctx.getELContext(),
                                                               ve.getExpressionString(),
                                                               Void.TYPE,
                                                               VALIDATOR_ARGS);

                ((EditableValueHolder) target).addValidator(
                      new MethodExpressionValidator(
                            new ContextualCompositeMethodExpression(ve,
                                                                    me)));

            }


            public String getAttribute() {

                return VALIDATOR;

            }

        } // END ValidatorRegargetHandler


        /**
         * This handler is responsible for creating/retargeting MethodExpressions defined
         * associated with the <code>valueChangeListener</code> attribute
         */
        private static final class ValueChangeListenerRegargetHandler extends AbstractRetargetHandler {

            private static final String VALUE_CHANGE_LISTENER = "valueChangeListener";
            private static final Class[] VALUE_CHANGE_LISTENER_ARGS = new Class[]{
                  ValueChangeEvent.class
            };


            // ------------------------------ Methods from MethodRetargetHandler


            public void retarget(FacesContext ctx,
                                 CompCompInterfaceMethodMetadata metadata,
                                 Object sourceValue,
                                 UIComponent target) {

                ValueExpression ve = (ValueExpression) sourceValue;
                ExpressionFactory f = ctx.getApplication().getExpressionFactory();
                MethodExpression me = f.createMethodExpression(ctx.getELContext(),
                                                               ve.getExpressionString(),
                                                               Void.TYPE,
                                                               VALUE_CHANGE_LISTENER_ARGS);
                MethodExpression noArg = f.createMethodExpression(ctx.getELContext(),
                                                                  ve.getExpressionString(),
                                                                  Void.TYPE,
                                                                  NO_ARGS);

                ((EditableValueHolder) target).addValueChangeListener(
                      new MethodExpressionValueChangeListener(
                            new ContextualCompositeMethodExpression(ve,
                                                                    me),
                            new ContextualCompositeMethodExpression(ve,
                                                                    noArg)));

            }


            public String getAttribute() {
                return VALUE_CHANGE_LISTENER;
            }

        } // END ValueChangeListenerRegargetHandler


        /**
         * This handler is responsible for creating/retargeting MethodExpressions defined
         * using arbitrary attribute names.
         */
        private static final class ArbitraryMethodRegargetHandler extends AbstractRetargetHandler {


            // ------------------------------ Methods from MethodRetargetHandler


            public void retarget(FacesContext ctx, CompCompInterfaceMethodMetadata metadata, Object sourceValue, UIComponent target) {

                ValueExpression ve = (ValueExpression) sourceValue;
                ExpressionFactory f = ctx.getApplication()
                      .getExpressionFactory();

                // There is no explicit methodExpression property on
                // an inner component to which this MethodExpression
                // should be retargeted.  In this case, replace the
                // ValueExpression with a method expresson.

                // Pull apart the methodSignature to derive the
                // expectedReturnType and expectedParameters

                String methodSignature = metadata.getMethodSignature(ctx);
                assert (null != methodSignature);
                methodSignature = methodSignature.trim();
                Class<?> expectedReturnType;
                Class<?>[] expectedParameters = NO_ARGS;

                // Get expectedReturnType
                int j, i = methodSignature.indexOf(" ");
                if (-1 != i) {
                    String strValue = methodSignature.substring(0, i);
                    try {
                        expectedReturnType = Util.getTypeFromString(strValue.trim());
                    } catch (ClassNotFoundException cnfe) {
                        throw new FacesException(methodSignature
                                                 + " : Unable to load type '"
                                                 + strValue
                                                 + '\'');
                    }
                } else {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.severe(
                              "Unable to determine expected return type for " +
                              methodSignature);
                    }
                    return;
                }

                // derive the arguments
                i = methodSignature.indexOf("(");
                if (-1 != i) {
                    j = methodSignature.indexOf(")", i + 1);
                    if (-1 != j) {
                        String strValue = methodSignature.substring(i + 1, j);
                        if (0 < strValue.length()) {
                            String[] params = strValue.split(",");
                            expectedParameters = new Class[params.length];
                            boolean exceptionThrown = false;
                            for (i = 0; i < params.length; i++) {
                                try {
                                    expectedParameters[i] =
                                          Util.getTypeFromString(params[i].trim());
                                } catch (ClassNotFoundException cnfe) {
                                    if (LOGGER.isLoggable(Level.SEVERE)) {
                                        LOGGER.log(Level.SEVERE,
                                                   "Unable to determine parameter type for "
                                                   + methodSignature,
                                                   cnfe);
                                    }
                                    exceptionThrown = true;
                                    break;
                                }
                            }
                            if (exceptionThrown) {
                                return;
                            }

                        } else {
                            expectedParameters = NO_ARGS;
                        }
                    }

                }

                assert (null != expectedReturnType);
                assert (null != expectedParameters);

                MethodExpression me = f
                      .createMethodExpression(ctx.getELContext(),
                                              ve.getExpressionString(),
                                              expectedReturnType,
                                              expectedParameters);
                target.getAttributes().put(metadata.getName(),
                                           new ContextualCompositeMethodExpression(
                                                 ve,
                                                 me));

            }


            public String getAttribute() {
                return null;
            }

        } // END ArbitraryMethodRegargetHandler

    } // END MethodRegargetHandlerManager


    /**
     * Implementations of this interface provide the <code>strategy</code> to
     * properly retarget a method expression for a particular attribute.
     */
    private interface MethodRetargetHandler {

        /**
         * Constructs and retargets a <code>MethodExpression</code> as appropriate
         * based on the provided arguments.
         *
         * @param ctx the <code>FacesContext</code> for the current request
         * @param metadata the metadata describing the method to be retargeted
         * @param sourceValue typically, this will be a ValueExpression, however,
         *  there are cases where this could be provided as a literal.  It basically
         *  represents the attribute value being passed to the composite component
         * @param target the component that will be target of the method expression
         */
        void retarget(FacesContext ctx,
                      CompCompInterfaceMethodMetadata metadata,
                      Object sourceValue,
                      UIComponent target);

        /**
         * @return the attribute name this <code>MethodRetargetHandler</code>
         *  is designed to handle
         */
        String getAttribute();

    } // END MethodRetargetHandler


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
