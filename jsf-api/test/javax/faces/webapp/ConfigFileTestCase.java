/*
 * $Id: ConfigFileTestCase.java,v 1.1 2003/04/07 21:45:35 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.digester.Digester;

import org.xml.sax.InputSource;

/**
 * <p>Unit tests for Configuration File processing.</p>
 */

public class ConfigFileTestCase extends TestCase {


    // The public identifier of our DTD
    protected String CONFIG_DTD_PUBLIC_ID =
        "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.0//EN";


    // ----------------------------------------------------- Instance Variables


    /**
     * The Digester instance we will use to parse configuration files.
     */
    protected Digester digester = null;



    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ConfigFileTestCase(String name) {

        super(name);

    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {

        digester = createDigester();
        configureRules(digester);

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(ConfigFileTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        digester = null;

    }


    // ------------------------------------------------ Individual Test Methods


    // Test parsing an empty configuration file
    public void testEmpty() throws Exception {

        ConfigBase base =
            parseConfig(relativeURL("test/javax/faces/webapp/config-file-0.xml"));
        assertNotNull(base);

    }


    // Test parsing a full configuration file
    public void testFull() throws Exception {

        // Retrieve entire configuration metadata instance
        ConfigBase base =
            parseConfig(relativeURL("test/javax/faces/webapp/config-file-1.xml"));
        assertNotNull(base);

        // <application>
        assertEquals("com.mycompany.MyActionListener",
                     base.getActionListener());
        assertEquals("com.mycompany.MyNavigationHandler",
                     base.getNavigationHandler());
        assertEquals("com.mycompany.MyPropertyResolver",
                     base.getPropertyResolver());
        assertEquals("com.mycompany.MyVariableResolver",
                     base.getVariableResolver());

        // <converter>
        Map converters = base.getConverters();
        assertNotNull(converters);
        ConfigConverter cc1 = (ConfigConverter) converters.get("First");
        assertNotNull(cc1);
        assertEquals("First Converter Description",
                     cc1.getDescription());
        assertEquals("First Converter Display Name",
                     cc1.getDisplayName());
        assertEquals("firstConverter.gif",
                     cc1.getLargeIcon());
        assertEquals("firstConverter.jpg",
                     cc1.getSmallIcon());
        assertEquals("First",
                     cc1.getConverterId());
        assertEquals("com.mycompany.MyFirstConverter",
                     cc1.getConverterClass());
        ConfigConverter cc2 = (ConfigConverter) converters.get("Second");
        assertNotNull(cc2);
        assertEquals("Second Converter Description",
                     cc2.getDescription());
        assertEquals("Second Converter Display Name",
                     cc2.getDisplayName());
        assertEquals("secondConverter.gif",
                     cc2.getLargeIcon());
        assertEquals("secondConverter.jpg",
                     cc2.getSmallIcon());
        assertEquals("Second",
                     cc2.getConverterId());
        assertEquals("com.mycompany.MySecondConverter",
                     cc2.getConverterClass());

    }


    // Test a pristine Digester instance
    public void testPristine() {
    }


    // ------------------------------------------------------ Protected Methods


    // Create a Digester instance with no rules yet
    protected Digester createDigester() throws Exception {

        Digester digester = new Digester();
        digester.register(CONFIG_DTD_PUBLIC_ID,
                          relativeURL("doc/faces-config.dtd").toString());
        return (digester);

    }


    // Configure the matching rules for the specified Digester instance
    // Rules assume that a ConfigBase bean is pushed on the stack first
    protected void configureRules(Digester digester) {

        configureRulesApplication(digester);
        configureRulesConverter(digester);

    }


    // Configure the rules for an <application> element
    protected void configureRulesApplication(Digester digester) {

        digester.addCallMethod("faces-config/application/action-listener",
                               "setActionListener", 0);
        digester.addCallMethod("faces-config/application/navigation-handler",
                               "setNavigationHandler", 0);
        digester.addCallMethod("faces-config/application/property-resolver",
                               "setPropertyResolver", 0);
        digester.addCallMethod("faces-config/application/variable-resolver",
                               "setVariableResolver", 0);

    }


    // Configure the rules for a <converter> element
    protected void configureRulesConverter(Digester digester) {

        String prefix = "faces-config/converter";

        digester.addObjectCreate(prefix,
                                 "javax.faces.webapp.ConfigConverter");
        digester.addSetNext(prefix,
                            "addConverter",
                            "javax.faces.webapp.ConfigConverter");
        configureRulesFeature(digester, prefix);
        digester.addCallMethod(prefix + "/converter-id",
                               "setConverterId", 0);
        digester.addCallMethod(prefix + "/converter-class",
                               "setConverterClass", 0);

    }


    // Configure the generic feature rules for the specified prefix
    protected void configureRulesFeature(Digester digester, String prefix) {

        digester.addCallMethod(prefix + "/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "/display-name",
                               "setDisplayName", 0);
        digester.addCallMethod(prefix + "/icon/large-icon",
                               "setLargeIcon", 0);
        digester.addCallMethod(prefix + "/icon/small-icon",
                               "setSmallIcon", 0);

    }


    // Parse the configuration file at the specified URL
    protected ConfigBase parseConfig(URL config) throws Exception {

        digester.clear();
        digester.push(new ConfigBase());
        InputSource iso = new InputSource(config.toExternalForm());
        InputStream ist = config.openStream();
        iso.setByteStream(ist);
        ConfigBase base = (ConfigBase) digester.parse(iso);
        ist.close();
        return (base);

    }


    // Return the URL of the specified path, relative to our base directory
    protected URL relativeURL(String relativePath) throws Exception {

        File file = new File(System.getProperty("base.dir"), relativePath);
        return (file.toURL());

    }


}
