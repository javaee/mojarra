/*
 * $Id: ObjectTable.java,v 1.2 2001/11/13 23:17:55 edburns Exp $
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

// ObjectTable.java

package javax.faces;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 *  <B>ObjectTable</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ObjectTable.java,v 1.2 2001/11/13 23:17:55 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public abstract class ObjectTable
{
    public interface ActiveValue {
        Object getValue(Scope scope, Object scopeKey, Object name);
    }


    public interface LazyValue {
        Object getValue(Scope scope, Object scopeKey, Object name);
    }


    public interface ManagedValue {
        void willRemove(Scope scope, Object scopeKey, Object name);
        void didRemove(Scope scope, Object scopeKey, Object name);
    }


    public static interface Scope {
        public boolean isA(Object scopeKey);

	public void put(Object name, Object value);

	public Object get(Object scopeKey, Object name);

	public void enter(Object scopeKey);

	public void exit(Object scopeKey);
    }


    public static Scope RequestScope = null;

    public static Scope SessionScope = null;

    public static Scope GlobalScope = null;

private static Scope keyToScope(Object scopeKey) {
    Scope result = null;
    // PENDING(edburns): can we use Scope.isA() here?
    if (scopeKey instanceof HttpServletRequest) {
	result = RequestScope;
    }
    else if (scopeKey instanceof HttpSession) {
	result = SessionScope;
    }
    else {
	result = GlobalScope;
    }
    return result;
}

    public static void put(Scope scope, Object name, Object value) {
	scope.put(name, value);
    }


    public static void put(Scope scope, Object name, Class value) {
	final Class finalValue = value;
	Object createValue = new LazyValue() {
	    public Object getValue(Scope scope, Object scopeKey, Object name) {
		Object result = null;
		try {
		    result = finalValue.newInstance();
		}
		catch (Exception e) {
		    System.out.println(e.getMessage());
		}
		return result;
	    }
        };
	scope.put(name, createValue);
    }


    public static Object get(Object scopeKey, Object name) {
	Scope scope = keyToScope(scopeKey);
	return scope.get(scopeKey, name);
    }


    public static Object get(Object name) {
	return GlobalScope.get("global", name);
    }

    // PENDING(edburns): why does ObjectTable itself have enter and exit?
    public static void enter(Object scopeKey) {
	Scope scope = keyToScope(scopeKey);
	scope.enter(scopeKey);
    }


    public static void exit(Object scopeKey) {
	Scope scope = keyToScope(scopeKey);
	scope.exit(scopeKey);
    }

    // PENDING(edburns): Hans had these static, don't see why.

    public abstract List getScopes();
    public abstract void setScopes(List scopes);
}

				   

