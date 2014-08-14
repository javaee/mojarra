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

package com.sun.faces.config;

import com.sun.faces.util.Util;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.w3c.dom.ls.LSResourceResolver;
import org.w3c.dom.ls.LSInput;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.io.Reader;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.ServletContext;
import com.sun.faces.util.FacesLogger;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;


/**
 * <p>Create and configure DocumentBuilderFactory instances.</p>
 */
public class DbfFactory {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    private static final String AS_INSTALL_ROOT = "com.sun.aas.installRoot";

    private static final String AS_SCHEMA_DIR =
          System.getProperty(AS_INSTALL_ROOT)
             + File.separatorChar
             + "lib"
             + File.separatorChar
             + "schemas"
             + File.separatorChar;

    private static final String AS_DTD_DIR =
          System.getProperty(AS_INSTALL_ROOT)
             + File.separatorChar
             + "lib"
             + File.separatorChar
             + "dtds"
             + File.separatorChar;
    
    /**
     * Location of the facelet-taglib 2.0 Schema
     */
    private static final String FACELET_TAGLIB_2_0_XSD =
        "/com/sun/faces/web-facelettaglibrary_2_0.xsd";

    /**
     * Location of the facelet-taglib 2.2 Schema
     */
    private static final String FACELET_TAGLIB_2_2_XSD =
        "/com/sun/faces/web-facelettaglibrary_2_2.xsd";

    /**
     * Location of the Faces 2.0 Schema
     */
    private static final String FACES_2_0_XSD =
        "/com/sun/faces/web-facesconfig_2_0.xsd";

    /**
     * Location of the Faces 2.1 Schema
     */
    private static final String FACES_2_1_XSD =
        "/com/sun/faces/web-facesconfig_2_1.xsd";

     /**
     * Location of the Faces 2.2 Schema
     */
    private static final String FACES_2_2_XSD =
        "/com/sun/faces/web-facesconfig_2_2.xsd";

    /**
     * Location of the Faces 1.2 Schema
     */
    private static final String FACES_1_2_XSD =
         "/com/sun/faces/web-facesconfig_1_2.xsd";


    /**
     * Location of the Faces private 1.1 Schema
     */
    private static final String FACES_1_1_XSD =
         "/com/sun/faces/web-facesconfig_1_1.xsd";


    /**
     * Location of the facelet taglib xsd within GlassFish.
     */
    private static final String FACELET_TAGLIB_2_0_XSD_FILE =
          AS_SCHEMA_DIR + "web-facelettaglibrary_2_0.xsd";

    /**
     * Location of the facelet taglib xsd within GlassFish.
     */
    private static final String FACELET_TAGLIB_2_2_XSD_FILE =
          AS_SCHEMA_DIR + "web-facelettaglibrary_2_2.xsd";

    /**
     * Location of the faces 2.0 xsd within GlassFish.
     */
    private static final String FACES_2_0_XSD_FILE =
          AS_SCHEMA_DIR + "web-facesconfig_2_0.xsd";

    /**
     * Location of the faces 2.1 xsd within GlassFish.
     */
    private static final String FACES_2_1_XSD_FILE =
          AS_SCHEMA_DIR + "web-facesconfig_2_1.xsd";

    /**
     * Location of the faces 2.1 xsd within GlassFish.
     */
    private static final String FACES_2_2_XSD_FILE =
          AS_SCHEMA_DIR + "web-facesconfig_2_2.xsd";

    /**
     * Location of the faces 1.2 xsd within GlassFish.
     */
    private static final String FACES_1_2_XSD_FILE =
          AS_SCHEMA_DIR + "web-facesconfig_1_2.xsd";

    /**
     * EntityResolver
     */
    public static final EntityResolver FACES_ENTITY_RESOLVER =
         new FacesEntityResolver();

    /**
     * The constant that points to the schema map (in the servlet context).
     */
    private static final String SCHEMA_MAP = "com.sun.faces.config.schemaMap";
    
    public enum FacesSchema {

        FACES_20,
        FACES_21,
        FACES_22,
        FACES_12,
        FACES_11,
        FACELET_TAGLIB_20,
        FACELET_TAGLIB_22;        
    }

