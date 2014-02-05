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

package com.sun.faces.systest.model;

import java.util.Set;
import java.util.List;
import java.util.Collection;
import java.util.HashSet;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.faces.model.SelectItem;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.FacesException;


public class SelectMany05Bean {

    private final Collection<SelectItem> possibleValues;
    private Set<String> setValues;
    private SortedSet<String> sortedSetValues;
    private List<String> listValues;
    private Collection<String> collectionValues;
    private String[] arrayValues;
    private SortedSet<String> initialSortedSetValues;
    private Collection<String> initialCollectionValues;
    private Set<String> initialSetValues;
    private List<String> initialListValues;
    private Collection<String> collectionFromHintValues;
    private Collection<String> collectionFromHintValues2;
    private Object someValues;
    private Collection<HobbitBean> hobbitCollection;
    private DataModel<HobbitBean> hobbitDataModel;

    // ------------------------------------------------------------ Constructors

    public SelectMany05Bean() {
        HobbitBean[] hobbits = getHobbitBeanArray();

        Set<SelectItem> items = new LinkedHashSet<SelectItem>();
        for (HobbitBean hobbit : hobbits) {
            items.add(new SelectItem(hobbit.getName()));
        }
        hobbitCollection = new TreeSet<HobbitBean>();
        hobbitCollection.addAll(Arrays.asList(hobbits));
        possibleValues = Collections.unmodifiableSet(items);
        initialSortedSetValues = new TreeSet<String>(Collections.reverseOrder());
        initialSortedSetValues.add("Pippin");
        initialSortedSetValues.add("Frodo");
        initialCollectionValues = new LinkedHashSet<String>(2);
        initialCollectionValues.add("Bilbo");
        initialCollectionValues.add("Merry");
        initialSetValues = new CopyOnWriteArraySet<String>(); // not Cloneable
        initialSetValues.add("Frodo");
        initialListValues = new Vector<String>();
        initialListValues.add("Bilbo");
        initialListValues.add("Pippin");
        initialListValues.add("Merry");
        hobbitDataModel = new ListDataModel<HobbitBean>(new ArrayList<HobbitBean>(Arrays.asList(hobbits)));

    }
    
    protected HobbitBean [] getHobbitBeanArray() {
        HobbitBean[] hobbits = {
              new HobbitBean("Bilbo", "Bilbo - <Ring Finder>"),
              new HobbitBean("Frodo", "Frodo - <Ring Bearer>"),
              new HobbitBean("Merry", "Merry - <Trouble Maker>"),
              new HobbitBean("Pippin", "Pipping - <Trouble Maker>")
        };
        return hobbits;
    }


    // ---------------------------------------------------------- Public Methods


    public Collection<HobbitBean> getHobbitCollection() {
        return hobbitCollection;
    }

    public DataModel<HobbitBean> getHobbitDataModel() {
        return hobbitDataModel;
    }

    public Set<String> getSetValues() {
        return setValues;
    }

    public void setSetValues(Set<String> setValues) {
        if (!(setValues instanceof HashSet)) {
            throw new FacesException("[setSetValues] Error: Expected value to be ArrayList");
        }
        this.setValues = setValues;
    }

    public List<String> getListValues() {
        return listValues;
    }

    public void setListValues(List<String> listValues) {
        if (!(listValues instanceof ArrayList)) {
            throw new FacesException("[setListValues] Error: Expected value to be ArrayList");
        }
        this.listValues = listValues;
    }

    public String[] getArrayValues() {
        return arrayValues;
    }

    public void setArrayValues(String[] arrayValues) {
        this.arrayValues = arrayValues;
    }

    public SortedSet<String> getSortedSetValues() {
        return sortedSetValues;
    }

    public void setSortedSetValues(SortedSet<String> sortedSetValues) {
        if (!(sortedSetValues instanceof TreeSet)) {
            throw new FacesException("[setSortedSetValues] Error: Expected value to be TreeSet");
        }
        if (((TreeSet) sortedSetValues).comparator() != null) {
            throw new FacesException("[setSortedSetValues] Error: Expected null comparator");
        }
        this.sortedSetValues = sortedSetValues;
    }

    public Collection<String> getCollectionValues() {
        return collectionValues;
    }

    public void setCollectionValues(Collection<String> collectionValues) {
        if (!(collectionValues instanceof ArrayList)) {
            throw new FacesException("[setCollectionValues] Error: Expected value to be ArrayList");
        }
        this.collectionValues = collectionValues;
    }

    public Collection<SelectItem> getPossibleValues() {
        return possibleValues;
    }

    public Collection<?> getEmptyCollection() {
        return Collections.emptyList();
    }

    ////////////////////////////////////////////////////////////////////////////


