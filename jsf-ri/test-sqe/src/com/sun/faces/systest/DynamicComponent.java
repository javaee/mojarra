/*
 * $Id: DynamicComponent.java,v 1.2 2005/08/22 22:11:35 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.systest;


import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


/**
 * <p>Test <code>UIComponent</code> for sys tests that dynamically creates
 * or removes children UIOutput components with specified ids and values.
 * It pays attention to the following request parameters in the
 * <code>encodeBegin()</code> method:</p>
 * <ul>
 * <li><code>?mode=create&id=foo&value=bar</code> - Create a new
 * <code>UIOutput</code> child with a component identifier of
 * <code>foo</code> and a value of <code>bar</code> (optional).  Set the
 * <code>rendererType</code> property to <code>Text</code>.  The
 * new child will be appended to the child list.</li>
 * <li><code>?mode=delete&id=foo</code> - Remove any child with a
 * component identifier of <code>foo</code>.</li>
 * </ul>
 *
 * <p>In accordance with our current restrictions, this component sets
 * <code>rendersChildren</code> to <code>true</code>, and recursively
 * renders its children in <code>encodeChildren</code>.  This component
 * itself renders "{" at the beginning and "}" at the end, just like
 * <code>ChildrenComponent</code>.</p>
 */

public class DynamicComponent extends UIComponentBase {


    public static final String COMPONENT_FAMILY = "Dynamic";

    // ------------------------------------------------------------ Constructors


    public DynamicComponent() {
        this("dynamic");
    }


    public DynamicComponent(String componentId) {
        super();
        setId(componentId);
    }


    // ----------------------------------------------------- UIComponent Methods

    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    public boolean getRendersChildren() {
        return (true);
    }


    public void encodeBegin(FacesContext context) throws IOException {
        process(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.write("{ ");
    }


    public void encodeChildren(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Iterator kids = getChildren().iterator();
        while (kids.hasNext()) {
            encodeRecursive(context, (UIComponent) kids.next());
            writer.write(" ");
        }
    }


    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.write(" }\n");
    }


    // --------------------------------------------------------- Private Methods


    private void encodeRecursive(FacesContext context, UIComponent component)
        throws IOException {

        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator kids = component.getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                encodeRecursive(context, kid);
            }
        }
        component.encodeEnd(context);

    }


    private void process(FacesContext context) {
        Map map = context.getExternalContext().getRequestParameterMap();
        String mode = (String) map.get("mode");
        String id = (String) map.get("id");
        String value = (String) map.get("value");
        if (mode == null) {
            return;
        } else if ("create".equals(mode)) {
            UIOutput output = new UIOutput();
            output.setId(id);
            output.setRendererType("javax.faces.Text");
            output.setValue(value);
            getChildren().add(output);
        } else if ("delete".equals(mode)) {
            Iterator kids = getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                if (id.equals(kid.getId())) {
                    getChildren().remove(kid);
                    break;
                }
            }
        }

    }


}
