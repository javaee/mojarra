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

package javax.faces.component;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


/**
 * <p><strong class="changed_modified_2_0">UISelectOne</strong> is a
 * {@link UIComponent} that represents the user's choice of zero or one
 * items from among a discrete set of available options.  The user can
 * modify the selected value.  Optionally, the component can be
 * preconfigured with a currently selected item, by storing it as the
 * <code>value</code> property of the component.</p>
 *
 * <p>This component is generally rendered as a select box or a group of
 * radio buttons.</p>
 *
 * <p>By default, the <code>rendererType</code> property is set to
 * "<code>javax.faces.Menu</code>".  This value can be changed by
 * calling the <code>setRendererType()</code> method.</p>
 */

public class UISelectOne extends UIInput {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.SelectOne";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.SelectOne";


    /**
     * <p>The message identifier of the
     * {@link javax.faces.application.FacesMessage} to be created if
     * a value not matching the available options is specified.
     */
    public static final String INVALID_MESSAGE_ID =
        "javax.faces.component.UISelectOne.INVALID";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectOne} instance with default property
     * values.</p>
     */
    public UISelectOne() {

        super();
        setRendererType("javax.faces.Menu");

    }


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    // ------------------------------------------------------ Validation Methods


    /**
     * <p><span class="changed_modified_2_0">In</span> addition to the
     * standard validation behavior inherited from {@link UIInput},
     * ensure that any specified value is equal to one of the available
     * options.  Before comparing each option, coerce the option value
     * type to the type of this component's value following the
     * Expression Language coercion rules.  If the specified value is
     * not equal to any of the options, enqueue an error message and set
     * the <code>valid</code> property to <code>false</code>.</p>
     *
     * <p class="changed_added_2_0">If {@link #isRequired} returns
     * <code>true</code>, and the current value is equal to the value of
     * an inner {@link UISelectItem} whose {@link
     * UISelectItem#isNoSelectionOption} method returns
     * <code>true</code>, enqueue an error message and set the
     * <code>valid</code> property to <code>false</code>.</p>
     *
     * @param context The {@link FacesContext} for the current request
     *
     * @param value The converted value to test for membership.
     *
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    protected void validateValue(FacesContext context, Object value) {

        // Skip validation if it is not necessary
        super.validateValue(context, value);

        if (!isValid() || (value == null)) {
            return;
        }

        // Ensure that the value matches one of the available options
        boolean found = SelectUtils.matchValue(getFacesContext(),
                                               this,
                                               value,
                                               new SelectItemsIterator(context, this),
                                               getConverter());

        boolean isNoSelection = SelectUtils.valueIsNoSelectionOption(getFacesContext(),
                                               this,
                                               value,
                                               new SelectItemsIterator(context, this),
                                               getConverter());

        // Enqueue an error message if an invalid value was specified
        if ((!found) || 
            (isRequired() && isNoSelection)) {
            FacesMessage message =
                MessageFactory.getMessage(context, INVALID_MESSAGE_ID,
                     MessageFactory.getLabel(context, this));
            context.addMessage(getClientId(context), message);
            setValid(false);
        }
        
    }

}