    /**
     * ErrorHandler
     */
    public static final FacesErrorHandler FACES_ERROR_HANDLER =
         new FacesErrorHandler();


    // ---------------------------------------------------------- Public Methods


    public static DocumentBuilderFactory getFactory() {

        DocumentBuilderFactory factory = Util.createDocumentBuilderFactory();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        return factory;

    }   


    // ----------------------------------------------------------- Inner Classes

   private static class FacesEntityResolver extends DefaultHandler implements LSResourceResolver {

        /**
         * <p>Contains associations between grammar name and the physical
         * resource.</p>
         */
        private static final String[][] DTD_SCHEMA_INFO = {
            {
                "web-facesconfig_1_0.dtd",
                "/com/sun/faces/web-facesconfig_1_0.dtd",
                AS_DTD_DIR + "web-facesconfig_1_0.dtd"
            },
            {
                "web-facesconfig_1_1.dtd",
                "/com/sun/faces/web-facesconfig_1_1.dtd",
                AS_DTD_DIR + "web-facesconfig_1_1.dtd"
            },
            {
                "web-facesconfig_2_0.xsd",
                 FACES_2_0_XSD,
                 FACES_2_0_XSD_FILE
            },
            {
                "web-facesconfig_2_1.xsd",
                 FACES_2_1_XSD,
                 FACES_2_1_XSD_FILE
            },
            {
                "web-facesconfig_2_2.xsd",
                 FACES_2_2_XSD,
                 FACES_2_2_XSD_FILE
            },
            {
                "facelet-taglib_1_0.dtd",
                "/com/sun/faces/facelet-taglib_1_0.dtd",
                null
            },
            {
                "web-facelettaglibrary_2_0.xsd",
                 FACELET_TAGLIB_2_0_XSD,
                 FACELET_TAGLIB_2_0_XSD_FILE
            },
            {
                "web-facesconfig_1_2.xsd",
                FACES_1_2_XSD,
                FACES_1_2_XSD_FILE
            },
            {
                "web-facesconfig_1_1.xsd",
                FACES_1_1_XSD,
                null
            },
            {
                "javaee_5.xsd",
                "/com/sun/faces/javaee_5.xsd",
                AS_SCHEMA_DIR + "javaee_5.xsd"
            },
            {
                "javaee_6.xsd",
                "/com/sun/faces/javaee_6.xsd",
                AS_SCHEMA_DIR + "javaee_6.xsd"
            },
            {
                "javaee_7.xsd",
                "/com/sun/faces/javaee_7.xsd",
                AS_SCHEMA_DIR + "javaee_7.xsd"
            },
            {
                "javaee_web_services_client_1_2.xsd",
                "/com/sun/faces/javaee_web_services_client_1_2.xsd",
                AS_SCHEMA_DIR + "javaee_web_services_client_1_2.xsd"
            },
            {
                "javaee_web_services_client_1_3.xsd",
                "/com/sun/faces/javaee_web_services_client_1_3.xsd",
                AS_SCHEMA_DIR + "javaee_web_services_client_1_3.xsd"
            },
            {
                "javaee_web_services_client_1_4.xsd",
                "/com/sun/faces/javaee_web_services_client_1_4.xsd",
                AS_SCHEMA_DIR + "javaee_web_services_client_1_4.xsd"
            },
            {
                "xml.xsd",
                "/com/sun/faces/xml.xsd",
                AS_SCHEMA_DIR + "xml.xsd"
            },
            {
                "datatypes.dtd",
                "/com/sun/faces/datatypes.dtd",
                AS_SCHEMA_DIR + "datatypes.dtd"
            },
            {
                "XMLSchema.dtd",
                "/com/sun/faces/XMLSchema.dtd",
                AS_SCHEMA_DIR + "XMLSchema.dtd"
            }
        };

        /**
         * <p>Contains mapping between grammar name and the local URL to the
         * physical resource.</p>
         */
        private HashMap<String,String> entities =
             new HashMap<String, String>(12, 1.0f);

        // -------------------------------------------------------- Constructors


