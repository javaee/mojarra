/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.agnostic.facelets.html;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "dataTableCollectionBean")
@RequestScoped
public class DataTableCollectionBean implements Serializable {
    
    Collection<DataTableCollectionItem> users;

    public Collection<DataTableCollectionItem> getUsers() {
        return users;
    }

    public void setUsers(Collection<DataTableCollectionItem> users) {
        this.users = users;
    }
    
    public DataTableCollectionBean() {
        users = new MyCollection(new ArrayList<DataTableCollectionItem>());
        DataTableCollectionItem b;
        for (int i = 0; i < 3; i++) {
            b = new DataTableCollectionItem();
            b.setFirstName("First" + i);
            b.setLastName("Last"+i);
            users.add(b);
        }
    }
    
    private static class MyCollection implements Collection {
        
        private Collection inner;
        
        private MyCollection(Collection c) {
            inner = c;
        }

        @Override
        public boolean add(Object e) {
            return inner.add(e);
        }

        @Override
        public boolean addAll(Collection c) {
            return inner.addAll(c);
        }

        @Override
        public void clear() {
            inner.clear();
        }

        @Override
        public boolean contains(Object o) {
            return inner.contains(o);
        }

        @Override
        public boolean containsAll(Collection c) {
            return inner.containsAll(c);
        }

        @Override
        public boolean isEmpty() {
            return inner.isEmpty();
        }

        @Override
        public Iterator iterator() {
            return inner.iterator();
        }

        @Override
        public boolean remove(Object o) {
            return inner.remove(o);
        }

        @Override
        public boolean removeAll(Collection c) {
            return inner.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection c) {
            return inner.retainAll(c);
        }

        @Override
        public int size() {
            return inner.size();
        }

        @Override
        public Object[] toArray() {
            return inner.toArray();
        }

        @Override
        public Object[] toArray(Object[] a) {
            return inner.toArray(a);
        }   
    }    
}
