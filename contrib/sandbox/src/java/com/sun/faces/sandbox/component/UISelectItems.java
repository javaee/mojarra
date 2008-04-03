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

package com.sun.faces.sandbox.component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.List;

import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import com.sun.faces.sandbox.util.Util;

/**
 * <p>
 * UISelectItems is meant to be a drop-in replacement for &lt;f:selectItems&gt;
 * with the additional capability of passing in lists of POJOs to the componenet
 * instead of being limited to only a SelectItem[]. Three additional optional
 * attributes are introduced: itemValue, itemLabel, and itemVar.
 * </p>
 * <p>
 * The default behavior is to convert the POJO to a SelectItem using the result
 * of toString() for both the value and the label.
 * </p>
 * 
 * <pre>
 *     &lt;risb:selectItems value=&quot;#{bean.listOfIntegers}&quot; /&gt;
 * </pre>
 * 
 * <p>
 * The value or label of the SelectItem can be controlled using EL in the
 * itemValue or itemLabel. The default variable mapping of "item" is used to
 * represent the individual POJO.
 * </p>
 * 
 * <pre>
 *     &lt;risb:selectItems value=&quot;#{bean.personList}&quot;
 *                     itemValue=&quot;#{item.id}&quot;
 *                     itemLabel=&quot;#{item.lastName}, #{item.firstName}&quot; /&gt;
 * </pre>
 * 
 * <p>
 * If there is a naming conflict with "item", then the variable used can be
 * controlled using the "itemVar" attribute. Note that this is only necessary if
 * you intend to use the external "item" variable within the selectItem EL.
 * UISelectItems will not stomp on your defined beans.
 * </p>
 * 
 * <pre>
 *     &lt;risb:selectItems value=&quot;#{bean.personList}&quot;
 *                     itemVar=&quot;person&quot;
 *                     itemValue=&quot;#{person.id}&quot;
 *                     itemLabel=&quot;Foo #{item.foo} for #{person.lastName}&quot; /&gt;
 * </pre>
 * 
 * @author Mitch Blevins, International Environmental Corporation
 * 
 */
public class UISelectItems extends javax.faces.component.UISelectItems {
    public static final String COMPONENT_TYPE = "com.sun.faces.sandbox.SelectItems";

    protected class ListWrapper implements InvocationHandler {
        protected class IteratorWrapper implements Iterator {
            protected Iterator i;
            protected String itemVar;
            protected boolean saveOldBinding;
            protected UISelectItems si;
            protected boolean useItemLabel;
            protected boolean useItemValue;

            public IteratorWrapper(Iterator i, UISelectItems si) {
                this.i = i;
                this.si = si;
                this.itemVar = si.getItemVar();
                if (this.itemVar == null) {
                    this.itemVar = "item";
                }
                this.useItemLabel = si.useItemLabel();
                this.useItemValue = si.useItemValue();
                this.saveOldBinding = getValueBinding("#{"+this.itemVar+"}") != null;
            }

            public boolean hasNext() {
                return i.hasNext();
            }

            public Object next() {
                Object o = i.next();
                if (o == null) {
                    return null;
                }
                if (o instanceof SelectItem || o instanceof SelectItemGroup) {
                    return (o);
                }

                // Coerce it into a SelectItem...
                // First, see if we already have that variable to override
                Object oldBinding = null;
                if (this.saveOldBinding) {
                    oldBinding = getValueBinding("#{"+this.itemVar+"}").getValue(getFacesContext());
                }

                // Create the select item
                // All this conditional stuff is just for performance to keep
                // from unnecessarily
                // calling EL stuff within the iteration loop
                String itemValue = null;
                String itemLabel = null;
                if (this.useItemLabel || this.useItemValue) {
                    ValueBinding vb = Util.getValueBinding("#{"+itemVar+"}");
                    vb.setValue(getFacesContext(), o);
                }
                if (this.useItemValue) {
                    itemValue = si.getItemValue();
                }
                if (this.useItemLabel) {
                    itemLabel = si.getItemLabel();
                }

                if (itemValue == null) {
                    itemValue = o.toString();
                }
                if (itemLabel == null) {
                    itemLabel = o.toString();
                }
                SelectItem item = new SelectItem(itemValue, itemLabel);

                // Replace the old value
                if (oldBinding != null) {
                    ValueBinding vb = Util.getValueBinding("#{"+itemVar+"}");
                    vb.setValue(getFacesContext(), oldBinding);
                }

                // Return the coerced selectItem
                return item;
            }

            public void remove() {
                i.remove();
            }
        }

        protected List list;

        protected UISelectItems si;

        public ListWrapper(List list, UISelectItems si) {
            this.list = list;
            this.si = si;
        }

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            if ("iterator".equals(method.getName())
                    && method.getReturnType().equals(Iterator.class)) {
                return new IteratorWrapper(list.iterator(), si);
            }
            return method.invoke(list, args);
        }
    }
    protected String itemLabel;
    protected String itemValue;
    protected String itemVar;

    /**
     * Returns the itemLabel property of the UISelectItems.
     */
    public String getItemLabel() { return ComponentHelper.getValue(this, "itemLabel",itemLabel); }

    /**
     * Returns the itemValue property of the UISelectItems.
     */
    public String getItemValue() { return ComponentHelper.getValue(this, "itemValue", itemValue); }

    /**
     * 
     * Returns the itemVar property of the UISelectItems.
     */
    public String getItemVar() { return ComponentHelper.getValue(this, "itemVar", itemVar); }

    /*
     * This code inspired by
     * http://forum.java.sun.com/profile.jspa?userID=575815 I just changed it to
     * use a dynamic proxy instead of hard-coding the whole interface. Also, a
     * List instead of a Collection is expected by the JSF 1.2 RI.
     */
    /**
     * Returns the value property for the UISelectItems.
     */
    public Object getValue() {
        Object ret = super.getValue();

        // return our wrapper if it is a collection
        if (ret instanceof List) {
            return (List) Proxy.newProxyInstance(ret.getClass()
                    .getClassLoader(), new Class[] { List.class },
                    new ListWrapper((List) ret, this));
        }

        // otherwise, just keep on truckin
        return ret;
    }

    /**
     * Sets the itemLabel property of the UISelectItems.
     * 
     * @param itemLabel
     *            the label for each selectItem.
     */
    public void setItemLabel(String itemLabel) { this.itemLabel = itemLabel; }

    /**
     * Sets the itemValue property of the UISelectItems.
     * 
     * @param itemValue
     *            the value for each selectItem.
     */
    public void setItemValue(String itemValue) { this.itemValue = itemValue; }

    /**
     * Sets the itemVar property of the UISelectItems. Used to override the
     * default variable mapping of "item" used in both the itemValue and
     * itemLabel properties.
     * 
     * @param itemVar
     *            the variable to map for each indiviual SelectItem.
     */
    public void setItemVar(String itemVar) { this.itemVar = itemVar; }

    protected boolean useItemLabel() {
        return this.itemLabel != null
                || this.getValueBinding("itemLabel") != null;
    }

    protected boolean useItemValue() {
        return this.itemValue != null
                || this.getValueBinding("itemValue") != null;
    }
}