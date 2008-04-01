/*
 * $Id: SessionListener.java,v 1.1 2001/12/05 20:29:59 edburns Exp $
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

import javax.faces.ObjectTable;
import javax.faces.Constants;

/**
 *
 *  <B>SessionListener</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SessionListener.java,v 1.1 2001/12/05 20:29:59 edburns Exp $
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


public void sessionCreated(HttpSessionEvent sce) {
    ObjectTable ot = ObjectTable.getInstance();
    Assert.assert_it(null != ot);

    sce.getSession().setAttribute(Constants.REF_SESSIONINSTANCE,
				  sce.getSession().getId());
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

    // exit the scope for this session
    ot.exit(sce.getSession());
}

} // end of class SessionListener
