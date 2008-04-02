/*
 * $Id: FactoryFinder.java,v 1.33 2007/01/29 18:02:52 rlubke Exp $
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

package javax.faces;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.lang.reflect.Constructor;


/**
 * <p><strong>FactoryFinder</strong> implements the standard discovery
 * algorithm for all factory objects specified in the JavaServer Faces
 * APIs.  For a given factory class name, a corresponding implementation
 * class is searched for based on the following algorithm.  Items are
 * listed in order of decreasing search precedence:</p> <ul>
 * <p/>
 * <li>If the JavaServer Faces configuration file bundled into the
 * <code>WEB-INF</code> directory of the webapp contains a
 * <code>factory</code> entry of the given factory class name, that
 * factory is used.</li>
 * <p/>
 * <li>If the JavaServer Faces configuration files named by the
 * <code>javax.faces.CONFIG_FILES</code>
 * <code>ServletContext</code> init parameter contain any
 * <code>factory</code> entries of the given factory class name, those
 * factories are used, with the last one taking precedence.</li>
 * <p/>
 * <li>If there are any JavaServer Faces configuration files bundled
 * into the <code>META-INF</code> directory of any jars on the
 * <code>ServletContext</code>'s resource paths, the
 * <code>factory</code> entries of the given factory class name in those
 * files are used, with the last one taking precedence.</li>
 * <p/>
 * <li>If a <code>META-INF/services/{factory-class-name}</code> resource
 * is visible to the web application class loader for the calling
 * application (typically as a result of being present in the manifest
 * of a JAR file), its first line is read and assumed to be the name of
 * the factory implementation class to use.</li>
 * <p/>
 * <li>If none of the above steps yield a match, the JavaServer Faces
 * implementation specific class is used.</li>
 * <p/>
 * </ul>
 * <p/>
 * <p>If any of the factories found on any of the steps above happen to
 * have a one-argument constructor, with argument the type being the
 * abstract factory class, that constructor is invoked, and the previous
 * match is passed to the constructor.  For example, say the container
 * vendor provided an implementation of {@link
 * javax.faces.context.FacesContextFactory}, and identified it in
 * <code>META-INF/services/javax.faces.context.FacesContextFactory</code>
 * in a jar on the webapp ClassLoader.  Also say this implementation
 * provided by the container vendor had a one argument constructor that
 * took a <code>FacesContextFactory</code> instance.  The
 * <code>FactoryFinder</code> system would call that one-argument
 * constructor, passing the implementation of
 * <code>FacesContextFactory</code> provided by the JavaServer Faces
 * implementation.</p>
 * <p/>
 * <p>If a Factory implementation does not provide a proper one-argument
 * constructor, it must provide a zero-arguments constructor in order to
 * be successfully instantiated.</p>
 * <p/>
 * <p>Once the name of the factory implementation class is located, the
 * web application class loader for the calling application is requested
 * to load this class, and a corresponding instance of the class will be
 * created.  A side effect of this rule is that each web application will
 * receive its own instance of each factory class, whether the JavaServer
 * Faces implementation is included within the web application or is made
 * visible through the container's facilities for shared libraries.</p>
 */

public final class FactoryFinder {

    // ----------------------------------------------------------- Constructors


    /**
     * Package-private constructor to disable instantiation of this class.
     */
    FactoryFinder() {
    }

    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The property name for the
     * {@link javax.faces.application.ApplicationFactory} class name.</p>
     */
    public final static String APPLICATION_FACTORY =
         "javax.faces.application.ApplicationFactory";


    /**
     * <p>The property name for the
     * {@link javax.faces.context.FacesContextFactory} class name.</p>
     */
    public final static String FACES_CONTEXT_FACTORY =
         "javax.faces.context.FacesContextFactory";


    /**
     * <p>The property name for the
     * {@link javax.faces.lifecycle.LifecycleFactory} class name.</p>
     */
    public final static String LIFECYCLE_FACTORY =
         "javax.faces.lifecycle.LifecycleFactory";


    /**
     * <p>The property name for the
     * {@link javax.faces.render.RenderKitFactory} class name.</p>
     */
    public final static String RENDER_KIT_FACTORY =
         "javax.faces.render.RenderKitFactory";

    // ------------------------------------------------------- Static Variables

    /**
     * <p>Keys are web application class loaders.  Values are factory
     * maps for each web application.</p>
     * <p/>
     * <p>For the nested map, the keys are the factoryName, which must
     * be one of the <code>*_FACTORY</code> constants above.  Values are
     * one of: </p>
     * <p/>
     * <ol>
     * <p/>
     * <li><p>the actual factory class, if {@link getFactory} has been
     * called before on this factoryName</p></li>
     * <p/>
     * <li><p>An <code>ArrayList</code> of <code>Strings</code>
     * representing the configured implementations of for the
     * factoryName.</p></li>
     * <p/>
     * </ol>
     */
    @SuppressWarnings({"CollectionWithoutInitialCapacity"})
    private static final Map<ClassLoader, Map<String, Object>> applicationMaps =
         new HashMap<ClassLoader, Map<String, Object>>();


