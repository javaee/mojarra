/*
 * $Id: Util.java,v 1.5 2002/02/26 21:24:48 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Util.java

package com.sun.faces.util;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import javax.faces.Constants;

/**
 *
 *  <B>Util</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.5 2002/02/26 21:24:48 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Util extends Object
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
private static long id = 0;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

private Util()
{
    throw new IllegalStateException();
}

//
// Class methods
//
    /**
     * Save a new transaction token in the user's current session.
     * Taken from Struts, by Craig McClanahan. <P>

     * This is called from the FormRenderer.
     *
     * @param session our current session
     * @return the token just added
     */

    public static String saveToken(HttpSession session) {

        String token = generateToken(session);
        if (token != null)
            session.setAttribute(Constants.TRANSACTION_TOKEN_KEY_SESSION, 
				 token);
	return token;
    }

    /**
     * Generate a new identifier currently used to uniquely identify
     * components.
     */
    public static synchronized String generateId() {
        if (id == Long.MAX_VALUE) {
            id = 0;
        } else { 
            id++;
        }
        return Long.toHexString(id);
    }
    

    /**
     * Generate a new transaction token, to be used for enforcing a
     * single request for a particular transaction. Taken from Struts,
     * by Craig McClanahan.
     *
     * @param session our current session
     */
    public static String generateToken(HttpSession session) {

        try {
            byte id[] = session.getId().getBytes();
            byte now[] =
                new Long(System.currentTimeMillis()).toString().getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(id);
            md.update(now);
            return (toHex(md.digest()));
        } catch (IllegalStateException e) {
            return (null);
        } catch (NoSuchAlgorithmException e) {
            return (null);
        }

    }

    /**
     * Convert a byte array to a String of hexadecimal digits and return
     * it.  Taken from Struts, by Craig McClanahan.
     *
     * @param buffer The byte array to be converted
     */
    public static String toHex(byte buffer[]) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buffer.length; i++)
            sb.append(Integer.toHexString((int) buffer[i] & 0xff));
        return (sb.toString());

    }

    /**
     * Return <code>true</code> if there is a transaction token stored in
     * the user's current session, and the value submitted as a request
     * parameter with this action matches it.  Returns <code>false</code>
     * under any of the following circumstances:
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match the
     *     transaction token in the user's session</li>
     * </ul> <P>

     * Taken from Struts, by Craig McClanahan.
     *
     * @param request The servlet request we are processing
     */
    public static boolean isTokenValid(HttpServletRequest request) {

        // Retrieve the saved transaction token from our session
        HttpSession session = request.getSession(false);

        if (session == null)
            return (false);
        String saved = (String) session.getAttribute(Constants.TRANSACTION_TOKEN_KEY_SESSION);
        if (saved == null)
            return (false);

        // Retrieve the transaction token included in this request
        String token = (String) request.getParameter(Constants.REQUEST_TOKEN_KEY);
        if (token == null)
            return (false);

        // Do the values match?
        return (saved.equals(token));

    }

    /**
     * Reset the saved transaction token in the user's session.  This
     * indicates that transactional token checking will not be needed on
     * the next request that is submitted. Taken from Struts, by Craig
     * McClanahan.
     *
     * @param request The servlet request we are processing
     */
    public static void resetToken(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null)
            return;
        session.removeAttribute(Constants.TRANSACTION_TOKEN_KEY_SESSION);

    }


    public static boolean hasParameters(HttpServletRequest request) {
	boolean result = false;
	java.util.Enumeration param_names = request.getParameterNames();

	if (null != param_names) {
	    result = param_names.hasMoreElements();
	}
	return result;
    }

    public static Class loadClass(String name) throws ClassNotFoundException {
	ClassLoader loader =
	    Thread.currentThread().getContextClassLoader();
	if (loader == null) {
	    return Class.forName(name);
	}
	else {
	    return loader.loadClass(name);
	}
    }

//
// General Methods
//

// The testcase for this class is TestParamBlockingRequestWrapper.java 

} // end of class Util
