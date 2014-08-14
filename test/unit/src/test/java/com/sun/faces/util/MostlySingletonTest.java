package com.sun.faces.util;

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class MostlySingletonTest {
    
    public MostlySingletonTest() {
    }
    
    @Test
    public void testAdd() {
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(2);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());
        
    }
    
    @Test
    public void testAddWithNulls() {
        
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(null);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());
        
        
    }
    
    @Test
    public void testAddAll() {
        Set<Integer> otherSet = new HashSet<Integer>();
        otherSet.add(4);
        otherSet.add(5);
        otherSet.add(6);
        
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(2);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());
        
        set.addAll(otherSet);
        assertTrue(6 == set.size());
        
        otherSet.add(3);
        assertTrue(4 == otherSet.size());
        set.clear();
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(2);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());
        
        set.addAll(otherSet);
        assertTrue(6 == set.size());
        
        set.clear();
        assertTrue(0 == set.size());
        set.add(1);
        set.addAll(otherSet);
        assertTrue(5 == set.size());
        
        set.clear();
        otherSet.clear();
        otherSet.add(1);
        set.addAll(otherSet);
        assertTrue(1 == set.size());
        
        
    }
    
    
    @Test
    public void testRemove() {
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(2);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());
        
        boolean didRemove = set.remove(2);
        assertTrue(didRemove);
        assertTrue(2 == set.size());
        
        didRemove = set.remove(1);
        assertTrue(didRemove);
        assertTrue(1 == set.size());
        
        didRemove = set.remove(3);
        assertTrue(didRemove);
        assertTrue(0 == set.size());
        
        didRemove = set.remove(4);
        assertTrue(!didRemove);
        assertTrue(0 == set.size());
        
        set.clear();
        assertTrue(0 == set.size());
        set.add(1);
        set.add(2);
        assertTrue(2 == set.size());
        
        set.remove(2);
        assertTrue(1 == set.size());
        set.remove(1);
        assertTrue(0 == set.size());
        
    }
    
    @Test
    public void testRemoveWithNulls() {
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(null);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());
        
        boolean didRemove = set.remove(null);
        assertTrue(didRemove);
        assertTrue(2 == set.size());
        
        didRemove = set.remove(1);
        assertTrue(didRemove);
        assertTrue(1 == set.size());
        
        didRemove = set.remove(3);
        assertTrue(didRemove);
        assertTrue(0 == set.size());
        
        didRemove = set.remove(4);
        assertTrue(!didRemove);
        assertTrue(0 == set.size());
        
        set.clear();
        assertTrue(0 == set.size());
        set.add(null);
        assertTrue(1 == set.size());
        set.remove(null);
        assertTrue(0 == set.size());
        
        set.clear();
        assertTrue(0 == set.size());
        set.add(1);
        set.add(null);
        assertTrue(2 == set.size());
        
        set.remove(null);
        assertTrue(1 == set.size());
        set.remove(1);
        assertTrue(0 == set.size());
        
        set.clear();
        set.add(null);
        set.remove(null);
        
        
    }
    
    @Test
    public void testRemoveAll() {
        Set<Integer> otherSet = new HashSet<Integer>();
        otherSet.add(1);
        otherSet.add(2);
        otherSet.add(3);
        
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(2);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());
        
        boolean didRemove = set.removeAll(otherSet);
        assertTrue(didRemove);
        assertTrue(0 == set.size());
        
        
        set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(2);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());      
        
        set.add(4);
        assertTrue(4 == set.size());
        
        set.add(5);
        assertTrue(5 == set.size());
        
        didRemove = set.removeAll(otherSet);
        assertTrue(didRemove);
        assertTrue(2 == set.size());
        
        set.clear();
        assertTrue(0 == set.size());
        set.add(1);
        set.removeAll(otherSet);
    }
    
    @Test
    public void testRemoveAllWithNulls() {
        Set<Integer> otherSet = new HashSet<Integer>();
        otherSet.add(1);
        otherSet.add(null);
        otherSet.add(3);
        
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(null);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());
        
        boolean didRemove = set.removeAll(otherSet);
        assertTrue(didRemove);
        assertTrue(0 == set.size());
        
        set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(null);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());      
        
        set.add(4);
        assertTrue(4 == set.size());
        
        set.add(5);
        assertTrue(5 == set.size());
        
        didRemove = set.removeAll(otherSet);
        assertTrue(didRemove);
        assertTrue(2 == set.size());
        
        
        set.clear();
        assertTrue(0 == set.size());
        set.add(null);
        set.removeAll(otherSet);
        
    }
    
    @Test
    public void testRetainAll() {
        Set<Integer> otherSet = new HashSet<Integer>();
        otherSet.add(1);
        otherSet.add(2);
        otherSet.add(3);
        
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(2);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());

        boolean didRemove = set.retainAll(otherSet);
        assertTrue(!didRemove);
        assertTrue(3 == set.size());
        
        set.add(4);
        assertTrue(4 == set.size()) ;
        set.retainAll(otherSet);
        assertTrue(3 == set.size());
        assertTrue(!set.contains(4));
        
        set.clear();
        assertTrue(0 == set.size());
        otherSet.clear();
        assertTrue(0 == otherSet.size());
        set.add(1);
        assertTrue(1 == set.size());
        otherSet.add(2);
        assertTrue(1 == otherSet.size());
        
        set.retainAll(otherSet);
        assertTrue(0 == set.size());
    }
    
    
    @Test
    public void testRetainAllWithNulls() {
        Set<Integer> otherSet = new HashSet<Integer>();
        otherSet.add(1);
        otherSet.add(null);
        otherSet.add(3);
        
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(1 == set.size());
        
        set.add(null);
        assertTrue(2 == set.size());
        
        set.add(3);
        assertTrue(3 == set.size());

        boolean didRemove = set.retainAll(otherSet);
        assertTrue(!didRemove);
        assertTrue(3 == set.size());
        
        set.add(4);
        assertTrue(4 == set.size()) ;
        set.retainAll(otherSet);
        assertTrue(3 == set.size());
        assertTrue(!set.contains(4));
        
        set.clear();
        assertTrue(0 == set.size());
        otherSet.clear();
        assertTrue(0 == otherSet.size());
        set.add(null);
        assertTrue(1 == set.size());
        otherSet.add(2);
        assertTrue(1 == otherSet.size());
        
        set.retainAll(otherSet);
        assertTrue(0 == set.size());
        
    }
    
    @Test
    public void testContains() {
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(set.contains(1));
        assertTrue(!set.contains(2));
        
        set.add(2);
        assertTrue(set.contains(2));
        
        set.remove(2);
        assertTrue(!set.contains(2));
        assertTrue(1 == set.size());
        
        set.remove(1);
        assertTrue(!set.contains(1));
        assertTrue(0 == set.size());
        
        set.add(1);
        assertTrue(set.contains(1));
        assertTrue(!set.contains(2));
        
        set.add(2);
        assertTrue(set.contains(2));

        
    }
    
    @Test
    public void testContainsWithNulls() {
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(null);
        assertTrue(set.contains(null));
        assertTrue(!set.contains(2));
        
        set.add(2);
        assertTrue(set.contains(2));
        
        set.remove(2);
        assertTrue(!set.contains(2));
        assertTrue(1 == set.size());
        
        set.remove(null);
        assertTrue(!set.contains(null));
        assertTrue(0 == set.size());
        
        set.add(1);
        assertTrue(set.contains(1));
        assertTrue(!set.contains(2));
        
        set.add(null);
        assertTrue(set.contains(null));

        
    }
    
    @Test
    public void testContainsAll() {
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(1);
        assertTrue(set.contains(1));
        assertTrue(!set.contains(2));

        Set<Integer> otherSet = new HashSet<Integer>();
        otherSet.add(1);
        assertTrue(set.containsAll(otherSet));
        
        set.add(2);
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        
        assertTrue(set.containsAll(otherSet));
        otherSet.clear();
        assertTrue(set.containsAll(otherSet));
    }
        
    @Test
    public void testContainsAllWithNulls() {
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        set.add(null);
        assertTrue(set.contains(null));
        assertTrue(!set.contains(2));

        Set<Integer> otherSet = new HashSet<Integer>();
        otherSet.add(null);
        assertTrue(set.containsAll(otherSet));
        
        set.add(2);
        assertTrue(set.contains(null));
        assertTrue(set.contains(2));
        
        assertTrue(set.containsAll(otherSet));
        otherSet.clear();
        assertTrue(set.containsAll(otherSet));
    }
        
    
    @Test
    public void testIsEmpty() {
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        assertTrue(set.isEmpty());
        set.add(1);
        assertTrue(!set.isEmpty());
        
        set.remove(1);
        assertTrue(set.isEmpty());
        
        set.add(1);
        set.add(2);
        assertTrue(!set.isEmpty());
        set.remove(1);
        set.remove(2);
        assertTrue(set.isEmpty());
        
    }
    
    @Test
    public void testIsEmptyWithNulls() {
        MostlySingletonSet<Integer> set = new MostlySingletonSet<Integer>();
        
        assertTrue(set.isEmpty());
        set.add(null);
        assertTrue(!set.isEmpty());
        
        set.remove(null);
        assertTrue(set.isEmpty());
        
        set.add(1);
        set.add(null);
        assertTrue(!set.isEmpty());
        set.remove(1);
        set.remove(null);
        assertTrue(set.isEmpty());
        
    }
   
    @Test
    public void testEquals() {
        MostlySingletonSet<Integer> setA = new MostlySingletonSet<Integer>();
        
        setA.add(1);
        assertTrue(1 == setA.size());
        
        setA.add(2);
        assertTrue(2 == setA.size());
        
        setA.add(3);
        assertTrue(3 == setA.size());
        
        Set<Integer> otherSet = new HashSet<Integer>();
        otherSet.add(1);
        otherSet.add(2);
        otherSet.add(3);

        assertTrue(setA.equals(otherSet));
        assertTrue(otherSet.equals(setA));
        
        setA.clear();
        otherSet.clear();
        assertTrue(setA.equals(otherSet));
        assertTrue(otherSet.equals(setA));

        setA.add(1);
        setA.add(2);
        setA.add(3);
        
        MostlySingletonSet<Integer> setB = new MostlySingletonSet<Integer>();
        setB.add(1);
        setB.add(2);
        setB.add(3);
        assertTrue(setA.equals(setB));
        assertTrue(setB.equals(setA));
        
        setA.clear();
        setB.clear();
        assertTrue(setA.equals(setB));
        assertTrue(setB.equals(setA));
    }
    
    @Test
    public void testToString() {
        
        MostlySingletonSet<Integer> setA = new MostlySingletonSet<Integer>();
        
        setA.add(1);
        assertTrue(1 == setA.size());
        
        setA.add(2);
        assertTrue(2 == setA.size());
        
        setA.add(3);
        assertTrue(3 == setA.size());
        
        String toString = setA.toString();
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("2"));
        assertTrue(toString.contains("3"));
        assertTrue(!toString.contains("4"));
        
    }
    
    @Test
    public void testToStringWithNulls() {
        
        MostlySingletonSet<Integer> setA = new MostlySingletonSet<Integer>();
        
        setA.add(1);
        assertTrue(1 == setA.size());
        
        setA.add(null);
        assertTrue(2 == setA.size());
        
        setA.add(3);
        assertTrue(3 == setA.size());
        
        String toString = setA.toString();
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("null"));
        assertTrue(toString.contains("3"));
        assertTrue(!toString.contains("4"));
        
    }

}
