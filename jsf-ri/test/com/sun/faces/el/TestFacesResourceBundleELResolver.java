/*
 * $Id: TestFacesResourceBundleELResolver.java,v 1.4 2006/03/29 22:39:42 rlubke Exp $
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

// TestFacesResourceBundleELResolver.java

package com.sun.faces.el;

import com.sun.faces.cactus.ServletFacesTestCase;
import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.faces.component.UIViewRoot;


public class TestFacesResourceBundleELResolver extends ServletFacesTestCase {


    private FacesResourceBundleELResolver resolver = null;


    // ------------------------------------------------------------ Constructors


     public TestFacesResourceBundleELResolver()
    {

        super("TestFacesResourceBundleELResolver");

    }


    public TestFacesResourceBundleELResolver(String name)
    {

        super(name);

    }


    // ---------------------------------------------------------- Public Methods


    public void setUp() {

        super.setUp();
        UIViewRoot root = new UIViewRoot();
        root.setLocale(Locale.ENGLISH);
        getFacesContext().setViewRoot(root);
        resolver = new FacesResourceBundleELResolver();

    }


    public void testGetFeatureDescriptorsPositive() throws Exception {

        Iterator iter = resolver.getFeatureDescriptors(getFacesContext().getELContext(), null);
        
        assertNotNull(iter);
        FeatureDescriptor cur = null;
        String 
            displayName1 = "Testo Pesto",
            displayName2 = "Second ResourceBundle";
        String 
            var1 = "testResourceBundle",
            var2 = "testResourceBundle2";
        boolean test = false;
        while (iter.hasNext()) {
            test = false;
            cur = (FeatureDescriptor) iter.next();
            assertEquals(Boolean.TRUE, cur.getValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME));
            assertEquals(ResourceBundle.class, cur.getValue(ELResolver.TYPE));
            assertTrue(!cur.isExpert());
            assertTrue(!cur.isHidden());
            assertTrue(cur.isPreferred());
            assertNotNull(cur.getDisplayName());
            test = cur.getDisplayName().equals(displayName1) ||
                   cur.getDisplayName().equals(displayName2);
            assertTrue(test);
            test = cur.getName().equals(var1) || cur.getName().equals(var2);
            assertTrue(test);
        }

    }


    public void testGetTypeNegative() throws Exception {

        Class result = resolver.getType(getFacesContext().getELContext(), 
                "non-null", "testResourceBundle");
        assertTrue(null == result);
        boolean exceptionThrown = false;
        try {
            resolver.getType(getFacesContext().getELContext(), null, null);
        }
        catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

    }


    public void testGetTypePositive() throws Exception {

        getFacesContext().getELContext().setPropertyResolved(false);
        Class type = resolver.getType(getFacesContext().getELContext(), null, "testResourceBundle");
        assertTrue(getFacesContext().getELContext().isPropertyResolved());
        assertEquals(ResourceBundle.class, type);
        
    }


    public void testGetValueNegative() throws Exception {

        ResourceBundle bundle1 = (ResourceBundle)
            resolver.getValue(getFacesContext().getELContext(), "hello",
                "testResourceBundle");
        assertNull(bundle1);
        boolean exceptionThrown = false;
        try {
            bundle1 = (ResourceBundle)
                resolver.getValue(getFacesContext().getELContext(), null, null);
        }
        catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        bundle1 = (ResourceBundle)
            resolver.getValue(getFacesContext().getELContext(), null,
                "nonExistent");
        
        assertNull(bundle1);

    }


    public void testGetValuePositive() throws Exception {

        ResourceBundle bundle1 = (ResourceBundle)
            resolver.getValue(getFacesContext().getELContext(), null,
                "testResourceBundle");
        assertNotNull(bundle1);
        String value = bundle1.getString("value2");
        assertNotNull(value);
        assertEquals("Bob", value);

    }


    public void testIsReadOnlyNegative() throws Exception {

        boolean result = resolver.isReadOnly(getFacesContext().getELContext(), 
                "non-null Base", null);
        assertTrue(!result);
        
        getFacesContext().getELContext().setPropertyResolved(false);
        boolean exceptionThrown = false;
        try {
            resolver.isReadOnly(getFacesContext().getELContext(), null, null);
        }
        catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        assertFalse(getFacesContext().getELContext().isPropertyResolved());

    }


    public void testIsReadOnlyPositive() throws Exception {

        getFacesContext().getELContext().setPropertyResolved(false);
        boolean result = resolver.isReadOnly(getFacesContext().getELContext(),
                null, "testResourceBundle");
        assertTrue(result);
        assertTrue(getFacesContext().getELContext().isPropertyResolved());
        
    }


    public void testSetValeNegative() throws Exception {

        boolean exceptionThrown = false;
        try {
            resolver.setValue(getFacesContext().getELContext(), null, null, null);
        }
        catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            resolver.setValue(getFacesContext().getELContext(), null, "testResourceBundle",
                    "Value");
        }
        catch (PropertyNotWritableException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
            
    }

}