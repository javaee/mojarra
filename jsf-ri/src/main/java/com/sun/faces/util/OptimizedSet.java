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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * This class is not thread safe.
 * 
// Todo: we are using a HashSet with a default initial capacity here because it
// is convenient.  However, this is not optimal.  A better approach would be to
// optimize for a small collection size, since the most common dynamic component
// cases likely involve a small # of removes per-parent.  However, we do want this
// to scale well in the event that a large # of components are used.  One approach
// would be to create a wrapper collection that switches between:
// 1. Collections.singleton(): 1 element case
// 2. ArrayList(): small # of elements (eg. 2-4)
// 3. HashSet: medium-large # of elements (eg. > 5)
// Minimally, we should support #1 and #3 - ie. we should optimize
// the single element case.
 * 
 * 
 */
public class OptimizedSet<E> implements Set<E>, Serializable {

    // <editor-fold defaultstate="collapsed" desc="Instance state">
    
    private static final long serialVersionUID = 3680543227669203349L;

    private Collection<E> singleton;
    private ArrayList<E> small;
    private HashSet<E> large;
    
    static final int MAX_SMALL_THRESHOLD = 4;
    
    enum State {
        none,
        singleton,
        small, 
        large
    }
    
    private State state;
    
    // </editor-fold>

    public OptimizedSet() {
        state = State.none;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Helper methods">
    
    State getState() {
        return state;
    }

    private Collection<E> getModifiableInner() {
        Collection<E> result = Collections.emptyList();
        
        if (State.singleton == state) {
            result = singleton;
        } else if (State.small == state) {
            result = small;
        } else if (State.large == state) {
            result = large;
        }
        
        return result;
    }
    
    private Collection<E> getUnmodifiableInner() {
        return Collections.unmodifiableCollection(getModifiableInner());
    }
    
    private void adjustDown() {
        int size = size();
        
        // Short circuit case when no adjustment is necessary
        if (State.none == state && 0 == size) {
            return;  
        } else if (State.singleton == state && 1 == size) {
            return;
        } else if (State.small == state && 1 < size && size <= MAX_SMALL_THRESHOLD) {
            return;
        } else if (State.large == state && MAX_SMALL_THRESHOLD < size) {
            return;
        }
        
        // We need adjustment.
        
        // If adjusting to 0.
        if (0 == size) {
            singleton = null;
            small = null;
            large = null;
            state = State.none;
        } 
        // If going to singleton...
        else if (1 == size && State.singleton != state) {
            // ... from small
            if (State.small == state) {
                singleton = Collections.singleton(small.iterator().next());
                small.clear();
                small = null;
            } 
            // ... from large
            else if (State.large == state) {
                singleton = Collections.singleton(large.iterator().next());
                large.clear();
                large = null;
            } else {
                throw new IllegalStateException();
            }
            state = State.singleton;
        } 
        // If going to small from large
        else if (size <= MAX_SMALL_THRESHOLD && State.large == state) {
            small = new ArrayList<E>(MAX_SMALL_THRESHOLD);
            small.addAll(large);
            large.clear();
            large = null;
            state = State.small;
        } 
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Object overrides">
    

    @Override
    public boolean equals(Object obj) {
        return getModifiableInner().equals(obj);
    }

    @Override
    public int hashCode() {
        return getModifiableInner().hashCode();
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Non-modifying methods">

    @Override
    public int size() {
        return getModifiableInner().size();
    }
    
    @Override
    public boolean contains(Object o) {
        return getModifiableInner().contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return getModifiableInner().containsAll(c);
    }

    @Override
    public boolean isEmpty() {
        return getModifiableInner().isEmpty();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="(Potentially) modifying methods">
    
    /*
     * The result is unmodifiable.
     * 
     */
    @Override
    public Iterator<E> iterator() {
        return getUnmodifiableInner().iterator();
    }

    /*
     * The result is unmodifiable.
     * 
     */
    @Override
    public Object[] toArray() {
        return getUnmodifiableInner().toArray();
    }

    /*
     * The result is unmodifiable.
     * 
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return getUnmodifiableInner().toArray(a);
    }
    
    @Override
    public boolean add(E e) {
        boolean result = false;
        if (State.none == state) {
            singleton = Collections.singleton(e);
            state = State.singleton;
            result = true;
        } else if (State.singleton == state) {
            small = new ArrayList<E>(MAX_SMALL_THRESHOLD);
            result = small.add(singleton.iterator().next());
            small.add(e);
            singleton = null;
            state = State.small;
        } else if (State.small == state) {
            if (small.size() + 1 <= MAX_SMALL_THRESHOLD) {
                result = small.add(e);
            } else {
                large = new HashSet<E>();
                result = large.addAll(small);
                large.add(e);
                small.clear();
                small = null;
                state = State.large;
            }
        } else if (State.large == state) {
            result = large.add(e);
        } else {
            throw new IllegalStateException();
        }
        
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        int cSize = c.size();
        
        // Case 0: empty input
        if (0 == cSize) {
            return result;
        }
        int mySize = size();
        
        // Case 1: already large, stay large.
        if (State.large == state) {
            result = large.addAll(c);
        } else {
            // Case 2: no state.
            if (State.none == state) {
                if (1 == cSize) {
                    result = add(c.iterator().next());
                } else if (cSize <= MAX_SMALL_THRESHOLD) {
                    small = new ArrayList<E>(MAX_SMALL_THRESHOLD);
                    result = small.addAll(c);
                    state = State.small;
                } else if (MAX_SMALL_THRESHOLD < cSize) {
                    large = new HashSet<E>();
                    result = large.addAll(c);
                    state = State.large;
                }
            }
            // Case 3: singleton
            else if (State.singleton == state) {
                // If going from singleton to small.
                if (cSize + mySize <= MAX_SMALL_THRESHOLD) {
                    small = new ArrayList<E>(MAX_SMALL_THRESHOLD);
                    small.add(singleton.iterator().next());
                    singleton = null;
                    result = small.addAll(c);
                    state = State.small;
                }
                // If going from singleton to large.
                else if (MAX_SMALL_THRESHOLD < cSize) {
                    large = new HashSet<E>();
                    large.add(singleton.iterator().next());
                    singleton = null;
                    result = large.addAll(c);
                    state = State.large;
                }

            } 
            // Case 4: small
            else if (State.small == state) {
                // If staying small.
                if (cSize + mySize <= MAX_SMALL_THRESHOLD) {
                    result = small.addAll(c);
                }
                // If going from small to large.
                else {
                    large = new HashSet<E>();
                    large.addAll(small);
                    small.clear();
                    small = null;
                    large.addAll(c);
                }
            }
        }
        
        return result;
    }

    @Override
    public void clear() {
        if (State.singleton == state) {
            singleton = null;
            state = State.none;
        } else {
            getModifiableInner().clear();
            adjustDown();
        }
    }

    @Override
    public boolean remove(Object o) {
        boolean result = false;
        if (State.singleton == state) {
            Object cur = singleton.iterator().next();
            if (cur.equals(o)) {
                result = true;
                singleton = null;
                state = State.none;
            }
        } else {
            result = getModifiableInner().remove(o);
            adjustDown();
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = getModifiableInner().removeAll(c);
        adjustDown();
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = getModifiableInner().retainAll(c);
        adjustDown();
        return result;
    }

    // </editor-fold>

}
