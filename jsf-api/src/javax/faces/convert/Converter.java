/*
 * $Id: Converter.java,v 1.2 2002/08/31 19:38:49 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.convert;


import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p><strong>Converter</strong> is an interface describing a Java class
 * that can perform Object-to-String and String-to-Object conversions
 * between model data objects and a String representation of those objects
 * that is suitable for rendering.  <code>Converter</code> instances are
 * shared, so they must be programmed in a thread-safe manner.</p>
 */

public interface Converter {


    /**
     * <p>Convert the specified string value, which is associated with
     * the specified {@link UIComponent}, into a model data object that
     * is appropriate for being stored during the <em>Update Model Values</em>
     * phase of the request processing lifecycle.</p>
     *
     * @param context {@link FacesContext} for the request being processed
     * @param component {@link UIComponent} with which this model object
     *  value is associated
     * @param value String value to be converted (may be null)
     *
     * @exception ConverterException if conversion cannot be successfully
     *  performed
     * @exception NullPointerException if <code>context</code> or
     *  <code>component</code> is null
     */
    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) throws ConverterException;



    /**
     * <p>Convert the specified model object value, which is associated with
     * the specified {@link UIComponent}, into a String that is suitable
     * for being included in the response generated during the
     * <em>Render Response</em> phase of the request processing
     * lifeycle.</p>
     *
     * @param context {@link FacesContext} for the request being processed
     * @param component {@link UIComponent} with which this model object
     *  value is associated
     * @param value Model object value to be converted (may be null)
     *
     * @exception ConverterException if conversion cannot be successfully
     *  performed
     * @exception NullPointerException if <code>context</code> or
     *  <code>component</code> is null
     */
    public String getAsString(FacesContext context, UIComponent component,
                              Object value) throws ConverterException;


}
