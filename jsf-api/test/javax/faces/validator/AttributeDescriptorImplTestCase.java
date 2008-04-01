/*
 * $Id: AttributeDescriptorImplTestCase.java,v 1.1 2002/07/12 00:30:05 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import java.util.Locale;
import javax.faces.component.AttributeDescriptor;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Test case for the
 * <strong>javax.faces.validator.AttributeDescriptorImpl</strong>
 * concrete class.</p>
 */

public class AttributeDescriptorImplTestCase extends TestCase {


    // ----------------------------------------------------- Instance Variables


    /**
     * The instance to be tested for each test.
     */
    protected AttributeDescriptor descriptor = null;


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public AttributeDescriptorImplTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        descriptor =
            new AttributeDescriptorImpl("maxLength",
                                        String.class,
                                        "maxLength");

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(AttributeDescriptorImplTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        descriptor = null;

    }


    // ------------------------------------------------ Individual Test Methods


    /**
     * Scalar properties of AttributeDescriptor instance.
     */
    public void testScalarProperties() {

        assertEquals("attribute name", "maxLength",
                     descriptor.getName());
        assertEquals("attribute type", String.class,
                     descriptor.getType());

    }


    /**
     * Localizable properties of AttributeDescriptor instance.
     */
    public void testLocalizableProperties() {

        // Default system locale -- FIXME will this always work?
        assertEquals("Default display name", "Maximum Length",
                     descriptor.getDisplayName());
        assertEquals("Default description",
                     "The maximum number of characters allowed for this value",
                     descriptor.getDescription());

        // English locale -- should always work
        assertEquals("English display name", "Maximum Length",
                     descriptor.getDisplayName(Locale.ENGLISH));
        assertEquals("English description",
                     "The maximum number of characters allowed for this value",
                     descriptor.getDescription(Locale.ENGLISH));

        // French locale -- FIXME update when resources are available
        assertEquals("French display name", "Maximum Length",
                     descriptor.getDisplayName(Locale.FRENCH));
        assertEquals("French description",
                     "The maximum number of characters allowed for this value",
                     descriptor.getDescription(Locale.FRENCH));

        // German locale -- FIXME update when resources are available
        assertEquals("German display name", "Maximum Length",
                     descriptor.getDisplayName(Locale.GERMAN));
        assertEquals("German description",
                     "The maximum number of characters allowed for this value",
                     descriptor.getDescription(Locale.GERMAN));

    }


}
