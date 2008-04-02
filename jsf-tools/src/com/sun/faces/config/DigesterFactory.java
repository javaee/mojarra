/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * $Id: DigesterFactory.java,v 1.1 2004/10/29 00:56:39 rlubke Exp $
 */


package com.sun.faces.config;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;

/**
 * <p>A simple factory to hide <code>Digester</code> configuration
 * details.</p>
 */
public class DigesterFactory {

    private static final Log LOG = LogFactory.getLog(DigesterFactory.class);

    /**
     * <p><code>Xerces</code> specific feature to enable both
     * DTD and Schema validation.</p>
     */
    private static final String APACHE_DYNAMIC_VALIDATION =
        "http://apache.org/xml/features/validation/dynamic";

    /**
     * <p><code>Xerces</code> specific feature to enable both
     * DTD and Schema validation.</p>
     */
    private static final String APACHE_SCHEMA_VALIDATION =
        "http://apache.org/xml/features/validation/schema";

    /**
     * <p>Custom <code>EntityResolver</code>.</p>
     */
    private static final JsfEntityResolver RESOLVER = new JsfEntityResolver();

    /**
     * <p>Custom <code>ErrorHandler</code>.</p>
     */
    private static final MessageLogger MESSAGE_LOGGER = new MessageLogger();

    /**
     * <p>Indicates whether or not document validation is
     * requested or not.</p>
     */
    private boolean validating;



    // ------------------------------------------------------------ Constructors


    /**
     * <p>Creates a new DigesterFactory instance.</p>
     * @param isValidating - <code>true</code> if the <code>Digester</code>
     *  instance that is ultimately returned should be configured (if possible)
     *  for document validation.  If validation is not desired, pass
     *  <code>false</code>.
     */
    private DigesterFactory(boolean isValidating) {

        validating = isValidating;

    } // END DigesterFactory


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Returns a new <code>DigesterFactory</code> instance that will create
     * a non-validating <code>Digester</code> instance.</p>
     * @return
     */
    public static DigesterFactory newInstance() {

        return DigesterFactory.newInstance(false);

    } // END newInstance


    /**
     * <p>Creates a new <code>DigesterFactory</code> instance that will
     * create a <code>Digester</code> instance where validation depends
     * on the value of <code>isValidating</code>.</p>
     * @param isValidating - <code>true</code> if the <code>Digester</code>
     *  instance that is ultimately returned should be configured (if possible)
     *  for document validation.  If validation is not desired, pass
     *  <code>false</code>.
     * @return a new <code>DigesterFactory</code> capable of creating
     *  <code>Digester</code>instances
     */
    public static DigesterFactory newInstance(boolean isValidating) {

        return new DigesterFactory(isValidating);

    } // END newInstance


    /**
     * <p>Creates a new <code>Digester</code> instance configured for use
     * with JSF.</p>
     * @return
     */
    public Digester createDigester() {

        Digester digester = new Digester();
        configureDigester(digester);

        return digester;

    } // END getDigester


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Configures the provided <code>Digester</code> instance appropriate
     * for use with JSF.</p>
     * @param digester - the <code>Digester</code> instance to configure
     */
    private void configureDigester(Digester digester) {

        digester.setNamespaceAware(true);
        digester.setUseContextClassLoader(true);
        digester.setErrorHandler(MESSAGE_LOGGER);
        digester.setEntityResolver(RESOLVER);

        if (validating) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("Attempting to configure Digester to perform" +
                    " document validation.");
            }

            // In order to validate using *both* DTD and Schema, certain
            // Xerces specific features are required.  Try to set these
            // features.  If an exception is thrown trying to set these
            // features, then disable validation.

            XMLReader reader;
            try {
                reader = digester.getXMLReader();
            } catch (SAXException e) {

                if (LOG.isWarnEnabled()) {
                    LOG.warn("Unable to obtain XMLReader from Digester.  " +
                        "Disabling validation");
                }

                digester.setValidating(false);
                return;
            }

