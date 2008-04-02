/*
 * $Id: LifecycleManagedBeanListener.java,v 1.1 2005/08/29 19:40:24 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package components.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
 *
 * @author edburns
 */
public class LifecycleManagedBeanListener implements ServletRequestListener, HttpSessionListener, ServletContextListener {
    
    /**
     * Creates a new instance of LifecycleManagedBeanListener 
     */
    public LifecycleManagedBeanListener() {
    }
    
    // 
    // Methods from ServletRequestListener
    //
    
    public void requestDestroyed(ServletRequestEvent sre) {
        callPreDestroyMethods((List<Object []>) sre.getServletRequest().getAttribute("components.model"));

    }
    
    public void requestInitialized(ServletRequestEvent sre) {
        
    }
    
    //
    // Methods from HttpSessionListener
    //
    
    public void sessionCreated(HttpSessionEvent se) {
        
    }
    public void sessionDestroyed(HttpSessionEvent se) {
        callPreDestroyMethods((List<Object []>) se.getSession().getAttribute("components.model"));
        
    }
    
    //
    // Methods from ServletContextListener
    //
    
    public void contextDestroyed(ServletContextEvent sce) {
        callPreDestroyMethods((List<Object []>) sce.getServletContext().getAttribute("components.model"));        
    }
    public void contextInitialized(ServletContextEvent sce) {
        
    }
    
    private void callPreDestroyMethods(List<Object []> destroyMethods) {
        if (null == destroyMethods) {
            return;
        }
        for (Object [] destroyMethod : destroyMethods) {
            try {
                ((Method)destroyMethod[0]).invoke(destroyMethod[1]);
                
            } catch (IllegalAccessException accessE) {
                
            } catch (IllegalArgumentException argumentE) {
                
            } catch (InvocationTargetException ite) {
                
            }
        }
        
    }
    
}
