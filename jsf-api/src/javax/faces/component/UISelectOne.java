/*
 * $Id: UISelectOne.java,v 1.56 2007/07/27 19:59:08 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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


import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * <p><strong>UISelectOne</strong> is a {@link UIComponent} that represents
 * the user's choice of zero or one items from among a discrete set of
 * available options.  The user can modify the selected value.  Optionally,
 * the component can be preconfigured with a currently selected item, by
 * storing it as the <code>value</code> property of the component.</p>
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
     * <p>In addition to the standard validation behavior inherited from
     * {@link UIInput}, ensure that any specified value is equal to one of
     * the available options.  Before comparing each option, coerce the 
     * option value type to the type of this component's value following
     * the Expression Language coercion rules.  If the specified value is 
     * not equal to any of the options,  enqueue an error message
     * and set the <code>valid</code> property to <code>false</code>.</p>
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
        boolean found = matchValue(value, new SelectItemsIterator(this));

        // Enqueue an error message if an invalid value was specified
        if (!found) {
            FacesMessage message =
                MessageFactory.getMessage(context, INVALID_MESSAGE_ID,
                     MessageFactory.getLabel(context, this));
            context.addMessage(getClientId(context), message);
            setValid(false);
        }
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return <code>true</code> if the specified value matches one of the
     * available options, performing a recursive search if if a
     * {@link SelectItemGroup} instance is detected.</p>
     *
     * @param value {@link UIComponent} value to be tested
     * @param items Iterator over the {@link SelectItem}s to be checked
     */
    private boolean matchValue(Object value, Iterator items) {

        while (items.hasNext()) {
            SelectItem item = (SelectItem) items.next();
            if (item instanceof SelectItemGroup) {
                SelectItem subitems[] =
                    ((SelectItemGroup) item).getSelectItems();
                if ((subitems != null) && (subitems.length > 0)) {
                    if (matchValue(value, new ArrayIterator(subitems))) {
                        return (true);
                    }
                }
            } else {
                //Coerce the item value type before comparing values.
                Class type = value.getClass();
                Object newValue;
                try {
                    newValue = getFacesContext().getApplication().
                        getExpressionFactory().coerceToType(item.getValue(), type);
                } catch (ELException ele) {
                    newValue = item.getValue();
                } catch (IllegalArgumentException iae) {
                    // If coerceToType fails, per the docs it should throw
                    // an ELException, however, GF 9.0 and 9.0u1 will throw
                    // an IllegalArgumentException instead (see GF issue 1527).                    
                    newValue = item.getValue();
                }
                if (value.equals(newValue)) {
                    return (true);
                }
            }
        }
        return (false);

    }


    static class ArrayIterator implements Iterator {

        public ArrayIterator(Object items[]) {
            this.items = items;
        }

        private Object items[];
        private int index = 0;

        public boolean hasNext() {
            return (index < items.length);
        }

        public Object next() {
            try {
                return (items[index++]);
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


}
