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

package javax.faces;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;

/*
 * Bug 20458755
 
 * This class provides a utility method to look up the current FacesContext
 * without performing the additional check introduced in FacesContext.getCurrentInstance()
 * by this bug fix.

 * This class also is a FacesContextFactory implementation that ignores 
 * all arguments and is able to look up the FacesContext corresponding
 * to the ServletContext corresponding to the current Thread context ClassLoader.

 * This FacesContextFactory implementation is used by FacesContext.getCurrentInstance()
 * so that the init FacesContext can be correctly looked up regardless of 
 * thread re-use.
 */
final class ServletContextFacesContextFactory  extends FacesContextFactory {
    static final String SERVLET_CONTEXT_FINDER_NAME = "com.sun.faces.ServletContextFacesContextFactory";
    static final String SERVLET_CONTEXT_FINDER_REMOVAL_NAME = "com.sun.faces.ServletContextFacesContextFactory_Removal";
    
    private static final Logger LOGGER;
    
    static {
        LOGGER = Logger.getLogger("javax.faces", "javax.faces.LogStrings");
    }

    private ThreadLocal<FacesContext> facesContextCurrentInstance;
    private ConcurrentHashMap<Thread, FacesContext> facesContextThreadInitContextMap;
    private ConcurrentHashMap<FacesContext, ServletContext> initContextServletContextMap;

    ServletContextFacesContextFactory() {
        try {
            Field instanceField = FacesContext.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            facesContextCurrentInstance = (ThreadLocal<FacesContext>) instanceField.get(null);
            
            Field threadInitContextMapField = FacesContext.class.getDeclaredField("threadInitContext");
            threadInitContextMapField.setAccessible(true);
            facesContextThreadInitContextMap = (ConcurrentHashMap<Thread, FacesContext>) threadInitContextMapField.get(null);
            
            Field initContextServletContextMapField = FacesContext.class.getDeclaredField("initContextServletContext");
            initContextServletContextMapField.setAccessible(true);
            initContextServletContextMap = (ConcurrentHashMap<FacesContext, ServletContext>) initContextServletContextMapField.get(null);
            
        } catch (IllegalAccessException ex) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unable to access instance field of FacesContext", ex);
            }
        } catch (IllegalArgumentException ex) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unable to access instance field of FacesContext", ex);
            }
        } catch (NoSuchFieldException ex) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unable to access instance field of FacesContext", ex);
            }
        } catch (SecurityException ex) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unable to access instance field of FacesContext", ex);
            }
        }
    }
    
    /*
     * This method does what FacesContext.getCurrentInstance() did *before*
     * the fix for Bug 20458755.
     */
    FacesContext getFacesContextWithoutServletContextLookup() {
        FacesContext result = facesContextCurrentInstance.get();
        if (null == result) {
            if (null != facesContextThreadInitContextMap && !facesContextThreadInitContextMap.isEmpty()) {
                result = facesContextThreadInitContextMap.get(Thread.currentThread());
            }
        }
        return result;
    }
    
    /*
     * Consult the initContextServletContextMap (reflectively obtained from
     * the FacesContext in our ctor).  If it is non-empty, obtain
     * the ServletContext corresponding to the current Thread's context ClassLoader.
     * If found, use the initContextServletContextMap to find the FacesContext
     * corresponding to that ServletContext.
     */

    @Override
    public FacesContext getFacesContext(Object context, Object request, Object response, Lifecycle lifecycle) throws FacesException {
        FacesContext result = null;
        if (null != initContextServletContextMap && !initContextServletContextMap.isEmpty()) {
            ServletContext servletContext = (ServletContext) FactoryFinder.FACTORIES_CACHE.getServletContextForCurrentClassLoader();
            if (null != servletContext) {
                for (Map.Entry<FacesContext, ServletContext> entry : initContextServletContextMap.entrySet()) {
                    if (servletContext.equals(entry.getValue())) {
                        result = entry.getKey();
                        break;
                    }
                }
            }
        }
            
        return result;
    }
    
    

}
