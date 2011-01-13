/*
 * $Id: ConfigFileTestCase.java,v 1.78 2007/04/27 22:02:04 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.application.ApplicationImpl;


import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;

import java.util.*;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.el.ValueBinding;


/**
 * <p>Unit tests for Configuration File processing.</p>
 */

public class ConfigFileTestCase extends ServletFacesTestCase {


    // ----------------------------------------------------- Instance Variables


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ConfigFileTestCase(String name) {

        super(name);

    }

    // ------------------------------------------------ Individual Test Methods


    public void testMapAndListPositive() throws Exception {
        loadFromInitParam("/WEB-INF/config-lists-and-maps.xml");
        ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder.getFactory(
            FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl) aFactory.getApplication();

        ValueBinding valueBinding =
            application.createValueBinding("#{simpleList}");
        assertNotNull(valueBinding);

        List list = (List) valueBinding.getValue(getFacesContext());
        assertNotNull(list);

        assertEquals("simpleList size not as expected", 4, list.size());
        assertEquals("simpleList.get(0) not as expected",
                     new Integer(10), list.get(0));
        assertEquals("simpleList.get(1) not as expected",
                     new Integer(20), list.get(1));
        assertEquals("simpleList.get(2) not as expected",
                     new Integer(60), list.get(2));
        assertNull("simpleList.get(3) not as expected", list.get(3));

        valueBinding = application.createValueBinding("#{objectList}");
        assertNotNull(valueBinding);

        list = (List) valueBinding.getValue(getFacesContext());
        assertNotNull(list);

        assertEquals("simpleList size not as expected", 4, list.size());
        assertTrue("simpleList.get(0) not as expected",
                   list.get(0) instanceof SimpleBean);
        assertTrue("simpleList.get(1) not as expected",
                   list.get(1) instanceof SimpleBean);
        assertTrue("simpleList.get(2) not as expected",
                   list.get(2) instanceof SimpleBean);
        assertNull("simpleList.get(3) not as expected", list.get(3));


        valueBinding = application.createValueBinding("#{floatMap}");
        assertNotNull(valueBinding);

        Map
            nestedMap = null,
            map = (Map) valueBinding.getValue(getFacesContext());
        assertNotNull(map);

        Iterator keys = map.keySet().iterator();
        Float
            key1 = new Float(3.1415),
            key2 = new Float(3.14),
            key3 = new Float(6.02),
            key4 = new Float(0.00001);
        Object
            curKey = null,
            value = null;

        while (keys.hasNext()) {
            assertTrue((curKey = keys.next()) instanceof Float);
            if (null != (value = map.get(curKey))) {
                assertTrue(value instanceof SimpleBean);
            }
        }

        assertTrue("map.get(key1) not a SimpleBean",
                   map.get(key1) instanceof SimpleBean);
        assertTrue("map.get(key2) not a SimpleBean",
                   map.get(key2) instanceof SimpleBean);
        assertTrue("map.get(key3) not a SimpleBean",
                   map.get(key3) instanceof SimpleBean);
        assertNull("map.get(key4) not null", map.get(key4));

        valueBinding = application.createValueBinding("#{crazyMap}");
        assertNotNull(valueBinding);

        map = (Map) valueBinding.getValue(getFacesContext());
        assertNotNull(map);

        keys = map.keySet().iterator();
        while (keys.hasNext()) {
            assertTrue((curKey = keys.next()) instanceof String);
            if (null != (value = map.get(curKey))) {
                assertTrue(value instanceof Map);
                nestedMap = (Map) value;
                assertTrue("nestedMap.get(key1) not a SimpleBean",
                           nestedMap.get(key1) instanceof SimpleBean);
                assertTrue("nestedMap.get(key2) not a SimpleBean",
                           nestedMap.get(key2) instanceof SimpleBean);
                assertTrue("nestedMap.get(key3) not a SimpleBean",
                           nestedMap.get(key3) instanceof SimpleBean);
                assertNull("nestedMap.get(key4) not null",
                           nestedMap.get(key4));
            }
        }
        assertTrue("map.get(one) not a Map",
                   map.get("one") instanceof Map);
        assertTrue("map.get(two) not a Map",
                   map.get("two") instanceof Map);
        assertNull("map.get(three) not null", map.get("three"));

    }

    public void testMap1701() throws Exception {
        loadFromInitParam("/WEB-INF/config-lists-and-maps.xml");
        ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder.getFactory(
            FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl) aFactory.getApplication();
        ExpressionFactory ef = application.getExpressionFactory();
        ValueExpression ve = ef.createValueExpression(getFacesContext().getELContext(),
                "#{headAndFoot}", Map.class);
        Map headAndFoot = (Map) ve.getValue(getFacesContext().getELContext());
        assertNotNull(headAndFoot);
        Map banners = (Map) headAndFoot.get("banners");
        Object result = banners.get("headerUrl");
        assertNotNull(result);
        assertEquals("http://foo.utah.edu", result);
        result = banners.get("urlName");
        assertNotNull(result);
        assertEquals("Request For Change", result);

    }
 
}