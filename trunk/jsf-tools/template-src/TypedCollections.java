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

package @package@;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@protection@ class TypedCollections {

    /**
     * Dynamically check that the members of the collection are all
     * instances of the given type (or null).
     */
    private static boolean checkCollectionMembers(Collection<?> c, Class<?> type) {
        for (Object element : c) {
            if (element != null && !type.isInstance(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Dynamically check that the members of the collection are all
     * instances of the given type (or null), and that the collection
     * itself is of the given collection type.
     * 
     * @param <E>
     *                the collection's element type
     * @param c
     *                the collection to cast
     * @param type
     *                the class of the collection's element type.
     * @return the dynamically-type checked collection.
     * @throws java.lang.ClassCastException
     */
    @SuppressWarnings("unchecked")
    @protection@ static <E,TypedC extends Collection<E>> TypedC dynamicallyCastCollection(Collection<?> c,
                                                                                          Class<E> type, 
                                                                                          Class<TypedC> collectionType) {
        if (c == null)
            return null;
        if (!collectionType.isInstance(c))
            throw new ClassCastException(c.getClass().getName());
        assert checkCollectionMembers(c, type) :
            "The collection contains members with a type other than " + type.getName();

        return collectionType.cast(c);
    }

    /**
     * Dynamically check that the members of the list are all instances of
     * the given type (or null).
     * 
     * @param <E>
     *                the list's element type
     * @param list
     *                the list to cast
     * @param type
     *                the class of the list's element type.
     * @return the dynamically-type checked list.
     * @throws java.lang.ClassCastException
     */
    @SuppressWarnings("unchecked")
    @protection@ static <E> List<E> dynamicallyCastList(List<?> list, Class<E> type) {
        return dynamicallyCastCollection(list, type, List.class);
    }

    /**
     * Dynamically check that the members of the set are all instances of
     * the given type (or null).
     * 
     * @param <E>
     *                the set's element type
     * @param set
     *                the set to cast
     * @param type
     *                the class of the set's element type.
     * @return the dynamically-type checked set.
     * @throws java.lang.ClassCastException
     */
    @SuppressWarnings("unchecked")
    @protection@ static <E> Set<E> dynamicallyCastSet(Set<?> set, 
                                                      Class<E> type) {
        return dynamicallyCastCollection(set, type, Set.class);
    }

    /**
     * Dynamically check that the keys and values in the map are all
     * instances of the correct types (or null).
     * 
     * @param <K>
     *                the map's key type
     * @param <V>
     *                the map's value type
     * @param map
     *                the map to cast
     * @param keyType
     *                the class of the map's key type.
     * @param keyType
     *                the class of the map's key type.
     * @return the dynamically-type checked map.
     * @throws java.lang.ClassCastException
     */
    @SuppressWarnings("unchecked")
    @protection@ static <K, V> Map<K, V> dynamicallyCastMap(Map<?, ?> map,
                                                            Class<K> keyType, 
                                                            Class<V> valueType) {
        if (map == null) {
            return null;                                                                     
        }
        assert checkCollectionMembers(map.keySet(), keyType) :
            "The map contains keys with a type other than " + keyType.getName();
        assert checkCollectionMembers(map.values(), valueType) :
            "The map contains values with a type other than " + valueType.getName();

        return (Map<K, V>) map;
    }
}
