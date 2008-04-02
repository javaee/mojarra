/*
 * $Id: MapEntriesHolder.java,v 1.2 2004/01/27 20:13:41 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Interface denoting a configuration bean that stores a
 * <code>mapEntries</code> property.
 */

public interface MapEntriesHolder {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the current <code>mapEntries</code> object
     * for this configuration bean.</p>
     */
    public MapEntriesBean getMapEntries();


    /**
     * <p>Set the current <code>mapEntries</code> object
     * for this configuration bean.</p>
     *
     * @param mapEntries The new <code>mapEntries</code> object
     */
    public void setMapEntries(MapEntriesBean mapEntries);


}
