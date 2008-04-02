/*
 * $Id: StoreServletContext.java,v 1.1 2004/05/07 13:53:23 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.config;

import javax.servlet.ServletContext;

/**
 * <p>The purpose of this class is to call the package private
 * getThreadLocalServletContext() method to set the ServletContext into
 * ThreadLocalStorage, if IS_UNIT_TEST_MODE == true.</p>
 */

public class StoreServletContext extends Object {
    public void setServletContext(ServletContext sc) {
	ConfigureListener.getThreadLocalServletContext().set(sc);
    }
}
