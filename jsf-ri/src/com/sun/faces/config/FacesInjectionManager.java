/*
 * $Id: FacesInjectionManager.java,v 1.1 2005/07/29 17:42:20 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;
import com.sun.faces.util.Util;

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
            return;
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
            Method switchMethod = switchClass.getMethod("getSwitch", null);
            // invoke the method and get an instance of Switch
            Object switchObj = switchMethod.invoke(null, null);
            
            // look up getInjectionManager method
            Method injectManagerMethod = 
                        switchClass.getMethod("getInjectionManager", null);
            // invoke the method and get an instance of InjectionManager
            injectionManager = injectManagerMethod.invoke(switchObj, null);
            // look up injectInstance method.
            injectMethod=  injectionManager.getClass().
                    getMethod("injectInstance", new Class[] {Object.class });  
            injectionPossible = Boolean.TRUE;
        } catch (Exception e) {            
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Resource Injection APIs not available ", 
                        e);
            }
            return;
        }
    }

}
