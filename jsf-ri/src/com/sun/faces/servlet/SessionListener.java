/*
 * $Id: SessionListener.java,v 1.4 2002/01/10 22:20:10 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.servlet;

// SessionListener.java

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

import javax.faces.ObjectManager;
import javax.faces.Constants;

/**
 *
 *  <B>SessionListener</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SessionListener.java,v 1.4 2002/01/10 22:20:10 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SessionListener extends Object implements HttpSessionListener
{
//
// Protected Constants
//

//
// Class Variables
//

private static boolean didGlobalInit = false;

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public SessionListener()
{
    super();
    // ParameterCheck.nonNull();
    this.init();
}

protected void init()
{
    // super.init();
}

//
// Class methods
//

//
// General Methods
//

//
// Methods from HttpSessionListener
//


/**

* @see com.sun.faces.ObjectManagerImpl#fixScopeKey

*/

public void sessionCreated(HttpSessionEvent sce) {
    HttpSession session = sce.getSession();
    String sessionId = session.getId();

    // The below code is because the ObjectManager cannot safely use an
    // actual HttpSession instance as a hash key, because the servlet
    // container doesn't guarantee the same actual Object instance
    // representing the Session will be given to us each time.  Rather,
    // the container can wrap the Session with another instance that
    // implements HttpSession.  This new instance will have a different
    // hashCode(), so entries in the ObjectManager that should be found
    // will not be found.

    // Store it in the session's attr set so the case where the
    // container serializes and deserializes the attr set is correctly
    // handled.  This works because the session's attr set is safely
    // serialized.
    session.setAttribute(Constants.REF_SESSIONINSTANCE, sessionId);

    // Now, you may wonder why we're putting it in the ServletContext's
    // attr set, in addition to the Session's?  We must put it in the
    // ServletContext's attr set because when it comes time to exit this
    // session's scope (via an HttpSessionListener.sessionDestroyed()
    // call), the session's attr set isn't accessible, because the
    // session is already destroyed :(
    session.getServletContext().setAttribute(sessionId, sessionId);
}

/**

* This is where we take the session out of scope. <P>

* PRECONDITION: none <P>

* POSTCONDITION: The ObjectManager's scope for this session, has been
* exited.

*/

public void sessionDestroyed(HttpSessionEvent sce) {
    HttpSession session = sce.getSession();
    String sessionId = session.getId();

    ObjectManager objectManager;
    objectManager = (ObjectManager) session.getServletContext().
	getAttribute(Constants.REF_OBJECTMANAGER);
    Assert.assert_it(null != objectManager);
    
    // exit the scope for this session
    objectManager.exit(session);
    session.getServletContext().removeAttribute(sessionId);
}

} // end of class SessionListener
