/*
 * $Id: ListEntriesHolder.java,v 1.3 2004/02/04 23:46:07 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Interface denoting a configuration bean that stores a
 * <code>listEntries</code> property.
 */

public interface ListEntriesHolder {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the current <code>listEntries</code> object
     * for this configuration bean.</p>
     */
    public ListEntriesBean getListEntries();


    /**
     * <p>Set the current <code>listEntries</code> object
     * for this configuration bean.</p>
     *
     * @param listEntries The new <code>listEntries</code> object
     */
    public void setListEntries(ListEntriesBean listEntries);


}
