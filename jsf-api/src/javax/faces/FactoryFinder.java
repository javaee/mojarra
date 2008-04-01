/*
 * $Id: FactoryFinder.java,v 1.5 2002/04/11 22:51:21 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;

import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Stolen from JAXP. <P>

 * Not part of the public API. <P>

 * This class is a general purpose way of associating an interface or
 * abstract class with an implementation. <P>

 * PENDING(edburns): move this into ri, and make it package private.
 *
 */
public class FactoryFinder {

    /**
     * Figure out which ClassLoader to use.  For JDK 1.2 and later use
     * the context ClassLoader.
     */           
    private static ClassLoader findClassLoader()
        throws FactoryConfigurationError
    {
        ClassLoader classLoader;
        Method m = null;

        try {
            m = Thread.class.getMethod("getContextClassLoader", null);
        } catch (NoSuchMethodException e) {
            // Assume that we are running JDK 1.1, use the current ClassLoader
	    /* PENDING(edburns): 
            if (debug) {
                debugPrintln("assuming JDK 1.1");
            }
	    */
            classLoader = FactoryFinder.class.getClassLoader();
        }

        try {
            classLoader = (ClassLoader) m.invoke(Thread.currentThread(), null);
        } catch (IllegalAccessException e) {
            // assert(false)
            throw new FactoryConfigurationError("Unexpected IllegalAccessException",
                                         e);
        } catch (InvocationTargetException e) {
            // assert(e.getTargetException() instanceof SecurityException)
            throw new FactoryConfigurationError("Unexpected InvocationTargetException",
                                         e);
        }

        return classLoader;
    }

    /**
     * Create an instance of a class using the specified ClassLoader
     */
    private static Object newInstance(String className,
                                      ClassLoader classLoader)
        throws FactoryConfigurationError
    {
        try {
            Class spiClass;
            if (classLoader == null) {
		ClassLoader loader =
		    Thread.currentThread().getContextClassLoader();
		if (loader == null) {
		    spiClass =  Class.forName(className);
		}
		else {
		    spiClass = loader.loadClass(className);
		}
            } else {
                spiClass = classLoader.loadClass(className);
            }
            return spiClass.newInstance();
        } catch (ClassNotFoundException x) {
            throw new FactoryConfigurationError(
                "Provider " + className + " not found", x);
        } catch (Exception x) {
            throw new FactoryConfigurationError(
                "Provider " + className + " could not be instantiated: " + x,
                x);
        }
    }

    /**

     * Main entry point. Finds the implementation Class object in the
     * following order. <P>

     	<OL>

	  <LI><P> Look in the System properties for a property named
	  factoryId.  If found, it is used as the classname of the
	  factory to find. </P></LI>

	  <LI><P> Look in $java.home/lib/faces.properties.  Look for a
	  property named factoryId.  If found, it is used as the
	  classname of the factory to find.  </P></LI>

	  <LI><P>Load the system resource from
	  <CODE>&qt;META-INF/services/&qt; + factoryId</CODE>.  This
	  file is treated as a file containing one line, the contents of
	  which is used as the classname of the factory to find.
	  </P></LI>

	  <LI><P> Use the fallbackClassName as the classname of the
	  factory to find.  </P></LI>


	</OL>


     * @return Class object of factory, never null
     *
     * @param factoryId             Name of the factory to find, same as
     *                              a property name
     * @param fallbackClassName     Implementation class name, if nothing else
     *                              is found.  Use null to mean no fallback.
     *
     * @exception FactoryConfigurationError
     *
     // PENDING(edburns): make this package private again.
     * Package private so this code can be shared.
     */
    public static Object find(String factoryId, String fallbackClassName)
        throws FactoryConfigurationError
    {
        ClassLoader classLoader = findClassLoader();

        // Use the system property first
        try {
            String systemProp =
                System.getProperty( factoryId );
            if( systemProp!=null) {
	    /* PENDING(edburns): 
                debugPrintln("found system property " + systemProp);
	    */
                return newInstance(systemProp, classLoader);
            }
        } catch (SecurityException se) {
        }

        // try to read from $java.home/lib/faces.properties
        try {
            String javah=System.getProperty( "java.home" );
            String configFile = javah + File.separator +
                "lib" + File.separator + "faces.properties";
            File f=new File( configFile );
            if( f.exists()) {
                Properties props=new Properties();
		FileInputStream fos = new FileInputStream(f);
                props.load(fos);
                String factoryClassName = props.getProperty(factoryId);
		fos.close();
	    /* PENDING(edburns): 
                debugPrintln("found java.home property " + factoryClassName);

	    */
		if (null != factoryClassName) {
		    return newInstance(factoryClassName, classLoader);
		}
            }
        } catch(Exception ex ) {
	    /* PENDING(edburns): 
            if( debug ) ex.printStackTrace();
	    */
        }

        String serviceId = "META-INF/services/" + factoryId;
        // try to find services in CLASSPATH
        try {
            InputStream is=null;
            if (classLoader == null) {
                is=ClassLoader.getSystemResourceAsStream( serviceId );
            } else {
                is=classLoader.getResourceAsStream( serviceId );
            }
        
            if( is!=null ) {
	    /* PENDING(edburns): 
                debugPrintln("found " + serviceId);

	    */
                BufferedReader rd =
                    new BufferedReader(new InputStreamReader(is, "UTF-8"));
        
                String factoryClassName = rd.readLine();
                rd.close();

                if (factoryClassName != null &&
                    ! "".equals(factoryClassName)) {
	    /* PENDING(edburns): 
                    debugPrintln("loaded from services: " + factoryClassName);

	    */
                    return newInstance(factoryClassName, classLoader);
                }
            }
        } catch( Exception ex ) {
	    /* PENDING(edburns): 
            if( debug ) ex.printStackTrace();

	    */
        }

        if (fallbackClassName == null) {
            throw new FactoryConfigurationError(
                "Provider for " + factoryId + " cannot be found", null);
        }

	    /* PENDING(edburns): 
        debugPrintln("loaded from fallback value: " + fallbackClassName);

	    */
        return newInstance(fallbackClassName, classLoader);
    }

    // The testcase for this class is com.sun.faces.TestAbstractFactory

}
