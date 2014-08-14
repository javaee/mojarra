/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.convert;


import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p><strong class="changed_modified_2_0 changed_modified_2_2">Converter</strong> is an
 * interface describing a Java class that can perform Object-to-String
 * and String-to-Object conversions between model data objects and a
 * String representation of those objects that is suitable for
 * rendering.</p>

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
 *
 * <p class="changed_added_2_0">If the class implementing
 * <code>Converter</code> has a {@link
 * javax.faces.application.ResourceDependency} annotation, the action
 * described in <code>ResourceDependency</code> must be taken when
 * {@link javax.faces.component.ValueHolder#setConverter} is called.
 * If the class implementing <code>Converter</code> has a {@link
 * javax.faces.application.ResourceDependencies} annotation, the
 * action described in <code>ResourceDependencies</code> must be taken 
 * when {@link javax.faces.component.ValueHolder#setConverter} is called.</p>
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


    /**
     * <p class="changed_added_2_2">
     * If this param is set, and calling toLowerCase().equals("true") on a
     * String representation of its value returns true,
     * Application.createConverter() must guarantee that the default for the
     * timezone of all javax.faces.convert.DateTimeConverter instances must
     * be equal to TimeZone.getDefault() instead of "GMT".
     * </p>
     * 
     * @since 2.0
     */
    public static final String DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE_PARAM_NAME = 
            "javax.faces.DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE";
    
}
