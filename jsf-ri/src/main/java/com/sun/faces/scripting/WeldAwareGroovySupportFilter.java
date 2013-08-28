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

package com.sun.faces.scripting;

import com.sun.faces.scripting.groovy.GroovyHelper;
import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;

class WeldAwareGroovySupportFilter implements Filter {

    private boolean helperChecked;
    private GroovyHelper helper;
    private ServletContext sc;
    
    private Class containerClass;
    private Field instanceField;
    private Method instanceMethod;
    
    private Class singletonClass;
    private Method singletonSetMethod;

    public void init(FilterConfig filterConfig) throws ServletException {
        sc = filterConfig.getServletContext();
        try {
            obtainReflectionReferences();
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
    
    private void obtainReflectionReferences() throws Exception {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        
        containerClass = tccl.loadClass("org.jboss.weld.Container");
        instanceField = containerClass.getDeclaredField("instance");
        instanceField.setAccessible(true);
        Class [] paramTypes = new Class[0];
        instanceMethod = containerClass.getDeclaredMethod("instance", paramTypes);
        instanceMethod.setAccessible(true);
        
        singletonClass = tccl.loadClass("org.jboss.weld.bootstrap.api.Singleton");
        paramTypes = new Class[1];
        paramTypes[0] = Object.class;
        singletonSetMethod = singletonClass.getDeclaredMethod("set", paramTypes);
        
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
          throws IOException, ServletException {

        if (!helperChecked) {
            helper = GroovyHelper.getCurrentInstance(sc);
            // if null at this point, it will be null for the remainder
            // of the app.  Set a flag so that we don't continually hit
            // the ServletContext looking up the helper.
            helperChecked = true;
        }
        if (helper != null) {
            // Get the "real" ContextClassLoader
            Thread currentThread = Thread.currentThread();
            ClassLoader contextClassLoader = currentThread.getContextClassLoader();
            
            // Get the MojarraGroovyClassLoader
            helper.setClassLoader();
            ClassLoader mojarraGroovyClassLoader = currentThread.getContextClassLoader();
            
            // Set the ContextClassLoader back
            currentThread.setContextClassLoader(contextClassLoader);
            
            // Obtain the Container
            Object c = null;
            try {
                c = getWeldContainerInstance();
            } catch (Exception ex) {
                Logger.getLogger(WeldAwareGroovySupportFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Set the MojarraGroovyClassLoader back
            currentThread.setContextClassLoader(mojarraGroovyClassLoader);
            try {
                installMojarraGroovyClassLoaderToContainer(c);
            } catch (Exception ex) {
                Logger.getLogger(WeldAwareGroovySupportFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Re-install the MojarraGroovyClassLoader
            helper.setClassLoader();


        }
        filterChain.doFilter(servletRequest, servletResponse);
        
    }
    
    private Object getWeldContainerInstance() throws Exception {
        Object [] args = new Object[0];
        Object result = instanceMethod.invoke(null, args);
        
        return result;
    }
    
    private void installMojarraGroovyClassLoaderToContainer(Object c) throws NoSuchFieldException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Object aclSingletonProvider = instanceField.get(null);
        
        singletonSetMethod.invoke(aclSingletonProvider, c);
        
        
    }
    

    public void destroy() {
        // no-op
    }
    
}
