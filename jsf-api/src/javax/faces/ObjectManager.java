/*
 * $Id: ObjectManager.java,v 1.2 2002/01/18 21:51:34 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ObjectManager.java

package javax.faces;

import java.util.List;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

/**

Web server applications must manage large collections of name to
object bindings whose scope and lifetime depend on how they are used.
For example, one might have a different binding of "account" for each
user session, and a single binding of "bank" for the entire
application.  The lifetime of the binding depends on it's scope.  For
example a session specific binding of "account" can be cleared when
the session has been terminated.  Similarly, such a name to object
mapping only needs to be created each time a new session is entered.

<p>
This class supports storing and retrieving dynmically computed objects
in a two-level, scope/name, table.  Uniquely named objects are stored
in the table in a <i>scope</i>, an instance of ObjectManager.Scope.
ObjectManager.Scope objects represent name space types, like request and
session. Object lookup requires two keys: the name, and an object that
defines a specific name space instance, like an HttpServletRequest
object or an HttpSession.  We use the term "bind" to refer to the
correspondence between a name and a factory that creates
mappings from that name to a scope specific value.


<p>
A single statically scoped instance of the table is created by this
class.  A typical application will get this instance, with
ObjectManager.getInstance(), each time it needs to store or retrieve
an object.

<p>
Here's an example:

<pre>
ObjectManager table = ObjectManager.getInstance();
table.bind(ObjectManager.Request, "foo", Foo.class);
Foo foo1 = (Foo)(table.get(httpRequest1, "foo"));
Foo foo2 = (Foo)(table.get(httpRequest2, "foo"));
</pre>

<p>
In this example a new Foo instance is created by the ObjectManager get
method for each unique ServletRequest object.  We'll refer to the
HttpRequest as a "scope key".  It defines a specific instance of the
request scope.  The value returned by ObjectManager.get is created
lazily, at lookup time, with Foo's nullary constructor.

<p>
The ObjectManager bind method defines a unique mapping from a name to
an object for each instance of a particular scope.  In the example
we've defined a mapping from "foo" to a newly constructed instance of
Foo for each unique ServletRequest.  The table.bind() statement in the
example above is equivalent to:

<pre>
Object createFoo = new ObjectManager.LazyValue() {
    public Object getValue(Scope scope, Object scopeKey, Object name) {
        return new Foo();
    }
};
ObjectManager table = ObjectManager.getInstance();
table.bind(ObjectManager.Request, "foo", createFoo);
</pre>


<p>
Applications can check to see if there's a binding for a particular
name/scope, without triggering the construction of the binding's
value, with the isBound() method.  In the example below, the call
to isBound() does not cause an instance of Foo.class to be constructed:

<pre>
ObjectManager table = ObjectManager.getInstance();
table.bind(ObjectManager.Request, "foo", Foo.class);
table.isBound(myHttpRequest, "foo"); // => true
</pre>


<p>
ObjectManager contains two interfaces that can be used to define how the
value for a table entry is constructed: LazyValue and ActiveValue.
The first time an instance of LazyValue is retrieved for a specific
scope key, it's getValue method is used to create the object.
An ActiveValue constructs its object each time it's retrieved.


<p>
ObjectManager defines the following scopes:

<ul>
<li>
ObjectManager.Global - defines permanent name to object mappings with
the same scope as the Java VM itself.  A scopeKey is not required to
look up a global binding, so one can use and one argument overload of
ObjectManager.get() to lookup a global value.

<li>
ObjectManager.Session - defines name to object mappings per
HttpSession.  To retrieve a session scoped object, the scopeKey must
be an instance of HttpSession.  If there isn't a session binding for a
given name, then the binding of the name in the Global scope is
returned.

<li>
ObjectManager.Request - defines name to object mappings per
ServletRequest.  To retrieve a request scoped object, the scopeKey
must an instance of ServletRequest.  If there isn't a request binding
for a given name and the request is associated with a session,
i.e. the request is an instance of HttpServletRequest and the value of
HttpServletRequest.getSession(false) is non-null, then the binding of
the name in the ObjectManager.Session scope is returned.
</ul>

<p>
New scopes can be defined by creating an instance of Scope and
adding it to the ObjectManager's list of scopes.  Scopes override
Scope.isA() so that it returns true for Objects that will serve as
scope keys, and they may override Scope.put() and Scope.get() to
define how mappings are stored and if this scope delegates lookup to
another scope.  Here's an example of a scope that corresponds to a
single JSP page.  A PageContext object is used as the scope key.

<pre>
Scope myPageScope = new ObjectManager.Scope {
    public boolean isA(Object scopeKey) {
        return scopeKey instanceof PageContext;
    }
};
ObjectManager.setScopes(ObjectManager.getScopes().add(myPageScope));
</pre>

<p>
Applications may need to synchronize allocation or cleanup work with
scope entry and exit.  For example an application might want to
eagerly create mappings when a scope is entered, or dispose of related
resources when a scope is exited.  By default, exiting a scope causes
all of the mappings created for the corresponding scope key to be
removed.  Applications can use scope listeners to add their own
enter/exit behavior.  For example:

<pre>
ObjectManager.ScopeListener listener = new ObjectManager.ScopeListener() {
    public void willEnter(Scope scope, Object scopeKey) {
        scope.put(scopeKey, "eager1", eager1);
        scope.put(scopeKey, "eager2", eager2);
    }
    public void willExit(Scope scope, Object scopeKey) {
        myApp.cleanup(scopeKey);
    }
};
ObjectManager.Request.addScopeListener(listener);
</pre>

<p>
In the previous example the listener creates two mappings for eagerly
created objects "eager1" and "eager2" when the scope is entered.  When
the scope defined by scopeKey is exited, all mappings, including the
ones created by the listeners willEnter method are removed by the Scope
implementation - after the listener's willExit method has run.  In this
example, the listener's willExit method calls an application cleanup
method when the scope is exited.

<p>
Some applications prefer to have individual objects notified directly
when their mapping is removed from a scope.  Objects that implement
the ObjectManager.ManagedValue interface are notified before and after
they're removed.  The notification methods are willRemove and
didRemove.  Here's an example:

<pre>
class MyBankAccount implements ObjectManager.ManagedValue {
    // ...
    public void willRemove(Scope scope, Object scopeKey, Object name) {
        logAccountState();
    }
    public void didRemove(Scope scope, Object scopeKey, Object name) {
        resetAccountState();
    }
}
</pre>

<p>
In the example above, our bank account object's willRemove method
writes a log file entry that summarizes the state of the account.
Note that this method could safely lookup other objects in the same
scope - all of the ManagedValue object's willRemove methods are run
before any of their bindings are cleared.  The didRemove method
resets the state of the MyBankAccount object, perhaps by resetting
the object's private fields.  This is safe because the didRemove
method is called when the object can no longer be found in this
scope.

*/

