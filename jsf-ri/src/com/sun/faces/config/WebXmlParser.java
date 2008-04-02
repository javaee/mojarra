/*
 * $Id: WebXmlParser.java,v 1.6 2004/02/06 18:54:21 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.faces.FacesException;
import javax.servlet.ServletContext;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class WebXmlParser extends Object {

    //
    // Private Constants
    //
    
    // Log instance
    private static final Log log = LogFactory.getLog(WebXmlParser.class);

    //
    // Instance Variables
    //  
    Digester digester;
    ServletContext context;
    String servletName;
    List servletMappings;
    
    //
    // Constructors and Initializers    
    //

    /**
     * <p>Creates a new <code>WebXmlParser<code> instance.</p>
     *
     * @param context the <code>ServletContext</code> of the current
     *                application
     */
    public WebXmlParser(ServletContext context) {
        this.context = context;
        servletMappings = new ArrayList();
    }
  
    
    //
    // General Methods
    //
    
    
    /**
     * <p>Obtains the <code>url-pattern</code>s for the
     * all {@link javax.faces.webapp.FacesServlet}s referenced in this
     * application's deployment descriptor <code>(web.xml)</code>.</p>
     *
     * @return The <code>url-pattern</code> of the
     *         all {@link javax.faces.webapp.FacesServlet} instances
     */
    public List getFacesServletMappings() {
        digester = new Digester();
        digester.setUseContextClassLoader(true);
        
        // Set a custom EntityResolver so that the DTD or Schema
        // is effectively ignored
        digester.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(String string, String string1)
                throws SAXException, IOException {
                return new InputSource(new CharArrayReader(new char[0]));
            }

        });

        configureRules();
        URL url = null;
        try {
            url = context.getResource("/WEB-INF/web.xml");
        } catch (MalformedURLException mue) {
            ;
        }

        // No web.xml found. Don't try and parse.
        if (url == null) {
            if (log.isDebugEnabled()) {
                log.debug("Missing web.xml file. Cannot parse.");
            }
            return servletMappings;
        }

        InputSource in = new InputSource(escapeSpaces(url.toExternalForm()));
        InputStream inStream = null;
        try {
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            inStream = conn.getInputStream();
            in.setByteStream(inStream);
            digester.parse(in);
        } catch (Exception e) {
            String message = "Unexpected error parsing /WEB-INF/web.xml: " + e;
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }
            throw new FacesException(message, e);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ioe) {
                    ; // ignore
                }
            }
            digester = null;
        }

        return servletMappings;
    }


    /**
     * <p>Configure the <code>Rule</code>s for parsing this application's
     * <code>web.xml</code> file.</p>
     */
    private void configureRules() {

        // Rules for <servlet> elements
        digester.addObjectCreate("web-app/servlet",
                                 ServletBean.class);
        digester.addCallMethod("web-app/servlet/servlet-name",
                               "setServletName", 0);
        digester.addCallMethod("web-app/servlet/servlet-class",
                               "setServletClass", 0);
        digester.addRule("web-app/servlet", new ServletRule());
        
        // Rules for <servlet-mapping> elements
        digester.addObjectCreate("web-app/servlet-mapping",
                                 ServletMappingBean.class);
        digester.addCallMethod("web-app/servlet-mapping/servlet-name",
                               "setServletName", 0);
        digester.addCallMethod("web-app/servlet-mapping/url-pattern",
                               "setUrlPattern", 0);
        digester.addRule("web-app/servlet-mapping", new ServletMappingRule());
    }


    /**
     * <p>Replace occurances of a ' ' with another '%20'.</p>
     *
     * @param src input value
     *
     * @return the replacement result.
     */
    public static String escapeSpaces(String src) {
        if (src.indexOf(" ") == -1) {
            return src;
        }
        StringBuffer result = new StringBuffer(src.length());
        StringTokenizer toker = new StringTokenizer(src, " ", true);
        String curToken;
        while (toker.hasMoreTokens()) {
            // if the current token is a delimiter, replace it with "to"
            if ((curToken = toker.nextToken()).equals(" ")) {
                result.append("%20");
            } else {
                // it's not a delimiter, just output it.
                result.append(curToken);
            }
        }

        return result.toString();
    }
    
    //
    // Private Classes
    //
    
    // ---------------------------------------------------------- Digester Rules
    
    /**
     * <p>This <code>Rule<code> handles the population of the
     * </code>servlets</code> </code>Map</code>.</p>
     */
    private class ServletRule extends Rule {

        public void end(String namespace, String name) throws Exception {
            ServletBean servletBean = (ServletBean) this.digester.peek();
            String servletClass = servletBean.getServletClass();
            if (servletClass != null && servletClass.equals(
                "javax.faces.webapp.FacesServlet")) {
                if (servletName == null) {
                    servletName = servletBean.getServletName();
                } else {
                    throw new FacesException("More than one FacesServlet was" +
                                             " defined in this application.");
                }
            }
        }

    }

    /**
     * <p>This <code>Rule<code> handles the population of the
     * </code>servletMappings</code> </code>Map</code>.</p>
     */
    private class ServletMappingRule extends Rule {

        public void end(String namespace, String name) throws Exception {
            ServletMappingBean mappingBean = (ServletMappingBean)
                this.digester.peek();
            
            // massage the url-patterns that we put in the map
            // to reduce the number of String manipulation calls
            // later.
            if (servletName != null &&
                servletName.equals(mappingBean.getServletName())) {
                String urlPattern = mappingBean.getUrlPattern();

                if (urlPattern.charAt(0) == '*') {
                    urlPattern = urlPattern.substring(1);
                } else if (urlPattern.endsWith("/*")) {
                    urlPattern =
                        urlPattern.substring(0, urlPattern.length() - 2);
                }
                servletMappings.add(urlPattern);
            }
        }

    }

    //  TESTCASE: com.sun.faces.config.ConfigFileTestCase 

} // end of class WebXmlParser
