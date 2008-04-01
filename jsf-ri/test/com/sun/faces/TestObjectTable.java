/*
 * $Id: TestObjectTable.java,v 1.10 2001/12/20 22:26:43 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestObjectTable.java

package com.sun.faces;

import junit.framework.TestCase;

import javax.servlet.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import junit.framework.*;
import org.apache.cactus.*;

import javax.faces.RenderKit;
import javax.faces.Constants;
import javax.servlet.ServletRequest;
import javax.faces.RenderContextFactory;
import javax.faces.RenderContext;
import javax.faces.FacesException;
import javax.faces.ObjectTable;
import javax.faces.ObjectTableFactory;
import javax.faces.ObjectTable.Scope;
import javax.faces.ObjectTable.ActiveValue;
import javax.faces.ObjectTable.LazyValue;

import com.sun.faces.ObjectTableImpl.ScopeImpl;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 *
 *  <B>TestObjectTable</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestObjectTable.java,v 1.10 2001/12/20 22:26:43 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestObjectTable extends ServletTestCase
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

private ObjectTable objectTable = null;

//
// Constructors and Initializers    
//

    public TestObjectTable() {super("TestObjectTable");}
    public TestObjectTable(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

public void setUp() {
    ObjectTableFactory otf = ObjectTableFactory.newInstance();

    try {
	otf = ObjectTableFactory.newInstance();
	otf.newObjectTable();
	objectTable = ObjectTable.getInstance();
    } catch (Exception e) {
    }
    request.setAttribute(Constants.REF_REQUESTINSTANCE, request);
    HttpSession session = request.getSession();

    session.setAttribute(Constants.REF_SESSIONINSTANCE, session.getId());
    session.getServletContext().setAttribute(session.getId(), session.getId());
}

public void tearDown() {
    objectTable = null;
    session.removeAttribute(Constants.REF_SESSIONINSTANCE);
    session.getServletContext().removeAttribute(session.getId());
}

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
    scopes = objectTable.getScopes();
    iter = scopes.iterator();
    i = 0;
    while (iter.hasNext()) {
	i++;
	iter.next();
    }

    // There should be INITIAL_NUM_SCOPES scopes by default
    result = INITIAL_NUM_SCOPES == i;
    System.out.println("Testing ObjectTable has correct number of initial scopes,: " +
		       INITIAL_NUM_SCOPES + " " + result);

    assertTrue(result);

    // test the setScopes method
    scopes.add(stringScope);
    objectTable.setScopes(scopes);
    assertTrue(true);
    System.out.println("Testing Adding scope: " + stringScope);

    scopes = objectTable.getScopes() ;
    iter = scopes.iterator();
    i = 0;
    while (iter.hasNext()) {
	i++;
	iter.next();
    }
    // There should be four now
    
    result = (INITIAL_NUM_SCOPES + 1) == i;
    System.out.println("Testing ObjectTable has correct number of scopes after adding one: " 
		       + result);
    assertTrue(result);
}

public void testPutGet() {
    String fooName = "fooStr";
    String get1;
    boolean result;

    objectTable.put(objectTable.GlobalScope, fooName, StringBuffer.class);
    System.out.println("Testing put: true");
    assertTrue(true);

    StringBuffer foo1 = (StringBuffer)(objectTable.get(fooName));
    result = foo1 != null;
    System.out.println("Testing get: " + result);
    assertTrue(result);

    foo1.append("foo1");
    StringBuffer foo2 = (StringBuffer)(objectTable.get(fooName));
    foo2.append("foo2");
    get1 = foo2.toString();
    result = get1.equals("foo1foo2");
    System.out.println("Testing second get on same key returns same instance" +
		       "\n, proving correctness of lazyValue: " + result);
    assertTrue(result);

    // test extensible scope mechanism
    Scope stringScope = new ScopeImpl(String.class);
    List scopes = objectTable.getScopes();
    scopes.add(stringScope);
    objectTable.setScopes(scopes);
    objectTable.put(stringScope, fooName, StringBuffer.class);
    foo1 = (StringBuffer) objectTable.get("string", fooName);
    foo1.append("foo1");
    get1 = foo1.toString();
    result = get1.equals("foo1");
    System.out.println("Testing extensible string mechanism: " + result);
    assertTrue(result);

    objectTable.put("string", "foo2", StringBuffer.class);
    foo1 = (StringBuffer) objectTable.get("string", fooName);
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
    objectTable.put(session, name, value);
    getResult = objectTable.get(request, name);
    result = getResult == value;
    System.out.println("put under a request's session accessible from a get on that request: " + result);
    assertTrue(result);

    // Test that putting something under Global is accessible from a
    // request
    name = "putInGlobal";
    objectTable.put(ObjectTable.GlobalScope, name, value);
    getResult = objectTable.get(request, name);
    result = getResult == value;
    System.out.println("put under Global accessible from a get on that request: " + result);
    assertTrue(result);

    // Test that putting something under Global is accessible from a
    // session
    getResult = objectTable.get(session, name);
    result = getResult == value;
    System.out.println("put under Global accessible from a get on session: " + result);
    assertTrue(result);

    // Test that putting something two different objects under the same
    // name, one in request and one in the request's session, the
    // request stored entry is retrieved.
    name = "sameName";
    String value2 = "another value";
    objectTable.put(session, name, value);
    objectTable.put(request, name, value2);
    getResult = objectTable.get(request, name);
    result = getResult != value;
    System.out.println("put under same name in request and session; get under request returns request's put: " + result);
    assertTrue(result);

    // test that get on the session, return's the session's put
    getResult = objectTable.get(session, name);
    result = getResult == value;
    System.out.println("test that get on the session, return's the session's put: " + result);
    assertTrue(result);

    // test that global get on a non-existant object returns null
    getResult = objectTable.get("non-existant");
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
    objectTable.put(objectTable.GlobalScope, lazy, StringBuffer.class);
    
    one = objectTable.get(lazy);
    two = objectTable.get(lazy);
    result = one == two;
    System.out.println("result: " + result);
    assertTrue(result);

    result = false; 
    System.out.println("test putting LazyValue directly ");
    objectTable.put(objectTable.GlobalScope, lazy, 
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
    
    one = objectTable.get(lazy);
    two = objectTable.get(lazy);
    result = one == two;
    System.out.println("result: " + result);
    assertTrue(result);

    result = false;
    System.out.println("test putting ActiveValue directly ");
    objectTable.put(objectTable.GlobalScope, active, 
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
    
    one = objectTable.get(active);
    two = objectTable.get(active);
    result = one != two;
    System.out.println("result: " + result);
    assertTrue(result);
}

} // end of class TestObjectTable
