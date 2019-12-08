/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

import static com.sun.faces.RIConstants.DYNAMIC_COMPONENT;
import static com.sun.faces.RIConstants.FACELETS_ENCODING_KEY;
import static com.sun.faces.RIConstants.FLOW_DEFINITION_ID_SUFFIX;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FaceletsBufferSize;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FaceletsViewMappings;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.StateSavingMethod;
import static com.sun.faces.context.StateContext.getStateContext;
import static com.sun.faces.util.RequestStateManager.FACELET_FACTORY;
import static com.sun.faces.util.Util.getDOCTYPEFromFacesContextAttributes;
import static com.sun.faces.util.Util.getXMLDECLFromFacesContextAttributes;
import static com.sun.faces.util.Util.isViewIdExactMappedToFacesServlet;
import static com.sun.faces.util.Util.isViewPopulated;
import static com.sun.faces.util.Util.notNull;
import static com.sun.faces.util.Util.saveDOCTYPEToFacesContextAttributes;
import static com.sun.faces.util.Util.saveXMLDECLToFacesContextAttributes;
import static com.sun.faces.util.Util.setViewPopulated;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static javax.faces.FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY;
import static javax.faces.application.ProjectStage.Development;
import static javax.faces.application.Resource.COMPONENT_RESOURCE_KEY;
import static javax.faces.application.StateManager.IS_BUILDING_INITIAL_STATE;
import static javax.faces.application.StateManager.STATE_SAVING_METHOD_SERVER;
import static javax.faces.application.ViewHandler.CHARACTER_ENCODING_KEY;
import static javax.faces.application.ViewHandler.DEFAULT_FACELETS_SUFFIX;
import static javax.faces.application.ViewVisitOption.RETURN_AS_MINIMAL_IMPLICIT_OUTCOME;
import static javax.faces.component.UIComponent.BEANINFO_KEY;
import static javax.faces.component.UIComponent.COMPOSITE_FACET_NAME;
import static javax.faces.component.UIComponent.VIEW_LOCATION_KEY;
import static javax.faces.component.UIViewRoot.COMPONENT_TYPE;
import static javax.faces.view.AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY;
import static javax.faces.view.facelets.FaceletContext.FACELET_CONTEXT_KEY;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewVisitOption;
import javax.faces.component.ActionSource2;
import javax.faces.component.ContextCallback;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.event.MethodExpressionValueChangeListener;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.ResponseStateManager;
import javax.faces.validator.MethodExpressionValidator;
import javax.faces.view.ActionSource2AttachedObjectHandler;
import javax.faces.view.ActionSource2AttachedObjectTarget;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.AttachedObjectTarget;
import javax.faces.view.BehaviorHolderAttachedObjectHandler;
import javax.faces.view.BehaviorHolderAttachedObjectTarget;
import javax.faces.view.EditableValueHolderAttachedObjectHandler;
import javax.faces.view.EditableValueHolderAttachedObjectTarget;
import javax.faces.view.StateManagementStrategy;
import javax.faces.view.ValueHolderAttachedObjectHandler;
import javax.faces.view.ValueHolderAttachedObjectTarget;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;
import javax.faces.view.ViewMetadata;
import javax.faces.view.facelets.Facelet;
import javax.faces.view.facelets.FaceletContext;
import javax.servlet.http.HttpSession;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.context.StateContext;
import com.sun.faces.facelets.el.ContextualCompositeMethodExpression;
import com.sun.faces.facelets.el.VariableMapperWrapper;
import com.sun.faces.facelets.impl.DefaultFaceletFactory;
import com.sun.faces.facelets.impl.XMLFrontMatterSaver;
import com.sun.faces.facelets.tag.composite.CompositeComponentBeanInfo;
import com.sun.faces.facelets.tag.jsf.CompositeComponentTagHandler;
import com.sun.faces.facelets.tag.ui.UIDebug;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.scripting.groovy.GroovyHelper;
import com.sun.faces.util.Cache;
import com.sun.faces.util.Cache.Factory;
import com.sun.faces.util.ComponentStruct;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.HtmlUtils;
import com.sun.faces.util.RequestStateManager;
import com.sun.faces.util.Util;

/**
 * This {@link ViewHandlingStrategy} handles Facelets/PDL-based views.
 */
public class FaceletViewHandlingStrategy extends ViewHandlingStrategy {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    private ViewDeclarationLanguageFactory vdlFactory;

    private DefaultFaceletFactory faceletFactory;

    // Array of viewId extensions that should be handled by Facelets
    private String[] extensionsArray;

    // Array of viewId prefixes that should be handled by Facelets
    private String[] prefixesArray;
    
    public static final String IS_BUILDING_METADATA =
          FaceletViewHandlingStrategy.class.getName() + ".IS_BUILDING_METADATA";
    
    public static final String RESOURCE_LIBRARY_CONTRACT_DATA_STRUCTURE_KEY = 
          FaceletViewHandlingStrategy.class.getName() + ".RESOURCE_LIBRARY_CONTRACT_DATA_STRUCTURE";

    private MethodRetargetHandlerManager retargetHandlerManager =
          new MethodRetargetHandlerManager();


    private boolean groovyAvailable;
    private int responseBufferSize;
    private boolean responseBufferSizeSet;
    private boolean isTrinidadStateManager;

    private Cache<Resource, BeanInfo> metadataCache;
    private Map<String, List<String>> contractMappings;


    // ------------------------------------------------------------ Constructors

    public FaceletViewHandlingStrategy() {
        initialize();
    }

    
    // ------------------------------------------------------------ Constructors

    public static boolean isBuildingMetadata(FacesContext context) {
        return context.getAttributes().containsKey(IS_BUILDING_METADATA);
    }

    
    // ------------------------------------ Methods from ViewDeclarationLanguage
    
    /**
     * <p>
     * If {@link UIDebug#debugRequest(javax.faces.context.FacesContext)}} is <code>true</code>,
     * simply return a new UIViewRoot(), otherwise, call the default logic.
     * </p>
     * @see ViewDeclarationLanguage#restoreView(javax.faces.context.FacesContext, java.lang.String) 
     */
    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        notNull("context", context);
        notNull("viewId", viewId);

