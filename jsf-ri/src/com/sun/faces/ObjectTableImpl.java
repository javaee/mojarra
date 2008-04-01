/*
 * $Id: ObjectTableImpl.java,v 1.5 2001/11/22 02:02:17 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */
// ObjectTableImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.ObjectTable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


/**
 *
 *  <B>ObjectTableImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ObjectTableImpl.java,v 1.5 2001/11/22 02:02:17 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ObjectTableImpl extends ObjectTable
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

private ArrayList scopes = null;

//
// Constructors and Initializers    
//

public ObjectTableImpl()
{
    super();
    if (null == scopes) {
	synchronized(this) {
	    scopes = new ArrayList();
	    RequestScope = new ScopeImpl(HttpServletRequest.class);
	    scopes.add(RequestScope);
	    SessionScope = new ScopeImpl(HttpSession.class);
	    scopes.add(SessionScope);
	    GlobalScope = new ScopeImpl(Object.class); 
	    scopes.add(GlobalScope);
	}
    }
}

//
// Class methods
//

//
// Methods from ObjectTable
//

public List getScopes() {
    List result = null;
    Assert.assert_it(null != scopes);

    synchronized (GlobalScope) {
	result = (List) scopes.clone();
    }
    return result;
}

public void setScopes(List newScopes) {
    ParameterCheck.nonNull(newScopes);
    Assert.assert_it(null != scopes);
    
    synchronized (GlobalScope) {
	scopes = new ArrayList();
	scopes.addAll(newScopes);
    }
}

//
// Inner interfaces and classes
// 

protected static class ScopeImpl implements ObjectTable.Scope {

// Relationship Instance Variables

    private Map outerMap;
    private Map innerMap;

// Attribute Instance Variables

    private Class scopeClass;

//
// Constructors and Initializers    
//

protected ScopeImpl(Class yourScopeClass) {
    ParameterCheck.nonNull(yourScopeClass);
    // PENDING(edburns): tune size of hashmap;
    outerMap = new HashMap();
    innerMap = new HashMap();
    scopeClass = yourScopeClass;
}
    
//
// General Methods
//

/**

* @return true if the given scope is active

*/

private boolean inScope(Object scopeKey) {
    boolean result = false;
    result = (null != innerMap.get(scopeKey));
    return result;
}

//
// Methods from Scope
//

public boolean isA(Object scopeKey) {
    ParameterCheck.nonNull(scopeKey);
    Assert.assert_it(null != scopeClass);

    return scopeClass.isInstance(scopeKey);
}

public void put(Object name, Object value) {
    outerMap.put(name, value);
}

public Object get(Object scopeKey, Object name) {
    // lazily enter the scope if neccessary
    if (!inScope(scopeKey)) {
	enter(scopeKey);
    }

    // first, look in our innerMap for a Map, ask this map for a value
    // under name
    Map second = (Map) innerMap.get(scopeKey);
    
    // We must have a secondary map by nature of the lazy scope entrance above
    Assert.assert_it(null != second);
    
    Object result = second.get(name);

    // if not found, this is the first request for this name
    if (null == result) {
	// look in the outerMap
	result = outerMap.get(name);
	
	ActiveValue av;
	LazyValue lv;
	// Deal with Active and Lazy values
	if (result instanceof ActiveValue) {
	    av = (ActiveValue) result;
	    result = av.getValue(this, scopeKey, name);
	    // do not put an entry in the inner map
	}
	else if (result instanceof LazyValue) {
	    lv = (LazyValue) result;
	    result = lv.getValue(this, scopeKey, name);
	    // put an entry in the inner map.
	    ((Map)innerMap.get(scopeKey)).put(name, result);
	}
    }

    return result;
}

public void enter(Object scopeKey) {
    innerMap.put(scopeKey, new HashMap());
    // Call listeners
}
    
public void exit(Object scopeKey) {
    ParameterCheck.nonNull(scopeKey);
    // Call listeners

    // clear the map
    Map scopeMap = (Map) innerMap.get(scopeKey);
    Assert.assert_it(null != scopeMap);

    scopeMap.clear();
    innerMap.remove(scopeKey);
}
} // end of class ScopeImpl

} // end of class ObjectTableImpl