    /**
     * <p>The set of JavaServer Faces factory classes for which the factory
     * discovery mechanism is supported.</p>
     */
    private static String[] factoryNames = {
         APPLICATION_FACTORY,
         FACES_CONTEXT_FACTORY,
         LIFECYCLE_FACTORY,
         RENDER_KIT_FACTORY
    };

    /**
     * <p>Map of Class instances for the our factoryNames.</p>
     */

    private static Map<String, Class> factoryClasses = null;

    private static final Logger LOGGER =
         Logger.getLogger("javax.faces", "javax.faces.LogStrings");

    // --------------------------------------------------------- Public Methods


    /**
     * <p>Create (if necessary) and return a per-web-application instance of
     * the appropriate implementation class for the specified JavaServer Faces
     * factory class, based on the discovery algorithm described in the
     * class description.</p>
     *
     * @param factoryName Fully qualified name of the JavaServer Faces factory
     *                    for which an implementation instance is requested
     * @throws FacesException           if the web application class loader
     *                                  cannot be identified
     * @throws FacesException           if an instance of the configured factory
     *                                  implementation class cannot be loaded
     * @throws FacesException           if an instance of the configured factory
     *                                  implementation class cannot be instantiated
     * @throws IllegalArgumentException if <code>factoryName</code> does not
     *                                  identify a standard JavaServer Faces factory name
     * @throws IllegalStateException    if there is no configured factory
     *                                  implementation class for the specified factory name
     * @throws NullPointerException     if <code>factoryname</code>
     *                                  is null
     */
    public static Object getFactory(String factoryName)
         throws FacesException {

        validateFactoryName(factoryName);

        // Identify the web application class loader
        ClassLoader classLoader = getClassLoader();

        synchronized (applicationMaps) {
            Map<String, Object> appMap = getApplicationMap();
            // assert(null != appMap);

            Object factory;
            Object factoryOrList = appMap.get(factoryName);

            // If this factory has been retrieved before
            if (factoryOrList != null && (!(factoryOrList instanceof List))) {
                // just return it.
                return (factoryOrList);
            }
            // else, this factory has not been retrieved before; let's
            // find it.

            factory = getImplementationInstance(classLoader, factoryName,
                 (List) factoryOrList);

            if (null == factory) {
                ResourceBundle rb = LOGGER.getResourceBundle();
                String message = rb.getString("severe.no_factory");
                message = MessageFormat.format(message, factoryName);
                throw new IllegalStateException(message);
            }

            // Record and return the new instance
            appMap.put(factoryName, factory);
            return (factory);
        }

    }

    /**
     * <p>This method will store the argument
     * <code>factoryName/implName</code> mapping in such a way that
     * {@link #getFactory} will find this mapping when searching for a
     * match.</p>
     * <p/>
     * <p>This method has no effect if <code>getFactory()</code> has
     * already been called looking for a factory for this
     * <code>factoryName</code>.</p>
     * <p/>
     * <p>This method can be used by implementations to store a factory
     * mapping while parsing the Faces configuration file</p>
     *
     * @throws IllegalArgumentException if <code>factoryName</code> does not
     *                                  identify a standard JavaServer Faces factory name
     * @throws NullPointerException     if <code>factoryname</code>
     *                                  is null
     */

    public static void setFactory(String factoryName,
                                  String implName) {
        validateFactoryName(factoryName);
        Object previouslySetFactories;
        Map<String, Object> appMap;
        synchronized (applicationMaps) {
            appMap = getApplicationMap();
            // assert(null != appMap);

            // Has set or get been called on this factoryName before?
            if (null != (previouslySetFactories = appMap.get(factoryName))) {
                // Yes.  Has get been called on this factoryName before?
                // If previouslySetFactories is not a List, get has been
                // called.
                if (!(previouslySetFactories instanceof List)) {
                    // take no action.
                    return;
                }
                // get has not been called, previouslySetFactories is a
                // List
            } else {
                // No.  Create a List for this FactoryName.
                //noinspection CollectionWithoutInitialCapacity
                previouslySetFactories = new ArrayList<String>();
                appMap.put(factoryName, previouslySetFactories);
            }
            // Put this at the beginning of the list.
            (TypedCollections.dynamicallyCastList((List) previouslySetFactories, String.class)).add(0, implName);
        }
    }


