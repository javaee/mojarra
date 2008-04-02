/*
 * $Id: TestLRUMap_local.java,v 1.3 2006/03/29 23:05:02 rlubke Exp $
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

package com.sun.faces.util;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import junit.framework.TestCase;

/**
 * Validate LRU functionality of LRUMap
 */
public class TestLRUMap_local extends TestCase {

    // ------------------------------------------------------------ Constructors

    public TestLRUMap_local() {
        super("TestLRUMap_local");
    }

    public TestLRUMap_local(String name) {
        super(name);
    }

    // ------------------------------------------------------------ Test Methods

    /**
     * Ensure that LRUMap works as advertised.
     */
    public void testLRUMap() {

        LRUMap<String,String> map = new LRUMap<String,String>(5);
        map.put("one", "one");
        map.put("two", "two");
        map.put("three", "three");

        // order should be "three", "two", "one"
        String[] control = {
              "three", "two", "one"
        };

        int count = 3;
        display(control.clone(), map);
        for (String s : map.keySet()) {          
            assertEquals(control[--count], s);
        }

        map.put("four", "four");
        map.put("five", "five");
        map.put("three", "three");
        map.put("six", "six");
        control = new String[] {
              "six", "three", "five", "four", "two"
        };
        count = 5;
        display(control.clone(), map);
        for (String s: map.keySet()) {            
            assertEquals(control[--count], s);
        }
    }
    
    // --------------------------------------------------------- Private Methods
    
    private static void display(String[] expected, LRUMap<String,String> actual) {
        System.out.println("Expected order:");
        List<String> revControl = Arrays.asList(expected);
        Collections.reverse(revControl);
        for (String s: revControl) {
            System.out.print(s + ' ');
        }
        System.out.println('\n');
        System.out.println("Actual order:");
        for (String s: actual.keySet()) {
            System.out.print(s + ' ');
        }
        System.out.println();
    }
}
