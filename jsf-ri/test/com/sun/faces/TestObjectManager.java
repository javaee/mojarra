/*
 * $Id: TestObjectManager.java,v 1.3 2002/01/18 21:52:31 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestObjectManager.java

package com.sun.faces;

import javax.servlet.http.HttpSession;

import javax.faces.ObjectManager;
import javax.faces.ObjectManager.Scope;
import javax.faces.ObjectManager.ScopeListener;
import javax.faces.ObjectManager.ActiveValue;
import javax.faces.ObjectManager.LazyValue;

import com.sun.faces.ObjectManagerImpl.ScopeImpl;

import java.util.List;
import java.util.Iterator;

/**
 *
 *  <B>TestObjectManager</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestObjectManager.java,v 1.3 2002/01/18 21:52:31 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestObjectManager extends FacesTestCase
{
//
// Protected Constants
//

//
// Class Variables
//

private static final int INITIAL_NUM_SCOPES = 3;

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestObjectManager() {super("TestObjectManager");}
    public TestObjectManager(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

public void testScope() {
    boolean result;
    int i;
    
    // create a String scope
    Scope stringScope = new ScopeImpl(String.class);
    String str = "String";
    result = stringScope.isA(str);

    // test the isA method
    System.out.println("Testing Scope.isA(), returned: " + result);
    assertTrue(result);

    // test the getScopes method
    List scopes;
    java.util.Iterator iter;
    scopes = objectManager.getScopes();
    iter = scopes.iterator();
    i = 0;
    while (iter.hasNext()) {
	i++;
	iter.next();
    }

    // There should be INITIAL_NUM_SCOPES scopes by default
    result = INITIAL_NUM_SCOPES == i;
    System.out.println("Testing ObjectManager has correct number of initial scopes,: " +
		       INITIAL_NUM_SCOPES + " " + result);

    assertTrue(result);

    // test the setScopes method
    scopes.add(stringScope);
    objectManager.setScopes(scopes);
    assertTrue(true);
    System.out.println("Testing Adding scope: " + stringScope);

    scopes = objectManager.getScopes() ;
    iter = scopes.iterator();
    i = 0;
    while (iter.hasNext()) {
	i++;
	iter.next();
    }
    // There should be four now
    
    result = (INITIAL_NUM_SCOPES + 1) == i;
    System.out.println("Testing ObjectManager has correct number of scopes after adding one: " 
		       + result);
    assertTrue(result);
}

public void testPutGet() {
    String fooName = "fooStr";
    String get1;
    boolean result;

    objectManager.bind(objectManager.GlobalScope, fooName, StringBuffer.class);
    System.out.println("Testing put: true");
    assertTrue(true);

    StringBuffer foo1 = (StringBuffer)(objectManager.get(fooName));
    result = foo1 != null;
    System.out.println("Testing get: " + result);
    assertTrue(result);

    foo1.append("foo1");
    StringBuffer foo2 = (StringBuffer)(objectManager.get(fooName));
    foo2.append("foo2");
    get1 = foo2.toString();
    result = get1.equals("foo1foo2");
    System.out.println("Testing second get on same key returns same instance" +
		       "\n, proving correctness of lazyValue: " + result);
    assertTrue(result);

    // test extensible scope mechanism
    Scope stringScope = new ScopeImpl(String.class);
    List scopes = objectManager.getScopes();
    scopes.add(stringScope);
    objectManager.setScopes(scopes);
    objectManager.bind(stringScope, fooName, StringBuffer.class);
    foo1 = (StringBuffer) objectManager.get("string", fooName);
    foo1.append("foo1");
    get1 = foo1.toString();
    result = get1.equals("foo1");
    System.out.println("Testing extensible scope mechanism: " + result);
    assertTrue(result);

    objectManager.bind(stringScope, "foo2", StringBuffer.class);
    foo1 = (StringBuffer) objectManager.get("string", fooName);
    foo1.append("foo1");
    get1 = foo1.toString();
    result = get1.equals("foo1foo1");

    System.out.println("Testing the put with scopeKey as first arg" + result);
    assertTrue(result);
}

public void testNarrowToBroad() {
    System.out.println("testing narrow-to-broad search");

    HttpSession session = request.getSession();
    String value = "stringInstance";
    String name = "putInSession";
    Object getResult;
    boolean result = false;
    
    // Test that putting something in a request's session is correctly
    // obtainable from the request.
    objectManager.put(session, name, value);
    getResult = objectManager.get(request, name);
    result = getResult == value;
    System.out.println("put under a request's session accessible from a get on that request: " + result);
    assertTrue(result);

    // Test that putting something under Global is accessible from a
    // request
    name = "putInGlobal";
    objectManager.put(config.getServletContext(), name, value);
    getResult = objectManager.get(request, name);
    result = getResult == value;
    System.out.println("put under Global accessible from a get on that request: " + result);
    assertTrue(result);

    // Test that putting something under Global is accessible from a
    // session
    getResult = objectManager.get(session, name);
    result = getResult == value;
    System.out.println("put under Global accessible from a get on session: " + result);
    assertTrue(result);

    // Test that putting something two different objects under the same
    // name, one in request and one in the request's session, the
    // request stored entry is retrieved.
    name = "sameName";
    String value2 = "another value";
    objectManager.put(session, name, value);
    objectManager.put(request, name, value2);
    getResult = objectManager.get(request, name);
    result = getResult != value;
    System.out.println("put under same name in request and session; get under request returns request's put: " + result);
    assertTrue(result);

    // test that get on the session, return's the session's put
    getResult = objectManager.get(session, name);
    result = getResult == value;
    System.out.println("test that get on the session, return's the session's put: " + result);
    assertTrue(result);

    // test that global get on a non-existant object returns null
    getResult = objectManager.get("non-existant");
    result = null == getResult;
    System.out.println("test that global get on a non-existant object returns null: " + result);
    assertTrue(result);
}

public void testLazyActive() {
    boolean result = false;
    Object one, two;
    String lazy = "lazy";
    String active = "active";

    System.out.println("test convenience method: putting .class ");
    objectManager.bind(ObjectManager.GlobalScope, lazy, StringBuffer.class);
    
    one = objectManager.get(lazy);
    two = objectManager.get(lazy);
    result = one == two;
    System.out.println("result: " + result);
    assertTrue(result);

    result = false; 
    System.out.println("test putting LazyValue directly ");
    objectManager.bind(ObjectManager.GlobalScope, lazy, 
		    new LazyValue() {
			public Object getValue(Scope scope, Object scopeKey, Object name) {
			    Object result = null;
			    try {
				result = StringBuffer.class.newInstance();
			    }
			    catch (Exception e) {
				System.out.println(e.getMessage());
			    }
			    return result;
			}
		    });
    
    one = objectManager.get(lazy);
    two = objectManager.get(lazy);
    result = one == two;
    System.out.println("result: " + result);
    assertTrue(result);

    result = false;
    System.out.println("test putting ActiveValue directly ");
    objectManager.bind(ObjectManager.GlobalScope, active, 
		    new ActiveValue() {
			public Object getValue(Scope scope, Object scopeKey, Object name) {
			    Object result = null;
			    try {
				result = StringBuffer.class.newInstance();
			    }
			    catch (Exception e) {
				System.out.println(e.getMessage());
			    }
			    return result;
			}
		    });
    
    one = objectManager.get(active);
    two = objectManager.get(active);
    result = one != two;
    System.out.println("result: " + result);
    assertTrue(result);
}

public void testScopeListener() {
    ParamBlockingRequestWrapper wrapped = 
	new ParamBlockingRequestWrapper(request);
    String empty = "empty";
    final String didEnter = "didEnter";
    final String didExit = "didExit";
    System.setProperty(didEnter, empty);
    System.setProperty(didExit, empty);

    ScopeListener listener = new ScopeListener() {
	    public void willEnter(Scope scope, Object scopeKey) {
		System.out.println("Got Enter: " + scope + " " + scopeKey);
		System.setProperty(didEnter, didEnter);
	    }
	    public void willExit(Scope scope, Object scopeKey) {
		System.out.println("Got Exit: " + scope + " " + scopeKey);
		System.setProperty(didExit, didExit);
	    }
	};
    objectManager.RequestScope.addScopeListener(listener);
    objectManager.put(wrapped, didEnter, didEnter);
    
    try {
	System.out.println("Testing doFilter:");
	filter.doFilter(wrapped, response, filterChain);
	assertTrue(true);
	assertTrue(empty != System.getProperty(didEnter));
	assertTrue(empty != System.getProperty(didExit));
    }
    catch(Exception e) {
	System.out.println("testEnterExit: caught exception");
	assertTrue(false);
    }
}

} // end of class TestObjectManager
