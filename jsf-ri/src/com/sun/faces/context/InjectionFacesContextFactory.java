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

package com.sun.faces.context;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FacesException;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import javax.faces.FacesWrapper;

/**
 * This {@link FacesContextFactory} is responsible for injecting the
 * default {@link FacesContext} and {@link ExternalContext} instances into
 * the top-level {@link FacesContext} and {@link ExternalContext} instances
 * as configured by the runtime.  Doing this allows us to preserve backwards
 * compatibility as the API evolves without having the API rely on implementation
 * specific details.  
 */
public class InjectionFacesContextFactory extends FacesContextFactory implements FacesWrapper<FacesContextFactory> {

    private static final Logger LOGGER = FacesLogger.CONTEXT.getLogger();
    private FacesContextFactory delegate;
    private Field defaultFacesContext;
    private Field defaultExternalContext;

    
    // ------------------------------------------------------------ Constructors


    public InjectionFacesContextFactory(FacesContextFactory delegate) {

        Util.notNull("facesContextFactory", delegate);
        this.delegate = delegate;
         try {
            defaultFacesContext = FacesContext.class.getDeclaredField("defaultFacesContext");
            defaultFacesContext.setAccessible(true);
        } catch (NoSuchFieldException nsfe) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unable to find private field named 'defaultFacesContext' in javax.faces.context.FacesContext.");
            }
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
            defaultFacesContext = null;
        }

        try {
            defaultExternalContext = ExternalContext.class.getDeclaredField("defaultExternalContext");
            defaultExternalContext.setAccessible(true);
        } catch (NoSuchFieldException nsfe) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unable to find private field named 'defaultExternalContext' in javax.faces.context.ExternalContext.");
            }
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
            defaultExternalContext = null;
        }

    }


    // ---------------------------------------- Methods from FacesContextFactory


    public FacesContext getFacesContext(Object context,
                                        Object request,
                                        Object response,
                                        Lifecycle lifecycle)
    throws FacesException {

        FacesContext ctx = delegate.getFacesContext(context,
                                                    request,
                                                    response,
                                                    lifecycle);
        if (ctx == null) {
            // No i18n here
            String message = MessageFormat
                  .format("Delegate FacesContextFactory, {0}, returned null when calling getFacesContext().",
                          delegate.getClass().getName());
            throw new IllegalStateException(message);
        }
        injectDefaults(ctx);
        return ctx;

    }


    // ---------------------------------------------------------- Public Methods


    @Override
    public FacesContextFactory getWrapped() {

        return delegate;
        
    }


    // --------------------------------------------------------- Private Methods


    private void injectDefaults(FacesContext target) {

        if (defaultFacesContext != null && defaultExternalContext != null) {
            FacesContext defaultFC =
                  FacesContextImpl.getDefaultFacesContext();
            if (defaultFC != null) {
                try {
                    defaultFacesContext.set(target, defaultFC);
                    defaultExternalContext.set(target.getExternalContext(),
                                               defaultFC.getExternalContext());
                } catch (IllegalAccessException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, e.toString(), e);
                    }
                }
            }
        }

    }

}
