/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.cdi;

import static com.sun.faces.cdi.CdiUtils.getAnnotation;
import static java.util.Collections.unmodifiableMap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessManagedBean;
import javax.faces.annotation.ManagedProperty;
import javax.faces.model.DataModel;
import javax.faces.model.FacesDataModel;

import com.sun.faces.push.WebsocketChannelManager;
import com.sun.faces.push.WebsocketSessionManager;
import com.sun.faces.push.WebsocketUserManager;



/**
 * The CDI extension.
 */
public class CdiExtension implements Extension {
    
    /**
     * Map of classes that can be wrapped by a data model to data model implementation classes
     */
    private Map<Class<?>, Class<? extends DataModel<?>>> forClassToDataModelClass = new HashMap<>();
    
    private Set<Type> managedPropertyTargetTypes = new HashSet<>();

    
    /**
     * Before bean discovery.
     * 
     * @param beforeBeanDiscovery the before bean discovery.
     * @param beanManager the bean manager.
     */
    public void beforeBean(@Observes BeforeBeanDiscovery beforeBeanDiscovery, BeanManager beanManager) {
        beforeBeanDiscovery.addAnnotatedType(beanManager.createAnnotatedType(WebsocketUserManager.class), null);
        beforeBeanDiscovery.addAnnotatedType(beanManager.createAnnotatedType(WebsocketSessionManager.class), null);
        beforeBeanDiscovery.addAnnotatedType(beanManager.createAnnotatedType(WebsocketChannelManager.class), null);
        beforeBeanDiscovery.addAnnotatedType(beanManager.createAnnotatedType(WebsocketChannelManager.ViewScope.class), null);
        beforeBeanDiscovery.addAnnotatedType(beanManager.createAnnotatedType(InjectionPointGenerator.class), null);
        beforeBeanDiscovery.addAnnotatedType(beanManager.createAnnotatedType(WebsocketPushContextProducer.class), null);
    }

    /**
     * After bean discovery.
     *
     * @param afterBeanDiscovery the after bean discovery.
     */
    public void afterBean(final @Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager) {
        afterBeanDiscovery.addBean(new ApplicationProducer());
        afterBeanDiscovery.addBean(new ApplicationMapProducer());
        afterBeanDiscovery.addBean(new CompositeComponentProducer());
        afterBeanDiscovery.addBean(new ComponentProducer());
        afterBeanDiscovery.addBean(new FlashProducer());
        afterBeanDiscovery.addBean(new FlowMapProducer());
        afterBeanDiscovery.addBean(new HeaderMapProducer());
        afterBeanDiscovery.addBean(new HeaderValuesMapProducer());
        afterBeanDiscovery.addBean(new InitParameterMapProducer());
        afterBeanDiscovery.addBean(new RequestParameterMapProducer());
        afterBeanDiscovery.addBean(new RequestParameterValuesMapProducer());
        afterBeanDiscovery.addBean(new RequestProducer());
        afterBeanDiscovery.addBean(new RequestMapProducer());
        afterBeanDiscovery.addBean(new ResourceHandlerProducer());
        afterBeanDiscovery.addBean(new ExternalContextProducer());
        afterBeanDiscovery.addBean(new FacesContextProducer());
        afterBeanDiscovery.addBean(new RequestCookieMapProducer());
        afterBeanDiscovery.addBean(new SessionProducer());
        afterBeanDiscovery.addBean(new SessionMapProducer());
        afterBeanDiscovery.addBean(new ViewMapProducer());
        afterBeanDiscovery.addBean(new ViewProducer());
        afterBeanDiscovery.addBean(new DataModelClassesMapProducer());
        
        for (Type type : managedPropertyTargetTypes) {
            afterBeanDiscovery.addBean(new ManagedPropertyProducer(type, beanManager));
        }
    }
    
    /**
     * Processing of beans
     * 
     * @param event the process bean event
     * @param beanManager the current bean manager
     */
    @SuppressWarnings("unchecked")
    public <T extends DataModel<?>> void processBean(@Observes ProcessBean<T> event, BeanManager beanManager) {
        
        Optional<FacesDataModel> optionalFacesDataModel = getAnnotation(beanManager, event.getAnnotated(), FacesDataModel.class);
        if (optionalFacesDataModel.isPresent()) {
            forClassToDataModelClass.put(
                optionalFacesDataModel.get().forClass(), 
                (Class<? extends DataModel<?>>) event.getBean().getBeanClass());
        }
    }
    
    public <T> void collect(@Observes ProcessManagedBean<T> event) {
        for (AnnotatedField<? super T> field : event.getAnnotatedBeanClass().getFields()) {
            if (field.isAnnotationPresent(ManagedProperty.class) && field.getBaseType() instanceof Class) {
                managedPropertyTargetTypes.add(field.getBaseType());
            }
        }
    }
    
    
    /**
     * After deployment validation
     * 
     * @param event the after deployment validation event
     * @param beanManager the current bean manager
     */
    public void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager beanManager) {
        
        // Sort the classes wrapped by a DataModel that we collected in processBean() such that
        // for any 2 classes X and Y from this collection, if an object of X is an instanceof an object of Y,
        // X appears in the collection before Y. The collection's sorting is otherwise arbitrary.
        //
        // E.g.
        //
        // Given class B, class A extends B and class Q, two possible orders are;
        // 1. {A, B, Q}
        // 2. {Q, A, B}
        //
        // The only requirement here is that A appears before B, since A is a subclass of B.
        //
        // This sorting is used so given an instance of type Z that's being bound to a UIData or UIRepeat
        // component, we can find the most specific DataModel that can wrap Z by iterating through the sorted 
        // collection from beginning to end and stopping this iteration at the first match.
        
        List<Class<?>> sortedForDataModelClasses = new ArrayList<>();
        for (Class<?> clazz : forClassToDataModelClass.keySet()) {
            int highestSuper = -1;
            boolean added = false;
            for (int i = 0; i < sortedForDataModelClasses.size(); i++) {
                if (sortedForDataModelClasses.get(i).isAssignableFrom(clazz)) {
                    sortedForDataModelClasses.add(i, clazz);
                    added = true;
                    break;
                } else if (clazz.isAssignableFrom(sortedForDataModelClasses.get(i))) {
                    highestSuper = i;
                }
            }
            if (!added) {
                if (highestSuper > -1) {
                    sortedForDataModelClasses.add(highestSuper + 1, clazz);
                } else {
                    sortedForDataModelClasses.add(clazz);
                }
            }
        }
        
        // Use the sorting computed above to order the Map on this. Note that a linked hash map is used
        // to preserve this ordering.
        
        Map<Class<?>, Class<? extends DataModel<?>>> linkedForClassToDataModelClass = new LinkedHashMap<>();
        for (Class<?> sortedClass : sortedForDataModelClasses) {
            linkedForClassToDataModelClass.put(sortedClass, forClassToDataModelClass.get(sortedClass)); 
        }
        
        forClassToDataModelClass = unmodifiableMap(linkedForClassToDataModelClass);
    }
    
    /**
     * Gets the map of classes that can be wrapped by a data model to data model implementation classes
     * 
     * @return Map of classes that can be wrapped by a data model to data model implementation classes
     */
    public Map<Class<?>, Class<? extends DataModel<?>>> getForClassToDataModelClass() {
        return forClassToDataModelClass;
    }

}