        if (UIDebug.debugRequest(context)) {
            context.getApplication().createComponent(COMPONENT_TYPE);
        }
                        
        UIViewRoot viewRoot;
        
        /*
         * Check if we are stateless.
         */
        ViewHandler outerViewHandler = context.getApplication().getViewHandler();
        String renderKitId = outerViewHandler.calculateRenderKitId(context);
        ResponseStateManager rsm = RenderKitUtils.getResponseStateManager(context, renderKitId);
                
        if (rsm.isStateless(context, viewId))  {
            try {
                context.setProcessingEvents(true);
                ViewDeclarationLanguage vdl = vdlFactory.getViewDeclarationLanguage(viewId);
                viewRoot = vdl.createView(context, viewId);                
                context.setViewRoot(viewRoot);
                vdl.buildView(context, viewRoot);
                
                if (!viewRoot.isTransient()) {
                    throw new FacesException("Unable to restore view " + viewId);
                }
                
                return viewRoot;
            } catch (IOException ioe) {
                throw new FacesException(ioe);
            }
        }
        
        if (getStateContext(context).isPartialStateSaving(context, viewId)) {
            try {
                context.setProcessingEvents(false);
                ViewDeclarationLanguage vdl = vdlFactory.getViewDeclarationLanguage(viewId);
                viewRoot = vdl.getViewMetadata(context, viewId).createMetadataView(context);
                context.setViewRoot(viewRoot);
                outerViewHandler = context.getApplication().getViewHandler();
                renderKitId = outerViewHandler.calculateRenderKitId(context);
                rsm = RenderKitUtils.getResponseStateManager(context, renderKitId);
                Object[] rawState = (Object[]) rsm.getState(context, viewId);
                
                if (rawState != null) {
                    Map<String, Object> state = (Map<String, Object>) rawState[1];
                    if (state != null) {
                        String cid = viewRoot.getClientId(context);
                        Object stateObj = state.get(cid);
                        if (stateObj != null) {
                            context.getAttributes().put("com.sun.faces.application.view.restoreViewScopeOnly", true);
                            viewRoot.restoreState(context, stateObj);
                            context.getAttributes().remove("com.sun.faces.application.view.restoreViewScopeOnly");
                        }
                    }
                }
                
                context.setProcessingEvents(true);
                vdl.buildView(context, viewRoot);
            } catch (IOException ioe) {
                throw new FacesException(ioe);
            }
        }

        UIViewRoot root = super.restoreView(context, viewId);
        
        ViewHandler viewHandler = context.getApplication().getViewHandler();
        ViewDeclarationLanguage vdl = viewHandler.getViewDeclarationLanguage(context, viewId);
        context.setResourceLibraryContracts(vdl.calculateResourceLibraryContracts(context, viewId));       
        
        StateContext stateCtx = StateContext.getStateContext(context);
        stateCtx.startTrackViewModifications(context, root);
        
