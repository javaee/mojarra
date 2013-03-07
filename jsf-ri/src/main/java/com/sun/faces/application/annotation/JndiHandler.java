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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * An abstract RuntimeAnnotationHandler that is capable of dealing with JNDI.
 */
public abstract class JndiHandler implements RuntimeAnnotationHandler {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(JndiHandler.class.getName());
    /**
     * Stores the java:comp/env/ prefix
     */
    protected static final String JAVA_COMP_ENV = "java:comp/env/";

    /**
     * Look up the given object using its JNDI name.
     *
     * @param facesContext the Faces context.
     * @param name the JNDI name.
     * @return the object, or null if an error occurs.
     */
    public Object lookup(FacesContext facesContext, String name) {
        Object object = null;
        try {
            InitialContext context = new InitialContext();
            object = context.lookup(name);
        } catch (NamingException ne) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Unable to lookup: " + name, ne);
            }
            if (facesContext.isProjectStage(ProjectStage.Development)) {
                facesContext.addMessage(null, new FacesMessage("Unable to lookup: " + name, "Unable to lookup: " + name));
            }
        }
        return object;
    }

    /**
     * Set the field.
     *
     * @param facesContext the Faces context.
     * @param field the field.
     * @param instance the instance.
     * @param value the value.
     */
    public void setField(FacesContext facesContext, Field field, Object instance, Object value) {
        synchronized (instance) {
            try {
                boolean fieldAccessible = true;
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                    fieldAccessible = false;
                }
                field.set(instance, value);
                if (!fieldAccessible) {
                    field.setAccessible(false);
                }
            } catch (IllegalArgumentException iae) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Unable to set field: " + field.getName(), iae);
                }
                if (facesContext.isProjectStage(ProjectStage.Development)) {
                    facesContext.addMessage(null, new FacesMessage("Unable to set field: " + field.getName(), "Unable to set field: " + field.getName()));
                }
            } catch (IllegalAccessException iae) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Unable to set field: " + field.getName(), iae);
                }
                if (facesContext.isProjectStage(ProjectStage.Development)) {
                    facesContext.addMessage(null, new FacesMessage("Unable to set field: " + field.getName(), "Unable to set field: " + field.getName()));
                }
            }
        }
    }

    /**
     * Invoke the method.
     * 
     * @param facesContext the Faces context.
     * @param method the method.
     * @param instance the instance.
     * @param value the value.
     */
    protected void invokeMethod(FacesContext facesContext, Method method, Object instance, Object value) {
        synchronized (instance) {
            try {
                boolean accessible = method.isAccessible();
                method.setAccessible(false);
                method.invoke(instance, value);
                method.setAccessible(accessible);
            } catch (InvocationTargetException ite) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Unable to call method: " + method.getName(), ite);
                }
                if (facesContext.isProjectStage(ProjectStage.Development)) {
                    facesContext.addMessage(null, new FacesMessage("Unable to call method: " + method.getName(), "Unable to call method: " + method.getName()));
                }
            } catch (IllegalArgumentException iae) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Unable to call method: " + method.getName(), iae);
                }
                if (facesContext.isProjectStage(ProjectStage.Development)) {
                    facesContext.addMessage(null, new FacesMessage("Unable to call method: " + method.getName(), "Unable to call method: " + method.getName()));
                }
            } catch (IllegalAccessException iae) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Unable to call method: " + method.getName(), iae);
                }
                if (facesContext.isProjectStage(ProjectStage.Development)) {
                    facesContext.addMessage(null, new FacesMessage("Unable to call method: " + method.getName(), "Unable to call method: " + method.getName()));
                }
            }
        }
    }
}
