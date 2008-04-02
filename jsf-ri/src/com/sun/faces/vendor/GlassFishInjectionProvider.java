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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.vendor;

import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.spi.InjectionProviderException;
import com.sun.faces.util.Util;
import com.sun.enterprise.*;
import com.sun.enterprise.deployment.JndiNameEnvironment;
import com.sun.enterprise.deployment.InjectionInfo;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>This <code>InjectionProvider</code> is specific to the
 * GlassFish/SJSAS 9.x PE/EE application servers.</p>
 */
public class GlassFishInjectionProvider implements InjectionProvider {

    private static final Logger LOGGER = Util.getLogger(Util.FACES_LOGGER
                                                  + Util.APPLICATION_LOGGER);
    private InjectionManager injectionManager;
    private Switch theSwitch;
    private InvocationManager invokeMgr;

    /**
     * <p>Constructs a new <code>GlassFishInjectionProvider</code> instance.</p>
     */
    public GlassFishInjectionProvider() {
        theSwitch = Switch.getSwitch();
        invokeMgr = theSwitch.getInvocationManager();
        injectionManager = theSwitch.getInjectionManager();
    }

    /**
     * <p>The implementation of this method must perform the following
     * steps:
     * <ul>
     * <li>Inject the supported resources per the Servlet 2.5
     * specification into the provided object</li>    
     * </ul>
     * </p>
     *
     * @param managedBean the target managed bean
     */
    public void inject(Object managedBean) throws InjectionProviderException {
        try {
            injectionManager.injectInstance(managedBean,
                                            getNamingEnvironment(),
                                            false);
        } catch (InjectionException ie) {
            throw new InjectionProviderException(ie);
        }
    }

    /**
     * <p>The implemenation of this method must invoke any
     * method marked with the <code>@PreDestroy</code> annotation
     * (per the Common Annotations Specification).
     *
     * @param managedBean the target managed bean
     */
    public void invokePreDestroy(Object managedBean)
    throws InjectionProviderException {
        try {
            injectionManager.invokeInstancePreDestroy(managedBean);
        } catch (InjectionException ie) {
            throw new InjectionProviderException(ie);
        }
    }


    /**
     * <p>The implemenation of this method must invoke any
     * method marked with the <code>@PostConstruct</code> annotation
     * (per the Common Annotations Specification).
     *
     * @param managedBean the target managed bean
     *
     * @throws com.sun.faces.spi.InjectionProviderException
     *          if an error occurs when invoking
     *          the method annotated by the <code>@PostConstruct</code> annotation
     */
    public void invokePostConstruct(Object managedBean)
          throws InjectionProviderException {
            try {
                this.invokePostConstruct(managedBean, getNamingEnvironment());
            } catch (InjectionException ie) {
                throw new InjectionProviderException(ie);
            }

    }


    // --------------------------------------------------------- Private Methods

    /**
     * <p>This is based off of code in <code>InjectionManagerImpl</code>.</p>
     * @return <code>JndiNameEnvironment</code>
     * @throws InjectionException if we're unable to obtain the
     *  <code>JndiNameEnvironment</code>
     */
    private JndiNameEnvironment getNamingEnvironment()
         throws InjectionException {
        ComponentInvocation inv = invokeMgr.getCurrentInvocation();

        if (inv != null) {

            JndiNameEnvironment componentEnv = (JndiNameEnvironment)
                 theSwitch.getDescriptorFor(inv.getContainerContext());

            if (componentEnv != null) {
                return componentEnv;
            } else {
                throw new InjectionException("No descriptor registered for " + " current invocation : " + inv);
            }

        } else {
            throw new InjectionException("null invocation context");
        }
    }


    /**
     * <p>This is based off of code in <code>InjectionManagerImpl</code>.</p>
     *
     * @param instance managed bean instance
     * @param envDescriptor JNDI environment
     * @throws InjectionException if an error occurs
     */
    private void invokePostConstruct(Object instance,
                                     JndiNameEnvironment envDescriptor)
    throws InjectionException {
        LinkedList<Method> preDestroyMethods = new LinkedList<Method>();

        Class<? extends Object> nextClass = instance.getClass();

        // Process each class in the inheritance hierarchy, starting with
        // the most derived class and ignoring java.lang.Object.
        while ((nextClass != Object.class) && (nextClass != null)) {

            InjectionInfo injInfo =
                 envDescriptor.getInjectionInfoByClass(nextClass.getName());

            if (injInfo.getPostConstructMethodName() != null) {

                Method postConstructMethod = getPostConstructMethod
                     (injInfo, nextClass);

                // Invoke the preDestroy methods starting from
                // the least-derived class downward.
                preDestroyMethods.addFirst(postConstructMethod);
            }

            nextClass = nextClass.getSuperclass();
        }

        for (Method preDestroyMethod : preDestroyMethods) {

            invokeLifecycleMethod(preDestroyMethod, instance);

        }

    }


    /**
     * <p>This is based off of code in <code>InjectionManagerImpl</code>.</p>
     * @param injInfo InjectionInfo
     * @param resourceClass target class
     * @return a Method marked with the @PostConstruct annotation
     * @throws InjectionException if an error occurs
     */
    private Method getPostConstructMethod(InjectionInfo injInfo,
                                          Class<? extends Object> resourceClass)
        throws InjectionException {

        Method m = injInfo.getPostConstructMethod();

        if( m == null ) {
            String postConstructMethodName =
                injInfo.getPostConstructMethodName();

            // Check for the method within the resourceClass only.
            // This does not include super-classses.
            for(Method next : resourceClass.getDeclaredMethods()) {
                // InjectionManager only handles injection into PostConstruct
                // methods with no arguments.
                if( next.getName().equals(postConstructMethodName) &&
                    (next.getParameterTypes().length == 0) ) {
                    m = next;
                    injInfo.setPostConstructMethod(m);
                    break;
                }
            }
        }

        if( m == null ) {
            throw new InjectionException
                ("InjectionManager exception. PostConstruct method " +
                 injInfo.getPostConstructMethodName() +
                 " could not be found in class " +
                 injInfo.getClassName());
        }

        return m;
    }


    /**
     * <p>This is based off of code in <code>InjectionManagerImpl</code>.</p>
     * @param lifecycleMethod the method to invoke
     * @param instance the instanced to invoke the method against
     * @throws InjectionException if an error occurs
     */
     private void invokeLifecycleMethod(final Method lifecycleMethod,
                                       final Object instance)
        throws InjectionException {

        try {

            if(LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Calling lifecycle method " +
                             lifecycleMethod + " on class " +
                             lifecycleMethod.getDeclaringClass());
            }

            // Wrap actual value insertion in doPrivileged to
            // allow for private/protected field access.
            java.security.AccessController.doPrivileged(
                new java.security.PrivilegedExceptionAction() {
                    public java.lang.Object run() throws Exception {
                        if( !lifecycleMethod.isAccessible() ) {
                            lifecycleMethod.setAccessible(true);
                        }
                        lifecycleMethod.invoke(instance);
                        return null;
                    }
                });
        } catch( Exception t) {

                String msg = "Exception attempting invoke lifecycle "
                    + " method " + lifecycleMethod;
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, msg, t);
                }
                InjectionException ie = new InjectionException(msg);
                Throwable cause = (t instanceof InvocationTargetException) ?
                    t.getCause() : t;
                ie.initCause( cause );
                throw ie;

        }

        return;

    }


} // END GlassFishInjectionProvider