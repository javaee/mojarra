/*
 * $Id: ObjectAccessor.java,v 1.2 2002/01/12 01:38:08 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import javax.servlet.ServletRequest;

/**
 * The interface for defining an object which can resolve an object-reference
 * String to an associated object instance.  This interface provides
 * an extensible mechanism for managing application-level objects which
 * should be decoupled from user-interface component objects. 
 * <p>  
 * User-interface components (subclasses of UIComponent) store the
 * object-reference String, rather than a direct reference to the object,
 * and use an ObjectAccessor instance to resolve the object-reference String to
 * an appropriate object when access to the object is required. 
 * <p>
 * The UIComponent classes obtain an ObjectAccessor instance from the
 * appropriate context object.
 *
 * @see EventContext#getObjectAccessor
 * @see RenderContext#getObjectAccessor 
 * 
 */ 
public interface ObjectAccessor {

    /**
     * Sets the object to be associated with the specified object-reference String
     * to the specified value object.
     * @param servletRequest the request object which defines the scope
     *        to be used when resolving the object-reference
     * @param objectReference the String containing the reference used to obtain
     *        the associated object
     * @param value the Object to be associated with the object-reference in
     *        the scope defined by request
     * @throws IllegalArgumentException if objectReference is null
     * @throws FacesException if the object cannot be set
     */
    void setObject(ServletRequest request, String objectReference, 
		   Object value) throws FacesException;

    /**
     * @param servletRequest request the request object which defines the scope
     *        to be used when resolving the object-reference
     * @param objectReference the String containing the reference used to obtain
     *        the associated object
     * @throws IllegalArgumentException if objectReference is null
     * @throws FacesException if the object cannot be set
     * @return the Object associated with the specified object-reference String.
     */
    Object getObject(ServletRequest request, 
		     String objectReference) throws FacesException;
}