        return root;
    }
    
    @Override
    public ViewMetadata getViewMetadata(FacesContext context, String viewId) {
        notNull("context", context);
        notNull("viewId", viewId);

        return new ViewMetadataImpl(viewId);
    }
    
    /**
     * @see ViewDeclarationLanguage#createView(javax.faces.context.FacesContext, java.lang.String)
     */
    @Override
    public UIViewRoot createView(FacesContext ctx, String viewId) {
        notNull("context", ctx);
        notNull("viewId", viewId);

        if (UIDebug.debugRequest(ctx)) {
            UIViewRoot root = (UIViewRoot) ctx.getApplication().createComponent(COMPONENT_TYPE);
            root.setViewId(viewId);
            return root;
        }

        UIViewRoot result = super.createView(ctx, viewId);
        ViewHandler viewHandler = ctx.getApplication().getViewHandler();
        ViewDeclarationLanguage vdl = viewHandler.getViewDeclarationLanguage(ctx, viewId);

        ctx.setResourceLibraryContracts(vdl.calculateResourceLibraryContracts(ctx, viewId));
        
        return result;
    }
    
    /**
     * @see ViewDeclarationLanguage#buildView(FacesContext, UIViewRoot)
     */
    @Override
    public void buildView(FacesContext ctx, UIViewRoot view) throws IOException {
        StateContext stateCtx = StateContext.getStateContext(ctx);
        
        if (isViewPopulated(ctx, view)) {
            Facelet facelet = faceletFactory.getFacelet(ctx, view.getViewId());
            // Disable events from being intercepted by the StateContext by
            // virute of re-applying the handlers. 
            try {
                stateCtx.setTrackViewModifications(false);
                facelet.apply(ctx, view);
                reapplyDynamicActions(ctx);
                if (stateCtx.isPartialStateSaving(ctx, view.getViewId())) {
                    markInitialStateIfNotMarked(view);
                }
            } finally {
                stateCtx.setTrackViewModifications(true);
            }
            
            return;
        }

        view.setViewId(view.getViewId());

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.fine("Building View: " + view.getViewId());
        }
        
        if (faceletFactory == null) {
            ApplicationAssociate associate = ApplicationAssociate.getInstance(ctx.getExternalContext());
            faceletFactory = associate.getFaceletFactory();
            assert (faceletFactory != null);
        }
        RequestStateManager.set(ctx, FACELET_FACTORY, faceletFactory);
        Facelet facelet = faceletFactory.getFacelet(ctx, view.getViewId());

        // populate UIViewRoot
        try {
            ctx.getAttributes().put(IS_BUILDING_INITIAL_STATE, Boolean.TRUE);
            stateCtx.setTrackViewModifications(false);
            facelet.apply(ctx, view);
            
            if (facelet instanceof XMLFrontMatterSaver) {
                XMLFrontMatterSaver frontMatterSaver = (XMLFrontMatterSaver) facelet;
                
                String docType = frontMatterSaver.getSavedDoctype();
                if (docType != null) {
                    saveDOCTYPEToFacesContextAttributes(docType);
                }
                
                String XMLDECL = frontMatterSaver.getSavedXMLDecl();
                if (XMLDECL != null) {
                    saveXMLDECLToFacesContextAttributes(XMLDECL);
                }
            }
            
            if (!stateCtx.isPartialStateSaving(ctx, view.getViewId())) {
                reapplyDynamicActions(ctx);
            }
            
            doPostBuildActions(ctx, view);
        } finally {
            ctx.getAttributes().remove(IS_BUILDING_INITIAL_STATE);
        }
        ctx.getApplication().publishEvent(ctx, PostAddToViewEvent.class, UIViewRoot.class, view);
        markInitialState(ctx, view);
        
        setViewPopulated(ctx, view);
    }
    
    /**
     * @see javax.faces.view.ViewDeclarationLanguage#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
     */
    @Override
    public void renderView(FacesContext ctx, UIViewRoot viewToRender) throws IOException {

        // Suppress rendering if "rendered" property on the component is false
        if (!viewToRender.isRendered()) {
            return;
        }

        // Log request
        if (LOGGER.isLoggable(FINE)) {
            LOGGER.fine("Rendering View: " + viewToRender.getViewId());
        }

        WriteBehindStateWriter existingStateWriter = WriteBehindStateWriter.getCurrentInstance();
        WriteBehindStateWriter stateWriter = null;
        try {
            // Only build the view if this view has not yet been built.
            if (!isViewPopulated(ctx, viewToRender)) {
                ViewDeclarationLanguage vdl = vdlFactory.getViewDeclarationLanguage(viewToRender.getViewId());
                vdl.buildView(ctx, viewToRender);
            }

            // Setup writer and assign it to the ctx
            ResponseWriter origWriter = ctx.getResponseWriter();
            if (origWriter == null) {
                origWriter = createResponseWriter(ctx);
            }

            ExternalContext extContext = ctx.getExternalContext();
            
            /*
             * Make sure we have a session here if we are using server state
             * saving. The WriteBehindStateWriter needs an active session when
             * it writes out state to a server session.
             * 
             * Note if you flag a view as transient then we won't acquire the
             * session as you are stating it does not need one.
             */
            if (isServerStateSaving() && !viewToRender.isTransient()) {
                getSession(ctx);
            }            
            
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
                    if (LOGGER.isLoggable(FINE)) {
                        LOGGER.fine("ExternalContext.getFlash() throw UnsupportedOperationException -> Flash unavailable");
                    }
                }
            } else {
                if (ctx.isProjectStage(Development)) {
                    FormOmittedChecker.check(ctx);
                }
                
                // render the view to the response
                String xmlDecl = getXMLDECLFromFacesContextAttributes(ctx);
                if (null != xmlDecl) {
                    // Do not escape.
                    writer.writePreamble(xmlDecl);
                }

                String docType = getDOCTYPEFromFacesContextAttributes(ctx);
                if (null != docType) {
                    // Do not escape.
                    writer.writeDoctype(docType);
                }
                writer.startDocument();
                viewToRender.encodeAll(ctx);
                try {
                    ctx.getExternalContext().getFlash().doPostPhaseActions(ctx);
                } catch (UnsupportedOperationException uoe) {
                    if (LOGGER.isLoggable(FINE)) {
                        LOGGER.fine("ExternalContext.getFlash() throw UnsupportedOperationException -> Flash unavailable");
                    }
                }
                writer.endDocument();
            }
            
            // Finish writing
            writer.close();
            
            // Flush to origWriter
            if (stateWriter.stateWritten()) {
                stateWriter.flushToWriter();
            }

        } catch (FileNotFoundException fnfe) {
            handleFaceletNotFound(ctx, viewToRender.getViewId(), fnfe.getMessage());
        } catch (Exception e) {
            handleRenderException(ctx, e);
        } finally {
            if (stateWriter != null) {
                stateWriter.release();
            }
            if (existingStateWriter != null) {
                WriteBehindStateWriter.setCurrentInstance(existingStateWriter);
            }
        }
    }

    @Override
    public StateManagementStrategy getStateManagementStrategy(FacesContext context, String viewId) {
        StateManagementStrategy result;
        
        StateContext stateCtx = StateContext.getStateContext(context);
        if (stateCtx.isPartialStateSaving(context, viewId)) {
            result = new FaceletPartialStateManagementStrategy(context);
        } else {
            // Spec for this method says:
            
            // Implementations that provide the VDL for Facelets for JSF 2.0 
            // and later must return non-null from this method.
            
            // Limit the specification violating change to the case where
            // we are running in Trinidad.
            // 
            result = isTrinidadStateManager ? null : new JspStateManagementStrategy(context);
        }
        
        return result;
    }
    
    /**
     * Called by Application._createComponent(Resource).
     * 
     * This method creates two temporary UIComponent instances to aid in
     * the creation of the compcomp metadata.  These instances no longer
     * needed after the method returns and can be safely garbage
     * collected.
     *
     * PENDING(): memory analysis should be done to verify there are no
     * memory leaks as a result of this implementation.
     *
     * The instances are
     *
     * 1. tmp: a javax.faces.NamingContainer to serve as the temporary
     * top level component
     *
     * 2. facetComponent: a javax.faces.Panel to serve as the parent
     * UIComponent that is passed to Facelets so that the <cc:interface>
     * section can be parsed and understood.
     *
     * Per the compcomp spec, tmp has the compcomp Resource stored in
     * its attr set under the key Resource.COMPONENT_RESOURCE_KEY.  tmp
     * has the facetComponent added as its
     * UIComponent.COMPOSITE_FACET_NAME facet.
     *
     */
    @Override
    public BeanInfo getComponentMetadata(FacesContext context, Resource ccResource) {

        DefaultFaceletFactory factory = (DefaultFaceletFactory) RequestStateManager.get(context, FACELET_FACTORY);
        DefaultFaceletFactory ourFactory = (DefaultFaceletFactory) factory;
        
        if (ourFactory.needsToBeRefreshed(ccResource.getURL())) {
            metadataCache.remove(ccResource);
        }

        return metadataCache.get(ccResource);
    }
    
    public BeanInfo createComponentMetadata(FacesContext context, Resource ccResource) {

        // PENDING this implementation is terribly wasteful.
        // Must find a better way.
        FaceletContext faceletContext = (FaceletContext) context.getAttributes().get(FACELET_CONTEXT_KEY);
        DefaultFaceletFactory factory = (DefaultFaceletFactory) RequestStateManager.get(context, FACELET_FACTORY);
        VariableMapper orig = faceletContext.getVariableMapper();

        // Create tmp and facetComponent
        UIComponent tmp = context.getApplication().createComponent("javax.faces.NamingContainer");
        UIPanel facetComponent = (UIPanel) context.getApplication().createComponent("javax.faces.Panel");

        // PENDING I think this can be skipped because we don't render
        // this component instance.
        facetComponent.setRendererType("javax.faces.Group");

        // PENDING This could possibly be skipped too.  However, I think
        // this is important because other tag handlers, within
        // <cc:interface> expect it will be there.
        tmp.getFacets().put(COMPOSITE_FACET_NAME, facetComponent);
        
        // We have to put the resource in here just so the classes that eventually
        // get called by facelets have access to it.
        tmp.getAttributes().put(COMPONENT_RESOURCE_KEY, ccResource);
        
        Facelet facelet;

        try {
            facelet = factory.getFacelet(context, ccResource.getURL());
            VariableMapper wrapper = new VariableMapperWrapper(orig) {

                @Override
                public ValueExpression resolveVariable(String variable) {
                    return super.resolveVariable(variable);
                }
                
            };
            
            faceletContext.setVariableMapper(wrapper);
            context.getAttributes().put(IS_BUILDING_METADATA, TRUE);

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

            facelet.apply(context, facetComponent);

            // When facelet.apply() returns (and therefore
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
            faceletContext.setVariableMapper(orig);
        }
        
        // we extract the compcomp metadata and return it, making sure
        // to discard tmp and facetComponent.  The compcomp metadata
        // should be cacheable and shareable across threads, but this is
        // not yet implemented.
        return (CompositeComponentBeanInfo) tmp.getAttributes().get(BEANINFO_KEY);
    }
    
    /**
     * @see javax.faces.view.ViewDeclarationLanguage#getScriptComponentResource(javax.faces.context.FacesContext, javax.faces.application.Resource)
     */
    @Override
    public Resource getScriptComponentResource(FacesContext context, Resource componentResource) {
        notNull("context", context);
        notNull("componentResource", componentResource);

        if (!groovyAvailable) {
            return null;
        }
        Resource result = null;

        String resourceName = componentResource.getResourceName();
        if (resourceName.endsWith(".xhtml")) {
            resourceName = resourceName.substring(0, resourceName.length() - 6) + ".groovy";
            ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
            result = resourceHandler.createResource(resourceName, componentResource.getLibraryName());
        }
        
        return result;
    }

    /**
     * @see ViewHandlingStrategy#retargetAttachedObjects(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.util.List)
     */
    @SuppressWarnings({"unchecked"})
    @Override
    public void retargetAttachedObjects(FacesContext context, UIComponent topLevelComponent, List<AttachedObjectHandler> handlers) {
        notNull("context", context);
        notNull("topLevelComponent", topLevelComponent);
        notNull("handlers", handlers);

        if (handlers == null || handlers.isEmpty()) {
            return;
        }
        
        BeanInfo componentBeanInfo = (BeanInfo) topLevelComponent.getAttributes().get(BEANINFO_KEY);
        if (componentBeanInfo == null) {
            return;
        }
        
        BeanDescriptor componentDescriptor = componentBeanInfo.getBeanDescriptor();
        
        // There is an entry in targetList for each attached object in the 
        // <composite:interface> section of the composite component.
        List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>) componentDescriptor.getValue(ATTACHED_OBJECT_TARGETS_KEY);
        
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
    public void retargetMethodExpressions(FacesContext context, UIComponent topLevelComponent) {
        notNull("context", context);
        notNull("topLevelComponent", topLevelComponent);

        BeanInfo componentBeanInfo = (BeanInfo) topLevelComponent.getAttributes().get(BEANINFO_KEY);
        // PENDING(edburns): log error message if componentBeanInfo is null;
        if (componentBeanInfo == null) {
            return;
        }

        PropertyDescriptor attributes[] = componentBeanInfo.getPropertyDescriptors();

        MethodMetadataIterator allMetadata = new MethodMetadataIterator(context, attributes);
        for (CompCompInterfaceMethodMetadata metadata : allMetadata) {

            String attrName = metadata.getName();
            String[] targets = metadata.getTargets(context);

            Object attrValue = topLevelComponent.getValueExpression(attrName);

            // In all cases but one, the attrValue will be a ValueExpression.
            // The only case when it will not be a ValueExpression is
            // the case when the attrName is an action, and even then, it'll be a
            // ValueExpression in all cases except when it's a literal string.
            if (attrValue == null) {
                Map<String, Object> attrs = topLevelComponent.getAttributes();
                attrValue = attrs.containsKey(attrName) ? attrs.get(attrName) : metadata.getDefault();
                if (attrValue == null) {
                    if (metadata.isRequired(context)) {
                        Object location = attrs.get(VIEW_LOCATION_KEY);
                        if (location == null) {
                            location = "";
                        }
                        throw new FacesException(
                            location.toString() + 
                            ": Unable to find attribute with name \"" + 
                            attrName + 
                            "\" in top level component in consuming page, " + 
                            " or with default value in composite component.  " + 
                            "Page author or composite component author error.");
                    } else {
                        continue;
                    }
                }
            }

            String targetAttributeName = metadata.getTargetAttributeName(context);
            UIComponent targetComp = null;
            if (targetAttributeName != null) {
                attrName = targetAttributeName;
            }
            
            if (targets != null) {
                MethodRetargetHandler handler = retargetHandlerManager.getRetargetHandler(attrName);
                if (handler != null) {
                    for (String curTarget : targets) {
                        targetComp = topLevelComponent.findComponent(curTarget);
                        if (targetComp == null) {
                            throw new FacesException(
                                attrValue.toString() + 
                                " : Unable to re-target MethodExpression as inner component referenced by target id '" + 
                                curTarget + 
                                "' cannot be found."
                            );
                        }
                        handler.retarget(context, metadata, attrValue, targetComp);
                    }
                } else {
                    // the developer has specified a target for a MethodExpression
                    // but the attribute name doesn't match one action, actionListener,
                    // validator, or valueChangeListener.  We can ignore the
                    // target(s) in this case
                    if (LOGGER.isLoggable(WARNING)) {
                        LOGGER.log(WARNING,
                           "jsf.compcomp.unecessary.targets.attribute",
                           new Object[] { getCompositeComponentName(topLevelComponent), attrName });
                    }
                    handler = retargetHandlerManager.getDefaultHandler();
                    handler.retarget(context, metadata, attrValue, topLevelComponent);
                }
            } else {
                MethodRetargetHandler handler = null;
                if (targetAttributeName != null) {
                    targetComp = topLevelComponent.findComponent(metadata.getName());
                    handler = retargetHandlerManager.getRetargetHandler(attrName);
                }
                if (handler == null) {
                    targetComp = topLevelComponent;
                    handler = retargetHandlerManager.getDefaultHandler();
                }
                handler.retarget(context, metadata, attrValue, targetComp);
            }

            // clear out the ValueExpression that we've retargeted as a
            // MethodExpression
            topLevelComponent.setValueExpression(attrName, null);
        }
    }

    @Override
    public UIComponent createComponent(FacesContext context, String taglibURI, String tagName, Map<String, Object> attributes) {
        notNull("context", context);
        notNull("taglibURI", taglibURI);
        notNull("tagName", tagName);
        
        return associate.getFaceletFactory()._createComponent(context, taglibURI, tagName, attributes);
    }
    
    @Override
    public List<String> calculateResourceLibraryContracts(FacesContext context, String viewId) {
        List<String> result = null;
        String longestPattern = null;
        if (contractMappings == null) {
            return emptyList();
        }
        
        String longestMatch = null;
        for (Map.Entry<String, List<String>> mappings : contractMappings.entrySet()) {
            String urlPattern = mappings.getKey();
            if (urlPattern.endsWith("*")) { 
                String prefix = urlPattern.substring(0, urlPattern.length() - 1);
                if (viewId.startsWith(prefix)) {
                    if (longestPattern == null) {
                        longestPattern = urlPattern;
                        longestMatch = prefix;
                    } else if (longestMatch.length() < prefix.length()) {
                        longestPattern = urlPattern;
                        longestMatch = prefix;
                    }
                }
            } else if (viewId.equals(urlPattern)) {
                longestPattern = urlPattern;
                break;
            }
        }
        
        if (longestPattern != null) {
            result = contractMappings.get(longestPattern);
        }
        
        if (result == null) {
            result = contractMappings.get("*");
        }
        
        return result;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public boolean viewExists(FacesContext context, String viewId) {
        if (handlesViewId(viewId)) {
            return getFaceletFactory().getResourceResolver().resolveUrl(viewId) != null;
        }
           
        return false;
    }
    
    /**
     * @see javax.faces.view.ViewDeclarationLanguage#getViews(FacesContext, String)
     */
    @Override
    public Stream<String> getViews(FacesContext context, String path, ViewVisitOption... options) {
        return mapIfNeeded(
            super.getViews(context, path)
                 .filter(viewId -> handlesViewId(viewId)), 
            options
        );
    }
    
    /**
     * @see javax.faces.view.ViewDeclarationLanguage#getViews(FacesContext, String, int)
     */
    @Override
    public Stream<String> getViews(FacesContext context, String path, int maxDepth, ViewVisitOption... options) {
        return mapIfNeeded(
            super.getViews(context, path, maxDepth)
                 .filter(viewId -> handlesViewId(viewId)), 
            options
        );
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
             if (handlesByPrefixOrSuffix(viewId)) {
                 return true;
             }
             
             if (isViewIdExactMappedToFacesServlet(viewId)) {
                 // If the Facelets VDL is reached, no other ViewDeclarationLanguage has declared
                 // to handle the view (via ViewExists()), so we handle it if the viewId happens to be exact
                 // mapped to the FacesServlet. The JSP ViewDeclarationLanguage still comes after us,
                 // but we don't support that.
                 return true;
             }
        }

        return false;
    }
    
    private boolean handlesByPrefixOrSuffix(String viewId) {
        if (viewId.endsWith(FLOW_DEFINITION_ID_SUFFIX)) {
            return true;
        }
        
       // If there's no extensions array or prefixes array, then assume defaults.  
        // .xhtml extension is handled by the FaceletViewHandler and .jsp will be handled by
       // the JSP view handler
       if (extensionsArray == null && prefixesArray == null) {
           return isMatchedWithFaceletsSuffix(viewId) ? true : viewId.endsWith(DEFAULT_FACELETS_SUFFIX);
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
       
       return false;
    }

    @Override 
    public String getId() {
        return FACELETS_VIEW_DECLARATION_LANGUAGE_ID;
    }


    // ------------------------------------------------------- Protected Methods


    /**
     * Initialize the core Facelets runtime.
     */
    protected void initialize() {

        if (LOGGER.isLoggable(FINE)) {
            LOGGER.fine("Initializing FaceletViewHandlingStrategy");
        }

        this.initializeMappings();

        groovyAvailable = GroovyHelper.isGroovyAvailable(FacesContext.getCurrentInstance());

        metadataCache = new Cache<>(new Factory<Resource, BeanInfo>() {

            @Override
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


        if (LOGGER.isLoggable(FINE)) {
            LOGGER.fine("Initialization Successful");
        }

        vdlFactory = (ViewDeclarationLanguageFactory) FactoryFinder.getFactory(VIEW_DECLARATION_LANGUAGE_FACTORY);

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        Map<String, Object> appMap = extContext.getApplicationMap();
        Map<String, List<String>> contractDataStructure = 
                (Map<String, List<String>>) 
                appMap.remove(RESOURCE_LIBRARY_CONTRACT_DATA_STRUCTURE_KEY);
        if (null != contractDataStructure && !contractDataStructure.isEmpty()) {
            contractMappings = new ConcurrentHashMap<>();
            for (Map.Entry<String, List<String>> cur : contractDataStructure.entrySet()) {
                contractMappings.put(cur.getKey(), new CopyOnWriteArrayList<>(cur.getValue()));
                cur.getValue().clear();
            }
            contractDataStructure.clear();
        }
        if (null != context) {
            StateManager stateManager = Util.getStateManager(context);
            if (null != stateManager) {
                isTrinidadStateManager = stateManager.getClass().getName().contains("trinidad");
            }
        }        
    }

    /**
     * Initialize mappings, during the first request.
     */
    protected void initializeMappings() {

        String viewMappings = webConfig.getOptionValue(FaceletsViewMappings);
        if (viewMappings != null && viewMappings.length() > 0) {
            Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();

            String[] mappingsArray = Util.split(appMap, viewMappings, ";");

            List<String> extensionsList = new ArrayList<>(mappingsArray.length);
            List<String> prefixesList = new ArrayList<>(mappingsArray.length);

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
    protected ResponseWriter createResponseWriter(FacesContext context) throws IOException {

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
        String contentType = (String) context.getAttributes().get("facelets.ContentType");

        // get the encoding
        String encoding = (String) context.getAttributes().get(FACELETS_ENCODING_KEY);

        // Create a dummy ResponseWriter with a bogus writer,
        // so we can figure out what content type the ReponseWriter
        // is really going to ask for
        ResponseWriter writer = renderKit.createResponseWriter(NullWriter.INSTANCE,
                                                               contentType,
                                                               encoding);

        contentType = getResponseContentType(context, writer.getContentType());
        encoding = getResponseEncoding(context, writer.getCharacterEncoding());

        // apply them to the response
        char[] buffer = new char[1028];
        HtmlUtils.writeTextForXML(writer, contentType, buffer);
        String str = String.valueOf(buffer).trim();
        extContext.setResponseContentType(str);
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
    protected void handleRenderException(FacesContext context, Exception e) throws IOException {

        // Always log
        if (LOGGER.isLoggable(SEVERE)) {
            UIViewRoot root = context.getViewRoot();
            StringBuffer sb = new StringBuffer(64);
            sb.append("Error Rendering View");
            if (root != null) {
                sb.append('[');
                sb.append(root.getViewId());
                sb.append(']');
            }
            LOGGER.log(SEVERE, sb.toString(), e);
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
    protected void handleFaceletNotFound(FacesContext context, String viewId, String message) throws IOException {

        context.getExternalContext().responseSendError(
            SC_NOT_FOUND, 
            message != null ? viewId + ": " + message : viewId
        );
        
        context.responseComplete();
    }

    /**
     * @param context the {@link FacesContext} for the current request
     * @param orig the original encoding
     * @return the encoding to be used for this response
     */
    protected String getResponseEncoding(FacesContext context, String orig) {

        String encoding = orig;


        // 1. get it from request
        encoding = context.getExternalContext().getRequestCharacterEncoding();

        // 2. get it from the session
        if (encoding == null) {
            if (null != context.getExternalContext().getSession(false)) {
                Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
                encoding = (String) sessionMap.get(CHARACTER_ENCODING_KEY);
                if (LOGGER.isLoggable(FINEST)) {
                    LOGGER.log(FINEST,
                            "Session specified alternate encoding {0}",
                            encoding);
                }
            }
        }

        // see if we need to override the encoding
        Map<Object,Object> ctxAttributes = context.getAttributes();


        // 3. check the request attribute
        if (ctxAttributes.containsKey(FACELETS_ENCODING_KEY)) {
            encoding = (String) ctxAttributes.get(FACELETS_ENCODING_KEY);
            if (LOGGER.isLoggable(FINEST)) {
                LOGGER.log(FINEST,
                           "Facelet specified alternate encoding {0}",
                           encoding);
            }
            if (null != context.getExternalContext().getSession(false)) {
                Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
                sessionMap.put(CHARACTER_ENCODING_KEY, encoding);
            }
        }

        // 4. default it
        if (encoding == null) {
            if (null != orig && 0 < orig.length()) {
                encoding = orig;
            } else {
                encoding = "UTF-8";
            }
            if (LOGGER.isLoggable(FINEST)) {
                LOGGER.log(FINEST, "ResponseWriter created had a null CharacterEncoding, defaulting to {0}", orig);
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

        // See if we need to override the contentType
        Map<Object,Object> m = context.getAttributes();
        if (m.containsKey("facelets.ContentType")) {
            contentType = (String) m.get("facelets.ContentType");
            if (LOGGER.isLoggable(FINEST)) {
                LOGGER.finest("Facelet specified alternate contentType '"
                        + contentType + "'");
            }
        }

        // Safety check
        if (contentType == null) {
            contentType = "text/html";
            if (LOGGER.isLoggable(FINEST)) {
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
//        if (stateCtx.isPartialStateSaving(ctx, root.getViewId())) {
	    // lu4242            root.markInitialState();
  //      }
        stateCtx.startTrackViewModifications(ctx, root);
    }

     private void markInitialState(FacesContext ctx, UIViewRoot root)
     {
         StateContext stateCtx = StateContext.getStateContext(ctx);
         if (stateCtx.isPartialStateSaving(ctx, root.getViewId())) {
             try {
                 ctx.getAttributes().put(IS_BUILDING_INITIAL_STATE, Boolean.TRUE);
                 if (!root.isTransient()) {
                     markInitialState(root);
                 }
             } finally {
                 ctx.getAttributes().remove(IS_BUILDING_INITIAL_STATE);
             }
         }
     }
     
     private void markInitialState(final UIComponent component)
     {
         component.markInitialState();
         for (Iterator<UIComponent> it = component.getFacetsAndChildren() ; it.hasNext() ; ) {
             UIComponent child = it.next();
             if (!child.isTransient()) {
                 markInitialState(child);
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


    // ---------------------------------------------------------- Nested Classes


    /**
     * Provides iteration services over a composite component's
     * MethodExpression-enabled <code>PropertyDescriptors</code>.
     */
    private static final class MethodMetadataIterator implements Iterable<CompCompInterfaceMethodMetadata>, Iterator<CompCompInterfaceMethodMetadata> {

        private final PropertyDescriptor[] descriptors;
        private FacesContext context;
        private int curIndex = -1;

        // -------------------------------------------------------- Constructors


        MethodMetadataIterator(FacesContext context, PropertyDescriptor[] descriptors) {

            this.context = context;
            this.descriptors = descriptors;
            if (descriptors != null && descriptors.length > 0) {
                curIndex = 0;
            }

        }


        // ----------------------------------------------- Methods from Iterable


        @Override
        public Iterator<CompCompInterfaceMethodMetadata> iterator() {
            return this;
        }


        // ----------------------------------------------- Methods from Iterator


        @Override
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

        @Override
        public CompCompInterfaceMethodMetadata next() {


            return new CompCompInterfaceMethodMetadata(descriptors[curIndex++]);

        }

        @Override
        public void remove() {

            throw new UnsupportedOperationException();

        }

        private boolean shouldSkip(PropertyDescriptor pd) {
            boolean result;
            String name = pd.getName();
            ValueExpression ve = (ValueExpression) pd.getValue("targetAttributeName");
            String targetAttributeName = ((ve != null) ? (String) ve.getValue(context.getELContext()) : "");

            boolean isSpecialAttributeName = Util.isSpecialAttributeName(name) ||
                    Util.isSpecialAttributeName(targetAttributeName);
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
                    return Util.split(ctx.getExternalContext().getApplicationMap(), targets, " ");
                }
            }

            return null;

        }

        public String getTargetAttributeName(FacesContext ctx) {
            ValueExpression ve = (ValueExpression) pd.getValue("targetAttributeName");
            return ((ve != null) ? (String) ve.getValue(ctx.getELContext()) : null);

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
              new HashMap<>(4, 1.0f);
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


            @Override
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


            @Override
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


            @Override
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


            @Override
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


            @Override
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


            @Override
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


            @Override
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


            @Override
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


            @Override
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

                // JAVASERVERFACES-4073
                ELContext elContext = (ELContext) ctx.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
                if (null == elContext) {
                    elContext = ctx.getELContext();
                }
                MethodExpression me = f
                      .createMethodExpression(elContext ,
                                              ve.getExpressionString(),
                                              expectedReturnType,
                                              expectedParameters);
                target.getAttributes().put(metadata.getName(),
                                           new ContextualCompositeMethodExpression(
                                                 ve,
                                                 me));

            }


            @Override
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

        static final NullWriter INSTANCE = new NullWriter();

        @Override
        public void write(char[] buffer) {
        }

        @Override
        public void write(char[] buffer, int off, int len) {
        }

        @Override
        public void write(String str) {
        }

        @Override
        public void write(int c) {
        }

        @Override
        public void write(String str, int off, int len) {
        }

        @Override
        public void close() {
        }

        @Override
        public void flush() {
        }

    } 
    /**
     * Find the given component in the component tree.
     *
     * @param context the Faces context.
     * @param clientId the client id of the component to find.
     */
    private UIComponent locateComponentByClientId(final FacesContext context, final UIComponent parent, final String clientId) {
        final List<UIComponent> found = new ArrayList<>();
        UIComponent result = null;

        parent.invokeOnComponent(context, clientId, new ContextCallback() {

            @Override
            public void invokeContextCallback(FacesContext context, UIComponent target) {
                found.add(target);
            }
        });

        /*
         * Since we did not find it the cheaper way we need to assume there is a
         * UINamingContainer that does not prepend its ID. So we are going to
         * walk the tree to find it.
         */
        if (found.isEmpty()) {
            VisitContext visitContext = VisitContext.createVisitContext(context);
            parent.visitTree(visitContext, new VisitCallback() {

                @Override
                public VisitResult visit(VisitContext visitContext, UIComponent component) {
                    VisitResult result = VisitResult.ACCEPT;
                    if (component.getClientId(visitContext.getFacesContext()).equals(clientId)) {
                        found.add(component);
                        result = VisitResult.COMPLETE;
                        }
                    return result;
                }
            });
        }
        if (!found.isEmpty()) {
            result = found.get(0);
        }
        return result;
    }

    /**
     * Reapply the dynamic actions after Facelets reapply.
     *
     * <p> Note a precondition to this method is that tracking view
     * modifications is turned off during the execution of this method. The
     * caller of this method is responsible for turning tracking view
     * modifications off and on as required. </p>
     *
     * @param context the Faces context.
     */
    private void reapplyDynamicActions(FacesContext context) {
        StateContext stateContext = StateContext.getStateContext(context);
        List<ComponentStruct> actions = stateContext.getDynamicActions();
        if (actions != null) {
            for (ComponentStruct action : actions) {
                if (ComponentStruct.REMOVE.equals(action.action)) {
                    reapplyDynamicRemove(context, action);
                }
                if (ComponentStruct.ADD.equals(action.action)) {
                    reapplyDynamicAdd(context, action);
                }
            }
        }
    }

    /**
     * Reapply the dynamic add after Facelets reapply.
     *
     * @param context the Faces context. 
     * @param struct the component struct.
     */
    private void reapplyDynamicAdd(FacesContext context, ComponentStruct struct) {
        UIComponent parent = locateComponentByClientId(context, context.getViewRoot(), struct.parentClientId);

        if (parent != null) {
            
            UIComponent child = locateComponentByClientId(context, parent, struct.clientId);
            StateContext stateContext = StateContext.getStateContext(context);

            if (child == null) {
                child = stateContext.getDynamicComponents().get(struct.clientId);
            }

            if (child != null) {
                if (struct.facetName != null) {
                    parent.getFacets().remove(struct.facetName);
                    parent.getFacets().put(struct.facetName, child);
                    child.getClientId();
                } else {
                    int childIndex = -1;
                    if (child.getAttributes().containsKey(DYNAMIC_COMPONENT)) {
                        childIndex = (Integer) child.getAttributes().get(DYNAMIC_COMPONENT);
                    }
                    child.setId(struct.id);
                    if (childIndex >= parent.getChildCount() || childIndex == -1) {
                        parent.getChildren().add(child);
                    } else {
                        parent.getChildren().add(childIndex, child);
                    }
                    child.getClientId();
                    child.getAttributes().put(DYNAMIC_COMPONENT, child.getParent().getChildren().indexOf(child));
                }
                stateContext.getDynamicComponents().put(struct.clientId, child);
            }
        }
    }

    /**
     * Reapply the dynamic remove after Facelets reapply.
     *
     * @param context the Faces context.
     * @param struct the component struct.
     */
    private void reapplyDynamicRemove(FacesContext context, ComponentStruct struct) {
        UIComponent child = locateComponentByClientId(context, context.getViewRoot(), struct.clientId);
        if (child != null) {
            StateContext stateContext = StateContext.getStateContext(context);
            stateContext.getDynamicComponents().put(struct.clientId, child);
            UIComponent parent = child.getParent();
            parent.getChildren().remove(child);
        }
    }
    
    /**
     * Are we saving state server side?
     * 
     * @return true if we are, false otherwise.
     */
    private boolean isServerStateSaving() {
        if (STATE_SAVING_METHOD_SERVER.equals(webConfig.getOptionValue(StateSavingMethod))) {
            return true;
        }
        
        return false;
    }

    /**
     * Get a session (if we are using server state saving).
     * 
     * @param context the Faces context.
     * @return the session, or null if we are not using server state saving.
     */
    private HttpSession getSession(FacesContext context) {
        Object sessionObj = context.getExternalContext().getSession(true);
        if (sessionObj instanceof HttpSession) {
            return (HttpSession) sessionObj;
        }
        
        return null;
    }
    
    /**
     * Gets and if needed initializes the faceletFactory
     *   
     * @return the default faceletFactorys
     */
    private DefaultFaceletFactory getFaceletFactory() {
        if (faceletFactory == null) {
            faceletFactory = associate.getFaceletFactory();
            assert (faceletFactory != null);
        }
        
        return faceletFactory;
    }
    
    private boolean isMatchedWithFaceletsSuffix(String viewId) {
        String[] defaultsuffixes = webConfig.getOptionValue(WebConfiguration.WebContextInitParameter.FaceletsSuffix, " ");
        for (String suffix : defaultsuffixes) {
            if (viewId.endsWith(suffix)) {
                return true;
            }
        }
        
        return false;
    }
    
    private String getMatchedWithFaceletsSuffix(String viewId) {
        String[] defaultsuffixes = webConfig.getOptionValue(WebConfiguration.WebContextInitParameter.FaceletsSuffix, " ");
        for (String suffix : defaultsuffixes) {
            if (viewId.endsWith(suffix)) {
                return suffix;
            }
        }
        
        return null;
    }
    
    /**
     * Maps the element in the passed in stream of views according to the given options.
     * 
     * @param views The stream of views to potentially map
     * @param options Options telling if and if so how to map
     * @return The stream with views, possibly mapped if needed
     */
    private Stream<String> mapIfNeeded(Stream<String> views, ViewVisitOption... options) {
        if (!returnAsImplicitOutCome(options)) {
            return views;
        }
        
        return views.map(view -> toImplicitOutcome(view));
    }
    
    private boolean returnAsImplicitOutCome(ViewVisitOption... options) {
        return stream(options)
          .filter(option -> option == RETURN_AS_MINIMAL_IMPLICIT_OUTCOME)
          .findAny()
          .isPresent();
    }
    
    private String toImplicitOutcome(String viewId) {
        String suffix = getConfiguredSuffix(viewId);
        if (suffix != null) {
            return viewId.substring(0, viewId.lastIndexOf(suffix));
        }
        
        String prefix = getConfiguredPrefix(viewId);
        if (prefix != null) {
            return viewId.substring(prefix.length());
        }
        
        // Would be rare to reach this, since when toImplicitOutcome() is called
        // handlesViewId() should already have been called before that
        // and then either suffix or prefix would have matched.
        return viewId;
    }
    
    private String getConfiguredSuffix(String viewId) {
        if (viewId != null) {
            
            if (viewId.endsWith(FLOW_DEFINITION_ID_SUFFIX)) {
                return FLOW_DEFINITION_ID_SUFFIX;
            }
            
           // If there's no extensions array or prefixes array, then
           // assume defaults.  .xhtml extension is handled by
           // the FaceletViewHandler and .jsp will be handled by
           // the JSP view handler
           if (extensionsArray == null && prefixesArray == null) {
               String suffix = getMatchedWithFaceletsSuffix(viewId);
               if (suffix != null) {
                   return suffix;
               }
               
               if (viewId.endsWith(DEFAULT_FACELETS_SUFFIX)) {
                   return DEFAULT_FACELETS_SUFFIX;
               }
           }

           if (extensionsArray != null) {
               for (String extension : extensionsArray) {
                   if (viewId.endsWith(extension)) {
                       return extension;
                   }
               }
           }
       }

       return null;
    }
    
    private String getConfiguredPrefix(String viewId) {
        if (prefixesArray != null) {
            for (String prefix : prefixesArray) {
                if (viewId.startsWith(prefix)) {
                    return prefix;
                }
            }
        }
        
        return null;
    }

    /**
     * Mark the initial state if not already marked.
     */
    private void markInitialStateIfNotMarked(UIComponent component) {
        if (!component.isTransient()) {
            if (!component.getAttributes().containsKey(DYNAMIC_COMPONENT) && !component.initialStateMarked()) {
                component.markInitialState();
            }
            for (Iterator<UIComponent> it = component.getFacetsAndChildren() ; it.hasNext() ; ) {
                UIComponent child = it.next();
                markInitialStateIfNotMarked(child);
            }
        }
    }
}
