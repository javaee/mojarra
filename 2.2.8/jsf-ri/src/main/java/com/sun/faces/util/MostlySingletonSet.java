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
package com.sun.faces.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * Non-thread safe implementation of Set for use when most of the time there
 * is only one element, but sometimes there are more than one.
 * 
 */
public class MostlySingletonSet<E> implements Set<E>, Serializable {
    
    private static final long serialVersionUID = 2818326518724772145L;
    
    private Set<E> inner;
    
    public MostlySingletonSet() {
        
    }
    
    // <editor-fold defaultstate="collapsed" desc="Mutating methods">

    @Override
    public boolean add(E e) {
        boolean result = true;
        if (null == inner) {
            inner = Collections.singleton(e);
        } else {
            // If we need to transition from one to more-than-one
            if (1 == inner.size()) {
                HashSet<E> newSet = new HashSet<E>();
                newSet.add(inner.iterator().next());
                inner = newSet;
            }
            result = inner.add(e);
        }
        
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = true;
        
        if (null == inner && 1 == c.size()) {
            inner = (Set<E>) Collections.singleton(c.iterator().next());
        } else {
            // If we need to transition from one to more-than-one
            if (1 == inner.size()) {
                HashSet<E> newSet = new HashSet<E>();
                newSet.add(inner.iterator().next());
                inner = newSet;
            }
            result = inner.addAll(c);
        }
        return result;
    }

    @Override
    public void clear() {
        if (null != inner) {
            // If we need to transition from more-than-one to zero
            if (1 < inner.size()) {
                inner.clear();
            }
            inner = null;
        }
    }
    
    
    @Override
    public boolean remove(Object o) {
        boolean didRemove = false;
        
        if (null != inner) {
            if (1 == inner.size()) {
                // If we need to transition from one to zero
                E e = inner.iterator().next();
                // If our element is not null, and the argument is not null
                if (null != e && null != o) {
                    didRemove = e.equals(o);
                } else {
                    didRemove = null == o;
                }
                if (didRemove) {
                    inner = null;
                }
                
            } else {
                didRemove = inner.remove(o);
                if (didRemove && 1 == inner.size()) {
                    Set<E> newInner = Collections.singleton(inner.iterator().next());
                    inner.clear();
                    inner = newInner;
                }
            }
            
            
        }
        
        return didRemove;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        
        if (null != inner) {
            if (1 == inner.size()) {
                // May throw NPE per spec for Collection.removeAll()
                Iterator incomingIter = c.iterator();
                E oneAndOnlyElement = inner.iterator().next();
                // Iterate over the incoming collection
                // looking for a member that is equal to our one and only
                // element.
                while (incomingIter.hasNext()) {
                    Object cur = incomingIter.next();
                    if (null != oneAndOnlyElement) {
                        // This handles null == cur.
                        if (result = oneAndOnlyElement.equals(cur)) {
                            break;
                        } 
                    } else {
                        // oneAndOnlyElement is null
                        if (result = cur == null) {
                            break;
                        }
                    }
                }
                if (result) {
                    inner = null;
                }
            } else {
                result = inner.removeAll(c);
                if (result && 0 == inner.size()) {
                    inner = null;
                }
                
            }
        }
        
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean didModify = false;
        
        if (null != inner) {
            if (1 == inner.size()) {
                Iterator incomingIter = c.iterator();
                E oneAndOnlyElement = inner.iterator().next();
                // Iterate over the incoming collection
                // looking for a member that is equal to our one and only
                // element.  If found, we take no action, otherwise
                // we remove the oneAndOnlyElement.
                boolean found = false;
                while (incomingIter.hasNext()) {
                    Object cur = incomingIter.next();
                    if (null != oneAndOnlyElement) {
                        if (found = oneAndOnlyElement.equals(cur)) {
                            break;
                        }
                    } else {
                        if (found = cur == null) {
                            break;
                        }
                    }
                }
                if (didModify = !found) {
                    inner = null;
                }
                
            } else {
                didModify = inner.retainAll(c);
            }
        }
        
        return didModify;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Non-mutating methods">
    
    @Override
    public boolean contains(Object o) {
        boolean result = false;
        
        if (null != inner) {
            result = inner.contains(o);
        }
        
        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean result = false;
        
        if (null != inner) {
            result = inner.containsAll(c);
        }
        
        return result;
    }

    @Override
    public boolean isEmpty() {
        boolean result = true;
        
        if (null != inner) {
            result = inner.isEmpty();
        }
        
        return result;
    }


    @Override
    public int size() {
        int size = 0;
        if (null != inner) {
            size = inner.size();
        }
        return size;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null) {
            if (obj instanceof MostlySingletonSet) {
                final MostlySingletonSet<E> other = (MostlySingletonSet<E>) obj;
                if (this.inner != other.inner && (this.inner == null || !this.inner.equals(other.inner))) {
                    result = false;
                } else {
                    result = true;
                }
            } else if (obj instanceof Collection) {
                Collection otherCollection = (Collection) obj;
                
                if (null != inner) {
                    result = inner.equals(otherCollection);
                } else {
                    result = otherCollection.isEmpty();
                }
                
            } 
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.inner != null ? this.inner.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        String result = "empty";
        if (null != inner) {
            result = inner.toString();
        }
        return result;
    }
    
    
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Iteration and array">

    @Override
    public Iterator<E> iterator() {
        Iterator<E> result;

        if (null != inner) {
            result = inner.iterator();
        } else {
            result = Collections.EMPTY_SET.iterator();
        }
        
        return result;
    }

    @Override
    public Object[] toArray() {
        Object [] result = null;
        if (null != inner) {
            result = inner.toArray();
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T [] result = null;
        if (null != inner) {
            result = inner.toArray(a);
        }
        return result;
    }
    
    // </editor-fold>
    
}
