/*
 * $Id: ObjectTableImpl.java,v 1.10 2001/12/20 21:05:07 edburns Exp $
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
import javax.faces.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


/**
 *
 *  <B>ObjectTableImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ObjectTableImpl.java,v 1.10 2001/12/20 21:05:07 edburns Exp $
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
    // PENDING(edburns): see ObjectTable.setInstance()
    ObjectTable.setInstance(this);
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

/**

* Work around these problems: <P>

1) the HttpSession instance is different from request to request, even
though each instance represents the same logical http session.  <P>

2) Another problem is that the HttpServletRequest one gets by calling
pageContext.getRequest() from inside a tag's doStartTag() method is
different from the one passed into the HttpServlet.service() method,
even though they are logically the same request. <P>

* @see com.sun.faces.servlet.SessionListener#sessionCreated

*/

private Object fixScopeKey(Object scopeKey) {
    Object result = scopeKey;
    if (scopeKey instanceof HttpSession) {
	HttpSession session = (HttpSession) scopeKey;
	String sessionId = session.getId();
	result = session.getServletContext().getAttribute(sessionId);
	// if we can't get it out of the servletContext's attr set, we
	// are dealing with a session that has been serialized and
	// de-serialized by the container.  Rely on the logic in our
	// SessionListener.
	if (null == result) {
	    result = session.getAttribute(Constants.REF_SESSIONINSTANCE);
	    // this is necessary so the
	    // SessionListener.sessionDestroyed() works.
	    session.setAttribute(sessionId, sessionId);
	}
    }
    else if (scopeKey instanceof HttpServletRequest) {
	result = ((HttpServletRequest)scopeKey).
		  getAttribute(Constants.REF_REQUESTINSTANCE);
    }
    Assert.assert_it(null != result);
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

public void put(Object scopeKey, Object name, Object value) {
    scopeKey = fixScopeKey(scopeKey);
    // lazily enter the scope if neccessary
    if (!inScope(scopeKey)) {
	enter(scopeKey);
    }
    
    Map second = (Map) innerMap.get(scopeKey);
    // We must have a secondary map by nature of the lazy scope entrance above
    Assert.assert_it(null != second);
    second.put(name, value);
}

public Object get(Object scopeKey, Object name) {
    scopeKey = fixScopeKey(scopeKey);
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
    ParameterCheck.nonNull(scopeKey);
    innerMap.put(scopeKey, new HashMap());
    // Call listeners
}
    
public void exit(Object scopeKey) {
    ParameterCheck.nonNull(scopeKey);
    scopeKey = fixScopeKey(scopeKey);
    // Call listeners

    // clear the map
    Map scopeMap = (Map) innerMap.get(scopeKey);
    if (null != scopeMap) {
	Iterator iter;
	Set keys = scopeMap.keySet();
	if (null != keys) {
	    iter = keys.iterator();
	    // remove all the entries in the outer map for this scopeKey
	    while (iter.hasNext()) {
		outerMap.remove((String) iter.next());
	    }
	}
	scopeMap.clear();
	innerMap.remove(scopeKey);
    }
}
} // end of class ScopeImpl

} // end of class ObjectTableImpl
