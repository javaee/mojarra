/*
 * $Id: BeanManager.java,v 1.3 2007/04/27 22:00:59 ofung Exp $
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
