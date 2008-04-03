/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License 
 * (the License).  You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at 
 * https://jsftemplating.dev.java.net/cddl1.html or
 * jsftemplating/cddl1.txt.
 * See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL 
 * Header Notice in each file and include the License file 
 * at jsftemplating/cddl1.txt.  
 * If applicable, add the following below the CDDL Header, 
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information: 
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */
package com.sun.faces.sandbox.component.factory;

import com.sun.faces.sandbox.component.HtmlEditor;
import com.sun.jsftemplating.annotation.UIComponentFactory;
import com.sun.jsftemplating.component.factory.ComponentFactoryBase;
import com.sun.jsftemplating.layout.descriptors.LayoutComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 *  <p> This factory is responsible for instantiating an <code>HtmlEditor
 *      UIComponent</code>.</p>
 *
 *  <p> The {@link com.sun.jsftemplating.layout.descriptors.ComponentType}
 *      id for this factory is: "risb:htmlEditor".</p>
 *
 *  @author Jason Lee (jdlee@dev.java.net)
 */
@UIComponentFactory("risb:htmlEditor")
public class HtmlEditorFactory extends ComponentFactoryBase {

    /**
     *  <p> This is the factory method responsible for creating the
     *      <code>UIComponent</code>.</p>
     *
     *  @param  context     The <code>FacesContext</code>.
     *  @param  descriptor  The {@link LayoutComponent} descriptor associated
     *                      with the requested <code>UIComponent</code>.
     *  @param  parent      The parent <code>UIComponent</code>.
     *
     *  @return The newly created <code>HtmlEditor</code>.
     */
    public UIComponent create(FacesContext context, LayoutComponent descriptor, UIComponent parent) {
        // Create the UIComponent
        UIComponent comp = //createComponent(context, COMPONENT_TYPE, descriptor, parent);
            context.getApplication().createComponent(COMPONENT_TYPE);

        // Set all the attributes / properties
        setOptions(context, descriptor, comp);

        // Return the component
        return comp;
    }

    /**
     *  <p> The <code>UIComponent</code> type that must be registered in the
     *      <code>faces-config.xml</code> file mapping to the UIComponent class
     *      to use for this <code>UIComponent</code>.</p>
     */
    public static final String COMPONENT_TYPE = HtmlEditor.COMPONENT_TYPE;
}