            try {
                reader.setFeature(APACHE_DYNAMIC_VALIDATION, true);
                reader.setFeature(APACHE_SCHEMA_VALIDATION, true);
                digester.setValidating(true);
            } catch (SAXNotSupportedException e) {

                if (LOG.isWarnEnabled()) {
                    LOG.warn("Attempt to set supported feature on XMLReader, but" +
                        " the value provided was not accepted.  " +
                        "Validation will be disabledb.");
                }

                digester.setValidating(false);

            } catch (SAXNotRecognizedException e) {

                if (LOG.isWarnEnabled()) {
                    LOG.warn("Attempt to set unsupported feature on XMLReader " +
                        "necessary for validation.  Validation will be" +
                        "disabled.");
                }

                digester.setValidating(false);

            }
        } else {
            digester.setValidating(false);
        }

    } // END configureDigester


    // ----------------------------------------------------------- Inner Classes


    private static class MessageLogger extends DefaultHandler {

        /**
         * <p>Simple <code>MessageFormat</code> for displaying
         * parser messages.</p>
         */
        private final MessageFormat format =
            new MessageFormat("({0}: {1}, {2}): {3}");


        // ----------------------------------------- Methods from DefaultHandler


        public void warning(SAXParseException spe) {

            if (LOG.isWarnEnabled()) {
                LOG.warn(getMessage(spe));
            }

        } // END warning


        public void error(SAXParseException spe) {

            if (LOG.isErrorEnabled()) {
                LOG.error(getMessage(spe), spe);
            }

        } // END error


        public void fatalError(SAXParseException spe) throws SAXParseException {

            if (LOG.isFatalEnabled()) {
                LOG.fatal(getMessage(spe), spe);
            }
            throw spe;

        } // END fatalError


        // ----------------------------------------------------- Private Methods


        private String getMessage(SAXParseException spe) {

            String msg = format.format(new Object[]
            {
                spe.getSystemId(),
                new Integer(spe.getLineNumber()),
                new Integer(spe.getColumnNumber()),
                spe.getMessage()
            });

            return msg;

        } // END getMessage

    } // END ErrorPrinter


    private static class JsfEntityResolver extends DefaultHandler {

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
                "/com/sun/faces/web-facesconfig_1_2.xsd"
            },
            {
                "j2ee_1_4.xsd",
                "/com/sun/faces/j2ee_1_4.xsd"
            },
            {
                "j2ee_web_services_client_1_1.xsd",
                "/com/sun/faces/j2ee_web_services_client_1_1.xsd"
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
        private HashMap entities = new HashMap();


        // -------------------------------------------------------- Constructors


        public JsfEntityResolver() {

            // Add mappings between last segment of system ID and
            // the expected local physical resource.  If the resource
            // cannot be found, then rely on default entity resolution
            // and hope a firewall isn't in the way or a proxy has
            // been configured
            for (int i = 0; i < DTD_SCHEMA_INFO.length; i++) {
                URL url = this.getClass().getResource(DTD_SCHEMA_INFO[i][1]);
                if (url == null) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Unable to locate local resource '" +
                            DTD_SCHEMA_INFO[i][1] + "'.  Standard entity " +
                            "resolution will be used when request are present " +
                            "for '" + DTD_SCHEMA_INFO[i][0] + '\'');
                    }
                } else {
                    entities.put(DTD_SCHEMA_INFO[i][0], url.toString());
                }
            }

        } // END JsfEntityResolver


        // ----------------------------------------- Methods from DefaultHandler


        /**
         * <p>Resolves the physical resource using the last segment of
         * the <code>systemId</code> (e.g. http://java.sun.com/dtds/web-facesconfig_1_1.dtd,
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
                return super.resolveEntity(publicId, systemId);
            }

            String grammarName =
                systemId.substring(systemId.lastIndexOf('/') + 1);

            String entityURL = (String) entities.get(grammarName);

            InputSource source = null;
            if (entityURL == null) {
                // we don't have a registered mapping, so defer to our
                // superclass for resolution

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Unknown entity, deferring to superclass.");
                }
                source = super.resolveEntity(publicId, systemId);

            } else {

                try {
                    source = new InputSource(new URL(entityURL).openStream());
                } catch (MalformedURLException mre) {
                    System.out.println(mre);
                    ; // won't happen as URL string obtained via classloader
                } catch (IOException ioe) {
                    System.out.println(ioe);
                    // do something
                }
            }

            return source;

        } // END resolveEntity

    } // END JsfEntityResolver

}
