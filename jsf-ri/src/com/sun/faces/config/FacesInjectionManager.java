/*
 * $Id: FacesInjectionManager.java,v 1.5 2006/03/29 23:03:43 rlubke Exp $
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

package com.sun.faces.config;
import com.sun.faces.util.Util;
import static com.sun.faces.RIConstants.EMPTY_CLASS_ARGS;
import static com.sun.faces.RIConstants.EMPTY_METH_ARGS;

import java.lang.reflect.Method;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * InjectionManager provides runtime resource injection(@Resource, @EJB, etc.) 
 * services.  It performs the actual injection into the fields and methods of 
 * Java EE 5 component instances if Sun Java EE RI/GlassFish/SJSAS9.0 is
 * available during runtime. This is a singleton and can be obtained by 
 * invoking the static method.
 */
public class FacesInjectionManager {
    // Log instance for this class
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER 
            + Util.CONFIG_LOGGER);
    
    Method injectMethod = null;
    Boolean injectionPossible = null;
    static FacesInjectionManager facesInjectionManager = null;
    Object injectionManager = null;
    
    public static FacesInjectionManager getInjectionManager() {
        if ( facesInjectionManager == null ) {
            facesInjectionManager = new FacesInjectionManager();
        }
        return facesInjectionManager;
    }
    
    /**
     * Inject the given object instance with the resources from its
     * component environment by delegating to com.sun.enterprise.InjectionManager.
     * The applicable component naming environment information will be retrieved 
     * from the current invocation context. 
     */
    public void injectInstance(Object instance){
        if (injectionPossible == null) {
            getInjectionMethod();
        }
        if (injectionPossible.equals(Boolean.FALSE)) {
            // Resource injection APIs not available.
            return;
        }
        
        assert(injectionManager != null);
        assert(injectMethod != null);
        assert(instance != null);
      
        // inject the instance passed in.
        try {
            // invoke injectInstance method passing in the managed bean instance
            injectMethod.invoke(injectionManager, new Object[] {instance});   
        } catch (Exception e) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Resource Injection failed, ", 
                        e);
            }           
        }
    }
    
    /**
     * Caches the InjectionManager as well as the injectMethod instance if its
     * available to keep reflection to a minimum.
     */
    public void getInjectionMethod() {
        injectionPossible = Boolean.FALSE;
        try {
            // look up Switch class.
            Class switchClass = Class.forName("com.sun.enterprise.Switch");
            // look up getSwitch method.
            Method switchMethod = switchClass.getMethod("getSwitch", 
                                                        EMPTY_CLASS_ARGS);
            // invoke the method and get an instance of Switch
            Object switchObj = switchMethod.invoke(null, EMPTY_METH_ARGS);
            
            // look up getInjectionManager method
            Method injectManagerMethod = 
                        switchClass.getMethod("getInjectionManager", 
                                              EMPTY_CLASS_ARGS);
            // invoke the method and get an instance of InjectionManager
            injectionManager = injectManagerMethod.invoke(switchObj, 
                                                          EMPTY_METH_ARGS);
            // look up injectInstance method.
            injectMethod=  injectionManager.getClass().
                    getMethod("injectInstance", new Class[] {Object.class });  
            injectionPossible = Boolean.TRUE;
        } catch (Exception e) {            
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Resource Injection APIs not available ", 
                        e);
            }            
        }
    }

}
