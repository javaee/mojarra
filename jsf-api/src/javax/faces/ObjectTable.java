/*
 * $Id: ObjectTable.java,v 1.9 2001/12/02 01:00:59 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved
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
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *

 *  <B>ObjectTable</B> is a class ... <P>

 * <P>This is a very early implementation, based on Hans Muller's
 * pre-expert-group-posting proposal.</P>

<P>Here is how this implementation differs from the proposal:</P>

	<UL>

	  <LI><P><B>Usage pattern</B> I feel strongly that the usage
	  pattern for ObjectTable should be like this: There is one
	  instance of ObjectTable per VM (Hans's proposal has no
	  instance of ObjectTable, all methods and data are static).
	  The ObjectTable client obtains the singleton instance from
	  somewhere, currently RenderContext.getObjectTable().  The
	  ObjectTable client then uses that instance for all its
	  ObjectTable needs. </P>

	  <P>This approach has pros(+) and cons(-):</P>

<PRE>

+ More amenable to future changes in implementation, by allowing the
  separation of interface and implementation.

+ Allows other Faces implementors the flexability to implement their own
  ObjectTable, or use the default one.

+ Client code using this approach can easily be switched to Hans's
  approach.

- Slightly Increased complexity and slightly decreased ease of use.

</PRE>

          </LI>

	  <LI><P><B>Static vs. Non-Static Methods and Data</B> Hans's
  proposal has all ObjectTable methods static. I've moved them to be
  non-static.</P></LI>

	  <LI><P>I have made Scope be an interface, not a class.
	  </P></LI>

	</UL>

<P>This implementation will certainly change as we haggle out the
  details.</P>


 *
 * <B>Lifetime And Scope</B> <P>There is one instance of ObjectTable per
  VM.  Clients obtain a reference to it by asking the RenderContext.</P>
 *
 * @version $Id: ObjectTable.java,v 1.9 2001/12/02 01:00:59 edburns Exp $
 * 
 * @see	javax.faces.RenderContext#getObjectTable
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

    private static ObjectTable instance = null;

/**
  *

  *  get the List of Scope instances and iterate over it until you find
  *  an instance for which the isA() method returns true for the
  *  argument object.


  * @see ObjectTable#get

  * @param scopeKey The instance for which a Scope should be found.
  * Usually something like an HttpServletRequest or HttpSession
  * instance, but could be any object.

  * @return	The Scope, if found, or null, if not found.
 
  */


private Scope keyToScope(Object scopeKey) {
    Scope result = null;
    Iterator iter;
    List scopes = getScopes();

    if (null == scopes) {
	return result;
    }

    iter = scopes.iterator();
    while (iter.hasNext()) {
	result = (Scope) iter.next();
	if (result.isA(scopeKey)) {
	    break;
	}
	else {
	    result = null;
	}
    }
    return result;
}

private Object getEnclosingScope(Object scopeKey) {
    Object result = null;

    // PENDING(edburns): I really don't like hard coding scope enclosure
    // logic, especially if it requires me to do instanceof.  There has
    // to be a better way.  Also, this logic makes it so newly added
    // scopes don't have the ability to define an enclosure hierarchy.

    if (scopeKey instanceof HttpServletRequest) {
	result = ((HttpServletRequest)scopeKey).getSession();
    }
    else if (scopeKey instanceof HttpSession) {
	// Object is the type of GlobalScope
	result = new Object();
    }
    
    return result;
}

    public void put(Object scopeKey, Object name, Object value) {
	Scope s = keyToScope(scopeKey);
	if (null != s) {
	    put(s, name, value);
	}
    }

    public void put(Scope scope, Object name, Object value) {
	scope.put(name, value);
    }


    public void put(Scope scope, Object name, Class value) {
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


    public Object get(Object scopeKey, Object name) {
	Scope scope = keyToScope(scopeKey);
	Object result = null;
	// this loop searches through scope instances for a hit for this
	// (scopeKey, name) pair.  It starts out with the above scope,
	// then calls getEnclosingScope() until either:

	// 1. A hit is found

	// 2. There are no more enclosing scopes.
	
	if (null != scope) {
	    do {
		result = scope.get(scopeKey, name);
		if (null == result) {
		    scopeKey = getEnclosingScope(scopeKey);
		    scope = null;
		    if (null != scopeKey) {
			scope = keyToScope(scopeKey);
		    }
		}
	    } while ((result == null) && (null != scope));
	}
	return result;
    }


    public Object get(Object name) {
	return GlobalScope.get("global", name);
    }

    public void enter(Object scopeKey) {
	Scope scope = keyToScope(scopeKey);
	scope.enter(scopeKey);
    }


    public void exit(Object scopeKey) {
	Scope scope = keyToScope(scopeKey);
	scope.exit(scopeKey);
    }

    // PENDING(edburns): Hans had these static, don't see why.

    public abstract List getScopes();
    public abstract void setScopes(List scopes);

    /**

    * PENDING(edburns): this is a temporary method to allow the static
    * ObjectTable.getInstance() method to work.  It is called by the
    * ObjectTableFactoryImpl. <P>

    * PRECONDITION: This method is being called from the ObjectTableImpl
    * ctor. <P>

    * POSTCONDITION: Subsequent calls to ObjectTable.getInstance() will
    * return this instance. <P>

    * @param: ot the ObjectTable instance to be set so subsequent calls
    * to ObjectTable.getInstance() return this instance. 

    * @see ObjectTable#getInstance

    */

    protected static void setInstance(ObjectTable ot) {
	instance = ot;
    }

    /**

    * PRECONDITION: ObjectTable.setObjectTable() has been called with
    * <B>the</B> ObjectTable instance. <P>

    * POSTCONDITION: No change in state. <P>
    
    * @result: the singleton ObjectTable instance.

    * @see ObjectTable#setInstance

    */

    public static ObjectTable getInstance() {
	return instance;
    }
}

				   