    public SortedSet<String> getInitialSortedSetValues() {
        return initialSortedSetValues;
    }

    public void setInitialSortedSetValues(SortedSet<String> initialSortedSetValues) {
        if (!(initialSortedSetValues instanceof TreeSet)) {
            throw new FacesException("[setInitialSortedSetValues] Error: Expected value to be TreeMap");
        }
        if (!Collections.reverseOrder().equals(((TreeSet) initialSortedSetValues).comparator())) {
            throw new FacesException("[setInitialSortedSetValues] Error: Comparator is not equivalent to Collections.reverseOrder()");
        }
        this.initialSortedSetValues = initialSortedSetValues;
    }

    public Collection<String> getInitialCollectionValues() {
        return initialCollectionValues;
    }

    public void setInitialCollectionValues(Collection<String> initialCollectionValues) {
        if (!(initialCollectionValues instanceof LinkedHashSet)) {
            throw new FacesException("[setCollectionValues] Error: Expected value to be LinkedHashSet");
        }
        this.initialCollectionValues = initialCollectionValues;
    }

    public Set<String> getInitialSetValues() {
        return initialSetValues;
    }

    public void setInitialSetValues(Set<String> initialSetValues) {
        if (!(initialSetValues instanceof CopyOnWriteArraySet)) {
            throw new FacesException("[initialSetValues] Error: Expected value to be CopyOnWriteArraySet");
        }
        this.initialSetValues = initialSetValues;
    }

    public List<String> getInitialListValues() {
        return initialListValues;
    }

    public void setInitialListValues(List<String> initialListValues) {
        if (!(initialListValues instanceof Vector)) {
            throw new FacesException("[initialListValues] Error: Expected value to be Vector");
        }
        this.initialListValues = initialListValues;
    }

    ////////////////////////////////////////////////////////////////////////////

    public Collection<String> getCollectionFromHintValues() {
        return collectionFromHintValues;
    }

    public void setCollectionFromHintValues(Collection<String> collectionFromHintValues) {
        if (!(collectionFromHintValues instanceof LinkedList)) {
            throw new FacesException("[collectionFromHintValues] Error: Expected value to be LinkedList");
        }
        this.collectionFromHintValues = collectionFromHintValues;
    }

     public Collection<String> getCollectionFromHintValues2() {
        return collectionFromHintValues2;
    }

    public void setCollectionFromHintValues2(Collection<String> collectionFromHintValues) {
        if (!(collectionFromHintValues instanceof LinkedList)) {
            throw new FacesException("[collectionFromHintValues2] Error: Expected value to be LinkedList");
        }
        this.collectionFromHintValues2 = collectionFromHintValues;
    }

    public Class<? extends Collection> getCollectionType() {
        return LinkedList.class;
    }

    ////////////////////////////////////////////////////////////////////////////

    private int[] selectedIntValues;
    private int[] intOptions = new int[] { 1, 2, 3, 4 };

    public int[] getSelectedIntValues() {
        return selectedIntValues;
    }

    public void setSelectedIntValues(int[] selectedIntValues) {
        this.selectedIntValues = selectedIntValues;
    }

    public int[] getIntOptions() {
        return intOptions;
    }

    ////////////////////////////////////////////////////////////////////////////

    private Integer[] selectedIntegerValues;
    private Integer[] integerOptions = new Integer[] { 1, 2, 3, 4 };

    public Integer[] getSelectedIntegerValues() {
        return selectedIntegerValues;
    }

    public void setSelectedIntegerValues(Integer[] selectedIntegerValues) {
        this.selectedIntegerValues = selectedIntegerValues;
    }

    public Integer[] getIntegerOptions() {
        return integerOptions;
    }

    ////////////////////////////////////////////////////////////////////////////

    public Object getSomeValues() {
        return someValues;
    }

    public void setSomeValues(Object someValues) {
        // validate the case where the type is Object.class.  The logic should
        // default the value to Object[].
        if (!someValues.getClass().isArray()) {
            throw new FacesException("[someValues] Error: Expected value to be an array type");
        }
        this.someValues = someValues;
    }


    // ---------------------------------------------------------- Nested Classes


    public static final class HobbitBean implements Comparable {

        private String name;
        private String bio;


        // -------------------------------------------------------- Constructors


        public HobbitBean(String name, String bio) {
            this.name = name;
            this.bio = bio;
        }


        public String getName() {
            return name;
        }

        public String getBio() {
            return bio;
        }

        @Override
        public String toString() {
            return name;
        }

        // --------------------------------------------- Methods from Comparable


        public int compareTo(Object o) {
            return name.compareTo(((HobbitBean) o).name);
        }
    }
}
