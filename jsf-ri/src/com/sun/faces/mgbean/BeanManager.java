/*
 * $Id: BeanManager.java,v 1.8.4.1 2008/03/14 02:41:03 edburns Exp $
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

package com.sun.faces.mgbean;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.sun.faces.el.ELUtils;
import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;

/**
 * <p>Main interface for dealing with JSF managed beans</p>
 */
public class BeanManager {

    private static final Logger LOGGER = FacesLogger.MANAGEDBEAN.getLogger();

    @SuppressWarnings({"CollectionWithoutInitialCapacity"})
    private Map<String,BeanBuilder> managedBeans =
         new HashMap<String,BeanBuilder>();
    private InjectionProvider injectionProvider;
    private boolean configPreprocessed;
    private boolean lazyBeanValidation;

    // ------------------------------------------------------------ Constructors


    public BeanManager(InjectionProvider injectionProvider,
                       boolean lazyBeanValidation) {

        this.injectionProvider = injectionProvider;
        this.lazyBeanValidation = lazyBeanValidation;

    }


    public BeanManager(InjectionProvider injectionProvider,
                       Map<String,BeanBuilder> managedBeans,
                       boolean lazyBeanValidation) {

        this(injectionProvider, lazyBeanValidation);
        this.managedBeans = managedBeans;

    }


    // ---------------------------------------------------------- Public Methods


    public void register(ManagedBeanInfo beanInfo) {
        if (beanInfo.hasListEntry()) {
            if (beanInfo.hasMapEntry() || beanInfo.hasManagedProperties()) {
                String message =
                     MessageUtils.getExceptionMessageString(
                          MessageUtils.MANAGED_BEAN_AS_LIST_CONFIG_ERROR_ID,
                          beanInfo.getName());
                addBean(beanInfo.getName(), new ErrorBean(beanInfo, message));
            } else {
                addBean(beanInfo.getName(),
                        new ManagedListBeanBuilder(beanInfo));
            }
        } else if (beanInfo.hasMapEntry()) {
            if (beanInfo.hasManagedProperties()) {
                String message =
                     MessageUtils.getExceptionMessageString(
                          MessageUtils.MANAGED_BEAN_AS_MAP_CONFIG_ERROR_ID,
                          beanInfo.getName());
                addBean(beanInfo.getName(), new ErrorBean(beanInfo, message));
            } else {
                addBean(beanInfo.getName(), new ManagedMapBeanBuilder(beanInfo));
            }
        } else {
            addBean(beanInfo.getName(), new ManagedBeanBuilder(beanInfo));
        }
    }


    public Map<String,BeanBuilder> getRegisteredBeans() {

        return managedBeans;

    }


     public boolean isManaged(String name) {

        return (managedBeans != null && managedBeans.containsKey(name));

    }


    public BeanBuilder getBuilder(String name) {

        if (managedBeans != null) {
            return managedBeans.get(name);
        }
        return null;

    }


    /**
     * This should only be called during application init
     */
    public void preProcessesBeans() {

        if (!configPreprocessed && !lazyBeanValidation) {
            configPreprocessed = true;
            for (Map.Entry<String, BeanBuilder> entry : managedBeans
                 .entrySet()) {
                preProcessBean(entry.getKey(), entry.getValue());
            }
        }

    }


    public boolean isBeanInScope(String name, FacesContext context) {

        ELUtils.Scope scope = this.getBuilder(name).getScope();
        ExternalContext externalContext = context.getExternalContext();
        // check to see if the bean is already in scope
        switch (scope) {
            case REQUEST:
                if (externalContext.getRequestMap().containsKey(name)) {
                    return true;
                }
            case VIEW:
                if (context.getViewRoot().getViewMap().containsKey(name)) {
                    return true;
                }
            case SESSION:
                if (externalContext.getSessionMap().containsKey(name)) {
                    return true;
                }
            case APPLICATION:
                if (externalContext.getApplicationMap().containsKey(name)) {
                    return true;
                }
        }
        return false;
        
    }




    // ------------------------------------------------------- Lifecycle Methods


    public Object create(String name, FacesContext facesContext) {

        BeanBuilder builder = managedBeans.get(name);

        if (builder != null) {
            if (lazyBeanValidation && !builder.isBaked()) {
                preProcessBean(name, builder);
            }
            if (builder.hasMessages()) {
                throw new ManagedBeanCreationException(buildMessage(name,
                                                                    builder.getMessages(),
                                                                    true));
            } else {
                ELUtils.Scope scope = builder.getScope();
                Object bean;
                switch (scope) {
                    case APPLICATION:
                        synchronized (facesContext.getExternalContext()
                             .getContext()) {
                            bean = createAndPush(name,
                                                 builder,
                                                 scope,
                                                 facesContext);
                        }
                        break;
                    case SESSION:
                        synchronized(facesContext.getExternalContext().getSession(true)) {
                            bean = createAndPush(name,
                                                 builder,
                                                 scope,
                                                 facesContext);
                        }
                        break;
                    case VIEW:
                        synchronized(facesContext.getViewRoot().getViewMap()) {
                            bean = createAndPush(name,
                                                 builder,
                                                 scope,
                                                 facesContext);
                        }
                        break;
                    case REQUEST:
                    default:
                        bean = createAndPush(name,
                                             builder,
                                             scope,
                                             facesContext);
                }

                return bean;
            }
        }

        return null;

    }

    public void destroy(String beanName, Object bean) {

        BeanBuilder builder = managedBeans.get(beanName);
        if (builder != null) {
            builder.destroy(injectionProvider, bean);
        }

    }


    // --------------------------------------------------------- Private Methods


