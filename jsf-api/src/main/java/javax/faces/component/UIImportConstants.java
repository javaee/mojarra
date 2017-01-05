/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

import javax.el.ValueExpression;
import javax.faces.view.ViewMetadata;

/**
 * <div class="changed_added_2_3">
 * 
 * <p><strong>UIImportConstants</strong> imports a mapping of all constant field values of the given type in the current
 * view.</p>
 * 
 * <p>The {@link javax.faces.view.ViewDeclarationLanguage} implementation must cause an instance of this component to be
 * placed in the view for each occurrence of an <code>&lt;f:importConstants /&gt;</code> element placed inside of an
 * <code>&lt;f:metadata /&gt;</code> element. The user must place <code>&lt;f:metadata /&gt;</code> as a direct child of
 * the <code>UIViewRoot</code>. The {@link ViewMetadata#createMetadataView(javax.faces.context.FacesContext)} must 
 * take care of actual task of importing the constants.</p>
 * 
 * <p>Instances of this class participate in the regular JSF lifecycle, including on Ajax requests.</p>
 * 
 * <p>The purpose of this component is to provide a mapping of all constant field values of the given type in the
 * current view. Constant field values are all <code>public static final</code> fields of the given type. The map key
 * represents the constant field name as <code>String</code>. The map value represents the actual constant field value.
 * This works for classes, interfaces and enums.</p>
 * 
 * <h3 id="usage">Usage</h3>
 * 
 * <p>The below constant fields:</p>
 * 
 * <pre>
 * package com.example;
 * 
 * public class Foo {
 *     public static final String FOO1 = "foo1";
 *     public static final String FOO2 = "foo2";
 * }
 * </pre>
 * 
 * <pre>
 * package com.example;
 * 
 * public interface Bar {
 *     public static final String BAR1 = "bar1";
 *     public static final String BAR2 = "bar2";
 * }
 * </pre>
 * 
 * <pre>
 * package com.example;
 * 
 * public enum Baz {
 *     BAZ1, BAZ2;
 * }
 * </pre>
 * 
 * <p>Can be imported as below:</p>
 * 
 * <pre>
 * &lt;f:metadata&gt;
 *     &lt;f:importConstants type="com.example.Foo" /&gt;
 *     &lt;f:importConstants type="com.example.Bar" var="Barrr" /&gt;
 *     &lt;f:importConstants type="com.example.Baz" /&gt;
 * &lt;/f:metadata&gt;
 * </pre>
 * 
 * <p>And can be referenced as below:</p>
 * 
 * <pre>
 * #{Foo.FOO1}, #{Foo.FOO2}, #{Barrr.BAR1}, #{Barrr.BAR2}, #{Baz.BAZ1}, #{Baz.BAZ2}
 * </pre>
 * 
 * <pre>
 * &lt;h:selectOneMenu value="#{bean.baz}" &gt;
 *     &lt;f:selectItems value="#{Baz}" /&gt;
 * &lt;/h:selectOneMenu&gt;
 * </pre>
 * 
 * </div>
 * 
 * @since 2.3
 */
public class UIImportConstants extends UIComponentBase {

    // ---------------------------------------------------------------------------------------------- Manifest Constants

    /**
     * <p>
     * The standard component type for this component.
     * </p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.ImportConstants";

    /**
     * <p>
     * The standard component family for this component.
     * </p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.ImportConstants";

    /**
     * Properties that are tracked by state saving.
     */
    enum PropertyKeys {
        type, var;
    }

    // ---------------------------------------------------------------------------------------------------- Constructors

    /**
     * <p>
     * Create a new {@link UIImportConstants} instance with renderer type set to <code>null</code>.
     * </p>
     */
    public UIImportConstants() {
        setRendererType(null);
    }

    // ------------------------------------------------------------------------------------------------------ Properties

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * <p>
     * Returns the fully qualified name of the type to import the constant field values for.
     * </p>
     * 
     * @return The fully qualified name of the type to import the constant field values for.
     */
    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type);
    }

    /**
     * <p>
     * Sets the fully qualified name of the type to import the constant field values for.
     * </p>
     * 
     * @param type The fully qualified name of the type to import the constant field values for.
     */
    public void setType(final String type) {
        getStateHelper().put(PropertyKeys.type, type);
    }

    /**
     * <p>
     * Returns name of request scope attribute under which constants will be exposed as a Map.
     * </p>
     * 
     * @return Name of request scope attribute under which constants will be exposed as a Map.
     */
    public String getVar() {
        return (String) getStateHelper().eval(PropertyKeys.var);
    }

    /**
     * <p>
     * Sets name of request scope attribute under which constants will be exposed as a Map.
     * </p>
     * 
     * @param var Name of request scope attribute under which constants will be exposed as a Map.
     */
    public void setVar(final String var) {
        getStateHelper().put(PropertyKeys.var, var);
    }

    // --------------------------------------------------------------------------------------------- UIComponent Methods

    /**
     * <p>
     * Set the {@link ValueExpression} used to calculate the value for the specified attribute or property name, if any.
     * If a {@link ValueExpression} is set for the <code>var</code> property, throw an illegal argument exception.
     * </p>
     *
     * @throws IllegalArgumentException If <code>name</code> is one of <code>id</code>, <code>parent</code>, or
     * <code>var</code>.
     * @throws NullPointerException If <code>name</code> is <code>null</code>.
     */
    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if (PropertyKeys.var.toString().equals(name)) {
            throw new IllegalArgumentException(name);
        }

        super.setValueExpression(name, binding);
    }

}
