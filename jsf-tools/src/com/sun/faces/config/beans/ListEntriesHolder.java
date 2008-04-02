/*
 * $Id: ListEntriesHolder.java,v 1.2 2004/01/27 20:13:38 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
