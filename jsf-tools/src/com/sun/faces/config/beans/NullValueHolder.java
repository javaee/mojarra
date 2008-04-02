/*
 * $Id: NullValueHolder.java,v 1.3 2004/02/04 23:46:09 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
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
