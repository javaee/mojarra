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


import javax.faces.model.SelectItem;



/**
 * <p><strong class="changed_modified_2_0
 * changed_modified_2_0_rev_a">UISelectItem</strong> is a component that
 * may be nested inside a {@link UISelectMany} or {@link UISelectOne}
 * component, and causes the addition of a {@link SelectItem} instance
 * to the list of available options for the parent component.  The
 * contents of the {@link SelectItem} can be specified in one of the
 * following ways:</p> <ul> <li>The <code>value</code> attribute's value
 * is an instance of {@link SelectItem}.</li> <li>The associated {@link
 * javax.el.ValueExpression} points at a model data item of type {@link
 * SelectItem}.</li> <li>A new {@link SelectItem} instance is
 * synthesized from the values of the <code>itemDescription</code>,
 * <code>itemDisabled</code>, <code>itemLabel</code>, <code
 * class="changed_modified_2_0_rev_a">itemEscaped</code>, and
 * <code>itemValue</code> attributes.</li> </ul>
 */

public class UISelectItem extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.SelectItem";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.SelectItem";


    enum PropertyKeys {
        itemDescription,
        itemDisabled,
        itemEscaped,
        itemLabel,
        itemValue,
        value,
        noSelectionOption
    }


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectItem} instance with default property
     * values.</p>
     */
    public UISelectItem() {

        super();
        setRendererType(null);

    }


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>Return the description for this selection item.</p>
     */
    public String getItemDescription() {

        return (String) getStateHelper().eval(PropertyKeys.itemDescription);

    }


    /**
     * <p>Set the description for this selection item.</p>
     *
     * @param itemDescription The new description
     */
    public void setItemDescription(String itemDescription) {

        getStateHelper().put(PropertyKeys.itemDescription, itemDescription);

    }

    /**
     * <p>Return the disabled setting for this selection item.</p>
     */
    public boolean isItemDisabled() {

        return (Boolean) getStateHelper().eval(PropertyKeys.itemDisabled, false);

    }

    /**
     * <p>Set the disabled value for this selection item.</p>
     *
     * @param itemDisabled The new disabled flag
     */
    public void setItemDisabled(boolean itemDisabled) {

        getStateHelper().put(PropertyKeys.itemDisabled, itemDisabled);

    }
    
    /**
     * <p>Return the escape setting for the label of this selection item.</p>
     */
    public boolean isItemEscaped() {

        return (Boolean) getStateHelper().eval(PropertyKeys.itemEscaped, true);

    }

    /**
     * <p>Set the escape value for the label of this selection item.</p>
     *
     * @param itemEscaped The new disabled flag
     */
    public void setItemEscaped(boolean itemEscaped) {

        getStateHelper().put(PropertyKeys.itemEscaped, itemEscaped);

    }
    

    /**
     * <p>Return the localized label for this selection item.</p>
     */
    public String getItemLabel() {

        return (String) getStateHelper().eval(PropertyKeys.itemLabel);

    }


    /**
     * <p>Set the localized label for this selection item.</p>
     *
     * @param itemLabel The new localized label
     */
    public void setItemLabel(String itemLabel) {

        getStateHelper().put(PropertyKeys.itemLabel, itemLabel);

    }


    /**
     * <p>Return the server value for this selection item.</p>
     */
    public Object getItemValue() {

        return getStateHelper().eval(PropertyKeys.itemValue);

    }


    /**
     * <p>Set the server value for this selection item.</p>
     *
     * @param itemValue The new server value
     */
    public void setItemValue(Object itemValue) {

        getStateHelper().put(PropertyKeys.itemValue, itemValue);

    }



    /**
     * <p>Returns the <code>value</code> property of the
     * <code>UISelectItem</code>.</p>
     */
    public Object getValue() {

        return getStateHelper().eval(PropertyKeys.value);

    }


    /**
     * <p>Sets the <code>value</code> property of the
     * <code>UISelectItem</code>.</p>
     * 
     * @param value the new value
     */
    public void setValue(Object value) {

        getStateHelper().put(PropertyKeys.value,  value);

    }
    
    /** <p class="changed_added_2_0">Return the value of the
     * <code>noSelectionOption</code> property.  If the value of this
     * property is <code>true</code>, the system interprets the option
     * represented by this <code>UISelectItem</code> instance as
     * representing a "no selection" option.  See {@link
     * UISelectOne#validateValue} and {@link UISelectMany#validateValue}
     * for usage.</p>
     *
     * @since 2.0
     */
    public boolean isNoSelectionOption() {

        return (Boolean) getStateHelper().eval(PropertyKeys.noSelectionOption, false);
    }

    /**
     * <p class="changed_added_2_0">Set the value of the
     * <code>noSelectionOption</code> property.</p>
     *
     * @since 2.0
     */
    public void setNoSelectionOption(boolean noSelectionOption) {

        getStateHelper().put(PropertyKeys.noSelectionOption, noSelectionOption);

    }

}
