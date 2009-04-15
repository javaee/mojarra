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

import javax.faces.bean.*;
import javax.faces.context.FacesContext;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.mgbean.ManagedBeanInfo;

/**
 * <p>
 * <code>ConfigAnnotationHandler</code> for {@link ManagedBean} annotated
 * classes.
 * </p>
 */
public class ManagedBeanConfigHandler implements ConfigAnnotationHandler {

    private static final Class<?>[] SCOPES = {
          RequestScoped.class,
          ViewScoped.class,
          SessionScoped.class,
          ApplicationScoped.class,
          NoneScoped.class,
          CustomScoped.class
    };

    private static final Collection<Class<? extends Annotation>> HANDLES;
    static {
        Collection<Class<? extends Annotation>> handles =
              new ArrayList<Class<? extends Annotation>>(2);
        handles.add(ManagedBean.class);
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

            manager.register(getBeanInfo(annotatedClass,
                                         (ManagedBean) annotation));
    }


    private ManagedBeanInfo getBeanInfo(Class<?> annotatedClass,
                                        ManagedBean metadata) {

        String name = getName(metadata, annotatedClass);
        String scope = getScope(annotatedClass);
        boolean eager = metadata.eager();
        
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


    private String getScope(Class<?> annotatedClass) {

        for (Class<?> scope : SCOPES) {
            //noinspection unchecked
            Annotation a = annotatedClass.getAnnotation((Class<? extends Annotation>) scope);
            if (a != null) {
                if (a instanceof RequestScoped) {
                    return "request";
                } else if (a instanceof ViewScoped) {
                    return "view";
                } if (a instanceof SessionScoped) {
                    return "session";
                } else if (a instanceof ApplicationScoped) {
                    return "application";
                } else if (a instanceof NoneScoped) {
                    return "none";
                } else if (a instanceof CustomScoped) {
                    return ((CustomScoped) a).value();
                }
            }
        }

        return "request";

    }


    private String getName(ManagedBean managedBean, Class<?> annotatedClass) {

        String name = managedBean.name();

        if (name.length() == 0) {
            String t = annotatedClass.getName();
            name = t.substring(t.lastIndexOf('.') + 1);
            char[] nameChars = name.toCharArray();
            nameChars[0] = Character.toLowerCase(nameChars[0]);
            name = new String(nameChars);
        }

        return name;

    }


}
