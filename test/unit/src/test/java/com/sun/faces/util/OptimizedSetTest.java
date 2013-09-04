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
package com.sun.faces.util;

import java.io.ObjectInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import static com.sun.faces.util.OptimizedSet.State;

public class OptimizedSetTest {
    
    
    @Test
    public void testInitialState() throws Exception {
        OptimizedSet<String> c = new OptimizedSet<String>();
        
        assertTrue(State.none == c.getState());
    }
    
    @Test 
    public void testSingletonState() throws Exception {
        OptimizedSet<String> c = new OptimizedSet<String>();
        
        assertTrue(State.none == c.getState());
        c.add("one");
        assertTrue(State.singleton == c.getState());
        assertEquals(1, c.size());
        
        c.clear();
        assertTrue(State.none == c.getState());
        assertEquals(0, c.size());

        c.add("two");
        assertTrue(State.singleton == c.getState());
        assertEquals(1, c.size());

        // Try to remove non-existent element.
        boolean result = c.remove("three");
        assertEquals(false, result);
        assertTrue(State.singleton == c.getState());
        assertEquals(1, c.size());
        
        c = serializeAndDeserialize(c);

        // Remove existing element
        result = c.remove("two");
        assertEquals(true, result);
        assertTrue(State.none == c.getState());
        assertEquals(0, c.size());
        
        c.add("one");
        assertTrue(State.singleton == c.getState());
        assertEquals(1, c.size());
    }
    
    @Test
    public void testSingletonToSmallState() throws Exception {
        OptimizedSet<String> c = new OptimizedSet<String>();

        assertTrue(State.none == c.getState());
        c.add("one");
        assertTrue(State.singleton == c.getState());
        assertEquals(1, c.size());

        c.add("two");
        assertTrue(State.small == c.getState());
        assertEquals(2, c.size());
        
        c.clear();
        assertTrue(State.none == c.getState());
        assertEquals(0, c.size());

        c.add("one");
        assertTrue(State.singleton == c.getState());
        assertEquals(1, c.size());

        c.add("two");
        assertTrue(State.small == c.getState());
        assertEquals(2, c.size());
        
        c = serializeAndDeserialize(c);

        c.remove("two");
        assertTrue(State.singleton == c.getState());
        assertEquals(1, c.size());
        
        c.add("two");
        assertTrue(State.small == c.getState());
        assertEquals(2, c.size());

        c.remove("one");
        assertTrue(State.singleton == c.getState());
        assertEquals(1, c.size());
        
        c.remove("two");
        assertTrue(State.none == c.getState());
        assertEquals(0, c.size());
        
    }
    
    @Test
    public void testSmallToLargeState() throws Exception {
        OptimizedSet<String> c = new OptimizedSet<String>();

        assertTrue(State.none == c.getState());
        c.add("one");
        assertTrue(State.singleton == c.getState());
        assertEquals(1, c.size());

        for (int i = 1; i < OptimizedSet.MAX_SMALL_THRESHOLD; i++) {
            c.add("two" + i);            
            assertTrue("State should be small when size is " + c.size(), 
                    State.small == c.getState());
            assertEquals(i + 1, c.size());
        }
        
        c.add("three");
        assertTrue(State.large == c.getState());
        assertEquals(OptimizedSet.MAX_SMALL_THRESHOLD + 1, c.size());
        
        c.remove("three");
        assertTrue(State.small == c.getState());
        assertEquals(OptimizedSet.MAX_SMALL_THRESHOLD, c.size());
        
        c.add("three");
        assertTrue(State.large == c.getState());
        assertEquals(OptimizedSet.MAX_SMALL_THRESHOLD + 1, c.size());
        
        c = serializeAndDeserialize(c);

        c.remove("four");
        assertTrue(State.large == c.getState());
        assertEquals(OptimizedSet.MAX_SMALL_THRESHOLD + 1, c.size());
        
        c.clear();
        assertTrue(State.none == c.getState());
        assertEquals(0, c.size());
        
        c.add("one");
        assertTrue(State.singleton == c.getState());
        assertEquals(1, c.size());
        
    }
    
    private OptimizedSet<String> serializeAndDeserialize(OptimizedSet<String> c) throws Exception {
        OptimizedSet<String> result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(c);
        oos.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        result = (OptimizedSet<String>) ois.readObject();
        
        return result;
    }
}