    private void addBean(String beanName, BeanBuilder builder) {

        if (configPreprocessed) {
            preProcessBean(beanName, builder);
        }
        managedBeans.put(beanName, builder);

    }


    private void validateReferences(BeanBuilder builder,
                                    List<String> references,
                                    List<String> messages) {

        List<String> refs = builder.getReferences();
        if (refs != null) {
            for (String ref : refs) {
                if (isManaged(ref)) {
                    if (references.contains(ref)) {
                        StringBuilder sb = new StringBuilder(64);
                        String[] ra =
                             references.toArray(new String[references.size()]);
                        for (int i = 0; i < ra.length; i++) {
                            sb.append(ra[i]);
                            sb.append(" -> ");
                        }
                        sb.append(ref);

                        String message = MessageUtils
                             .getExceptionMessageString(MessageUtils.CYCLIC_REFERENCE_ERROR_ID,
                                                        ra[0],
                                                        sb.toString());
                        messages.add(message);
                    } else {
                        BeanBuilder b = getBuilder(ref);
                        // If the bean has no references, then it's not
                        // a target for cyclic detection.  
                        if (b.getReferences() != null) {
                            references.add(ref);
                            validateReferences(b, references, messages);
                            references.remove(ref);
                        }
                    }
                }
            }
        }

    }


    private synchronized void preProcessBean(String beanName,
                                             BeanBuilder builder) {
        if (!builder.isBaked()) {
            try {
                builder.bake();

                // preProcess any dependent beans
                List<String> propRefs = builder.getReferences();
                if (propRefs != null) {
                    for (String reference : propRefs) {
                        if (isManaged(reference)) {
                            BeanBuilder b = getBuilder(reference);
                            preProcessBean(reference, b);
                        }
                    }
                }

                //noinspection CollectionWithoutInitialCapacity
                List<String> refs = new ArrayList<String>();
                refs.add(beanName);
                //noinspection CollectionWithoutInitialCapacity
                ArrayList<String> messages = new ArrayList<String>();
                validateReferences(builder, refs, messages);
                if (!messages.isEmpty()) {
                    builder.queueMessages(messages);
                }

                if (builder.hasMessages()) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   buildMessage(beanName,
                                                builder.getMessages(),
                                                false));
                    }
                }
            } catch (ManagedBeanPreProcessingException mbpe) {
                if (ManagedBeanPreProcessingException.Type.CHECKED
                     .equals(mbpe.getType())) {
                    builder.queueMessage(mbpe.getMessage());
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   buildMessage(beanName,
                                                builder.getMessages(),
                                                false));
                    }
                } else {
                    String message = MessageUtils.getExceptionMessageString(
                         MessageUtils.MANAGED_BEAN_UNKNOWN_PROCESSING_ERROR_ID,
                         beanName);
                    throw new ManagedBeanPreProcessingException(message, mbpe);
                }
            }
        }

    }

    private Object createAndPush(String name,
                                 BeanBuilder builder,
                                 ELUtils.Scope scope,
                                 FacesContext facesContext) {

        Object bean = builder.build(injectionProvider, facesContext);
        ScopeManager.pushToScope(name, bean, scope, facesContext);
        return bean;

    }


    private String buildMessage(String name, List<String> messages,
                                boolean runtime) {

        StringBuilder sb = new StringBuilder(128);
        if (runtime) {
        sb.append(MessageUtils.getExceptionMessageString(
             MessageUtils.MANAGED_BEAN_PROBLEMS_ERROR_ID, name));
        } else {
            sb.append(MessageUtils.getExceptionMessageString(
                 MessageUtils.MANAGED_BEAN_PROBLEMS_STARTUP_ERROR_ID, name));
        }
        for (String message : messages) {
            sb.append("\n     - ").append(message);
        }
        return sb.toString();

    }


    // ----------------------------------------------------------- Inner Classes


    private static class ScopeManager {

        private static final EnumMap<ELUtils.Scope,ScopeHandler> handlerMap =
             new EnumMap<ELUtils.Scope,ScopeHandler>(ELUtils.Scope.class);

        
        static {
            handlerMap.put(ELUtils.Scope.REQUEST, new RequestScopeHandler());
            handlerMap.put(ELUtils.Scope.VIEW, new ViewScopeHandler());
            handlerMap.put(ELUtils.Scope.SESSION, new SessionScopeHandler());
            handlerMap.put(ELUtils.Scope.APPLICATION, new ApplicationScopeHandler());
        }


        static void pushToScope(String name,
                                Object bean,
                                ELUtils.Scope scope,
                                FacesContext context) {

            ScopeHandler handler = handlerMap.get(scope);
            if (handler != null) {
                handler.handle(name, bean, context);
            }
            
        }

        private interface ScopeHandler {

            void handle(String name, Object bean, FacesContext context);

        }

        private static class RequestScopeHandler implements ScopeHandler {

            public void handle(String name, Object bean, FacesContext context) {

                context.getExternalContext().getRequestMap().put(name, bean);

            }

        }
        
        private static class ViewScopeHandler implements ScopeHandler {

            public void handle(String name, Object bean, FacesContext context) {

                context.getViewRoot().getViewMap().put(name, bean);

            }

        }
        

        private static class SessionScopeHandler implements ScopeHandler  {

            public void handle(String name, Object bean, FacesContext context) {

                context.getExternalContext().getSessionMap().put(name, bean);

            }

        }

        private static class ApplicationScopeHandler implements ScopeHandler {

            public void handle(String name, Object bean, FacesContext context) {

                context.getExternalContext().getApplicationMap().put(name, bean);

            }

        }
    }

}
