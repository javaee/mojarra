/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.action;

import com.sun.faces.action.RequestMappingInfo.MappingType;
import com.sun.faces.lifecycle.RenderResponsePhase;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ActionLifecycle extends Lifecycle {
    
    public static final String ACTION_LIFECYCLE = "com.sun.faces.action.ActionLifecycle";
    
    private final RenderResponsePhase renderResponsePhase;
    private BeanManager beanManager;
    
    public BeanManager getBeanManager() {
        
        if (beanManager == null) {
            Object result = null;

            try {
                InitialContext initialContext = new InitialContext();
                result = initialContext.lookup("java:comp/BeanManager");
            } catch(NamingException exception) {
                try {
                    InitialContext initialContext = new InitialContext();
                    result = initialContext.lookup("java:comp/env/BeanManager");
                } catch(NamingException exception2) {
                }
            }

            if (result != null) {
                beanManager = (BeanManager) result;
            } else {
                beanManager = null;
            }
        }
        
        return beanManager;
    }

    public ActionLifecycle() {
        renderResponsePhase = new RenderResponsePhase();
    }

    @Override
    public void addPhaseListener(PhaseListener listener) {
    }

    @Override
    public void execute(FacesContext context) throws FacesException {
        
        /*
         * 1. Find the bean + method that matches the correct @RequestMapping. 
         */
        Set<Bean<?>> beans = getBeanManager().getBeans(Object.class,new AnnotationLiteral<Any>() {});
        Iterator<Bean<?>> beanIterator = beans.iterator();
        RequestMappingInfo current = null;
        
        while (beanIterator.hasNext()) {
            Bean<?> bean = beanIterator.next();
            RequestMappingInfo info = findMethodRequestMapping(context, bean);
            
            if (current == null) {
                current = info;
            } else if (info != null && info.getLength() > current.getLength()) {
                current = info;
            }
        }
        
        String viewId = null;
        
        if (current != null) {
            /*
             * 2. Get an instance of that bean.
             */
            Instance instance = CDI.current().select(
                    current.getBean().getBeanClass(), new AnnotationLiteral<Any>() {});
            
            try {
                /*
                 * 3. Call the required method and capture its result.
                 *
                 * Currently assuming String invoke() signature, but that obviously
                 * needs to be expanded.
                 */
                viewId = (String) current.getMethod().invoke(instance.get(), new Object[0]);
            } catch (Throwable throwable) {
                throw new FacesException(throwable);
            }
            if (context.getViewRoot() == null) {
                UIViewRoot viewRoot = new UIViewRoot();
                viewRoot.setRenderKitId("HTML_BASIC");
                /*
                 * 4. Set the resulting view id on the viewroot.
                 */
                viewRoot.setViewId(viewId);
                context.setViewRoot(viewRoot);
            }
        }
    }

    @Override
    public PhaseListener[] getPhaseListeners() {
        return new PhaseListener[0];
    }

    @Override
    public void removePhaseListener(PhaseListener listener) {
    }

    @Override
    public void render(FacesContext context) throws FacesException {
        renderResponsePhase.doPhase(context, this, Arrays.asList(getPhaseListeners()).listIterator());
    }

    private RequestMappingInfo findMethodRequestMapping(FacesContext context, Bean<?> bean) {
        RequestMappingInfo result = null;
        Class clazz = bean.getBeanClass();
        AnnotatedType annotatedType = beanManager.createAnnotatedType(clazz);
        Set<AnnotatedMethod> annotatedMethodSet = annotatedType.getMethods();
        for (AnnotatedMethod method : annotatedMethodSet) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String[] mappings = requestMapping.value();
                String mapping = null;
                for (String current : mappings) {
                    String pathInfo = context.getExternalContext().getRequestPathInfo();
                    if (pathInfo.equals(current)) {
                        result = new RequestMappingInfo();
                        result.setBean(bean);
                        result.setMethod(method.getJavaMember());
                        result.setRequestMapping(mapping);
                        result.setMappingType(MappingType.EXACT);
                        break;
                    } else if (current.endsWith("*")) {
                        current = current.substring(0, current.length() - 1);
                        if (pathInfo.startsWith(current)) {
                            if (result == null) {
                                result = new RequestMappingInfo();
                                result.setBean(bean);
                                result.setMethod(method.getJavaMember());
                                result.setRequestMapping(current);
                                result.setMappingType(MappingType.PREFIX);
                            } else if (current.length() > result.getLength()) {
                                result.setBean(bean);
                                result.setMethod(method.getJavaMember());
                                result.setRequestMapping(current);
                                result.setMappingType(MappingType.PREFIX);
                            }
                        }
                    } else if (current.startsWith("*")) {
                        current = current.substring(1);
                        if (pathInfo.endsWith(current)) {
                            result = new RequestMappingInfo();
                            result.setBean(bean);
                            result.setMethod(method.getJavaMember());
                            result.setRequestMapping(current);
                            result.setMappingType(MappingType.EXTENSION);
                            break;
                        }
                    }
                }
            }
            if (result != null &&
                    (result.getMappingType().equals(MappingType.EXACT) ||
                    (result.getMappingType().equals(MappingType.EXTENSION)))) {
                break;
            }
        }
        return result;
    }
}
