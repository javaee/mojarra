/*
 * $Id: NullValueHolder.java,v 1.2 2004/01/27 20:13:42 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Interface denoting a configuration bean that stores a
 * <code>nullValue</code> property.
 */

public interface NullValueHolder {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return a flag indicating that the value of the parent
     * compoennt should be set to <code>null</code>.</p>
     */
    public boolean isNullValue();


    /**
     * <p>Set a flag indicating that the value of the parent
     * component should be set to <code>null</code>.</p>
     *
     * @param nullValue The new null value flag
     */
    public void setNullValue(boolean nullValue);


}
