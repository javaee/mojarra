/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * %W% %G%
 */

package com.sun.faces.el.impl;

public class ElException extends Exception {

    Throwable t;

    public ElException() {
        super();
    }

    public ElException(String message) {
        super(message);
    }

    public ElException(String message, Throwable t) {
        super(message);
        this.t = t;
    }

    public ElException(Throwable t) {
        this.t = t;
    }

    public Throwable getCause() {
        return t;
    }
}