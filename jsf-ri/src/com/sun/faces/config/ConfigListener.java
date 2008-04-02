/*
 * $Id: ConfigListener.java,v 1.17 2003/09/12 19:48:43 rkitain Exp $
 */
/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.sun.faces.config;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import javax.faces.FacesException;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.util.Assert;
import org.xml.sax.InputSource;


public class ConfigListener implements ServletContextListener
{
    //
    // Protected Constants
    //

    // Log instance for this class
    protected static Log log = LogFactory.getLog(ConfigListener.class);

    //
    // Class Variables
    //
    private static Set loaders = new HashSet();

    //
    // Instance Variables
    //
    private ConfigParser configParser = null;
    private ClassLoader loader = null;


    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public ConfigListener()
    {
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from ServletContextListener
    //

    public void contextInitialized(ServletContextEvent e) 
    {
	// If this method has already executed for this webapp...
	//
	ClassLoader loader = Thread.currentThread().getContextClassLoader();
	if (loader == null) {
            loader = this.getClass().getClassLoader();
	}
	if (isContextInitialized()) {
	    return;
	}

	ServletContext servletContext = e.getServletContext();
        configParser = new ConfigParser(servletContext);
	String initParamFileList = null;
	InputStream jarInputStream = null;
	
	// Step 0, load our own JSF_RI_CONFIG
	jarInputStream = this.getClass().getClassLoader().
	    getResourceAsStream(RIConstants.JSF_RI_CONFIG);
	Assert.assert_it(null != jarInputStream);
        if (log.isDebugEnabled()) {
            log.debug("Loading JSF_RI_CONFIG configuration resources from " +
                      RIConstants.JSF_RI_CONFIG);
        }
	try {
            configParser.parseConfig(jarInputStream);
	} catch (Exception ee) {
	    Object[] obj = new Object[1];
	    obj[0]=jarInputStream;
	    String msg = Util.getExceptionMessage(
	        Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj);
	    if (log.isWarnEnabled()) {
	        log.warn(msg, ee);
	    }
	    throw new FacesException(msg, ee);
	}
	// It's an error if this doesn't load.
        if (log.isDebugEnabled()) {
            log.debug("Loading JSF_RI_CONFIG completed");
        }

	// Step 1: scan the META-INF directory of all jar files in
	// "/WEB-INF/lib" for "faces-config.xml" files.
	scanJarsForConfigFile(servletContext, configParser);

	// Step 2. If the init parameter exists, load the config from
	// there
	if (null != (initParamFileList = 
		     servletContext.getInitParameter(RIConstants.CONFIG_FILES_INITPARAM))) {
	    StringTokenizer toker = new StringTokenizer(initParamFileList, 
							",");
	    String cur;
	    while (toker.hasMoreTokens()) {
		cur = toker.nextToken();
		if (null != cur && 0 < cur.length()) {
		    cur = cur.trim();

		    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Trying to load application file " + cur);
                        }
			configParser.parseConfig(cur, servletContext);
                        if (log.isDebugEnabled()) {
                            log.debug("Application file " + cur + " loaded");
                        }
		    } catch (Exception ee) {
                        Object[] obj = new Object[1];
                        obj[0] = cur;
			String msg = Util.getExceptionMessage(
			    Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj);
	                if (log.isWarnEnabled()) {
			    log.warn(msg, ee);
			}
			throw new FacesException(msg, ee);
		    }
		}
	    }
	}
	else {
	    // Step 3, load the app's "/WEB-INF/faces-config.xml"
	    try {
                if (log.isDebugEnabled()) {
                    log.debug("Trying to default configuration file");
                }
		configParser.parseConfig("/WEB-INF/faces-config.xml",
						      servletContext);
                if (log.isDebugEnabled()) {
                    log.debug("Default configuration file loaded");
                }

	    }
	    catch (Exception toIgnore) {
		// do nothing, apps are not required to have a faces-config file
	    }
	}	      
        servletContext.setAttribute(RIConstants.CONFIG_ATTR, new Boolean(true)); 
        configParser = null;
    }

    public void contextDestroyed(ServletContextEvent e) {  
        e.getServletContext().removeAttribute(RIConstants.CONFIG_ATTR);
	if (loader != null) {
	    synchronized(loaders) {
	        loaders.remove(loader);
            }
	}
        if (log.isTraceEnabled()) {
	    log.trace("CONTEXT DESTROYED CALLED...");
        }
    }

    /**
     * <p>Algorithm:</p>
     *
     * <p>Scan the ServletContext's resourcePaths space for all jars in
     * "/WEB-INF/lib".</p>
     *
     * <p>For each jar, look for a file called
     * "/META-INF/faces-config.xml".  If that file is present, parse it
     */

    protected void scanJarsForConfigFile(ServletContext servletContext, 
                                         ConfigParser configParser) {

        Set jarSet = servletContext.getResourcePaths("/WEB-INF/lib/");
        if (jarSet == null) {
            return;
        }
        Iterator jarPaths = jarSet.iterator();
        while (jarPaths.hasNext()) {
            String jarPath = (String) jarPaths.next();
            if (!jarPath.endsWith(".jar")) {
                continue;
            }
            scanJarForConfigFile(servletContext, configParser, jarPath);
        }

    }


    /**
     * <p>Scan the specified JAR file for a "/META-INF/faces-config.xml"
     * resource.  If such a resource is found, parse it.
     *
     * @param servletContext The <code>ServletContext</code> for the
     *  current web application
     * @param configParser Parser for processing configuration resources
     * @param jarPath Context-relative resource path to the JAR file to check
     */
    protected void scanJarForConfigFile(ServletContext servletContext,
                                        ConfigParser configParser,
                                        String jarPath) {

        // Calculate a URL for the config resource (if it exists) in this JAR
        URL resourceURL = null;
        try {
            URL jarURL = servletContext.getResource(jarPath);
            // PENDING(craigmcc) - This technique is unlikely to succeed
            // on a servlet container that provides a custom URLStreamHandler
            // in the URL returned by getResource(), because creating a new
            // URL object loses access to it.  We can copy the JAR to a
            // temporary file and open it manually, but that's pretty nasty
            // on app startup performance
            resourceURL =
                new URL("jar:" +
                        Util.replaceOccurrences(jarURL.toExternalForm(),
                                                " ", "%20") +
                        "!/META-INF/faces-config.xml");
        } catch (MalformedURLException e) {
            resourceURL = null;
        }
        if (resourceURL == null) {
            return;
        }

        // Create an InputSource for this resource (if it exists)
        InputStream stream = null;
        InputSource source = null;
        try {
            stream = resourceURL.openStream();
            source = new InputSource(resourceURL.toExternalForm());
            source.setByteStream(stream);
        } catch (IOException e) {
            source = null;
        }
        if (source == null) {
	    try {
	        if (stream != null) {
		    stream.close();
		}
            } catch (Exception e) {
	        if (log.isWarnEnabled()) {
		    log.warn(Util.getExceptionMessage(Util.CANT_CLOSE_INPUT_STREAM_ID), e);
		}
	    }    
            return;
        }

        // The resource exists, so parse it
        try {
            if (log.isDebugEnabled()) {
                log.debug("Parsing application configuration resource " +
                          resourceURL.toExternalForm());
            }
            configParser.parseConfig(source);
            if (log.isDebugEnabled()) {
                log.debug("Finished application configuration resource " +
                          resourceURL.toExternalForm());
            }
        } catch (Exception e) {
            // PENDING(craigmcc) - We need to do a very thorough review
            // of our exception handling strategies throughout the RI
            // this kind of thing should really be handled at a higher level
	    Object[] obj = new Object[1];
	    obj[0]=resourceURL.toExternalForm();
	    String msg = Util.getExceptionMessage(
	        Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj);
	    if (log.isWarnEnabled()) {
	        log.warn(msg, e);
	    }
            throw new FacesException(msg, e);
        }
    }

    // Utility method to determine if the contextInitialized method
    // has executed already for the current webapp;
    //
    private boolean isContextInitialized() {
	//
	// Do this to ensure this method nly executes once per webapp
	//
	if (loader == null) {
	    return false;
	}
	synchronized(loaders) {
	    if (!loaders.contains(loader)) {
	        loaders.add(loader);
		if (log.isTraceEnabled()) {
		    log.trace("Added Classloader");
		}
		return false;
	    } else {
		if (log.isTraceEnabled()) {
		    log.trace("Listener already completed for this webapp");
		}
	        return true;
	    }
	}
    }

    // This is here so tests (same package) can manually clear the Set.
    //
    static void clearLoaderSet() {
        loaders = null;
    }
} 
