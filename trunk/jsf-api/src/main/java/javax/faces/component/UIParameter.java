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



/**
 * <p><strong>UIParameter</strong> is a {@link UIComponent} that represents
 * an optionally named configuration parameter for a parent component.</p>
 *
 * <p>Parent components should retrieve the value of a parameter by calling
 * <code>getValue()</code>.  In this way, the parameter value can be set
 * directly on the component (via <code>setValue()</code>), or retrieved
 * indirectly via the value binding expression.</p>
 *
 * <p>In some scenarios, it is necessary to provide a parameter name, in
 * addition to the parameter value that is accessible via the
 * <code>getValue()</code> method.
 * {@link javax.faces.render.Renderer}s that support parameter names on their
 * nested {@link UIParameter} child components should document
 * their use of this property.</p>
 *
 */

public class UIParameter extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Parameter";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Parameter";


    enum PropertyKeys {
        name,
        value,
        disble
    }


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIParameter} instance with default property
     * values.</p>
     */
    public UIParameter() {

        super();
        setRendererType(null);

    }


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>Return the optional parameter name for this parameter.</p>
     */
    public String getName() {

        return (String) getStateHelper().eval(PropertyKeys.name);

    }


    /**
     * <p>Set the optional parameter name for this parameter.</p>
     *
     * @param name The new parameter name,
     *  or <code>null</code> for no name
     */
    public void setName(String name) {

        getStateHelper().put(PropertyKeys.name, name);

    }



    /**
     * <p>Returns the <code>value</code> property of the
     * <code>UIParameter</code>.</p>
     */
    public Object getValue() {

        return getStateHelper().eval(PropertyKeys.value);

    }


    /**
     * <p>Sets the <code>value</code> property of the\
     * <code>UIParameter</code>.</p>
     *
     * @param value the new value
     */
    public void setValue(Object value) {

        getStateHelper().put(PropertyKeys.value, value);

    }

    /**
     * <p class="changed_added_2_0">Return the value of the <code>disable</code>
     * directive for this component. This directive determines whether the
     * parameter value should be disabled by assigning it a null value.
     * If true, the <code>value</code> set on this component is ignored.</p>
     * @since 2.0
     */
    public boolean isDisable() {

        return (Boolean) getStateHelper().eval(PropertyKeys.disble, false);

    }

    /**
     * <p>Sets the <code>disable</code> property of the <code>UIParameter</code>.</p>
     * @param disable
     * @since 2.0
     */
    public void setDisable(boolean disable) {

        getStateHelper().put(PropertyKeys.disble, disable);

    }


}
