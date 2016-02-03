/*
 * $Id: TestLRUMap_local.java,v 1.4 2007/04/27 22:02:11 ofung Exp $
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
