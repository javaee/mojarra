/*
 * $Id: ELConstants.java,v 1.1 2005/05/05 20:51:22 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sun.faces.el;

/**
 * @author jhook
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ELConstants {
    public static final int APPLICATION = 0;

    public static final int APPLICATION_SCOPE = 1;

    public static final int COOKIE = 2;

    public static final int FACES_CONTEXT = 3;

    public static final int HEADER = 4;

    public static final int HEADER_VALUES = 5;

    public static final int INIT_PARAM = 6;

    public static final int PARAM = 7;

    public static final int PARAM_VALUES = 8;

    public static final int REQUEST = 9;

    public static final int REQUEST_SCOPE = 10;

    public static final int SESSION = 11;

    public static final int SESSION_SCOPE = 12;

    public static final int VIEW = 13;
    
    public static final String[] IMPLICIT_OBJECTS = new String[] {
        "application", "applicationScope", "cookie", "facesContext",
	"header", "headerValues", "initParam", "param", "paramValues",
	"request", "requestScope", "session", "sessionScope", "view" };
}
