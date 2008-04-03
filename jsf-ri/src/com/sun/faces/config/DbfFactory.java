package com.sun.faces.config;

import com.sun.faces.util.FacesLogger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Create and configure DocumentBuilderFactory instances.</p>
 */
public class DbfFactory {

    private static final Logger LOGGER =
         Logger.getLogger(FacesLogger.CONFIG.getLoggerName());

    private static final String JAXP_SCHEMA_LANGUAGE =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA =
        "http://www.w3.org/2001/XMLSchema";
    private static final String JAXP_SCHEMA_SOURCE =
        "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final String FACES_1_2_SCHEMA =
         "/com/sun/faces/web-facesconfig_1_2.xsd";


    /**
     * EntityResolver
     */
    public static final EntityResolver FACES_ENTITY_RESOLVER =
         new FacesEntityResolver();

    public static final FacesErrorHandler FACES_ERROR_HANDLER =
         new FacesErrorHandler();



    // ---------------------------------------------------------- Public Methods


    public static DocumentBuilderFactory getFactory(boolean validating) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(validating);
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        factory.setAttribute(JAXP_SCHEMA_SOURCE,
                             FacesEntityResolver.class.getResource(
                                  FACES_1_2_SCHEMA).toExternalForm());
        return factory;

    }


    // ----------------------------------------------------------- Inner Classes


    private static class FacesEntityResolver extends DefaultHandler {

        /**
         * <p>Contains associations between grammar name and the physical
         * resource.</p>
         */
        private static final String[][] DTD_SCHEMA_INFO = {
            {
                "web-facesconfig_1_0.dtd",
                "/com/sun/faces/web-facesconfig_1_0.dtd"
            },
            {
                "web-facesconfig_1_1.dtd",
                "/com/sun/faces/web-facesconfig_1_1.dtd"
            },
            {
                "web-facesconfig_1_2.xsd",
                FACES_1_2_SCHEMA
            },
            {
                "javaee_5.xsd",
                "/com/sun/faces/javaee_5.xsd"
            },
            {
                "javaee_web_services_client_1_2.xsd",
                "/com/sun/faces/javaee_web_services_client_1_2.xsd"
            },
            {
                "xml.xsd",
                "/com/sun/faces/xml.xsd"
            }
        };

        /**
         * <p>Contains mapping between grammar name and the local URL to the
         * physical resource.</p>
         */
        private HashMap<String,String> entities =
             new HashMap<String, String>(6, 1.0f);

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
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING,
                                   "jsf.config.cannot_resolve_entities",
                                   new Object[]{
                                        aDTD_SCHEMA_INFO[1],
                                        aDTD_SCHEMA_INFO[0]
                                   });
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

                LOGGER.log(Level.FINE, "Unknown entity, deferring to superclass.");

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
                    LOGGER.log(Level.WARNING,
                               "jsf.config.cannot_create_inputsource",
                               entityURL);
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


        // ------------------------------------------------------ Public Methods

        public Map<String,String> getKnownEntities() {

            return Collections.unmodifiableMap(entities);

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
}
