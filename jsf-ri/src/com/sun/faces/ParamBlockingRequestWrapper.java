/*
 * $Id: ParamBlockingRequestWrapper.java,v 1.3 2002/03/08 22:16:08 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ParamBlockingRequestWrapper.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.NoSuchElementException;

/**
 *

 *  The sole purpose of <B>ParamBlockingRequestWrapper</B> is to wrap an
 *  HttpServletRequest and prevent accesses to that request's param
 *  data. <P>

 *
 * <B>Lifetime And Scope</B> <P>

 * A instance of this class is created just before we forward the
 * request to the request dispatcher in the event dispatching logic.
 * This currently happens in
 * com.sun.faces.CommandDispatcherImpl.dispatch().  No local reference
 * is kept to this instance and we rely on automatic garbage collection. <P>

 *
 * @version $Id: ParamBlockingRequestWrapper.java,v 1.3 2002/03/08 22:16:08 eburns Exp $
 * 
 * @see	com.sun.faces.CommandDispatcherImpl#dispatch
 *
 */

public class ParamBlockingRequestWrapper extends HttpServletRequestWrapper
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

/**
 
* This package scoped ctor is called from CommandDispatcherImpl.

*/

public ParamBlockingRequestWrapper(HttpServletRequest toWrap)
{
    super(toWrap);
}


//
// Class methods
//

//
// Methods from ServletRequest that we want consumers to ignore.
//

    public String getParameter(String param) {
	return null;
    }

    public Enumeration getParameterNames() {
	return new Enumeration() {
		public boolean hasMoreElements() { return false; }
		public Object nextElement() { return null; }
	    };
    }
    
    public String [] getParameterValues(String param) {
	return null;
    }

    public Map getParameterMap() {
	return new EmptyMap();
    }

class EmptyMap extends AbstractMap {
    public Set entrySet() {
	return new EmptySet();
    }
}

class EmptySet extends AbstractSet {
    public Iterator iterator() {
	return new Iterator() {
		public boolean hasNext() { return false; }
		public Object next() { throw new NoSuchElementException();}
		public void remove() { 
		    throw new UnsupportedOperationException(); 
		}
	    };
    }
    public int size() { return 0; }
}    

// The testcase for this class is TestParamBlockingRequestWrapper.java 


} // end of class ParamBlockingRequestWrapper


