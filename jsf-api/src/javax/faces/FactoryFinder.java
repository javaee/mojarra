/*
 * $Id: FactoryFinder.java,v 1.12 2002/09/20 00:24:03 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;


/**
 * <p><strong>FactoryFinder</strong> implements the standard discovery
 * algorithm for all factory objects specified in the JavaServer Faces APIs.
 * For a given factory class name, a corresponding implementation class is
 * searched for based on the following algorithm:</p>
 * <ul>
 * <li>If there is a system property whose name matches the name of the
 *     desired factory class, its value is assumed to be the name of the
 *     factory implementation class to use.</li>
 * <li>If a <code>faces.properties</code> file is visible as a resource
 *     to the web application class loader for the calling application,
 *     and this properties file contains property whose name matches the
 *     name of the desired factory class, its value is assumed to be the
 *     name of the factory implementation class to use.</li>
 * <li>If a <code>META-INF/services/{factory-class-name}</code> resource
 *     is visible to the web application class loader for the calling
 *     application (typically as a result of being present in the manifest
 *     of a JAR file), its first line is read and assumed to be the
 *     name of the factory implementation class to use.</li>
 * </ul>
 *
 * <p>Once the name of the factory implementation class is located, the
 * web application class loader for the calling application is requested
 * to load this class, and a corresponding instance of the class will be
 * created.  A side effect of this rule is that each web application will
 * receive its own instance of each factory class, whether the JavaServer
 * Faces implementation is included within the web application or is made
 * visible through the container's facilities for shared libraries.</p>
 *
 * <p>Factory implementation classes must include a zero-arguments
 * constructor in order to be successfully instantiated.</p>
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
     * The property name for the <code>ConverterFactory</code> class name.
     */
    public final static String CONVERTER_FACTORY =
        "javax.faces.convert.ConverterFactory";


    /**
     * The property name for the <code>FacesContextFactory</code> class name.
     */
    public final static String FACES_CONTEXT_FACTORY =
        "javax.faces.context.FacesContextFactory";


    /**
     * The property name for the <code>LifecycleFactory</code> class name.
     */
    public final static String LIFECYCLE_FACTORY =
        "javax.faces.lifecycle.LifecycleFactory";


    /**
     * The property name for the <code>MessageResourcesFactory</code>
     * clas name.
     */
    public final static String MESSAGE_RESOURCES_FACTORY =
        "javax.faces.context.MessageResourcesFactory";


    /**
     * The property name for the <code>RenderKitFactory</code> class name.
     */
    public final static String RENDER_KIT_FACTORY =
        "javax.faces.render.RenderKitFactory";


    /**
     * The property name for the <code>TreeFactory</code> class name.
     */
    public final static String TREE_FACTORY =
        "javax.faces.tree.TreeFactory";


    // ------------------------------------------------------- Static Variables


    /**
     * <p>The standard name of the JavaServer Faces configuration properties
     * file we will search for.</p>
     */
    private static final String CONFIG_PROPERTIES = "faces.properties";


    /**
     * <p>Mapping between web application class loaders (the keys) and a
     * <code>Map</code> that contains keys for each factory name, and values
     * for the corresponding implementation instances.</p>
     */
    private static HashMap applicationMaps = new HashMap();


    /**
     * <p>The set of JavaServer Faces factory classes for which the factory
     * discovery mechanism is supported.</p>
     */
    private static String factoryNames[] = {
        CONVERTER_FACTORY,
        FACES_CONTEXT_FACTORY,
        LIFECYCLE_FACTORY,
        MESSAGE_RESOURCES_FACTORY,
        RENDER_KIT_FACTORY,
        TREE_FACTORY,
    };


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Create (if necessary) and return a per-web-application instance of
     * the appropriate implementation class for the specified JavaServer Faces
     * factory class, based on the discovery algorithm described in the
     * class description.</p>
     *
     * @param factoryName Fully qualified name of the JavaServer Faces factory
     *  for which an implementation instance is requested
     *
     * @exception FacesException if the web application class loader
     *  cannot be identified
     * @exception FacesException if an instance of the configured factory
     *  implementation class cannot be loaded
     * @exception FacesException if an instance of the configured factory
     *  implementation class cannot be instantiated
     * @exception IllegalArgumentException if <code>factoryName</code> does not
     *  identify a standard JavaServer Faces factory name
     * @exception IllegalStateException if there is no configured factory
     *  implementation class for the specified factory name
     * @exception NullPointerException if <code>factoryname</code>
     *  is null
     */
    public static Object getFactory(String factoryName)
        throws FacesException {

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

        // Identify the web application class loader
        ClassLoader classLoader = getClassLoader();

        synchronized (applicationMaps) {

            // Return any previously instantiated factory instance (of the
            // specified name) for this web application
            HashMap map = (HashMap) applicationMaps.get(classLoader);
            if (map == null) {
                map = new HashMap();
                applicationMaps.put(classLoader, map);
            }
            Object factory = map.get(factoryName);
            if (factory != null) {
                return (factory);
            }

            // Instantiate an instance of the configured implementation class
            String implementationName =
                getImplementationName(classLoader, factoryName);
            factory =
                getImplementationInstance(classLoader, implementationName);

            // Record and return the new instance
            map.put(factoryName, factory);
            return (factory);

        }

    }


    /**
     * <p>Release any references to factory instances associated with the
     * class loader for the calling web application.  This method should be
     * called as apart of web application shutdown in a container where the
     * JavaServer Faces API classes are part of the container itself, rather
     * than being included inside the web application.</p>
     *
     * @exception FacesException if the web application class loader
     *  cannot be identified
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
     * @exception FacesException if the web application class loader
     *  cannot be identified
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
     * <p>Load and return an instance of the specified implementation class.
     * </p>
     *
     * @param classLoader Class loader for the web application that will
     *  be loading the implementation class
     * @param implementationName Fully qualified name of the desired
     *  factory implementation class
     *
     * @exception FacesException if the specified implementation class
     *  cannot be loaded
     * @exception FacesException if an instance of the specified implementation
     *  class cannot be instantiated
     */
    private static Object getImplementationInstance(ClassLoader classLoader,
                                                    String implementationName)
        throws FacesException {

        try {
            Class clazz = classLoader.loadClass(implementationName);
            return (clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException(implementationName, e);
        }

    }


    /**
     * <p>Discover and return the name of the factory implementation class
     * for the specified factory name.</p>
     *
     * @param classLoader Class loader for the web application that will
     *  receive the instantiated factory
     * @param factoryName Fully qualified name of the factory whose
     *  implementation class name is required
     *
     * @exception IllegalStateException if there is no configured
     *  implementation class name for the specified factory name
     */
    private static String getImplementationName(ClassLoader classLoader,
                                                String factoryName) {

        // Check for a system property of the specified name
        String value = null;
        try {
            value = System.getProperty(factoryName);
        } catch (SecurityException e) {
            ;
        }
        if (value != null) {
            return (value);
        }

        // Check for a faces configuration properties file
        Properties props = null;
        InputStream stream = null;
        try {
            stream = classLoader.getResourceAsStream(CONFIG_PROPERTIES);
            if (stream != null) {
                props = new Properties();
                props.load(stream);
                stream.close();
                stream = null;
            }
        } catch (IOException e) {
        } catch (SecurityException e) {
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable t) {
                    ;
                }
            }
            stream = null;
        }
        if (props != null) {
            value = props.getProperty(factoryName);
            if (value != null) {
                return (value);
            }
        }

        // Check for a services definition
        BufferedReader reader = null;
        String resourceName = "META-INF/services/" + factoryName;
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
                value = reader.readLine();
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
                reader = null;
                stream = null;
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable t) {
                    ;
                }
                stream = null;
            }
        }

        // Return the selected value (if any)
        if (value != null) {
            return (value);
        } else {
            throw new FacesException(factoryName);
        }

    }


}
