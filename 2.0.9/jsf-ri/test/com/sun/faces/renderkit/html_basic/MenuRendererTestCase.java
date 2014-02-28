/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.renderkit.html_basic;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.faces.FacesException;

import com.sun.faces.cactus.ServletFacesTestCase;


public class MenuRendererTestCase extends ServletFacesTestCase {

    // ----------------------------------------------------------- Setup Methods


    public MenuRendererTestCase() {
        super("MenuRendererTestCase.java");
    }


    public MenuRendererTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------------ Test Methods


    public void testCreateCollection() {

        TestMenuRenderer r = new TestMenuRenderer();

        // null instance using interface for the fallback should
        // result in a null return
        assertNull(r.createCollection(null, Set.class));

        Collection c = r.createCollection(new HashSet(), ArrayList.class);
        assertNotNull(c);
        assertTrue(c instanceof HashSet);
        assertTrue(c.isEmpty());

    }


    public void testCloneValue() {

        TestMenuRenderer r = new TestMenuRenderer();

        Collection<String> clonableCollection = new ArrayList<String>();
        clonableCollection.add("foo");

        Collection cloned = r.cloneValue(clonableCollection);
        assertNotNull(cloned);
        assertTrue(cloned.isEmpty());

        Collection nonClonableCollection = new CopyOnWriteArraySet();
        assertNull(r.cloneValue(nonClonableCollection));

    }

    public void testBestGuess() {

        TestMenuRenderer r = new TestMenuRenderer();
        assertTrue(r.bestGuess(Set.class,  1) instanceof HashSet);
        assertTrue(r.bestGuess(List.class, 1) instanceof ArrayList);
        assertTrue(r.bestGuess(SortedSet.class, 1) instanceof TreeSet);
        assertTrue(r.bestGuess(Queue.class, 1) instanceof LinkedList);
        assertTrue(r.bestGuess(Collection.class, 1) instanceof ArrayList);
        
    }

    public void testCreateCollectionFromHint() {

        TestMenuRenderer r = new TestMenuRenderer();

        assertTrue(r.createCollectionFromHint("java.util.ArrayList") instanceof ArrayList);
        assertTrue(r.createCollectionFromHint(LinkedList.class) instanceof LinkedList);
        try {
            r.createCollectionFromHint(java.util.Set.class);
            assertTrue(false);
        } catch (FacesException fe) {
            // expected
        }

        try {
            r.createCollectionFromHint(new Date());
            assertTrue(false);
        } catch (FacesException fe) {
            // expected
        }
    }


    // ---------------------------------------------------------- Nested Classes

    private static final class TestMenuRenderer extends MenuRenderer {

        @Override
        public Collection createCollection(Collection collection,
                                              Class<? extends Collection> fallBackType) {
            return super.createCollection(collection, fallBackType);
        }

        @Override
        public Collection createCollectionFromHint(Object collectionTypeHint) {
            return super.createCollectionFromHint(collectionTypeHint);
        }

        @Override
        public Collection bestGuess(Class<? extends Collection> type,
                                       int initialSize) {
            return super.bestGuess(type, initialSize);
        }

        @Override
        protected Collection cloneValue(Object value) {
            return super.cloneValue(value);
        }
    }
}
