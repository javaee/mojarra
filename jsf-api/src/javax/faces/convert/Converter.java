/*
 * $Id: Converter.java,v 1.10 2004/02/04 23:38:04 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.convert;


import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p><strong>Converter</strong> is an interface describing a Java class
 * that can perform Object-to-String and String-to-Object conversions
 * between model data objects and a String representation of those
 * objects that is suitable for rendering.</p>
 *
 * <p>{@link Converter} implementations must have a zero-arguments public
 * constructor.  In addition, if the {@link Converter} class wishes to have
 * configuration property values saved and restored with the component tree,
 * the implementation must also implement {@link StateHolder}.</p>
 *
 * <p>If any <code>Converter</code> implementation requires a
 * <code>java.util.Locale</code> to perform its job, it must obtain that
 * <code>Locale</code> from the {@link javax.faces.component.UIViewRoot}
 * of the current {@link FacesContext}, unless the
 * <code>Converter</code> maintains its own <code>Locale</code> as part
 * of its state.</p>
 */

public interface Converter {


    /**
     * <p>Convert the specified string value, which is associated with
     * the specified {@link UIComponent}, into a model data object that
     * is appropriate for being stored during the <em>Apply Request
     * Values</em> phase of the request processing lifecycle.</p>
     *
     * @param context {@link FacesContext} for the request being processed
     * @param component {@link UIComponent} with which this model object
     *  value is associated
     * @param value String value to be converted (may be <code>null</code>)
     * 
     * @return <code>null</code> if the value to convert is <code>null</code>, 
     *  otherwise the result of the conversion
     *
     * @exception ConverterException if conversion cannot be successfully
     *  performed
     * @exception NullPointerException if <code>context</code> or
     *  <code>component</code> is <code>null</code>
     */
    public Object getAsObject(FacesContext context, UIComponent component,
                              String value);



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
     * @param value Model object value to be converted 
     *  (may be <code>null</code>)
     * 
     * @return a zero-length String if value is <code>null</code>, 
     *  otherwise the result of the conversion
     *
     * @exception ConverterException if conversion cannot be successfully
     *  performed
     * @exception NullPointerException if <code>context</code> or
     *  <code>component</code> is <code>null</code>
     */
    public String getAsString(FacesContext context, UIComponent component,
                              Object value);


}