    /**
     * <p>Release any references to factory instances associated with the
     * class loader for the calling web application.  This method should be
     * called as apart of web application shutdown in a container where the
     * JavaServer Faces API classes are part of the container itself, rather
     * than being included inside the web application.</p>
     *
     * @throws FacesException if the web application class loader
     *                        cannot be identified
     */
    public static void releaseFactories() throws FacesException {

        // Identify the web application class loader
        ClassLoader cl = getClassLoader();

        // Release any and all factory instances corresponding to this
        // class loader
        synchronized (applicationMaps) {
            HashMap map = (HashMap) applicationMaps.get(cl);
            if (map != null) {
                map.clear();
                applicationMaps.remove(cl);
            }
        }

    }

    // -------------------------------------------------------- Private Methods


    /**
     * <p>Identify and return the class loader that is associated with the
     * calling web application.</p>
     *
     * @throws FacesException if the web application class loader
     *                        cannot be identified
     */
    private static ClassLoader getClassLoader() throws FacesException {

        // J2EE 1.3 (and later) containers are required to make the
        // web application class loader visible through the context
        // class loader of the current thread.
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            throw new FacesException("getContextClassLoader");
        }
        return (cl);

    }


    /**
     * <p>Load and return an instance of the specified implementation
     * class using the following algorithm.</p>
     * <p/>
     * <ol>
     * <p/>
     * <li><p>If the argument <code>implementations</code> list has
     * more than one element, or exactly one element, interpret the
     * last element in the list to be the fully qualified class name of
     * a class implementing <code>factoryName</code>.  Instantiate that
     * class and save it for return.  If the
     * <code>implementations</code> list has only one element, skip
     * this step.</p></li>
     * <p/>
     * <li><p>Look for a resource called
     * <code>/META-INF/services/&lt;factoryName&gt;</code>.  If found,
     * interpret it as a properties file, and read out the first entry.
     * Interpret the first entry as a fully qualify class name of a
     * class that implements <code>factoryName</code>.  If we have an
     * instantiated factory from the previous step <em>and</em> the
     * implementing class has a one arg constructor of the type for
     * <code>factoryName</code>, instantiate it, passing the
     * instantiated factory from the previous step.  If there is no one
     * arg constructor, just instantiate the zero arg constructor.  Save
     * the newly instantiated factory for return, replacing the
     * instantiated factory from the previous step.</p></li>
     * <p/>
     * <li><p>Treat each remaining element in the
     * <code>implementations</code> list as a fully qualified class name
     * of a class implementing <code>factoryName</code>.  If the current
     * element has a one arg constructor of the type for
     * <code>factoryName</code>, instantiate it, passing the
     * instantiated factory from the previous or step iteration.  If
     * there is no one arg constructor, just instantiate the zero arg
     * constructor, replacing the instantiated factory from the previous
     * step or iteration.</p></li>
     * <p/>
     * <li><p>Return the saved factory</p></li>
     * <p/>
     * </ol>
     *
     * @param classLoader     Class loader for the web application that will
     *                        be loading the implementation class
     * @param implementations A List of implementations for a given
     *                        factory class.
     * @throws FacesException if the specified implementation class
     *                        cannot be loaded
     * @throws FacesException if an instance of the specified implementation
     *                        class cannot be instantiated
     */
    private static Object getImplementationInstance(ClassLoader classLoader,
                                                    String factoryName,
                                                    List implementations)
         throws FacesException {
        Object result = null;
        String curImplClass;
        int len;
        int i = 0;

        // step 1.
        if (null != implementations &&
             (1 < (len = implementations.size()) || 1 == len)) {
            curImplClass = (String) implementations.remove(len - 1);
            // since this is the hard coded implementation default,
            // there is no preceding implementation, so don't bother
            // with a non-zero-arg ctor.
            result = getImplGivenPreviousImpl(classLoader, factoryName,
                 curImplClass, null);
        }

        // step 2.
        if (null != (curImplClass = getImplNameFromServices(classLoader,
             factoryName))) {
            result = getImplGivenPreviousImpl(classLoader, factoryName,
                 curImplClass, result);
        }

        // step 3.
        if (null != implementations) {
            for (len = (implementations.size() - 1); 0 <= len; len--) {
                curImplClass = (String) implementations.remove(len);
                result = getImplGivenPreviousImpl(classLoader, factoryName,
                     curImplClass, result);
            }
        }

        return result;
    }

    /**
     * <p>Perform the logic to get the implementation class for the
     * second step of {@link getImplementationInstance}.</p>
     */

    private static String getImplNameFromServices(ClassLoader classLoader,
                                                  String factoryName) {

        // Check for a services definition
        String result = null;
        BufferedReader reader = null;
        String resourceName = "META-INF/services/" + factoryName;
        Properties props = null;
        InputStream stream = null;
        try {
            stream = classLoader.getResourceAsStream(resourceName);
            if (stream != null) {
                // Deal with systems whose native encoding is possibly
                // different from the way that the services entry was created
                try {
                    reader =
                         new BufferedReader(new InputStreamReader(stream,
                              "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    reader = new BufferedReader(new InputStreamReader(stream));
                }
                result = reader.readLine();
                reader.close();
                reader = null;
                stream = null;
            }
        } catch (IOException e) {
        } catch (SecurityException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable t) {
                    ;
                }
                //noinspection UnusedAssignment
                reader = null;
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable t) {
                    ;
                }
                //noinspection UnusedAssignment
                stream = null;
            }
        }
        return result;
    }

    /**
     * <p>Implement the decorator pattern for the factory
     * implementation.</p>
     * <p/>
     * <p>If <code>previousImpl</code> is non-<code>null</code> and the
     * class named by the argument <code>implName</code> has a one arg
     * contstructor of type <code>factoryName</code>, instantiate it,
     * passing previousImpl to the constructor.</p>
     * <p/>
     * <p>Otherwise, we just instantiate and return
     * <code>implName</code>.</p>
     *
     * @param classLoader  the ClassLoader from which to load the class
     * @param factoryName  the fully qualified class name of the factory.
     * @param implName     the fully qualified class name of a class that
     *                     implements the factory.
     * @param previousImpl if non-<code>null</code>, the factory
     *                     instance to be passed to the constructor of the new factory.
     */

    private static Object getImplGivenPreviousImpl(ClassLoader classLoader,
                                                   String factoryName,
                                                   String implName,
                                                   Object previousImpl) {
        Class clazz;
        Class factoryClass = null;
        Class[] getCtorArg;
        Object[] newInstanceArgs = new Object[1];
        Constructor ctor;
        Object result = null;

        // if we have a previousImpl and the appropriate one arg ctor.
        if ((null != previousImpl) &&
             (null != (factoryClass = getFactoryClass(classLoader,
                  factoryName)))) {
            try {
                clazz = Class.forName(implName, false, classLoader);
                getCtorArg = new Class[1];
                getCtorArg[0] = factoryClass;
                ctor = clazz.getConstructor(getCtorArg);
                newInstanceArgs[0] = previousImpl;
                result = ctor.newInstance(newInstanceArgs);
            }
            catch (NoSuchMethodException nsme) {
                // fall through to "zero-arg-ctor" case
                factoryClass = null;
            }
            catch (Exception e) {
                throw new FacesException(implName, e);
            }
        }
        if (null == previousImpl || null == factoryClass) {
            // we have either no previousImpl or no appropriate one arg
            // ctor.
            try {
                clazz = Class.forName(implName, false, classLoader);
                // since this is the hard coded implementation default,
                // there is no preceding implementation, so don't bother
                // with a non-zero-arg ctor.
                result = clazz.newInstance();
            } catch (Exception e) {
                throw new FacesException(implName, e);
            }
        }
        return result;
    }

    /**
     * @return the <code>java.lang.Class</code> for the argument
     *         factory.
     */

    private static Class getFactoryClass(ClassLoader classLoader,
                                         String factoryClassName) {
        if (null == factoryClasses) {
            factoryClasses = new HashMap<String, Class>(factoryNames.length);
            factoryClasses.put(APPLICATION_FACTORY,
                 javax.faces.application.ApplicationFactory.class);
            factoryClasses.put(FACES_CONTEXT_FACTORY,
                 javax.faces.context.FacesContextFactory.class);
            factoryClasses.put(LIFECYCLE_FACTORY,
                 javax.faces.lifecycle.LifecycleFactory.class);
            factoryClasses.put(RENDER_KIT_FACTORY,
                 javax.faces.render.RenderKitFactory.class);
        }
        return factoryClasses.get(factoryClassName);
    }

    /**
     * <p>This method must only be called from within a synchronized
     * block for the {@link #applicationMaps} ivar.</p>
     */

    private static Map<String, Object> getApplicationMap() {
        // Identify the web application class loader
        ClassLoader classLoader = getClassLoader();
        Map<String, Object> result ;

        // Return any previously instantiated factory instance (of the
        // specified name) for this web application
        result = applicationMaps.get(classLoader);
        if (result == null) {
            //noinspection CollectionWithoutInitialCapacity
            result = new HashMap<String, Object>();
            applicationMaps.put(classLoader, result);
        }
        return result;
    }

    private static void validateFactoryName(String factoryName) {
        // Validate the requested factory name
        if (factoryName == null) {
            throw new NullPointerException();
        }
        boolean found = false;
        for (int i = 0; i < factoryNames.length; i++) {
            if (factoryName.equals(factoryNames[i])) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException(factoryName);
        }
    }


}
