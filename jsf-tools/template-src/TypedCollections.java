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

package @package@;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@protection@ class TypedCollections {

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
        return (Map<K, V>) map;
    }
}
