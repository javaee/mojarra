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
    
     public TestFacesResourceBundleELResolver()
    {
        super("TestFacesResourceBundleELResolver");
    }

    public TestFacesResourceBundleELResolver(String name)
    {
        super(name);
    }
    
    private FacesResourceBundleELResolver resolver = null;
    
    public void setUp() {
        super.setUp();
        UIViewRoot root = new UIViewRoot();
        root.setLocale(Locale.ENGLISH);
        getFacesContext().setViewRoot(root);
        resolver = new FacesResourceBundleELResolver();
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
    
    public void testGetTypePositive() throws Exception {
        getFacesContext().getELContext().setPropertyResolved(false);
        Class type = resolver.getType(getFacesContext().getELContext(), null, "testResourceBundle");
        assertTrue(getFacesContext().getELContext().isPropertyResolved());
        assertEquals(ResourceBundle.class, type);
        
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
 
    public void testIsReadOnlyPositive() throws Exception {
        getFacesContext().getELContext().setPropertyResolved(false);
        boolean result = resolver.isReadOnly(getFacesContext().getELContext(),
                null, "testResourceBundle");
        assertTrue(result);
        assertTrue(getFacesContext().getELContext().isPropertyResolved());
        
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

}