        public FacesEntityResolver() {

            // Add mappings between last segment of system ID and
            // the expected local physical resource.  If the resource
            // cannot be found, then rely on default entity resolution
            // and hope a firewall isn't in the way or a proxy has
            // been configured
            for (String[] aDTD_SCHEMA_INFO : DTD_SCHEMA_INFO) {
                URL url = this.getClass().getResource(aDTD_SCHEMA_INFO[1]);
                if (url == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   "jsf.config.cannot_resolve_entities",
                                   new Object[]{
                                        aDTD_SCHEMA_INFO[1],
                                        aDTD_SCHEMA_INFO[0]
                                   });
                    }
                    // the resource isn't available on the classpath, so
                    // assume that we're running within a GF environment
                    String path = aDTD_SCHEMA_INFO[2];
                    if (path != null) {
                        File f = new File(path);
                        if (f.exists()) {
                            try {
                                url = f.toURI().toURL();
                            } catch (MalformedURLException mue) {
                                if (LOGGER.isLoggable(Level.SEVERE)) {
                                    LOGGER.log(Level.SEVERE,
                                               mue.toString(),
                                               mue);
                                }
                            }
                            if (url == null) {
                                if (LOGGER.isLoggable(Level.FINE)) {
                                    LOGGER.log(Level.FINE,
                                               "jsf.config.cannot_resolve_entities",
                                               new Object[]{
                                                     aDTD_SCHEMA_INFO[1],
                                                     aDTD_SCHEMA_INFO[2]
                                               });
                                }
                            } else {
                                entities.put(aDTD_SCHEMA_INFO[0], url.toString());
                            }
                        }

                    }
                } else {
                    entities.put(aDTD_SCHEMA_INFO[0], url.toString());
                }
            }


        } // END JsfEntityResolver


        // ----------------------------------------- Methods from DefaultHandler


        /**
         * <p>Resolves the physical resource using the last segment of
         * the <code>systemId</code>
         * (e.g. http://java.sun.com/dtds/web-facesconfig_1_1.dtd,
         * the last segment would be web-facesconfig_1_1.dtd).  If a mapping
         * cannot be found for the segment, then defer to the
         * <code>DefaultHandler</code> for resolution.</p>
         */
        public InputSource resolveEntity(String publicId, String systemId)
        throws SAXException {

            // publicId is ignored.  Resolution performed using
            // the systemId.

            // If no system ID, defer to superclass
            if (systemId == null) {
                InputSource result;
                try {
                    result = super.resolveEntity(publicId, null);
                }
                catch (Exception e) {
                    throw new SAXException(e);
                }
                return result;
            }

            String grammarName =
                systemId.substring(systemId.lastIndexOf('/') + 1);

            String entityURL = entities.get(grammarName);

            InputSource source;
            if (entityURL == null) {
                // we don't have a registered mapping, so defer to our
                // superclass for resolution
            	if (LOGGER.isLoggable(Level.FINE)) {
            		LOGGER.log(Level.FINE, "Unknown entity, deferring to superclass.");
            	}

                try {
                    source = super.resolveEntity(publicId, systemId);
                }
                catch (Exception e) {
                    throw new SAXException(e);
                }

            } else {

                try {
                    source = new InputSource(new URL(entityURL).openStream());
                } catch (Exception e) {
                	if (LOGGER.isLoggable(Level.WARNING)) {
	                    LOGGER.log(Level.WARNING,
	                               "jsf.config.cannot_create_inputsource",
	                               entityURL);
                	}
                    source = null;
                }
            }

            // Set the System ID of the InputSource with the URL of the local
            // resource - necessary to prevent parsing errors
            if (source != null) {
                source.setSystemId(entityURL);

                if (publicId != null) {
                    source.setPublicId(publicId);
                }
            }

            return source;

        } // END resolveEntity

       public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
           try {
               InputSource source = resolveEntity(publicId, systemId);
               if (source != null) {
                   return new Input(source.getByteStream());
               }               
           } catch (Exception e) {
               throw new ConfigurationException(e);
           }
           return null;
       }


    } // END FacesEntityResolver


    private static class FacesErrorHandler implements ErrorHandler {
        public void warning(SAXParseException exception) throws SAXException {
            // do nothing
        }

        public void error(SAXParseException exception) throws SAXException {
            throw exception;
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            throw exception;
        }
    } // END FacesErrorHandler


    private static final class Input implements LSInput {
        InputStream in;
        public Input(InputStream in) {
           this.in = in;
        }
        public Reader getCharacterStream() {
            return null;
        }

        public void setCharacterStream(Reader characterStream) { }

        public InputStream getByteStream() {
            return in;
        }

        public void setByteStream(InputStream byteStream) { }

        public String getStringData() {
            return null;
        }

        public void setStringData(String stringData) { }

        public String getSystemId() {
            return null;
        }

        public void setSystemId(String systemId) { }

        public String getPublicId() {
            return null;
        }

        public void setPublicId(String publicId) { }

        public String getBaseURI() {
            return null;
        }

        public void setBaseURI(String baseURI) { }

        public String getEncoding() {
            return null;
        }

        public void setEncoding(String encoding) { }

        public boolean getCertifiedText() {
            return false;
        }

        public void setCertifiedText(boolean certifiedText) { }
    }

    /**
     * Get the schema for the given schema id.
     *
     * @param servletContext the backing servlet context.
     * @param schemaId the schema id.
     * @return the schema, or null if not found.
     */
    public static Schema getSchema(ServletContext servletContext, FacesSchema schemaId) {
        Map<FacesSchema, Schema> schemaMap = getSchemaMap(servletContext);
        if (!schemaMap.containsKey(schemaId)) {
            loadSchema(schemaMap, schemaId);
        }
        return schemaMap.get(schemaId);
    }
    
    /**
     * Get the schema map from the servlet context (or create it).
     * 
     * @param servletContext the servlet context.
     * @return the schema map.
     */
    private static Map<FacesSchema, Schema> getSchemaMap(ServletContext servletContext) {
        Map<FacesSchema, Schema> schemaMap = (Map<FacesSchema, Schema>)
                servletContext.getAttribute(SCHEMA_MAP);
        
        if (schemaMap == null) {
            synchronized(servletContext) {
                schemaMap = Collections.synchronizedMap(
                    new EnumMap<FacesSchema, Schema>(FacesSchema.class));
                servletContext.setAttribute(SCHEMA_MAP, schemaMap);
            }
        }
        
        return schemaMap;
    }
    
    /**
     * Remove the schema map from the servlet context.
     * 
     * @param servletContext the servlet context.
     */
    public static void removeSchemaMap(ServletContext servletContext) {
        servletContext.removeAttribute(SCHEMA_MAP);
    }
    
    /**
     * Load the schema for the given schema id.
     * 
     * @param schemaMap the schema map.
     * @param schemaId the schema id.
     */
    private static void loadSchema(Map<FacesSchema, Schema> schemaMap, FacesSchema schemaId) {
        URL url;
        URLConnection conn;
        InputStream in;
        SchemaFactory factory;
        File f;
        Schema schema;

        try {
            switch (schemaId) {
                case FACES_12:
                    url = DbfFactory.class.getResource(FACES_1_2_XSD);
                    if (url == null) {
                        // try to load from the file
                        f = new File(FACES_1_2_XSD_FILE);
                        if (!f.exists()) {
                            throw new IllegalStateException("Unable to find web-facesconfig_1_2.xsd");
                        }
                        url = f.toURI().toURL();
                    }
                    conn = url.openConnection();
                    conn.setUseCaches(false);
                    in = conn.getInputStream();
                    factory = Util.createSchemaFactory(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    factory.setResourceResolver((LSResourceResolver) DbfFactory.FACES_ENTITY_RESOLVER);
                    schema = factory.newSchema(new StreamSource(in));
                    schemaMap.put(schemaId, schema);
                    break;
                case FACES_11:
                    url = DbfFactory.class.getResource(FACES_1_1_XSD);
                    conn = url.openConnection();
                    conn.setUseCaches(false);
                    in = conn.getInputStream();
                    factory = Util.createSchemaFactory(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    factory.setResourceResolver((LSResourceResolver) DbfFactory.FACES_ENTITY_RESOLVER);
                    schema = factory.newSchema(new StreamSource(in));
                    schemaMap.put(schemaId, schema);
                    break;
                case FACES_21:
                    url = DbfFactory.class.getResource(FACES_2_1_XSD);
                    if (url == null) {
                        // try to load from the file
                        f = new File(FACES_2_1_XSD_FILE);
                        if (!f.exists()) {
                            throw new IllegalStateException("Unable to find web-facesconfig_2_1.xsd");
                        }
                        url = f.toURI().toURL();
                    }
                    conn = url.openConnection();
                    conn.setUseCaches(false);
                    in = conn.getInputStream();
                    factory = Util.createSchemaFactory(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    factory.setResourceResolver((LSResourceResolver) DbfFactory.FACES_ENTITY_RESOLVER);
                    schema = factory.newSchema(new StreamSource(in));
                    schemaMap.put(schemaId, schema);
                    break;
                case FACES_22:
                    url = DbfFactory.class.getResource(FACES_2_2_XSD);
                    if (url == null) {
                        // try to load from the file
                        f = new File(FACES_2_2_XSD_FILE);
                        if (!f.exists()) {
                            throw new IllegalStateException("Unable to find web-facesconfig_2_2.xsd");
                        }
                        url = f.toURI().toURL();
                    }
                    conn = url.openConnection();
                    conn.setUseCaches(false);
                    in = conn.getInputStream();
                    factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    factory.setResourceResolver((LSResourceResolver) DbfFactory.FACES_ENTITY_RESOLVER);
                    schema = factory.newSchema(new StreamSource(in));
                    schemaMap.put(schemaId, schema);
                    break;
                case FACES_20:
                    url = DbfFactory.class.getResource(FACES_2_0_XSD);
                    if (url == null) {
                        // try to load from the file
                        f = new File(FACES_2_0_XSD_FILE);
                        if (!f.exists()) {
                            throw new IllegalStateException("Unable to find web-facesconfig_2_0.xsd");
                        }
                        url = f.toURI().toURL();
                    }
                    conn = url.openConnection();
                    conn.setUseCaches(false);
                    in = conn.getInputStream();
                    factory = Util.createSchemaFactory(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    factory.setResourceResolver((LSResourceResolver) DbfFactory.FACES_ENTITY_RESOLVER);
                    schema = factory.newSchema(new StreamSource(in));
                    schemaMap.put(schemaId, schema);
                    break;
                case FACELET_TAGLIB_20:
                    url = DbfFactory.class.getResource(FACELET_TAGLIB_2_0_XSD);
                    if (url == null) {
                        // try to load from the file
                        f = new File(FACELET_TAGLIB_2_0_XSD_FILE);
                        if (!f.exists()) {
                            throw new IllegalStateException("Unable to find web-facelettaglibrary_2_0.xsd");
                        }
                        url = f.toURI().toURL();
                    }
                    conn = url.openConnection();
                    conn.setUseCaches(false);
                    in = conn.getInputStream();
                    factory = Util.createSchemaFactory(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    factory.setResourceResolver((LSResourceResolver) DbfFactory.FACES_ENTITY_RESOLVER);
                    schema = factory.newSchema(new StreamSource(in));
                    schemaMap.put(schemaId, schema);
                    break;
                case FACELET_TAGLIB_22:
                    url = DbfFactory.class.getResource(FACELET_TAGLIB_2_2_XSD);
                    if (url == null) {
                        // try to load from the file
                        f = new File(FACELET_TAGLIB_2_2_XSD_FILE);
                        if (!f.exists()) {
                            throw new IllegalStateException("Unable to find web-facelettaglibrary_2_2.xsd");
                        }
                        url = f.toURI().toURL();
                    }
                    conn = url.openConnection();
                    conn.setUseCaches(false);
                    in = conn.getInputStream();
                    factory = Util.createSchemaFactory(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    factory.setResourceResolver((LSResourceResolver) DbfFactory.FACES_ENTITY_RESOLVER);
                    schema = factory.newSchema(new StreamSource(in));
                    schemaMap.put(schemaId, schema);
                    break;
                default:
                    throw new ConfigurationException("Unrecognized Faces Version: " + schemaId.toString());
            }
        }
        catch (Exception e) {
            throw new ConfigurationException(e);
        }        
    }
}
