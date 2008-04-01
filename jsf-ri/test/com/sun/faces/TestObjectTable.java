/*
 * $Id: TestObjectTable.java,v 1.5 2001/12/01 02:30:54 edburns Exp $
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

// TestObjectTable.java

package com.sun.faces;

import junit.framework.TestCase;

import javax.servlet.*;

import junit.framework.*;
import org.apache.cactus.*;

import javax.faces.RenderKit;
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
 * @version $Id: TestObjectTable.java,v 1.5 2001/12/01 02:30:54 edburns Exp $
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
}

public void tearDown() {
    objectTable = null;
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
