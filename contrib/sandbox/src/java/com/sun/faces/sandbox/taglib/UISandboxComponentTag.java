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

/**
 * 
 */
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.webapp.UIComponentTag;

import com.sun.faces.sandbox.util.Util;

/**
 * @author Jason Lee, Adrian Mitev
 *
 */
public abstract class UISandboxComponentTag extends UIComponentTag {

    // --------------------------------------------------- Common Tag Properties

    // shared by all tags, but only used if exposed via the TLD
    private String disabled;

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    // --------------------------------------------- Methods from UIComponentTag


    protected void setProperties(UIComponent component) {

        super.setProperties(component);
        setBooleanProperty(component, "disabled", disabled);
        
    }


    // ------------------------------------------------------- Protected Methods

    /**
     * <p>
     * Calls through to {@link #setStringProperty(javax.faces.component.UIComponent, String, String, boolean)}
     * passing <code>false</code> for the <code>mustBeValueBinding</code> parameter.
     * </p>
     *
     * @see UISandboxComponentTag#setStringProperty(javax.faces.component.UIComponent, String, String, boolean)
     */
    protected void setStringProperty(UIComponent component,
                                     String attributeName,
                                     String attributeValue) {

        setStringProperty(component, attributeName, attributeValue, false);

    }


    /**
     * Set a String property on the target <code>component</code>.  The
     * value denoted by <code>attributeValue</code> may or may not represent
     * a <code>ValueBinding</code>.
     *
     * @param component the target component
     * @param attributeName the attribute/property name
     * @param attributeValue the attribute/property value
     * @param mustBeValueBinding if <code>true</code> <code>attributeValue</code>
     *  must be a value expression
     *
     * @throws IllegalArgumentException <code>mustBeValueBinding</code> is
     *  <code>true</code> and <code>attributeValue</code> isn't an expression
     */
    protected void setStringProperty(UIComponent component,
                                     String attributeName,
                                     String attributeValue,
                                     boolean mustBeValueBinding) {

        if (attributeValue != null) {
            if (isValueReference(attributeValue)) {
                component.setValueBinding(attributeName,
                        Util.getValueBinding(attributeValue));
            }
            else {
                if (mustBeValueBinding) {
                    throw new IllegalArgumentException("The value for '"
                                                       + attributeName
                                                       + "' must be a ValueBinding.");   
                } else {
                    component.getAttributes().put(attributeName, attributeValue);
                }
            }
        }
        
    }


    /**
     * <p>
     * Calls through to {@link #setIntegerProperty(javax.faces.component.UIComponent, String, String, boolean)}
     * passing <code>false</code> for the <code>mustBeValueBinding</code> parameter.
     * </p>
     *
     * @see UISandboxComponentTag#setIntegerProperty(javax.faces.component.UIComponent, String, String, boolean)
     */
    protected void setIntegerProperty(UIComponent component,
                                      String attributeName,
                                      String attributeValue) {

        setIntegerProperty(component, attributeName, attributeValue, false);

    }

    /**
     * <p>
     * Set an Integer property on the target <code>component</code>.  The
     * value denoted by <code>attributeValue</code> may or may not represent
     * a <code>ValueBinding</code>.
     * </p>
     *
     * @param component the target component
     * @param attributeName the attribute/property name
     * @param attributeValue the attribute/property value
     * @param mustBeValueBinding if <code>true</code> <code>attributeValue</code>
     *  must be a value expression
     *
     * @throws IllegalArgumentException <code>mustBeValueBinding</code> is
     *  <code>true</code> and <code>attributeValue</code> isn't an expression
     */
    protected void setIntegerProperty(UIComponent component,
                                      String attributeName,
                                      String attributeValue,
                                      boolean mustBeValueBinding) {

        if (attributeValue != null) {
            if (isValueReference(attributeValue)) {
                component.setValueBinding(attributeName,
                        Util.getValueBinding(attributeValue));
            }
            else {
                if (mustBeValueBinding) {
                    throw new IllegalArgumentException("The value for '"
                                                       + attributeName
                                                       + "' must be a ValueBinding.");
                } else {
                    component.getAttributes().put(attributeName,
                            Integer.valueOf(attributeValue));
                }
            }
        }

    }


    /**
     * <p>
     * Calls through to {@link #setBooleanProperty(javax.faces.component.UIComponent, String, String, boolean)}
     * passing <code>false</code> for the <code>mustBeValueBinding</code> parameter.
     * </p>
     *
     * @see UISandboxComponentTag#setIntegerProperty(javax.faces.component.UIComponent, String, String, boolean)
     */
    protected void setBooleanProperty(UIComponent component,
                                      String attributeName,
                                      String attributeValue) {

        setBooleanProperty(component, attributeName, attributeValue, false);

    }


     /**
     * <p>
     * Set an Boolean property on the target <code>component</code>.  The
     * value denoted by <code>attributeValue</code> may or may not represent
     * a <code>ValueBinding</code>.
     * </p>
     *
     * @param component the target component
     * @param attributeName the attribute/property name
     * @param attributeValue the attribute/property value
     * @param mustBeValueBinding if <code>true</code> <code>attributeValue</code>
     *  must be a value expression
     *
     * @throws IllegalArgumentException <code>mustBeValueBinding</code> is
     *  <code>true</code> and <code>attributeValue</code> isn't an expression
     */
    protected void setBooleanProperty(UIComponent component,
                                      String attributeName,
                                      String attributeValue,
                                      boolean mustBeValueBinding) {

        if (attributeValue != null) {
            if (isValueReference(attributeValue)) {
                component.setValueBinding(attributeName,
                        Util.getValueBinding(attributeValue));
            }
            else {
                if (mustBeValueBinding) {
                    throw new IllegalArgumentException("The value for '"
                                                       + attributeName
                                                       + "' must be a ValueBinding.");
                } else {
                    component.getAttributes().put(attributeName,
                            Boolean.valueOf(attributeValue));
                }
            }
        }

    }


     /**
     * <p>
     * Store <code>attributeValue</code> as a <code>ValueBinding</code>
     * keyed by <code>attributeName</code>
     * </p>
     *
     * @param component the target component
     * @param attributeName the attribute/property name
     * @param attributeValue the attribute/property value
     *
     * @throws IllegalArgumentException <code>mustBeValueBinding</code> is
     *  <code>true</code> and <code>attributeValue</code> isn't a syntactially
     *  valid expression
     */
    protected void setValueBinding(UIComponent component,
                                   String attributeName,
                                   String attributeValue) {

        if (attributeValue != null) {
            if (isValueReference(attributeValue)) {
                component.setValueBinding(attributeName,
                        Util.getValueBinding(attributeValue));
            }
            else {
                throw new IllegalStateException("The value for '"
                                                + attributeName
                                                + "' must be a ValueBinding.");
            }
        }

    }


    /**
     * <p>
     *  Creates a new <code>MethodBinding</code>/
     * </p>
     * @param methodReference the String from which to create
     *  the <code>MethodBinding</code> from
     * @param params the expected arguments of the target method
     * @return a new <code>MethodBinding</code> based off the provided
     *  arguments
     *
     * @throws IllegalArgumentException if <code>methodReference</code> isn't
     *  an expression
     */
    protected MethodBinding createMethodBinding(String methodReference,
                                                Class params[]) {
        MethodBinding mb = null;
        if (methodReference != null) {
            if (!isValueReference(methodReference)) {
                throw new IllegalArgumentException("Invalid MethodBinding expression:  "
                                                   + methodReference);
            }
            mb = FacesContext.getCurrentInstance().getApplication().createMethodBinding(methodReference, params);
        }
        return mb;
    }
}