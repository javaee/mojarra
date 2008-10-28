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

package com.sun.faces.application.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.text.MessageFormat;

import javax.faces.model.ManagedBean;
import javax.faces.model.ManagedBeans;
import javax.faces.model.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.mgbean.ManagedBeanInfo;
import com.sun.faces.el.ELUtils;

/**
 * <p>
 * <code>ConfigAnnotationHandler</code> for {@link ManagedBeans} and
 * {@link ManagedBean} annotated classes.
 * </p>
 */
public class ManagedBeanConfigHandler implements ConfigAnnotationHandler {

    private static final Collection<Class<? extends Annotation>> HANDLES;
    static {
        Collection<Class<? extends Annotation>> handles =
              new ArrayList<Class<? extends Annotation>>(2);
        handles.add(ManagedBean.class);
        handles.add(ManagedBeans.class);
        HANDLES = Collections.unmodifiableCollection(handles);
    }

    private Map<Class<?>,Annotation> managedBeans;


    // ------------------------------------ Methods from ConfigAnnotationHandler


    /**
     * @see com.sun.faces.application.annotation.ConfigAnnotationHandler#getHandledAnnotations()
     */
    public Collection<Class<? extends Annotation>> getHandledAnnotations() {

        return HANDLES;

    }


    /**
     * @see com.sun.faces.application.annotation.ConfigAnnotationHandler#collect(Class, java.lang.annotation.Annotation)
     */
    public void collect(Class<?> target, Annotation annotation) {

        if (managedBeans == null) {
            managedBeans = new HashMap<Class<?>,Annotation>();
        }
        managedBeans.put(target, annotation);

    }


    /**
     * @see com.sun.faces.application.annotation.ConfigAnnotationHandler#push(javax.faces.context.FacesContext)
     */
    public void push(FacesContext ctx) {

        if (managedBeans != null) {
            ApplicationAssociate associate =
                  ApplicationAssociate.getInstance(ctx.getExternalContext());
            if (associate != null) {
                BeanManager manager = associate.getBeanManager();
                for (Map.Entry<Class<?>,Annotation> entry : managedBeans.entrySet()) {
                    process(manager, entry.getKey(), entry.getValue());
                }
            }
        }
    }


    // --------------------------------------------------------- Private Methods


    private void process(BeanManager manager,
                         Class<?> annotatedClass,
                         Annotation annotation) {

        if (annotation instanceof ManagedBeans) {
            ManagedBeans beansAnnotation = (ManagedBeans) annotation;
            ManagedBean[] beans = beansAnnotation.value();
            if (beans.length > 0) {
                ManagedBeanInfo info = getBeanInfo(annotatedClass,
                                                   beans[0]);
                manager.register(info);
                for (int i = 1; i < beans.length; i++) {
                    ManagedBean managedBean = beans[i];

                    manager.register(info.clone(managedBean.name(),
                                                managedBean.scope(),
                                                managedBean.eager(),
                                                info));
                }
            }
        } else {
            manager.register(getBeanInfo(annotatedClass,
                                         (ManagedBean) annotation));
        }
        
    }


    private ManagedBeanInfo getBeanInfo(Class<?> annotatedClass,
                                        ManagedBean metadata) {

        String name = metadata.name();
        String scope = metadata.scope();
        boolean eager = metadata.eager();

        if (!ELUtils.isScopeValid(scope)) {
            // RELEASE_PENDING (i18n)
            throw new FacesException(
                  MessageFormat.format("ManagedBean annotation scope {0} on class {1} is invalid.  It must be one of 'request', 'session', 'application', or 'none'",
                                       scope, annotatedClass.getName()));
        }

        Field[] fields = annotatedClass.getDeclaredFields();
        List<ManagedBeanInfo.ManagedProperty> properties = null;
        if (fields.length > 0) {
            for (Field field : fields) {
                ManagedProperty property = field.getAnnotation(ManagedProperty.class);
                if (property != null) {
                    if (properties == null) {
                        properties = new ArrayList<ManagedBeanInfo.ManagedProperty>();
                    }
                    String n = property.name();
                    ManagedBeanInfo.ManagedProperty propertyInfo =
                          new ManagedBeanInfo.ManagedProperty(((n != null && n.length() != 0) ? n : field.getName()),
                                                              field.getType().getName(),
                                                              property.value(),
                                                              null,
                                                              null);
                    properties.add(propertyInfo);
                }
            }
        }

        return new ManagedBeanInfo(name,
                                   annotatedClass.getName(),
                                   scope,
                                   eager,
                                   null,
                                   null,
                                   properties,
                                   null);

    }


}
