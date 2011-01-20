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

package javax.faces.validator;


import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.EventListener;


/**
 * <p>A <strong class="changed_modified_2_0">Validator</strong>
 * implementation is a class that can perform validation (correctness
 * checks) on a {@link javax.faces.component.EditableValueHolder}.  Zero
 * or more <code>Validator</code>s can be associated with each {@link
 * javax.faces.component.EditableValueHolder} in the view, and are
 * called during the <em>Process Validations</em> phase of the request
 * processing lifecycle.</p>

 * <p/>
 * <p>Individual {@link Validator}s should examine the value and
 * component that they are passed, and throw a {@link ValidatorException}
 * containing a {@link javax.faces.application.FacesMessage}, documenting
 * any failures to conform to the required rules.
 * <p/>
 * <p>For maximum generality, {@link Validator} instances may be
 * configurable based on properties of the {@link Validator} implementation
 * class.  For example, a range check {@link Validator} might support
 * configuration of the minimum and maximum values to be used.</p>
 * <p/>
 * <p>{@link Validator} implementations must have a zero-arguments
 * public constructor.  In addition, if the {@link Validator} class
 * wishes to have configuration property values saved and restored with
 * the view, the implementation must also implement {@link
 * StateHolder}.</p>

 * <p class="changed_added_2_0">If the class implementing
 * <code>Validator</code> has a {@link
 * javax.faces.application.ResourceDependency} annotation, the action
 * described in <code>ResourceDependency</code> must be taken when
 * {@link javax.faces.component.EditableValueHolder#addValidator} is
 * called.  If the class implementing <code>Validator</code> has a {@link
 * javax.faces.application.ResourceDependencies} annotation, the
 * action described in <code>ResourceDependencies</code> must be taken
 * when {@link javax.faces.component.EditableValueHolder#addValidator} 
 * is called.</p>

 */

public interface Validator extends EventListener {

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the maximum or minimum value check fails, and both the maximum
     * and minimum values for this validator have been set.  The message
     * format string for this message may optionally include a
     * <code>{0}</code> placeholder, which will be replaced by the
     * configured minimum value, and a <code>{1}</code> placeholder,
     * which will be replaced by the configured maximum value.</p>
     *
     * @deprecated Use {@link DoubleRangeValidator#NOT_IN_RANGE_MESSAGE_ID} or
     *             {@link LongRangeValidator#NOT_IN_RANGE_MESSAGE_ID} instead.
     */
    public static final String NOT_IN_RANGE_MESSAGE_ID =
         "javax.faces.validator.NOT_IN_RANGE";

    /**
     * <p><span class="changed_modified_2_0">Perform</span> the
     * correctness checks implemented by this {@link Validator} against
     * the specified {@link UIComponent}.  If any violations are found,
     * a {@link ValidatorException} will be thrown containing the {@link
     * javax.faces.application.FacesMessage} describing the failure.
     *
     * <div class="changed_added_2_0">
     *
     * <p>For a validator to be fully compliant with Version 2 and later
     * of the specification, it must not fail validation on
     * <code>null</code> or empty values unless it is specifically
     * intended to address <code>null</code> or empty values.  An
     * application-wide <code>&lt;context-param&gt;</code> is provided
     * to allow validators designed for JSF 1.2 to work with JSF 2 and
     * later. The <code>javax.faces.VALIDATE_EMPTY_FIELDS</code>
     * <code>&lt;context-param&gt;</code> must be set to
     * <code>false</code> to enable this backwards compatibility
     * behavior.</p>
     *
     * </div>
     *
     * @param context   FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     * @param value     the value to validate
     * @throws ValidatorException   if validation fails
     * @throws NullPointerException if <code>context</code>
     *                              or <code>component</code> is <code>null</code>
     */
    public void validate(FacesContext context,
                         UIComponent component,
                         Object value) throws ValidatorException;


}