public abstract class ObjectManager
{

//
// Relationship Class Variables
//

    /**
     * Defines mappings per ServletRequest.  If there isn't a request binding
     * for a given name and the request is associated with a session,
     * i.e. the request is an instance of HttpServletRequest and the value of
     * HttpServletRequest.getSession(false) is non-null, then the binding of
     * the name in the ObjectManager.Session scope is returned.
     */
    public static Scope RequestScope = null;

    /**
     * Defines mappings per HttpSession.  To retrieve a session
     * scoped object, the scopeKey must be an instance of HttpSession.
     * If there isn't a session binding for a given name, then the
     * binding of the name in the Global scope is returned.
     */
    public static Scope SessionScope = null;

    /**
     * Defines permanent mappings with the same scope as the
     * Java VM itself.  A scopeKey is not required to look up a global
     * binding, so one can use and one argument overload of
     * ObjectManager.get() to lookup a global value.
     */
    public static Scope GlobalScope = null;

    private static ObjectManager instance = null;

    protected ServletContext servletContext;

// 
// Private helper methods
// 

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

    protected Scope keyToScope(Object scopeKey) {
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
    
    private Object getEnclosingScopeKey(Object scopeKey) {
	Object result = null;
	
	// PENDING(edburns): I really don't like hard coding scope enclosure
	// logic, especially if it requires me to do instanceof.  There has
	// to be a better way.  Also, this logic makes it so newly added
	// scopes don't have the ability to define an enclosure hierarchy.
	
	if (scopeKey instanceof HttpServletRequest) {
	    result = ((HttpServletRequest)scopeKey).getSession();
	}
	else if (scopeKey instanceof HttpSession) {
	    result = servletContext;
	}
	
	return result;
    }

//
// Public methods
//
    
    /**
     * Defines a binding for name.  For each unique instance of
     * scope, the value that name maps to is computed once
     * with the specified LazyValue object.
     */
    public void bind(Scope scope, Object name, LazyValue value) {
	scope.bind(name, value);
    }

    /**
     * Defines a binding for name.  For each unique instance of
     * scope, the value that name maps to is computed each
     * time it's retrieved with the specified ActiveValue object.
     */
    public void bind(Scope scope, Object name, ActiveValue value) {
	scope.bind(name, value);
    }

    /**
     * A convenience method that generates a binding to a LazyValue
     * that computes an instance, using the nullary constructor,
     * of the specified class.
     */
    public final void bind(Scope scope, Object name, Class value) {
	final Class finalValue = value;
	LazyValue createValue = new LazyValue() {
		public Object getValue(Scope scope, Object scopeKey, 
				       Object name) {
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
	scope.bind(name, createValue);
    }

    /**
     * Defines a single name to value mapping in the scope that
     * corresponds to scopeKey.  The effect of calling this method
     * is comparable to scope.put(scopeKey, name, value) where scope
     * is the first scope in the list returned by getScopes() where
     * scope.isA(scopeKey) returns true.
     */
    public void put(Object scopeKey, Object name, Object value) {
	Scope scope = keyToScope(scopeKey);
	if (null != scope) {
	    scope.put(scopeKey, name, value);
	}
    }

    /**
     * Returns value of name in the scope that corresponds to scopeKey,
     * or null if no mapping exists or scopeKey doesn't match any of the
     * scopes in the list returned by getScopes().  The effect of
     * calling this method is comparable to scope.get(scopeKey, name)
     * where scope is the first scope in the list returned by
     * getScopes() where scope.isA(scopeKey) returns true.  This search
     * runs narrow-to-broad.  Search order is Request->Session->Global.
     */
    public Object get(Object scopeKey, Object name) {
	Scope scope = keyToScope(scopeKey);
	Object result = null;
	// this loop searches through scope instances for a hit for this
	// (scopeKey, name) pair.  It starts out with the above scope,
	// then calls getEnclosingScopeKey() until either:

	// 1. A hit is found

	// 2. There are no more enclosing scopes.
	
	if (null != scope) {
	    do {
		result = scope.get(scopeKey, name);
		if (null == result) {
		    scopeKey = getEnclosingScopeKey(scopeKey);
		    scope = null;
		    if (null != scopeKey) {
			scope = keyToScope(scopeKey);
		    }
		}
	    } while ((result == null) && (null != scope));
	}
	return result;
    }

    /**
     * Returns the value of name in the ObjectManager.Global scope.
     */
    public final Object get(Object name) {
	return GlobalScope.get(servletContext, name);
    }


    /**
     * Returns all of the known scopes.  By default the list contains
     * ObjectManager.Request, ObjectManager.Session,
     * ObjectManager.Global.
     */
    public List getScopes() {
        return null;
    }


    /**
     * Replace the list of known scopes.  This method is typically
     * used to extend the list, e.g.
     * <pre>
     * ObjectManager.setScopes(ObjectManager.getScopes().add(myScope));
     * </pre>
     */
    public void setScopes(List scopes) {
    }


    /**
     * Returns the single shared instance of ObjectManager.
     */
    public static ObjectManager getInstance() {
	return instance;
    }

    /**
     * Replace the single shared instance of ObjectManager.
     */
    public static void setInstance(ObjectManager om) {
	instance = om;
    }

    /**
     * This interface is used to create ObjectManager bindings that
     * compute their value at ObjectManager.get() time.  If an object
     * that implements this interface is stored with ObjectManager.bind(),
     * then each time the value of the binding is retrieved,
     * the result of calling object.getValue() will be returned
     * instead of the ActiveValue object itself.
     */
    public interface ActiveValue {
        /**
         * Returns the value of the binding of name in the specified
         * scope. The value of scope.isA(scopeKey) is guaranteed to be
         * true.
         */
        Object getValue(Scope scope, Object scopeKey, Object name);
    }

    /**
     * This interface is used to create ObjectManager bindings that
     * compute their value the first time ObjectManager.get() is called.
     * If an object that implements this interface is stored with
     * ObjectManager.bind(), then the the first time the value of the
     * binding is retrieved, the object will be replaced by the
     * value returned by object.getValue().
     */
    public interface LazyValue {
        /**
         * Returns the value of the binding of name in the specified
         * scope.  The value of scope.isA(scopeKey) is guaranteed to be
         * true.
         */
        Object getValue(Scope scope, Object scopeKey, Object name);
    }


    /**
     * This interface is used to create objects that wish to be
     * notified when their ObjectManager mapping is removed.
     * If an object that implements this interface is put into
     * the ObjectManager, then it's willRemove method will be called
     * before any of the mapping for objects in the same scope
     * are removed.  The didRemove method will be called after
     * all of the mappings have been removed.
     */
    public interface ManagedValue {
        /**
         * Called before the mapping for this object, or any of
         * the mapping for other objects in this scope, is removed.
         * The value of scope.isA(scopeKey) is guaranteed to be
         * true.
         */
        void willRemove(Scope scope, Object scopeKey, Object name);

        /**
         * Called after the mapping for this object, and all of
         * the mapping for other objects in this scope, is removed.
         * The value of scope.isA(scopeKey) is guaranteed to be
         * true.
         */
        void didRemove(Scope scope, Object scopeKey, Object name);
    }


   /**
    * This interface is used to create objects that will be notified
    * when a Scope will be entered or exited.
    *
    * @see addListener
    * @see removeListener
    */
    public interface ScopeListener
    {
       /**
        * This method is called the first time the scope defined
        * by scopeKey is entered.  Scopes are typically entered
        * lazily, as a consequence of calling ObjectManager.get().
        * This method is called before any mappings are retrieved,
        * so it can be used to seed a scope with a set of mappings
        * by calling ObjectManager.put().
        */
        public void willEnter(Scope scope, Object scopeKey);

        /**
         * This method is called when the scope defined by scopeKey
         * is exited and before all of the mappings defined for
         * this scope are (automatically) removed.
         */
        public void willExit(Scope scope, Object scopeKey);
    } // end of class ScopeListener


    /**
     * The base class for defining the scope of a name to object
     * binding.  Singleton instances of Scope subclasses are used
     * to define the way scoped bindings are stored and retrieved.
     */
    public static class Scope {

        /**
         * Return true if the specified object defines a specific
         * scope of this type.
         */
        public boolean isA(Object scopeKey) {
            return false;
        }

        /**
         * Defines a factory for mappings of "name" in this Scope.
         * The first time name is retrieved for a given scopeKey,
         * with Scope.get(), its value is computed with the
         * LazyValue and stored.
         *
         */
        public void bind(Object name, LazyValue value) {
        }

        /**
         * Defines a factory for mappings of "name" in this Scope.
         * Each time name is retrieved for a given scopeKey,
         * with Scope.get(), its value is computed by the
         * ActiveValue and returned.
         *
         */
        public void bind(Object name, ActiveValue value) {
        }

        /**

	* Put a single instance of object Value in this scope under the
	* key "name"

	*/

        public void bind(Object name, Object value) {
	}

        /**
         * Add the specified name to value mapping to the single
         * instance of this scope associated with scopeKey.
         */
        public void put(Object scopeKey, Object name, Object value) {
        }


        /**
         * Return the value of the binding for name in the map
         * associated with scopeKey.
         */
        public Object get(Object scopeKey, Object name) {
            return null;
        }

	public void enter(Object scopeKey) {
	}

	public void exit(Object scopeKey) {
	}

        /**
         * Adds a Listener to the listener list.  Scope listeners are
         * called each time a specific instance of this scope (defined
         * by a scopeKey) is entered or exited.
         *
         * @see #removeListener
         */
        public void addScopeListener(ScopeListener l) {
        }

        /**
         * Remove the specified Listener.
         *
         * @see #addListener
         */
        public void removeScopeListener(ScopeListener l) {
        }
    } // end of class Scope
}
