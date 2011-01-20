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

package javax.faces.model;


import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;


/**
 * <p><strong>SelectItemGroup</strong> is a subclass of {@link SelectItem} that
 * identifies a set of options that will be made available as a subordinate
 * "submenu" or "options list", depending upon the requirements of the
 * {@link UISelectMany} or {@link UISelectOne} renderer that is actually used.
 * In general, the <code>value</code> property of this instance will be ignored,
 * and the <code>label</code> property of this instance will be used to label
 * the submenu.</p>
 *
 * <p>Although it is feasible to incorporate {@link SelectItemGroup} instances
 * in he <code>selectItems</code> property of this instance (thereby creating
 * a data structure suitable for cascading submenus), some renderers may place
 * restrictions on the level of nesting they support.  For example, HTML based
 * renderers that create an <code>&lt;select&gt;</code> element will typically
 * render this instance as an <code>&lt;optgroup&gt;</code> element, but the
 * HTML 4.01 Specification disallows nested option groups.</p>
 */

public class SelectItemGroup extends SelectItem {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a <code>SelectItemGroup</code> with no initialized property
     * values.</p>
     */
    public SelectItemGroup() {

        super();

    }


    /**
     * <p>Construct a <code>SelectItemGroup</code> with the specified label
     * and no associated <code>selectItem</code>s.  The <code>value</code>
     * property will be set to a zero-length String, the
     * <code>description</code> property will be set to <code>null</code>,
     * and the <code>disabled</code> property will be set to false.</p>
     *
     * @param label Label to be rendered for this group in the response
     *
     * @throws NullPointerException if <code>label</code>
     *  is <code>false</code>
     */
    public SelectItemGroup(String label) {

        super("", label);

    }


    /**
     * <p>Construct a <code>SelectItemGroup</code> with the specified
     * properties.  The <code>value</code> property will be set to a
     * zero-length String.</p>
     *
     * @param label Label to be rendered for this group in the response
     * @param description Description of this group, for use in tools
     * @param disabled Flag indicating that this group is disabled
     * @param selectItems Array of {@link SelectItem} describing the
     *  items available in this group
     *
     * @throws NullPointerException if <code>label</code>
     *  or <code>selectItems</code> is <code>false</code>
     */
    public SelectItemGroup(String label, String description, boolean disabled,
                           SelectItem selectItems[]) {

        super("", label, description, disabled);
        setSelectItems(selectItems);

    }


    // ------------------------------------------------------ Instance Variables


    private SelectItem selectItems[] = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the set of subordinate {@link SelectItem}s for this group.</p>
     */
    public SelectItem[] getSelectItems() {

        return (this.selectItems);

    }


    /**
     * <p>Set the set of subordinate {@link SelectItem}s for this group.</p>
     *
     * @param selectItems The new set of subordinate items
     *
     * @throws NullPointerException if <code>selectItems</code>
     *  is <code>null</code>
     */
    public void setSelectItems(SelectItem selectItems[]) {

        if (selectItems == null) {
            throw new NullPointerException();
        }
        this.selectItems = selectItems;

    }


}
