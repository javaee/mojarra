/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.application.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;
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
        
        Map<String,Field> annotatedFields = new LinkedHashMap<String,Field>();
        //Map<String, Method> annotatedMethods = new LinkedHashMap<String,Method>();
        collectAnnotatedFields(annotatedClass, annotatedFields);
        //collectAnnotatedMethods(annotatedClass,
        //                        annotatedMethods,
        //                        annotatedFields.keySet());

        List<ManagedBeanInfo.ManagedProperty> properties = null;

        if (!annotatedFields.isEmpty()) {
            properties = new ArrayList<ManagedBeanInfo.ManagedProperty>(annotatedFields.size());
            for (Map.Entry<String,Field> entry : annotatedFields.entrySet()) {
                Field f = entry.getValue();
                ManagedProperty property = f.getAnnotation(ManagedProperty.class);
                ManagedBeanInfo.ManagedProperty propertyInfo =
                          new ManagedBeanInfo.ManagedProperty(entry.getKey(),
                                                              f.getType().getName(),
                                                              property.value(),
                                                              null,
                                                              null);
                properties.add(propertyInfo);
            }
        }
        /*
        if (!annotatedMethods.isEmpty()) {
            if (properties == null) {
                properties = new ArrayList<ManagedBeanInfo.ManagedProperty>(annotatedMethods.size());
                for (Map.Entry<String,Method> entry : annotatedMethods.entrySet()) {
                    Method m = entry.getValue();
                    ManagedProperty property = m.getAnnotation(ManagedProperty.class);
                    String alias = property.name();
                    if (alias != null && alias.length() == 0) {
                        alias = null;
                    }
                    ManagedBeanInfo.ManagedProperty propertyInfo =
                          new ManagedBeanInfo.ManagedProperty(alias,
                                                              entry.getKey(),
                                                              m.getParameterTypes()[0].getName(),
                                                              property.value(),
                                                              null,
                                                              null);
                    properties.add(propertyInfo);
                }
            }
        }
        */

        return new ManagedBeanInfo(name,
                                   annotatedClass.getName(),
                                   scope,
                                   eager,
                                   null,
                                   null,
                                   properties,
                                   null);

    }


//    private void collectAnnotatedMethods(Class<?> baseClass,
//                                         Map<String,Method> annotatedMethods,
//                                         Set<String> annotatedFields) {
//
//        Method[] methods = baseClass.getDeclaredMethods();
//        for (Method method : methods) {
//            ManagedProperty property = method.getAnnotation(ManagedProperty.class);
//            if (property != null) {
//
//                if (!method.getName().startsWith("set")
//                    || method.getParameterTypes().length != 1) {
//                    continue;
//                }
//                StringBuilder sb =
//                      new StringBuilder(method.getName().substring(3));
//                char c = sb.charAt(0);
//                sb.deleteCharAt(0);
//                sb.insert(0, Character.toLowerCase(c));
//                String propName = sb.toString();
//
//                if (!annotatedFields.contains(propName) && !annotatedMethods.containsKey(propName)) {
//                    annotatedMethods.put(propName, method);
//                }
//            }
//        }
//        Class<?> superClass = baseClass.getSuperclass();
//        if (!Object.class.equals(superClass)) {
//            collectAnnotatedMethods(superClass, annotatedMethods, annotatedFields);
//        }
//    }


    private void collectAnnotatedFields(Class<?> baseClass, Map<String,Field> annotatedFields) {

        Field[] fields = baseClass.getDeclaredFields();
        for (Field field : fields) {
            ManagedProperty property = field.getAnnotation(ManagedProperty.class);
            if (property != null) {
                String propName = property.name();
                if (propName == null || propName.length() == 0) {
                    propName = field.getName();
                }
                // if the field has already been collected, don't replace the existing
                // value as that value represents an override.
                if (!annotatedFields.containsKey(propName)) {
                    annotatedFields.put(propName, field);
                }
            }
        }
        Class<?> superClass = baseClass.getSuperclass();
        if (!Object.class.equals(superClass)) {
            collectAnnotatedFields(superClass, annotatedFields);
        }

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
