/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.application;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.ClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

import java.util.Map;

import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import javax.faces.FacesWrapper;

import com.sun.faces.RIConstants;
import javax.faces.context.ExternalContext;

/**
 * This {@link javax.faces.application.ApplicationFactory} is responsible for injecting the
 * default {@link Application} instance into the top-level {@link Application}
 * as configured by the runtime.  Doing this allows us to preserve backwards
 * compatibility as the API evolves without having the API rely on implementation
 * specific details.
 */
public class InjectionApplicationFactory extends ApplicationFactory implements FacesWrapper<ApplicationFactory> {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    private static final String APPLICATION_FACTORY = RIConstants.FACES_PREFIX + "APPLICATION_FACTORY";
    private static String APPLICATION_FACTORY_CLASS = RIConstants.FACES_PREFIX + "APPLICATION_FACTORY_CLASS";
    
    // ------------------------------------------------------------ Constructors


    public InjectionApplicationFactory(ApplicationFactory delegate) {

        Util.notNull("applicationFactory", delegate);
        APPLICATION_FACTORY_CLASS = delegate.getClass().getName();
        FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put(APPLICATION_FACTORY, delegate);

    }


    // ----------------------------------------- Methods from ApplicationFactory

    private ApplicationFactory getDelegate() {

        //delegate is retrieved from externalContext's applicationMap
        //if null, make use of the variable APPLICATION_FACTORY_CLASS to
        //reflectively create a new delegate.
        ApplicationFactory delegate = (ApplicationFactory)FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(APPLICATION_FACTORY);
        if (delegate == null) {
            //this code gets executed when there are multiple virtual servers
            //the assumption is that the delegate class has public constructors
            String className = APPLICATION_FACTORY_CLASS;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try {
                Class cl = loader.loadClass(className);
                Constructor constructor = cl.getConstructor(new Class[] {ApplicationFactory.class});
                if (constructor != null) {
                    Object arglist[] = new Object[1];
                    arglist[0] = new ApplicationFactoryImpl();
                    delegate = (ApplicationFactory) constructor.newInstance(arglist);
                } else {
                    //llok for default constructor
                   constructor = cl.getConstructor(new Class[] {});
                   delegate = (ApplicationFactory) constructor.newInstance();
                }
                
                FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put(APPLICATION_FACTORY, delegate);
            } catch (ClassNotFoundException ce) {
                String message = "ClassNotFoundException occurred : " + ce.getMessage();
                throw new IllegalStateException(message);
            } catch (InstantiationException ie) {
                String message = "InstantiationException occurred : " + ie.getMessage();
                throw new IllegalStateException(message);
            } catch (InvocationTargetException le) {
                String message = "InvocationTargetException occurred : " + le.getMessage();
                throw new IllegalStateException(message);
            } catch (IllegalAccessException e) {
                String message = "IllegalAccessException occurred : " + e.getMessage();
                throw new IllegalStateException(message);
            } catch (NoSuchMethodException se) {
                String message = "IllegalAccessException occurred : " + se.getMessage();
                throw new IllegalStateException(message);
            }
        }
        return delegate;
    }

     public static void clearInstance(ExternalContext
         externalContext) {
        Map applicationMap = externalContext.getApplicationMap();

        applicationMap.remove(APPLICATION_FACTORY);
    }

    public Application getApplication() {
        
        Application application = getDelegate().getApplication();
        if (application == null) {
            // No i18n here
                String message = MessageFormat
                      .format("Delegate ApplicationContextFactory, {0}, returned null when calling getApplication().",
                    getDelegate().getClass().getName());
            throw new IllegalStateException(message);
        }
        
        injectDefaultApplication(application);

        return application;

    }

    
    public synchronized void setApplication(Application application) {

        getDelegate().setApplication(application);
        injectDefaultApplication(application);

    }


    // ----------------------------------------------- Methods from FacesWrapper


    @Override
    public ApplicationFactory getWrapped() {

        return getDelegate();
        
    }


    // --------------------------------------------------------- Private Methods


    private void injectDefaultApplication(Application application) {

        Application defaultApplication;
        Field defaultApplicationField;
        FacesContext ctx = FacesContext.getCurrentInstance();
        String attrName = ApplicationImpl.class.getName();
        defaultApplication = (Application) ctx.getExternalContext().getApplicationMap().get(attrName);

        if (defaultApplication != null) {
            try {
                defaultApplicationField =
                        Application.class.getDeclaredField("defaultApplication");
                defaultApplicationField.setAccessible(true);
                defaultApplicationField.set(application, defaultApplication);

            } catch (NoSuchFieldException nsfe) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Unable to find private field named 'defaultApplication' in javax.faces.application.Application.");
                }
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            }
        }
    }

}
