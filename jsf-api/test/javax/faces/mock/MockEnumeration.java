/*
 * $Id: MockEnumeration.java,v 1.1 2003/03/13 06:06:15 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.Enumeration;
import java.util.Iterator;


/**
 * <p>General purpose <code>Enumeration</code> wrapper around an
 * <code>Iterator</code> specified to our controller.</p>
 */

public class MockEnumeration implements Enumeration {


    public MockEnumeration(Iterator iterator) {
        this.iterator = iterator;
    }


    protected Iterator iterator;


    public boolean hasMoreElements() {
        return (iterator.hasNext());
    }


    public Object nextElement() {
        return (iterator.next());
    }


}
