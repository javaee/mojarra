/*
 * $Id: AttributeHolder.java,v 1.3 2004/02/04 23:46:06 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.beans;


/**
 * <p>Interface denoting a configuration bean that stores a named collection
 * of attributes.</p>
 */

public interface AttributeHolder {


    // ----------------------------------------------------------------- Methods


    /**
     * <p>Add the specified attribute descriptor, replacing any existing
     * descriptor for this attribute name.</p>
     *
     * @param descriptor Descriptor to be added
     */
    public void addAttribute(AttributeBean descriptor);


    /**
     * <p>Return the attribute descriptor for the specified attribute name,
     * if any; otherwise, return <code>null</code>.</p>
     *
     * @param name Name of the attribute for which to retrieve a descriptor
     */
    public AttributeBean getAttribute(String name);


    /**
     * <p>Return the descriptors of all attributes for which descriptors have
     * been registered, or an empty array if none have been registered.</p>
     */
    public AttributeBean[] getAttributes();


    /**
     * <p>Deregister the specified attribute descriptor, if it is registered.
     * </p>
     *
     * @param descriptor Descriptor to be removed
     */
    public void removeAttribute(AttributeBean descriptor);


}
