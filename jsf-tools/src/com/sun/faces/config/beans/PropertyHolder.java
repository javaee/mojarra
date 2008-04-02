/*
 * $Id: PropertyHolder.java,v 1.2 2004/01/27 20:13:43 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Interface denoting a configuration bean that stores a named collection
 * of properties.</p>
 */

public interface PropertyHolder {


    // ----------------------------------------------------------------- Methods


    /**
     * <p>Add the specified property descriptor, replacing any existing
     * descriptor for this property name.</p>
     *
     * @param descriptor Descriptor to be added
     */
    public void addProperty(PropertyBean descriptor);


    /**
     * <p>Return the property descriptor for the specified property name,
     * if any; otherwise, return <code>null</code>.</p>
     *
     * @param name Name of the property for which to retrieve a descriptor
     */
    public PropertyBean getProperty(String name);


    /**
     * <p>Return the descriptors of all properties for which descriptors have
     * been registered, or an empty array if none have been registered.</p>
     */
    public PropertyBean[] getProperties();


    /**
     * <p>Deregister the specified property descriptor, if it is registered.
     * </p>
     *
     * @param descriptor Descriptor to be removed
     */
    public void removeProperty(PropertyBean descriptor);


}
