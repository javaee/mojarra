/*
 * $Id: MockEnumeration.java,v 1.2 2004/02/04 23:39:11 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
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
