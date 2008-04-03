/*
 * $Id: BeanManager.java,v 1.1 2007/04/22 21:41:05 rlubke Exp $
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
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.mgbean;

import com.sun.faces.el.ELUtils;
import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Main interface for dealing with JSF managed beans</p>
 */
public class BeanManager {

    private static final Logger LOGGER =
         Logger.getLogger(FacesLogger.MANAGEDBEAN.getLoggerName());

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
            managedBeans.put(beanName, builder);
        } else {
            managedBeans.put(beanName, builder);
        }
    }


    private void validateReferences(BeanBuilder builder,
                                    Set<String> references,
                                    ArrayList<String> messages) {

        List<String> refs = builder.getReferences();
        if (refs != null) {
            for (String ref : refs) {
                if (isManaged(ref)) {
                    if (!references.add(ref)) {
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
                        validateReferences(getBuilder(ref),
                                           references,
                                           messages);
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

                //noinspection CollectionWithoutInitialCapacity
                Set refs = new HashSet();
                refs.add(beanName);
                //noinspection CollectionWithoutInitialCapacity
                ArrayList<String> messages = new ArrayList();
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
