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

import com.sun.faces.lifecycle.Phase;
import com.sun.faces.util.Util;
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
import javax.faces.event.PhaseId;

public class ActionPhase extends Phase {

    private BeanManager beanManager;

    public BeanManager getBeanManager(FacesContext facesContext) {
        if (beanManager == null) {
            beanManager = Util.getCdiBeanManager(facesContext);
        }
        return beanManager;
    }

    @Override
    public void execute(FacesContext context) throws FacesException {
        /*
         * 1. Find the bean + method that matches the correct @RequestMapping. 
         */
        Set<Bean<?>> beans = getBeanManager(context).getBeans(Object.class, new AnnotationLiteral<Any>() {
        });
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
                    current.getBean().getBeanClass(), new AnnotationLiteral<Any>() {
                    });

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
    public PhaseId getId() {
        return PhaseId.ANY_PHASE;
    }
    
    private RequestMappingInfo findMethodRequestMapping(FacesContext context, Bean<?> bean) {
        RequestMappingInfo result = null;
        Class clazz = bean.getBeanClass();
        AnnotatedType annotatedType = getBeanManager(context).createAnnotatedType(clazz);
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
                        result.setMappingType(RequestMappingInfo.MappingType.EXACT);
                        break;
                    } else if (current.endsWith("*")) {
                        current = current.substring(0, current.length() - 1);
                        if (pathInfo.startsWith(current)) {
                            if (result == null) {
                                result = new RequestMappingInfo();
                                result.setBean(bean);
                                result.setMethod(method.getJavaMember());
                                result.setRequestMapping(current);
                                result.setMappingType(RequestMappingInfo.MappingType.PREFIX);
                            } else if (current.length() > result.getLength()) {
                                result.setBean(bean);
                                result.setMethod(method.getJavaMember());
                                result.setRequestMapping(current);
                                result.setMappingType(RequestMappingInfo.MappingType.PREFIX);
                            }
                        }
                    } else if (current.startsWith("*")) {
                        current = current.substring(1);
                        if (pathInfo.endsWith(current)) {
                            result = new RequestMappingInfo();
                            result.setBean(bean);
                            result.setMethod(method.getJavaMember());
                            result.setRequestMapping(current);
                            result.setMappingType(RequestMappingInfo.MappingType.EXTENSION);
                            break;
                        }
                    }
                }
            }
            if (result != null &&
                    (result.getMappingType().equals(RequestMappingInfo.MappingType.EXACT) ||
                    (result.getMappingType().equals(RequestMappingInfo.MappingType.EXTENSION)))) {
                break;
            }
        }
        return result;
    }
    
}
