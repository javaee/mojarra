/*
 * $Id: Converter.java,v 1.15 2006/12/15 18:12:14 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
 * <p/>
 * <p>{@link Converter} implementations must have a zero-arguments public
 * constructor.  In addition, if the {@link Converter} class wishes to have
 * configuration property values saved and restored with the component tree,
 * the implementation must also implement {@link StateHolder}.</p>
 * <p/>
 * <p>Starting with version 1.2 of the specification, an exception to the above
 * zero-arguments constructor requirement has been introduced.  If a converter has
 * a single argument constructor that takes a <code>Class</code> instance and
 * the <code>Class</code> of the data to be converted is
 * known at converter instantiation time, this constructor
 * must be used to instantiate the converter instead of the zero-argument
 * version.  This enables the per-class conversion
 * of Java enumerated types.</p>
 * <p/>
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
     * @param context   {@link FacesContext} for the request being processed
     * @param component {@link UIComponent} with which this model object
     *                  value is associated
     * @param value     String value to be converted (may be <code>null</code>)
     * @return <code>null</code> if the value to convert is <code>null</code>,
     *         otherwise the result of the conversion
     * @throws ConverterException   if conversion cannot be successfully
     *                              performed
     * @throws NullPointerException if <code>context</code> or
     *                              <code>component</code> is <code>null</code>
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
     * @param context   {@link FacesContext} for the request being processed
     * @param component {@link UIComponent} with which this model object
     *                  value is associated
     * @param value     Model object value to be converted
     *                  (may be <code>null</code>)
     * @return a zero-length String if value is <code>null</code>,
     *         otherwise the result of the conversion
     * @throws ConverterException   if conversion cannot be successfully
     *                              performed
     * @throws NullPointerException if <code>context</code> or
     *                              <code>component</code> is <code>null</code>
     */
    public String getAsString(FacesContext context, UIComponent component,
                              Object value);


}
