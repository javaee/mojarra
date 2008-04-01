/*
 * $Id: ObjectTableImpl.java,v 1.1 2001/11/13 02:11:12 edburns Exp $
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

/**
 *
 *  <B>ObjectTableImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ObjectTableImpl.java,v 1.1 2001/11/13 02:11:12 edburns Exp $
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

//
// Constructors and Initializers    
//

public ObjectTableImpl()
{
    super();
    RequestScope = new ScopeImpl();
    SessionScope = new ScopeImpl();
    GlobalScope = new ScopeImpl(); 
}

//
// Class methods
//

//
// Methods from ObjectTable
//


//
// Inner interfaces and classes
// 

public static class ScopeImpl implements ObjectTable.Scope {
    private Map putMap = new HashMap();  
    private Map keyMap = new HashMap();  
    
//
// General Methods
//

/**

* @return true if the given scope is active

*/

private boolean inScope(Object scopeKey) {
    boolean result = false;
    result = (null != keyMap.get(scopeKey));
    return result;
}

//
// Methods from Scope
//

public boolean isA(Object scopeKey) {
    return false;
}

public void put(Object name, Object value) {
    // PENDING(edburns): what is the proper place for this value?
    putMap.put(name, value);
}

public Object get(Object scopeKey, Object name) {
    // lazily enter the scope if neccessary
    if (!inScope(scopeKey)) {
	enter(scopeKey);
    }

    Object result = ((Map)keyMap.get(scopeKey)).get(name);
    ActiveValue av;
    LazyValue lv;
    // Deal with Active and Lazy values
    if (result instanceof ActiveValue) {
	av = (ActiveValue) result;
	result = av.getValue(this, scopeKey, name);
    }
    else if (result instanceof LazyValue) {
	lv = (LazyValue) result;
	result = lv.getValue(this, scopeKey, name);
    }

    return result;
}

public void enter(Object scopeKey) {
    // PENDING(edburns): I don't think the second arg should be putMap
    keyMap.put(scopeKey, putMap);
    // Call listeners
}
    
public void exit(Object scopeKey) {
    // Call listeners
    // keyMap.remove(scopeKey);
}
} // end of class ScopeImpl

// ----VERTIGO_TEST_START

//
// Test methods
//


public static void simpleTest(ObjectTable objectTable) {
    String fooName = "fooStr";
    objectTable.put(ObjectTable.GlobalScope, fooName, StringBuffer.class);
    StringBuffer foo1 = (StringBuffer)(ObjectTable.get(fooName));
    foo1.append("foo1");
    System.out.println("For name " + fooName + " in GlobalScope, value is : " +
		       foo1);
    StringBuffer foo2 = (StringBuffer)(ObjectTable.get(fooName));
    foo2.append("foo2");
    System.out.println("For name " + fooName + " in GlobalScope, value is : " +
		       foo2);
}
    
public static void main(String [] args)
{
    Assert.setEnabled(true);
    ObjectTable me = new ObjectTableImpl();
    Log.setApplicationName("ObjectTableImpl");
    Log.setApplicationVersion("0.0");
    Log.setApplicationVersionDate("$Id: ObjectTableImpl.java,v 1.1 2001/11/13 02:11:12 edburns Exp $");

    simpleTest(me);
    
}

// ----VERTIGO_TEST_END

} // end of class ObjectTableImpl
