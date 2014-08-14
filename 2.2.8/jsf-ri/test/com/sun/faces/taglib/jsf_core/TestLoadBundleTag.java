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

// TestLoadBundleTag.java

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;

import javax.faces.component.UIViewRoot;
import javax.faces.application.Application;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import java.util.Map;
import java.util.Locale;

public class TestLoadBundleTag extends ServletFacesTestCase {

//
// Protected Constants
//

// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestLoadBundleTag() {
        super("TestLoadBundleTag.java");
    }


    public TestLoadBundleTag(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//

    public void testLoadBundle() throws Exception {
        getFacesContext().setViewRoot(Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null));
        getFacesContext().getViewRoot().setLocale(Locale.US);
        LoadBundleTag tag = new LoadBundleTag();
        ExpressionFactory factory =
            getFacesContext().getApplication().getExpressionFactory();
        ValueExpression expr =
            factory.createValueExpression(getFacesContext().getELContext(),
                "com.sun.faces.TestMessages",String.class);
                                         
        tag.setBasename(expr);
        tag.setVar("messages");
        tag.doStartTag();
        assertEquals("Didn't get expected value",
                     ((Map) getFacesContext().getExternalContext()
                            .getRequestMap()
                            .get("messages")).get("buckaroo"),
                     "banzai");
        assertEquals("Didn't get expected value",
                     ((Map) getFacesContext().getExternalContext()
                            .getRequestMap()
                            .get("messages")).get("john"),
                     "bigboote");
        assertEquals("???notpresent???",
                     ((Map) getFacesContext().getExternalContext()
                            .getRequestMap()
                            .get("messages")).get("notpresent"));

    }


    //test out full Map contract implementation of LoadBundleTag
    public void testLoadBundleMap() throws Exception {
        boolean gotException = false;
        Object key = "buckaroo";
        Object value = "banzai";
        ExpressionFactory factory =
            getFacesContext().getApplication().getExpressionFactory();
        ValueExpression expr =
            factory.createValueExpression(getFacesContext().getELContext(),
                "com.sun.faces.TestMessages",String.class);
                                          
        getFacesContext().setViewRoot(Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null));
        getFacesContext().getViewRoot().setLocale(Locale.US);
        LoadBundleTag tag = new LoadBundleTag();
        tag.setBasename(expr);
        tag.setVar("messages");
        tag.doStartTag();
        Map testMap = (Map) getFacesContext().getExternalContext()
            .getRequestMap()
            .get("messages");

        LoadBundleTag tag2 = new LoadBundleTag();
        tag2.setBasename(expr);
        tag2.setVar("messages2");
        tag2.doStartTag();
        Map testMap2 = (Map) getFacesContext().getExternalContext()
            .getRequestMap()
            .get("messages2");

        try {
            testMap.clear();
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.clear() should not be supported for immutable Map",
                   gotException);
        gotException = false;

        assertTrue("key not in Map", testMap.containsKey(key));
        assertTrue("value not in Map", testMap.containsValue(value));
        assertTrue("entrySet not correct for Map",
                   testMap.entrySet().equals(testMap2.entrySet()));
        assertTrue("Same maps are not equal", testMap.equals(testMap2));
        assertEquals("value not in Map", testMap.get(key), value);
        //two equal sets should have same hashcode
        assertTrue("HashCode not valid",
                   testMap.hashCode() == testMap2.hashCode());
        assertFalse("Map should not be empty", testMap.isEmpty());
        assertTrue("keySet not valid", testMap.keySet().contains(key));
        try {
            testMap.put(key, value);
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.put() should not be supported for immutable Map",
                   gotException);
        gotException = false;

        try {
            testMap.putAll(new java.util.HashMap());
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.putAll() should not be supported for immutable Map",
                   gotException);
        gotException = false;

        try {
            testMap.remove(key);
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.remove() should not be supported for immutable Map",
                   gotException);
        gotException = false;

        assertTrue("Map size incorrect", testMap.size() == 4);
        assertTrue("values from Map incorrect",
                   testMap.values().contains(value));

    }

} // end of class TestLoadBundleTag
