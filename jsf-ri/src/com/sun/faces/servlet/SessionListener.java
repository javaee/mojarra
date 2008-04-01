/*
 * $Id: SessionListener.java,v 1.2 2001/12/20 21:05:10 edburns Exp $
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

package com.sun.faces.servlet;

// SessionListener.java

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

import javax.faces.ObjectTable;
import javax.faces.Constants;

/**
 *
 *  <B>SessionListener</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SessionListener.java,v 1.2 2001/12/20 21:05:10 edburns Exp $
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

* @see com.sun.faces.ObjectTableImpl#fixScopeKey

*/

public void sessionCreated(HttpSessionEvent sce) {
    ObjectTable ot = ObjectTable.getInstance();
    Assert.assert_it(null != ot);
    HttpSession session = sce.getSession();
    String sessionId = session.getId();

    // The below code is because the ObjectTable cannot safely use an
    // actual HttpSession instance as a hash key, because the servlet
    // container doesn't guarantee the same actual Object instance
    // representing the Session will be given to us each time.  Rather,
    // the container can wrap the Session with another instance that
    // implements HttpSession.  This new instance will have a different
    // hashCode(), so entries in the ObjectTable that should be found
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

* POSTCONDITION: The ObjectTable's scope for this session, has been
* exited.

*/

public void sessionDestroyed(HttpSessionEvent sce) {
    ObjectTable ot = ObjectTable.getInstance();
    Assert.assert_it(null != ot);
    HttpSession session = sce.getSession();
    String sessionId = session.getId();
    
    // exit the scope for this session
    ot.exit(session);
    session.getServletContext().removeAttribute(sessionId);
}

} // end of class SessionListener
